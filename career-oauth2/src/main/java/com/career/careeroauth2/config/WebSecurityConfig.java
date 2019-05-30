package com.career.careeroauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careeroauth2.config
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/30
 * @Version: 1.0
 */
@Order(10)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/auth/login")
				.loginProcessingUrl("/auth/form")
				.and()
				.authorizeRequests()
				.antMatchers("/auth/login",
						"/auth/form",
						"/oauth/token",
						"/token**",
						"/actuator/**",
						"/oauth2/oauth/token",
						"/**/*.js",
						"/**/*.css",
						"/**/*.jpg",
						"/**/*.png",
						"/**/*.woff2"
						)
				.permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
