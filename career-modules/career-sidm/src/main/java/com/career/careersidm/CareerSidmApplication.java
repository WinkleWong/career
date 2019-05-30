package com.career.careersidm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CareerSidmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerSidmApplication.class, args);
	}

}
