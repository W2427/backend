package com.ose.tasks.entity.wps.simple;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "wps_simplified")
public class WpsSimplified extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5909412999289740497L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "WPS编号")
    @Column
    private String no;

    @Schema(description = "焊材类型")
    private String weldMaterialType;

    @Schema(description = "备注")
    @Column(length = 5000)
    private String remark;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWeldMaterialType() {
        return weldMaterialType;
    }

    public void setWeldMaterialType(String weldMaterialType) {
        this.weldMaterialType = weldMaterialType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
