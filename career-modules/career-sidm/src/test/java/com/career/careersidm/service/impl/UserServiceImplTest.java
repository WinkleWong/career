package com.career.careersidm.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


/**
 * Copyright © 2019 ChowSangSang Group All Rights Reserved.
 *
 * @Description: career
 * @Package: com.career.careersidm.service.impl
 * @Author: Winkle.huang.w.k
 * @Date: 2019/10/14
 * @Version: 1.0
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	/*get list value from properties file*/
	@Value("#{'${recipients}'.split(',')}")
	private List<String> recipients;

	/*get map value from properties file*/
	@Value("#{${winkle}}")
	private Map<String, String> winkle;

	@Test
	public void test() {
		log.warn(JSON.toJSONString(recipients));
		log.warn(JSON.toJSONString(winkle));
	}
}