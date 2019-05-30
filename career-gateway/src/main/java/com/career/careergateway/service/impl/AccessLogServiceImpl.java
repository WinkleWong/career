package com.career.careergateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.career.careergateway.service.AccessLogService;
import com.career.common.bean.log.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Date;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careergateway.service.impl
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
@Service
@Slf4j
public class AccessLogServiceImpl implements AccessLogService {
	/**
	 *  TODO: 性能待验证
	 * @param exchange
	 * @param code  处理结果
	 * @param error 错误日志
	 */
//    @Async
	@Override
	public void log(ServerWebExchange exchange, String code, String user, String error) {
		ServerHttpRequest request = exchange.getRequest();
		String clientAddr = request.getHeaders().getFirst("X-Forwarded-For");
		Route route = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		LogInfo info = new LogInfo();
		info.setTitle("gateway access log");
		info.setRequestUri(request.getPath().pathWithinApplication().value());
		info.setMethod(request.getMethodValue());
		info.setType(code);
		info.setRemoteAddr((clientAddr != null) ? clientAddr : request.getRemoteAddress().getAddress().getHostAddress());
		info.setServiceId(route.getUri().getHost());
		info.setUserAgent(request.getHeaders().getFirst("user-agent"));
		info.setParams(request.getQueryParams().toString());
		info.setCreateBy(user);
		info.setTime(new Date());
		info.setException(error);
		log.info(info.toString());
	}
}
