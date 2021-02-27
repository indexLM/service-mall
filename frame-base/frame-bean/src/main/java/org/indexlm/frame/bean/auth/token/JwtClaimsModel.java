package org.indexlm.frame.bean.auth.token;

import lombok.Data;

import java.io.Serializable;

/**
 * jwt载荷model
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Data
public class JwtClaimsModel implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long userId ;
    /**
     * 开放用户ID
     */
    private  Long openId;
    /**
     * 令牌签发时间戳
     */
    private long exp ;
}
