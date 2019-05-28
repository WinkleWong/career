package com.career.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 * @author Winkle.Huang.w.k
 * @description 
 * @date 2019/5/22 9:49
 * @param 
 * @return 
 */
@EnableEurekaServer
@SpringBootApplication
public class CareerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerEurekaApplication.class, args);
	}

}
