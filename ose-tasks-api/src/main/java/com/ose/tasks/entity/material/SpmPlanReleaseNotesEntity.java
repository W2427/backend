package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * SPM 计划放行单 实体类。
 */
@Entity
@Table(name = "mat_spm_plan_release_note")
public class SpmPlanReleaseNotesEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "SPM 放行单编号")
    private String spmRelnNumber;
//
//    @Schema(description = "SPM 放行单ID")
//    private int spmRelnId;
//
//    @Schema(description = "SPM 合同ID")
//    private int spmPohId;
//
//    @Schema(description = "SPM 合同编号")
//    private String spmPoNumber;
//
//    @Schema(description = "SPM 合同序号")
//    private int spmPoSupp;

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

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }
//
//    public int getSpmRelnId() {
//        return spmRelnId;
//    }
//
//    public void setSpmRelnId(int spmRelnId) {
//        this.spmRelnId = spmRelnId;
//    }
//
//    public int getSpmPohId() {
//        return spmPohId;
//    }
//
//    public void setSpmPohId(int spmPohId) {
//        this.spmPohId = spmPohId;
//    }
//
//    public String getSpmPoNumber() {
//        return spmPoNumber;
//    }
//
//    public void setSpmPoNumber(String spmPoNumber) {
//        this.spmPoNumber = spmPoNumber;
//    }
//
//    public int getSpmPoSupp() {
//        return spmPoSupp;
//    }
//
//    public void setSpmPoSupp(int spmPoSupp) {
//        this.spmPoSupp = spmPoSupp;
//    }
}
