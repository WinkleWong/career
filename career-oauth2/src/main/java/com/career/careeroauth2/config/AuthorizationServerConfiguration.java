package com.career.careeroauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2019 ChowSangSang . All rights reserved.Group
 *
 * @Description: career
 * @Package: com.career.careeroauth2.config
 * @Author: Winkle.huang.w.k
 * @Date: 2019/5/29
 * @Version: 1.0
 */
@Configuration
@Order(Integer.MIN_VALUE)
@RefreshScope
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${jwt.sign-key}")
	private String signKey;

	@Value("${jwt.storepass}")
	private String storepass;


	/**
	 * 配置客户端信息
	 * 客户端的信息既可以放在内存中，也可以放在数据库中，需要配置以下信息：
	 * clientId: 客户端Id,需要在Authorization Server 中是唯一的
	 * secret: 客户端的密码
	 * scope: 客户端的域
	 * authorizedGrantTypes: 授权类型
	 * authorities: 权限信息
	 *
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        TODO: 持久化到DB
        clients.inMemory()
				// client_id
                .withClient("winkle")
				// client_secret
                .secret(passwordEncoder.encode("winkle"))
				// grant_type
                .authorizedGrantTypes("authorization_code", "client_credentials", "password", "refresh_token")
                // 该client允许的授权类型 "authorization_code","refresh_token",
				// 允许的授权范围
                .scopes("server")
				//自动授权，false时会跳转到授权页面，需手动点击按钮来授权
                .autoApprove(true);
	}
	
	/**
	 * Token 节点安全策略
	 * @author Winkle.Huang.w.k
	 * @description Token 节点安全策略
	 * @date 2019/5/29 17:20
	 * @param [security]
	 * @return void
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("permitAll()")
				.allowFormAuthenticationForClients();
	}

	/**
	 * 配置授权Token的节点和Token服务
	 * authenticationManager: 只有配置了该项，密码认证才会开启
	 * userDetailsService: 配置获取用户认证信息的接口
	 * authorizationCodeServices: 配置验证码法务
	 * implicitGrantService: 配置管理implict验证的状态
	 * tokenGranter: 配置Token Granter
	 * Token 的管理策略 ：
	 * -InMemoryTokenStore:Token存储在内存中
	 * -JdbcTokenStore: Token存储在数据库中
	 * -JwtTokenStore: 采用JWT形式
	 * configure
	 * @author Winkle.Huang.w.k
	 * @description configure
	 * @date 2019/5/29 17:20
	 * @param [endpoints]
	 * @return void
	 */
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jwtTokenStore())
				.authenticationManager(authenticationManager)
				.accessTokenConverter(jwtAccessTokenConverter());

	}

	@Bean
	public TokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	/**
	 * 生成jwtToken
	 * @author Winkle.Huang.w.k
	 * @description 生成jwtToken
	 * @date 2019/5/29 17:44
	 * @param []
	 * @return org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
	 */
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				final Map<String, Object> additionalInformation = new HashMap<>();
				additionalInformation.put("user_info", authentication.getPrincipal().toString());
				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
				return super.enhance(accessToken, authentication);
			}
		};
		/*  RSA首次解码太慢，变更回对称加密*/
		converter.setSigningKey(signKey);

		// 修改为RSA非对称加密
//		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("auth-jwt.jks"), storepass.toCharArray()).getKeyPair("auth-jwt");
//		converter.setKeyPair(keyPair);
		return converter;
	}
}
