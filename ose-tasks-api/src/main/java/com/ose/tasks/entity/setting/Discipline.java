package com.ose.tasks.entity.setting;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * 专业实体类。
 */
@Entity
@Table(
    name = "discipline",
    indexes = {
        @Index(columnList = "name_en,status")
    }
)
public class Discipline extends BaseBizEntity {


    private static final long serialVersionUID = -7090299718207574748L;
    // 项目 ID
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // 组织 ID
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 工序名称
    @Column(name = "name_cn", nullable = false, length = 50)
    @NotNull(message = "discipline's name is required")
    private String nameCn;

    // 工序名称-英文
    @Column(name = "name_en", nullable = false, length = 30)
    private String nameEn;

    // 排序
    @Column(name = "order_no", length = 11, columnDefinition = "int default 0")
    private int orderNo = 0;

    // 备注
    @Column(length = 500)
    private String memo;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
