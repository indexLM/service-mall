package org.indexlm.frame.bean.common.exception;

import cn.hutool.core.util.StrUtil;
import org.indexlm.frame.bean.common.CommonCode;
import org.indexlm.frame.bean.common.ResultCode;

/**
 * 自定义异常 捕获会打印info级别日志
 *
 * @author LiuMing
 * @since 2020/7/10
 */
public class LogInfoException extends RuntimeException {
    /**
     * 结果状态信息
     */
    ResultCode resultCode;
    /**
     * 信息
     */
    String message;

    /**
     * 异常信息为错误代码+异常信息
     *
     * @param resultCode {@link ResultCode} 状态码枚举
     * @author LiuMing
     * @since 2021/2/25
     */
    private LogInfoException(ResultCode resultCode) {
        super("状态码:" + resultCode.code() + "状态信息:" + resultCode.message());
        this.resultCode=resultCode;
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
    protected LogInfoException(ResultCode resultCode, String message) {
        super("状态码:" + resultCode.code() + "状态信息:" + (StrUtil.isBlank(message) ? resultCode.message() : message));
        this.resultCode=resultCode;
        this.message = message;
    }

    /**
     * 使用此静态方法抛出自定义异常
     *
     * @param resultCode {@link ResultCode} 状态码枚举
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(ResultCode resultCode) {
        throw new LogInfoException(resultCode);
    }

    /**
     * 使用此静态方法抛出自定义异常与自定义异常信息
     *
     * @param resultCode {@link ResultCode} 状态码枚举
     * @param message    状态信息
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(ResultCode resultCode, String message) {
        throw new LogInfoException(resultCode, message);
    }

    /**
     * 使用此静态方法抛出自定义异常与自定义异常信息
     *
     * @param message    状态信息
     * @author LiuMing
     * @since 2021/2/25
     */
    public static void cast(String message) {
        throw new LogInfoException(CommonCode.FAIL, message);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
