package org.indexlm.frame.bean.common.exception;

import cn.hutool.core.util.StrUtil;
import org.indexlm.frame.bean.common.CommonCode;
import org.indexlm.frame.bean.common.ResultCode;

/**
 * 自定义异常 捕获会打印error级别日志
 *
 * @author LiuMing
 * @since 2021/2/25
 */
public class LogErrorException extends RuntimeException {

    /**
     * 状态码
     */
    int code;
    /**
     * 信息
     */
    String message;

    /**
     * 异常信息为错误代码+异常信息
     *
     * @param resultCode {@link ResultCode}
     * @author LiuMing
     * @since 2021/2/25
     */
    private LogErrorException(ResultCode resultCode) {
        super("状态码:" + resultCode.code() + "状态信息:" + resultCode.message());
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    /**
     * 若没有上传自定义异常信息,则使用错误码的错误信息
     * 异常信息为状态码+状态信息
     *
     * @param resultCode {@link ResultCode} 状态码枚举
     * @param message 状态信息
     * @author LiuMing
     * @since 2021/2/25
     */
    protected LogErrorException(ResultCode resultCode, String message) {
        super("状态码:" + resultCode.code() + "状态信息:" + (StrUtil.isBlank(message) ? resultCode.message() : message));
        this.code = resultCode.code();
        this.message = message;
    }

    /**
     * 使用此静态方法抛出自定义异常
     *
     * @param resultCode {@link ResultCode} 状态码
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(ResultCode resultCode) {
        throw new LogErrorException(resultCode);
    }

    /**
     * 使用此静态方法抛出自定义异常与自定义异常信息
     *
     * @param resultCode {@link ResultCode} 状态码
     * @param message    状态信息
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(ResultCode resultCode, String message) {
        throw new LogErrorException(resultCode, message);
    }

    /**
     * 使用此静态方法抛出自定义异常与自定义异常信息
     *
     * @param message    状态信息
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(String message) {
        throw new LogErrorException(CommonCode.FAIL, message);
    }
    public int getCode() {
        return code;
    }
}
