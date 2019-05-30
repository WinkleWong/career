package com.career.careersidm.service;

import com.career.careersidm.models.SidmUser;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careersidm.service
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/30
 * @Version: 1.0
 */
public interface UserService {
	SidmUser getSidmUserByUserId(String userID);
}
