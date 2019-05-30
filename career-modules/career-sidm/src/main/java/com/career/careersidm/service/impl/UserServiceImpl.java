package com.career.careersidm.service.impl;

import com.career.careersidm.models.SidmUser;
import com.career.careersidm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careersidm.service.impl
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/30
 * @Version: 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Value("${user.userId}")
	private String userId;

	@Value("${user.name}")
	private String name;

	@Value("${user.region}")
	private String region;

	@Value("${user.employeeType}")
	private String employeeType;

	@Value("${user.deptCde}")
	private String deptCde;

	@Value("${user.displayName}")
	private String displayName;

	@Override
	public SidmUser getSidmUserByUserId(String id) {
		if (userId.equals(id)) {
			return new SidmUser(userId, name, displayName, region, employeeType, deptCde);
		}
		return null;
	}
}
