package org.indexlm.frame.auth;

import org.indexlm.frame.bean.auth.properties.TokenProperties;
import org.indexlm.frame.bean.auth.token.CurrentUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 * OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
 * 获取方式
 * <p>
 * 令牌转换
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
    private final Logger log = LoggerFactory.getLogger(CustomAccessTokenConverter.class);

    /**
     * 定制 AccessToken 转换器，为添加额外信息在服务器端获取做准备
     *
     * @param map 信息Map
     * @return {@link OAuth2Authentication}
     */
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);
        try {
            CurrentUserDetail currentUserDetail = new CurrentUserDetail();
            currentUserDetail.setUserId((map.get(TokenProperties.USER_ID) == null || "".equals(map.get(TokenProperties.USER_ID))) ? null : Long.parseLong(map.get(TokenProperties.USER_ID).toString()));
            currentUserDetail.setUserType((map.get(TokenProperties.USER_TYPE) == null || "".equals(map.get(TokenProperties.USER_TYPE))) ? null : Integer.parseInt(map.get(TokenProperties.USER_TYPE).toString()));
            currentUserDetail.setBranchId((map.get(TokenProperties.BRANCH_ID) == null || "".equals(map.get(TokenProperties.BRANCH_ID))) ? null : Long.parseLong(map.get(TokenProperties.BRANCH_ID).toString()));
            authentication.setDetails(currentUserDetail);

        } catch (Exception e) {
            log.error("解析JWT令牌失败：", e);
        }

        return authentication;
    }
}
