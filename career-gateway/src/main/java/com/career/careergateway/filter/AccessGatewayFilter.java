package com.career.careergateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

	@Value("${HelloWorld}")
	private String user;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.warn(user);
		return null;
	}
}
