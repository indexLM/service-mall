package org.indexlm.auth.config;

import org.indexlm.auth.model.jwt.JwtDefault;
import org.indexlm.frame.bean.auth.properties.TokenProperties;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuMing
 * @since 2021/2/25
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        JwtDefault user = (JwtDefault) oAuth2Authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>(16);
        // 注意添加的额外信息，最好不要和已有的json对象中的key重名，容易出现错误
        additionalInfo.put(TokenProperties.USER_ID, user.getUserId() == null ? "" : user.getUserId());
        additionalInfo.put(TokenProperties.USER_TYPE, user.getUserType() == null ? "" : user.getUserType());
        additionalInfo.put(TokenProperties.BRANCH_ID, user.getBranchId() == null ? "" : user.getBranchId());
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}
