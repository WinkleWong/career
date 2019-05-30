package com.career.common.bean.common;

import lombok.Data;

import java.util.List;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.common.bean.common
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
@Data
public class JwtInfo {
	private String userName;
	private List<String> roleList;

	public JwtInfo(String userName, List<String> roleList) {
		this.userName = userName;
		this.roleList = roleList;
	}
}
