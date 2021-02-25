package org.indexlm.frame.bean.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应模型
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Data
public class PageRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 当前页
     */
    private Long currentPage;
    /**
     * 长度
     */
    private Long size;
    /**
     * 当前页
     */
    private Long pages;
    /**
     * 是否是下一页
     */
    private Boolean isNext;
    /**
     * 每页显示数据记录的集合
     */
    private List<T> dataList;

    public PageRes() {
    }

    public PageRes(Long totalCount, List<T> dataList) {
        this.totalCount = totalCount;
        this.dataList = dataList;
    }
}
