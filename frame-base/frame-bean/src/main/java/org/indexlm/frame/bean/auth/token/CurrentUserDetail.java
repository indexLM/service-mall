package org.indexlm.frame.bean.auth.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * 默认当前用户信息
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Data
public class CurrentUserDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 用户类型，1、员工、2、用户 3、管理员
     */
    private Integer userType;
    /**
     * 供应商门店账号所属门店ID
     */
    private Long branchId;

    public CurrentUserDetail() {
    }

    public CurrentUserDetail(Long userId, Integer userType, Long branchId) {
        this.userId = userId;
        this.userType = userType;
        this.branchId = branchId;
    }

}
