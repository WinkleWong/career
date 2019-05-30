package com.career.common.constant;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.common.constant
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
public interface CommonConstant {
	/**
	 * token请求头名称
	 */
	String REQ_HEADER = "Authorization";

	/**
	 * token分割符
	 */
	String TOKEN_SPLIT = "Bearer ";

	/**
	 * jwt签名
	 */
	String SIGN_KEY = "123";
	/**
	 * 删除
	 */
	String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	String STATUS_LOCK = "9";

	/**
	 * 菜单
	 */
	String MENU = "0";

	/**
	 * 按钮
	 */
	String BUTTON = "1";

	/**
	 * 删除标记
	 */
	String DEL_FLAG = "delFlag";

	/**
	 * 编码
	 */
	String UTF8 = "UTF-8";

	/**
	 * JSON 资源
	 */
	String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 阿里大鱼
	 */
	String ALIYUN_SMS = "aliyun_sms";

	/**
	 * 路由信息Redis保存的key
	 */
	String ROUTE_KEY = "_ROUTE_KEY";
}
