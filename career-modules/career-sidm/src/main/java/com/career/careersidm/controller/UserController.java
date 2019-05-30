package com.career.careersidm.controller;

import com.career.careersidm.models.SidmUser;
import com.career.careersidm.service.UserService;
import com.career.common.msg.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careersidm.controller
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/v1/{userId}")
	public R<SidmUser> getUserById(@PathVariable String userId) {
		SidmUser user = userService.getSidmUserByUserId(userId);
		return new R<>(user);
	}
}
