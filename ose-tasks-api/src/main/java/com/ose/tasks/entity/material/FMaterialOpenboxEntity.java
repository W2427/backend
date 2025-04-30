package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料开箱检验报告实体类。
 */
@Entity
@Table(name = "mat_f_material_open_box_inspection_report")
public class FMaterialOpenboxEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "fmobir_code")
    private String fmobirCode;

    @Column(name = "seq_number")
    private Integer seqNumber;

    @Column(name = "fmst_id")
    @Schema(description = "材料盘点 ID")
    private Long fmstId;

    @Column(name = "report_desc")
    private String reportDesc;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "inspection_status")
    private String openBoxInspectionResult;

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

    public String getFmobirCode() {
        return fmobirCode;
    }

    public void setFmobirCode(String fmobirCode) {
        this.fmobirCode = fmobirCode;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Long getFmstId() {
        return fmstId;
    }

    public void setFmstId(Long fmstId) {
        this.fmstId = fmstId;
    }

    public String getReportDesc() {
        return reportDesc;
    }

    public void setReportDesc(String reportDesc) {
        this.reportDesc = reportDesc;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOpenBoxInspectionResult() {
        return openBoxInspectionResult;
    }

    public void setOpenBoxInspectionResult(String openBoxInspectionResult) {
        this.openBoxInspectionResult = openBoxInspectionResult;
    }

    @Schema(description = "材料盘点信息")
    @JsonProperty(value = "fmstId", access = READ_ONLY)
    public ReferenceData getFmstIdRef() {
        return this.fmstId == null
            ? null
            : new ReferenceData(this.fmstId);
    }
}
