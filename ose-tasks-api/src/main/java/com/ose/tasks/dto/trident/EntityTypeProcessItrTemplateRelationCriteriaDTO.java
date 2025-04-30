package com.ose.tasks.dto.trident;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetType;


public class EntityTypeProcessItrTemplateRelationCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = -2537718894285526224L;
    private Long entityCategoryProcessRelationId;

	private String entityType;

	private String entitySubType;

	private Long entitySubTypeId;

	private Long processId;

    private String processStage;

    private String keyword;

    private String process;

    private CheckSheetStage checkSheetStage;

    private CheckSheetType checkSheetType;

    private Long projectId;

    private Long fileId;

    private String itrTemplateNo;

    private String itrTemplateDesc;

    private Integer assetTypeId;

    public Long getEntityCategoryProcessRelationId() {
        return entityCategoryProcessRelationId;
    }

    public void setEntityCategoryProcessRelationId(Long entityCategoryProcessRelationId) {
        this.entityCategoryProcessRelationId = entityCategoryProcessRelationId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getEntityCategoryId() {
        return entitySubTypeId;
    }

    public void setEntityCategoryId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public CheckSheetStage getCheckSheetStage() {
        return checkSheetStage;
    }

    public void setCheckSheetStage(CheckSheetStage checkSheetStage) {
        this.checkSheetStage = checkSheetStage;
    }

    public CheckSheetType getCheckSheetType() {
        return checkSheetType;
    }

    public void setCheckSheetType(CheckSheetType checkSheetType) {
        this.checkSheetType = checkSheetType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getItrTemplateNo() {
        return itrTemplateNo;
    }

    public void setItrTemplateNo(String itrTemplateNo) {
        this.itrTemplateNo = itrTemplateNo;
    }

    public String getItrTemplateDesc() {
        return itrTemplateDesc;
    }

    public void setItrTemplateDesc(String itrTemplateDesc) {
        this.itrTemplateDesc = itrTemplateDesc;
    }

    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
