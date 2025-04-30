package com.ose.tasks.entity.drawing;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 详细设计图纸
 */
@Entity
@Table(name = "detail_design_drawing")
public class DetailDesignDrawing extends WBSEntityBase {

    private static final long serialVersionUID = -7837891255273680991L;

    @Schema(description = "组织 ID")
    private Long orgId;

    @Schema(description = "项目 ID")
    private Long projectId;

    @Schema(description = "导入文件 ID")
    private Long importFileId;

    @Schema(description = "批处理任务 ID")
    private Long batchTaskId;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "设计分类")
    private String engineringCategory;

//    @Schema(description = "专业")
//    private String disciplineCode;

    @Schema(description = "文档类型")
    private String documentType;

    @Schema(description = "文档编号")
    private String documentNumber;

    @Schema(description = "文档标题")
    private String documentTitle;

    @Schema(description = "有效版本")
    private String activeRevision;

    @Schema(description = "计划要求提供时间")
    private String planRequiredTime;

    @Schema(description = "实际出图时间")
    private String actualDrawingTime;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Schema(description = "子类型")
    private String subType;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

//    public String getFunction() {
//        return function;
//    }
//
//    public void setFunction(String function) {
//        this.function = function;
//    }

    @Schema(description = "图纸区域，TOPSIDE/HULL")
    private String sector;

//    @Schema(description = "文档功能")
//    private String function;


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

    public Long getImportFileId() {
        return importFileId;
    }

    public void setImportFileId(Long importFileId) {
        this.importFileId = importFileId;
    }

    public Long getBatchTaskId() {
        return batchTaskId;
    }

    public void setBatchTaskId(Long batchTaskId) {
        this.batchTaskId = batchTaskId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEngineringCategory() {
        return engineringCategory;
    }

    public void setEngineringCategory(String engineringCategory) {
        this.engineringCategory = engineringCategory;
    }

//    public String getDisciplineCode() {
//        return disciplineCode;
//    }
//
//    public void setDisciplineCode(String disciplineCode) {
//        this.disciplineCode = disciplineCode;
//    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getActiveRevision() {
        return activeRevision;
    }

    public void setActiveRevision(String activeRevision) {
        this.activeRevision = activeRevision;
    }

    public String getPlanRequiredTime() {
        return planRequiredTime;
    }

    public void setPlanRequiredTime(String planRequiredTime) {
        this.planRequiredTime = planRequiredTime;
    }

    public String getActualDrawingTime() {
        return actualDrawingTime;
    }

    public void setActualDrawingTime(String actualDrawingTime) {
        this.actualDrawingTime = actualDrawingTime;
    }


    @Override
    public String getEntitySubType() {
        return documentType;
    }

    @Override
    public String getEntityBusinessType() {
        return documentType;
    }

    public String getNo(){
        return documentNumber;
    }
}
