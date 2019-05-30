package com.career.common.bean.log;

import lombok.Data;

import java.util.Date;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.common.bean.log
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
@Data
public class LogInfo {
	private static final long serialVersionUID = 1L;

	private String title;

	private String type;

	private String createBy;

	private Date time;

	private String remoteAddr;

	private String userAgent;

	private String requestUri;

	private String method;

	private String serviceId;

	private String params;

	private String exception;
}
