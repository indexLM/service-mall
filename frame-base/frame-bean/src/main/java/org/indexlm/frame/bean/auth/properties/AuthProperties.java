package org.indexlm.frame.bean.auth.properties;

/**
 * 认证常量
 *
 * @author LiuMing
 * @since 2020/9/16
 */
public interface AuthProperties {
    /**
     * 账号类型  用户名
     */
    int ACCOUNT_TYPE_PASSWORD = 10201;
    /**
     * 账号类型 手机号
     */
    int ACCOUNT_TYPE_MOBILE = 10202;
    /**
     * 账号类型 邮箱地址
     */
    int ACCOUNT_TYPE_EMAIL = 10203;
    /**
     * 账号类型 身份证
     */
    int ACCOUNT_TYPE_CARD = 10204;
    /**
     * 账号类型 微信
     */
    int ACCOUNT_TYPE_WECHAT = 10205;
    /**
     * 账号类型 员工号
     */
    int ACCOUNT_TYPE_STAFFNO = 10206;
    /**
     * 账号类型 手机号 验证码 redis存储
     */
    int ACCOUNT_TYPE_MOBILE_CODE = 10096;
    /**
     * #用户请求头 的key
     */
    String USER_TOKEN_REDIS_KEY = "user:token";
    /**
     * #用户信息再redis中的key的前缀  格式 setkey +  token
     */
    String ACCESS_TOKEN_HEADER = "user-token";
    /**
     * open开放平台所需要的token
     */
    String OPEN_ACCESS_TOKEN_HEADER = "Access-Token";
    /**
     * 令牌过期时间
     */
    long ACCESS_ACTIVE_TIME = 60 * 60 * 12L;

    /**
     * 令牌最短过期时间
     */
    Long ACCESS_MIN_HAVE_ACTIVE_TIME = 60 * 10L;

    /**
     * 登录用户名
     */
    String LOGIN_USERNAME = "login_username";
    /**
     * 登录IP
     */
    String IP = "ip";

    /**
     * jwt令牌
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * 刷新令牌
     */
    String REFRESH_TOKEN = "refresh_token";
    /**
     * 短令牌
     */
    String JTI_TOKEN = "jti";
    /**
     * 上次登录时间
     */
    String LAST_LOGIN_TIME = "last_login_time";

    /**
     * 用户ID
     */
    String USER_ID = "userId";

    /**
     * 登录验证码redis中的key
     */
    String LOGIN_CODE_REDIS_KEY = "code:login";

    /**
     * 登录验证码redis中的key
     */
    long LOGIN_CODE_REDIS_KEY_TIME = 60 * 10L;

    /**
     * 登录分隔符
     */
    String LOGIN_TAG = "===";
    /**
     * 登录携带信息的分隔符
     */
    String LOGIN_MSG_TAG = "=";
    /**
     * 管理员不用密码直接登录
     */
    String LOGIN_ADMIN_ACCOUNT="1";
    /**
     * 管理员不用密码直接登录 的默认密码
     */
    String LOGIN_ADMIN_ACCOUNT_PASSWORD="123456";
    /**
     * 管理员不用密码直接登录 的默认密码 (加密后)
     */
    String LOGIN_ADMIN_ACCOUNT_PASSWORD_ENCODE="$2a$10$vr0.XjpkGznJt6Ejfkpbv.K6XklEbp.MR3eK1RjzHpc6bMqISxbIu";

    /**
     * 手机号验证码登录
     */
    String LOGIN_MSG_CODE="2";


}
