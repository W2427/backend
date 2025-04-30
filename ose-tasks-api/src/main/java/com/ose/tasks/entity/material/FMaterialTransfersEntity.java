package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料配送实体类。
 */
@Entity
@Table(name = "mat_f_material_transfer")
public class FMaterialTransfersEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "fmt_code")
    private String fmtCode;

    @Column(name = "fmt_desc")
    private String fmtDesc;

    @Column(name = "seq_number")
    private Integer seqNumber;

    @Column(name = "fmir_id")
    private Long fmirId;

    @Column(name = "fmreq_id")
    private Long fmreqId;

    @Column(name = "receive_site")
    private String receiveSite;

    @Column(name = "receive_person")
    private String receivePerson;

    @Column(name = "process_id")
    private Long processId;

    @Column(name = "act_inst_id")
    private Long actInstId;

    @Column(name = "is_finished")
    private boolean isFinished;

    @Transient
    private String taskDefKey;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

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

    public String getFmtCode() {
        return fmtCode;
    }

    public void setFmtCode(String fmtCode) {
        this.fmtCode = fmtCode;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public Long getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(Long fmreqId) {
        this.fmreqId = fmreqId;
    }

    public String getReceiveSite() {
        return receiveSite;
    }

    public void setReceiveSite(String receiveSite) {
        this.receiveSite = receiveSite;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getFmtDesc() {
        return fmtDesc;
    }

    public void setFmtDesc(String fmtDesc) {
        this.fmtDesc = fmtDesc;
    }

    @Schema(description = "领料单信息")
    @JsonProperty(value = "fmreqId", access = READ_ONLY)
    public ReferenceData getFmreqIdRef() {
        return this.fmreqId == null
            ? null
            : new ReferenceData(this.fmreqId);
    }

    @Schema(description = "出库清单信息")
    @JsonProperty(value = "fmirId", access = READ_ONLY)
    public ReferenceData getFmirIdRef() {
        return this.fmirId == null
            ? null
            : new ReferenceData(this.fmirId);
    }
}
