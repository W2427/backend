package com.ose.tasks.entity.bpm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.ose.entity.BaseBizEntity;

/**
 * 工序阶段 实体类。
 */
@Entity
@Table(
    name = "bpm_process_stage",
    indexes = {
        @Index(columnList = "project_id,name_en,status"),
        @Index(columnList = "org_id,project_id,status")
    }
)
public class BpmProcessStage extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8635813266138087045L;

    //项目id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    //组织id
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 工序名称
    @Column(name = "name_cn", nullable = false, length = 128)
    @NotNull(message = "ProcessStage's name is required")
    private String nameCn;

    // 工序名称-英文
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;

    // 排序
    @Column(name = "order_no", length = 256, columnDefinition = "tinyint default 0")
    private short orderNo = 0;

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

    public short getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(short orderNo) {
        this.orderNo = orderNo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
