package org.indexlm.frame.bean.common;

/**
 * 公共代码枚举
 *
 * @author LiuMing
 * @since 2021/2/25
 */
public enum CommonCode implements ResultCode  {
    /**
     * 成功状态码
     */
    SUCCESS(10000, "操作成功"),

    /**
     * 失败状态码
     */
    FAIL(11111, "操作失败"),

    /**
     * 非法参数
     */
    INVALID_PARAM( 10001, "非法参数"),
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
    CommonCode(int code, String message) {
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
