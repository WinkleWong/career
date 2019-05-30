package com.career.careersidm.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careersidm.models
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/30
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SidmUser {
	@ApiModelProperty(value = "员工姓名",dataType = "String",example = "90175")
	private String userId;

	@ApiModelProperty(value = "员工姓名",dataType = "String",example = "黄文科")
	private String name;

	@ApiModelProperty(value = "用于陈列的名字",dataType = "String",example = "C78 黄文科 Winkle")
	private String displayName;

	@ApiModelProperty(value = "员工所在地区",dataType = "String",example = "CN")
	private String region;

	@ApiModelProperty(value = "员工类型",dataType = "String",example = "F")
	private String employeeType;

	@ApiModelProperty(value = "员工所在部门",dataType = "String",example = "C78")
	private String deptCde;

}
