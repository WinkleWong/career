package com.career.careersidm.service.impl;

import com.alibaba.fastjson.JSON;
import com.career.careersidm.models.SidmUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

//	@Autowired
//	ObjectMapper mapper;

//	/*get list value from properties file*/
//	@Value("#{'${recipients}'.split(',')}")
//	private List<String> recipients;
//
//	/*get map value from properties file*/
//	@Value("#{${winkle}}")
//	private Map<String, String> winkle;
//
//	@Test
//	public void test() {
//		log.warn(JSON.toJSONString(recipients));
//		log.warn(JSON.toJSONString(winkle));
//		log.warn("test origin/master");
//	}

	@Test
	public void test() throws Exception{
		String jsonList = "[{\"userId\":\"100001\",\"name\":\"Winkle\"},{\"userId\":\"100002\",\"name\":\"James\"}]";
		String jsonStr = "{\"userId\":\"100001\",\"name\":\"Winkle\"}";
		ObjectMapper mapper = new ObjectMapper();
		/*JSON -> List*/
		List<SidmUser> userList = mapper.readValue(jsonList, new TypeReference<List<SidmUser>>(){});
		/*JSON -> Object*/
		SidmUser user = mapper.readValue(jsonStr, SidmUser.class);
		/*Object -> JSON String*/
		log.warn(mapper.writeValueAsString(userList));
		log.warn(mapper.writeValueAsString(user));
	}
}