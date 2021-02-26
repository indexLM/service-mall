package org.indexlm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * token配置类
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Configuration()
public class TokenConfig {

    /**
     * token保存策略
     *
     * @return {@link TokenStore}
     * @author LiuMing
     * @since 2021/2/25
     */
    @Bean(name = "tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 设置私钥
     *
     * @return {@link JwtAccessTokenConverter}
     * @author LiuMing
     * @since 2021/2/25
     */
    @Bean(name = "jwtAccessTokenConverter")
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //非对称密钥加密方式，私钥加密
        ClassPathResource pathResource = new ClassPathResource("lecare-token.keystore");
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(pathResource, "lecare".toCharArray());
        KeyPair keyPair = keyFactory.getKeyPair("lecare");
        converter.setKeyPair(keyPair);
        return converter;
    }
}
