package com.career.careergateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careergateway.config
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "filter.ignore")
public class FilterIgnorePropertiesConfig {
	private List<String> urls = new ArrayList<>();
}
