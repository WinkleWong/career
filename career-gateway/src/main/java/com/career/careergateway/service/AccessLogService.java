package com.career.careergateway.service;

import org.springframework.web.server.ServerWebExchange;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careergateway.service
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
public interface AccessLogService {
	void log(ServerWebExchange exchange, String code, String user, String error) throws InterruptedException;
}
