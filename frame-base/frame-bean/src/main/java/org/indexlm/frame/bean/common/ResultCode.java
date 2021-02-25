package org.indexlm.frame.bean.common;

/**
 * 结果代码接口
 *
 * @author LiuMing
 * @since 2021/2/25
 */
public interface ResultCode {
    /**
     * 响应状态码
     *
     * @return {@link int}
     * @author LiuMing
     * @since 2021/2/25
     */
    int code();

    /**
     * 提示信息
     *
     * @return {@link String}
     * @author LiuMing
     * @since 2021/2/25
     */
    String message();
}
