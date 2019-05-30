package com.career.careergateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.career.careergateway.config.FilterIgnorePropertiesConfig;
import com.career.careergateway.service.AccessLogService;
import com.career.common.bean.common.JwtInfo;
import com.career.common.constant.CommonConstant;
import com.career.common.constant.SecurityConstants;
import com.career.common.msg.R;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;


import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careergateway.filter
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/28
 * @Version: 1.0
 */
@Configuration
@Slf4j
@RefreshScope
public class AccessGatewayFilter implements GlobalFilter {

	@Autowired
	private AccessLogService accessLogService;

	@Autowired
	private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

	@Value("${jwt.sign-key}")
	private String signKey;

	private static final String GATE_WAY_PREFIX = "/api";

	private static final String HEADER_TOKEN_PREFIX = "bearer";

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Value("${HelloWorld}")
	private String helloWorld;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.warn("Test get application: {}", helloWorld);
		ServerHttpRequest request = exchange.getRequest();
		String requestUri = request.getPath().pathWithinApplication().value();
		ServerHttpRequest.Builder mutate = request.mutate();
		// 不进行拦截的地址
		if (isStartWith(requestUri)) {
			ServerHttpRequest build = mutate.build();
			return chain.filter(exchange.mutate().request(build).build());
		}
		log.info(requestUri);
		// 获取jwt token
		String user = null;
		try {
			JwtInfo jwtInfo = getJwtInfo(getToken(request));
			if (! hasPermission(requestUri, request.getMethodValue(), jwtInfo)) {
				throw new AccessDeniedException("No Access Rights");
			}
			user = jwtInfo.getUserName();
			mutate.header(SecurityConstants.USER_HEADER, user);
			mutate.header(SecurityConstants.ROLE_HEADER,
					jwtInfo.getRoleList().stream().map(i -> i.toString()).collect(Collectors.joining(",")));
			ServerHttpRequest build = mutate.build();
			accessLogService.log(exchange, CommonConstant.STATUS_NORMAL, user, null);
			return chain.filter(exchange.mutate().request(build).build());
		} catch (Exception e) {
			log.warn("Authentication Failed: {}", e.getMessage());
			// 发送访问日志
			try {
				accessLogService.log(exchange, CommonConstant.STATUS_LOCK, user, e.getMessage());
			} catch (InterruptedException e1) {
				log.error("log send failed: {}", e1.getMessage());
			}
			return getVoidMono(exchange, new R(new AccessDeniedException("Access Forbidden")));
		}
	}

	private Mono<Void> getVoidMono(ServerWebExchange exchange, R body) {
		exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Flux.just(buffer));
	}

	private boolean isStartWith(String requestUrl) {
		for (String url : filterIgnorePropertiesConfig.getUrls()) {
			if (antPathMatcher.match(url, requestUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * getToken 获取请求中的Token
	 * @author Winkle.Huang.w.k
	 * @description getToken 获取请求中的Token
	 * @date 2019/5/29 14:38
	 * @param [request]
	 * @return java.lang.String
	 */
	private String getToken(ServerHttpRequest request) {
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isBlank(authHeader) || !authHeader.toLowerCase().startsWith(HEADER_TOKEN_PREFIX)) {
			log.warn("AUTHORIZATION NOT FOUND");
			return null;
		}
		return authHeader.substring(HEADER_TOKEN_PREFIX.length());
	}

	/**
	 * getJwtInfo
	 * @author Winkle.Huang.w.k
	 * @description getJwtInfo
	 * @date 2019/5/29 14:38
	 * @param [token]
	 * @return com.career.common.bean.common.JwtInfo
	 */
	private JwtInfo getJwtInfo(String token) {
		String key = Base64.getEncoder().encodeToString(signKey.getBytes());
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		String userName = (String) claims.get("user_name");
		List<String> roleList = (List<String>) claims.get("authorities");
		return new JwtInfo(userName, roleList);
	}

	private boolean hasPermission(String requestUri, String method, JwtInfo jwtInfo) {
		boolean hasPermission = false;
		List<String> roleList = jwtInfo.getRoleList();
		if (CollectionUtils.isEmpty(roleList)) {
			log.warn("Role List Is Null.");
			return hasPermission;
		} else {
			hasPermission = true;
		}
		return hasPermission;
	}
}
