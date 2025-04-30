package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "work_code")
public class WorkCode extends BaseEntity {

    @Schema(description = "组织Id")
    @Column
    private Long orgId;

    @Schema(description = "项目Id")
    @Column
    private Long projectId;

    @Schema(description = "工作项代码")
    @Column
    private String workCode;

    @Schema(description = "CUTTING权重")
    @Column
    private Integer cuttingWeights;

    @Schema(description = "FIT-UP权重")
    @Column
    private Integer fitUpWeights;

    @Schema(description = "WELDING权重")
    @Column
    private Integer weldingWeights;

    @Schema(description = "NDT权重")
    @Column
    private Integer ndtWeights;

    @Schema(description = "RELEASE权重")
    @Column
    private Integer releaseWeights;

    @Schema(description = "导出工作项报表名称")
    @Column
    private String wcReportName;

    @Schema(description = "工作项说明")
    @Column
    private String wcDesc;

    @Schema(description = "Excel文件的生成时间")
    @Column
    private Long excelGenDate;

    @Schema(description = "Excel文件是否在生成")
    @Column
    private boolean generating;

    @Schema(description = "Excel文件是否异步生成")
    @Column
    private boolean async;

    @Schema(description = "Excel 的存储路径")
    @Column(length = 500)
    private String filePath;

    @Schema(description = "Excel文件存储位置")
    @Column(length = 500)
    private Long fileId;

    @Schema(description = "创建时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date lastModifiedAt;

    @Schema(description = "最后操作人")
    @Column
    @JsonIgnore
    private Long lastModifiedBy;


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

    public String getWcReportName() {
        return wcReportName;
    }

    public void setWcReportName(String wcReportName) {
        this.wcReportName = wcReportName;
    }

    public String getWcDesc() {
        return wcDesc;
    }

    public void setWcDesc(String wcDesc) {
        this.wcDesc = wcDesc;
    }

    public Long getExcelGenDate() {
        return excelGenDate;
    }

    public void setExcelGenDate(Long excelGenDate) {
        this.excelGenDate = excelGenDate;
    }

    public boolean isGenerating() {
        return generating;
    }

    public void setGenerating(boolean generating) {
        this.generating = generating;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public Integer getCuttingWeights() {
        return cuttingWeights;
    }

    public void setCuttingWeights(Integer cuttingWeights) {
        this.cuttingWeights = cuttingWeights;
    }

    public Integer getFitUpWeights() {
        return fitUpWeights;
    }

    public void setFitUpWeights(Integer fitUpWeights) {
        this.fitUpWeights = fitUpWeights;
    }

    public Integer getWeldingWeights() {
        return weldingWeights;
    }

    public void setWeldingWeights(Integer weldingWeights) {
        this.weldingWeights = weldingWeights;
    }

    public Integer getNdtWeights() {
        return ndtWeights;
    }

    public void setNdtWeights(Integer ndtWeights) {
        this.ndtWeights = ndtWeights;
    }

    public Integer getReleaseWeights() {
        return releaseWeights;
    }

    public void setReleaseWeights(Integer releaseWeights) {
        this.releaseWeights = releaseWeights;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
