package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 退库清单关联出库清单表表
 */
@Entity
@Table(name = "mat_f_material_return_receipt_relation_issue")
public class FMaterialReturnReceiptRelationIssueEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "退库单ID")
    private Long fmReturnId;

    @Schema(description = "OSE 出库单ID")
    private Long fmirId;

    @Schema(description = "OSE 出库清单号")
    private String fmirCode;

    @Schema(description = "SPM 退库单ID")
    private Long spmReturnNumberId;

    @Schema(description = "SPM 退库单号")
    private String spmReturnNumber;

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

    public Long getFmReturnId() {
        return fmReturnId;
    }

    public void setFmReturnId(Long fmReturnId) {
        this.fmReturnId = fmReturnId;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public Long getSpmReturnNumberId() {
        return spmReturnNumberId;
    }

    public void setSpmReturnNumberId(Long spmReturnNumberId) {
        this.spmReturnNumberId = spmReturnNumberId;
    }

    public String getSpmReturnNumber() {
        return spmReturnNumber;
    }

    public void setSpmReturnNumber(String spmReturnNumber) {
        this.spmReturnNumber = spmReturnNumber;
    }

    @Schema(description = "OSE 出库单信息")
    @JsonProperty(value = "fmirId")
    public ReferenceData getFmirIdRef() {
        return this.fmirId == null
            ? null
            : new ReferenceData(this.fmirId);
    }

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }
}
