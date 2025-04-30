package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "overtime_apply_form_user_transfer_history")
public class OverTimeAppyFormUserTransferHistory extends BaseVersionedBizEntity {
    private static final long serialVersionUID = 5623339108800083586L;
    @Schema(description = "系统登录用户名")
    @Column(name = "overtime_apply_form_id")
    private Long overtimeApplyFormId;

    @Schema(description = "项目经理审核名称")
    @Column
    private String applyRoleName;

    @Schema(description = "项目经理审核Id")
    @Column(length = 65535)
    private String applyRoleId;

    public Long getOvertimeApplyFormId() {
        return overtimeApplyFormId;
    }

    public void setOvertimeApplyFormId(Long overtimeApplyFormId) {
        this.overtimeApplyFormId = overtimeApplyFormId;
    }

    public String getApplyRoleName() {
        return applyRoleName;
    }

    public void setApplyRoleName(String applyRoleName) {
        this.applyRoleName = applyRoleName;
    }

    public String getApplyRoleId() {
        return applyRoleId;
    }

    public void setApplyRoleId(String applyRoleId) {
        this.applyRoleId = applyRoleId;
    }
}
