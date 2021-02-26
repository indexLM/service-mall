package org.indexlm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权配置类
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Configuration
@EnableAuthorizationServer //开启授权服务
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //该对象用来将令牌信息存储到内存中
    @Resource(name = "tokenStore")
    TokenStore tokenStore;

    //客户端详情服务
    @Resource(name = "clientDetailsService")
    ClientDetailsService clientDetailsService;

    //用户详情服务
    @Resource(name = "userDetailsService")
    UserDetailsService userDetailsService;

    //用于支持授权码模式
    @Resource(name = "authorizationCodeServices")
    AuthorizationCodeServices authorizationCodeServices;

    //用于支持密码模式
    @Resource(name = "authenticationManager")
    AuthenticationManager authenticationManager;

    //jwt令牌转换器
    @Resource(name = "jwtAccessTokenConverter")
    JwtAccessTokenConverter jwtAccessTokenConverter;

    // 加密方式
    @Resource(name = "passwordEncoder")
    PasswordEncoder passwordEncoder;

    /**
     * jwt 增强
     * @return
     */
    @Bean
    public TokenEnhancer customTokenEnhancer(){
        return new CustomTokenEnhancer();
    }

    /**
     * 将客户端信息存储到数据库
     */
    @Bean(name = "clientDetailsService")
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    /**
     * 配置ClientDetailsServiceConfigurer 指定客户端信息来源  可以是内存中或者数据库中
     * 注意，除非你在下面的configure(AuthorizationServerEndpointsConfigurer)中指定了一个AuthenticationManager，否则密码授权方式不可用。
     * 至少配置一个client，否则服务器将不会启动。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 令牌管理服务   接口定义了一些操作使得你可以对令牌进行一些必要的管理，令牌可以被用来 加载身份信息，里面包含了这个令牌的相关
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        //客户端详情服务
        service.setClientDetailsService(clientDetailsService);
        //支持刷新令牌
        service.setSupportRefreshToken(true);
        // 允许重复使用refresh token
        service.setReuseRefreshToken(true);
        //令牌存储策略
        service.setTokenStore(tokenStore);
        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer(),jwtAccessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        // 令牌默认有效期24小时
        service.setAccessTokenValiditySeconds(24*3600);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(60*60*24*7);
        return service;
    }


    /**
     * 设置授权码模式的授权码如何存取
     */
    @Bean(name = "authorizationCodeServices")
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 定义授权和令牌端点以及令牌服务
     * <p>
     * Oauth2的主配置信息
     * 该方法是用来配置Authorization Server endpoints的一些非安全特性的，比如token存储、token自定义、授权类型等等的
     * 默认情况下，你不需要做任何事情，除非你需要密码授权，那么在这种情况下你需要提供一个AuthenticationManager
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //认证管理器
                .authenticationManager(authenticationManager)
                //授权码管理器
                .authorizationCodeServices(authorizationCodeServices)
                //令牌管理服务
                .tokenServices(tokenService())
                //用户信息service
                .userDetailsService(userDetailsService)
                //设置令牌请求方式
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//                //客户端信息service
//                .setClientDetailsService(clientDetailsService);
    }

    /**
     * 定义令牌端点上的安全约束
     * <p>
     * 配置授权服务器的安全，意味着实际上是/oauth/token端点。 检测token的策略
     * /oauth/authorize端点也应该是安全的
     * 默认的设置覆盖到了绝大多数需求，所以一般情况下你不需要做任何事情。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //表单认证（申请令牌）
                .allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder)
                //oauth/token_key是公开
                .tokenKeyAccess("permitAll()")
                //校验token需要认证通过
                .checkTokenAccess("permitAll()");
    }

/*
    //客户端配置 ，表明是由数据库获得客户端 客户端信息来源
    @Bean
    public JdbcClientDetailsService clientDetails(DataSource dataSource) {
        return new JdbcClientDetailsService(dataSource);
    }*/
}

