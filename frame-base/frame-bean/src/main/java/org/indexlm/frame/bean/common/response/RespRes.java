package org.indexlm.frame.bean.common.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.indexlm.frame.bean.common.CommonCode;
import org.indexlm.frame.bean.common.ResultCode;

import java.io.Serializable;

/**
 * 统一的返回结果类
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Data
@ApiModel(value = "响应结果", description = "响应结果")
public class RespRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 响应状态码
     */
    @ApiModelProperty(value = "响应状态码")
    int code;

    /**
     * 响应状态信息
     */
    @ApiModelProperty(value = "响应状态信息")
    String message;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;

    public RespRes() {
    }

    /**
     * 构造方法 通过ResultCode 给各个属性赋值
     *
     * @param resultCode {@link ResultCode} 提供了公共的返回结果的信息
     * @author LiuMing
     * @since 2021/2/25
     */
    private RespRes(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    /**
     * 构造方法 通过ResultCode和message给各个属性赋值
     *
     * @param resultCode {@link ResultCode} 提供了公共的返回结果的信息
     * @param message    自定义错误信息
     * @author LiuMing
     * @since 2021/2/25
     */
    private RespRes(ResultCode resultCode, String message) {
        this.code = resultCode.code();
        this.message = ((message == null || "".equals(message)) ? resultCode.message() : message);
    }

    /**
     * 返回有数据的返回结果
     *
     * @param data {@link T } 返回的数据
     * @author LiuMing
     * @since 2021/2/25
     */
    private RespRes(T data) {
        this.code = CommonCode.SUCCESS.code();
        this.message = CommonCode.SUCCESS.message();
        this.data = data;
    }

    /**
     * 操作成功
     *
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes success() {
        RespRes<Object> result = new RespRes<>(CommonCode.SUCCESS);
        result.setData(null);
        return result;
    }

    /**
     * 返回自定义Code信息
     *
     * @param resultCode {@link ResultCode} 响应状态码枚举
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes code(ResultCode resultCode) {
        RespRes<Object> RespRes = new RespRes<>(resultCode);
        RespRes.setData(null);
        return RespRes;
    }

    /**
     * 返回自定义Code信息
     *
     * @param resultCode {@link ResultCode} 响应状态码枚举
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes code(ResultCode resultCode, String message) {
        RespRes<Object> RespRes = new RespRes<>(resultCode, message);
        RespRes.setData(null);
        return RespRes;
    }


    /**
     * 返回自定义数据信息
     *
     * @param object {@link Object} 返回的数据
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes data(Object object) {
        return new RespRes<>(object);
    }

    /**
     * 改变data数据 其他参数不变
     *
     * @param resultResource
     * @param data
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes codeAndData(RespRes resultResource, Object data) {
        RespRes<Object> RespRes = new RespRes<>();
        RespRes.setCode(resultResource.code);
        RespRes.setMessage(resultResource.message);
        RespRes.setData(data);
        return RespRes;
    }

    /**
     * 返回自定义数据信息
     *
     * @param resultCode {@link ResultCode} 响应状态码枚举
     * @param object     {@link Object} 返回的数据
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes codeAndData(ResultCode resultCode, Object object) {
        RespRes RespRes = new RespRes<>(resultCode);
        RespRes.setData(object);
        return RespRes;
    }

    /**
     * 返回分页信息
     *
     * @param page {@link Page} 分页实体类
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static <T> RespRes<PageRes<T>> page(Page<T> page) {
        PageRes<T> pageRes = new PageRes<>();
        pageRes.setCurrentPage(page.getCurrent());
        pageRes.setSize(page.getSize());
        pageRes.setTotalCount(page.getTotal());
        pageRes.setPages(page.getPages());
        pageRes.setDataList(page.getRecords());
        pageRes.setIsNext(page.hasNext());
        return new RespRes<>(pageRes);
    }

    /**
     * 操作失败
     *
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes fail() {
        RespRes result = new RespRes<>(CommonCode.FAIL);
        result.setData(null);
        return result;
    }

    /**
     * 操作失败并返回自定义信息
     *
     * @return {@link RespRes}
     * @author LiuMing
     * @since 2021/2/25
     */
    public static RespRes fail(String msg) {
        RespRes<Object> result = new RespRes<>(CommonCode.FAIL);
        result.setMessage(msg);
        result.setData(null);
        return result;
    }

}
