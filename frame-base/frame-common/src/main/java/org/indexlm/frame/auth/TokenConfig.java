package org.indexlm.frame.auth;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

/**
 * 令牌配置类
 *
 * @author  LiuMing
 * @since 2021/2/27
 */
@Configuration
public class TokenConfig {

    @Autowired
    private CustomAccessTokenConverter customAccessTokenConverter;

    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //将自定义的token转换放入jwt转换器中
        converter.setAccessTokenConverter(customAccessTokenConverter);
        ClassPathResource resource = new ClassPathResource("pub.key");
        String publicKey ;
        try {
            publicKey = IOUtils.toString(resource.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        //资源服务器使用公钥来验证
        converter.setVerifierKey(publicKey);
        return converter;
    }

}
