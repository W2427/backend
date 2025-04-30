package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 放行单实体类。
 */
@Entity
@Table(name = "mat_heat_no",
indexes = {
    @Index(columnList = "orgId,projectId,heatNoCode,batchNoCode")
})
public class HeatNoEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    // 炉号
    @Schema(description = "炉号")
    private String heatNoCode;

    // 炉批号
    @Schema(description = "批号")
    private String batchNoCode;

    // 说明
    @Schema(description = "炉批号说明")
    private String heatNoDesc;

    @Schema(description = "证书编号 certCode")
    private String certCode;

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

    public String getHeatNoCode() {
        String printBatchNo = StringUtils.isEmpty(batchNoCode) ? "" : String.format("/%s", batchNoCode);

        return String.format("%s%s", StringUtils.isEmpty(heatNoCode) ? "" : heatNoCode, printBatchNo);
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getHeatNoDesc() {
        return heatNoDesc;
    }

    public void setHeatNoDesc(String heatNoDesc) {
        this.heatNoDesc = heatNoDesc;
    }

    public String getBatchNoCode() {
        return batchNoCode;
    }

    public void setBatchNoCode(String batchNoCode) {
        this.batchNoCode = batchNoCode;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }
}
