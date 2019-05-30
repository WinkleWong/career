package com.career.careeroauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CareerOauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(CareerOauth2Application.class, args);
	}

}
