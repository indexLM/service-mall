package org.indexlm.frame.bean.common;

/**
 * 公共代码枚举
 *
 * @author LiuMing
 * @since 2021/2/25
 */
public enum AuthCode implements ResultCode  {

    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST( 10011, "用户名不存在"),
    /**
     * 密码错误
     */
    PASSWORD_ERROR( 10012, "密码错误"),
    /**
     * 登录失败
     */
    LOGIN_FAILED( 10013, "登录失败"),
    /**
     * 无效的请求头
     */
    UN_AUTHORISE( 10014, "无效的请求头"),
    /**
     * 令牌已过期
     */
    TOKEN_EXPIRE( 10015, "令牌已过期"),
    ;
    /**
     * 操作代码
     */
    int code;

    /**
     * 提示信息
     */
    String message;

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 提示信息
     */
    AuthCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
