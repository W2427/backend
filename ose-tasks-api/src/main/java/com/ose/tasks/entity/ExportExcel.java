package com.ose.tasks.entity;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "export_excel")
public class ExportExcel extends BaseVersionedBizEntity {

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "导出Excel视图名称")
    @Column
    private String excelViewName;

    @Schema(description = "导出Excel的说明")
    @Column
    private String excelDesc;

    @Schema(description = "Excel文件的生成时间")
    @Column
    private Long excelGenDate;

    @Schema(description = "Excel文件是否正在生成")
    @Column
    private boolean generating;

    @Schema(description = "Excel 存储的路径")
    @Column(length = 500)
    private String filePath;

    @Schema(description = "Excel是否异步生成")
    @Column
    private boolean async;

    @Schema(description = "Excel文件存储位置")
    @Column(length = 500)
    private Long fileId;


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

    public String getExcelViewName() {
        return excelViewName;
    }

    public void setExcelViewName(String excelViewName) {
        this.excelViewName = excelViewName;
    }

    public String getExcelDesc() {
        return excelDesc;
    }

    public void setExcelDesc(String excelDesc) {
        this.excelDesc = excelDesc;
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

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
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
}
