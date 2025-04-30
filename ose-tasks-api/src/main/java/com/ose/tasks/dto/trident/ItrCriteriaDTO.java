package com.ose.tasks.dto.trident;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.tasks.vo.trident.CheckSheetType;

import java.util.Date;
import java.util.Set;


public class ItrCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = 8576212971511761416L;
    private String keyword;

    private Integer tridentCheckSheetId;

    private Date tridentCompletedDate;

    private Integer tridentTagId;

    private Integer tridentItrId;

    private String tridentFileRef;

    private Integer tridentProjPhaseId;

    private Long projectId;

    private Set<Long> ancestorHierarchyId;

    private String discipline;

    private Long fileId;

    private String itrCheckTemplate;

    private Long entityId;

    private String entityNo;

    private Long inChargePerson;

    private String ssCode;

    private CheckSheetStage checkSheetStage;

    private CheckSheetTridentTbl checkSheetTridentTbl;

    private CheckSheetType checkSheetType;

    private Boolean isInTrident;

    private Long processId;

    private Long processStageId;

    private Long entityTypeId;

    private Long entitySubTypeId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getTridentCheckSheetId() {
        return tridentCheckSheetId;
    }

    public void setTridentCheckSheetId(Integer tridentCheckSheetId) {
        this.tridentCheckSheetId = tridentCheckSheetId;
    }

    public Date getTridentCompletedDate() {
        return tridentCompletedDate;
    }

    public void setTridentCompletedDate(Date tridentCompletedDate) {
        this.tridentCompletedDate = tridentCompletedDate;
    }

    public Integer getTridentTagId() {
        return tridentTagId;
    }

    public void setTridentTagId(Integer tridentTagId) {
        this.tridentTagId = tridentTagId;
    }

    public Integer getTridentItrId() {
        return tridentItrId;
    }

    public void setTridentItrId(Integer tridentItrId) {
        this.tridentItrId = tridentItrId;
    }

    public String getTridentFileRef() {
        return tridentFileRef;
    }

    public void setTridentFileRef(String tridentFileRef) {
        this.tridentFileRef = tridentFileRef;
    }

    public Integer getTridentProjPhaseId() {
        return tridentProjPhaseId;
    }

    public void setTridentProjPhaseId(Integer tridentProjPhaseId) {
        this.tridentProjPhaseId = tridentProjPhaseId;
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

    public String getItrCheckTemplate() {
        return itrCheckTemplate;
    }

    public void setItrCheckTemplate(String itrCheckTemplate) {
        this.itrCheckTemplate = itrCheckTemplate;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getInChargePerson() {
        return inChargePerson;
    }

    public void setInChargePerson(Long inChargePerson) {
        this.inChargePerson = inChargePerson;
    }

    public CheckSheetStage getCheckSheetStage() {
        return checkSheetStage;
    }

    public void setCheckSheetStage(CheckSheetStage checkSheetStage) {
        this.checkSheetStage = checkSheetStage;
    }

    public CheckSheetTridentTbl getCheckSheetTridentTbl() {
        return checkSheetTridentTbl;
    }

    public void setCheckSheetTridentTbl(CheckSheetTridentTbl checkSheetTridentTbl) {
        this.checkSheetTridentTbl = checkSheetTridentTbl;
    }

    public CheckSheetType getCheckSheetType() {
        return checkSheetType;
    }

    public void setCheckSheetType(CheckSheetType checkSheetType) {
        this.checkSheetType = checkSheetType;
    }

    public Boolean getInTrident() {
        return isInTrident;
    }

    public void setInTrident(Boolean inTrident) {
        isInTrident = inTrident;
    }

    public Set<Long> getAncestorHierarchyId() {
        return ancestorHierarchyId;
    }

    public void setAncestorHierarchyId(Set<Long> ancestorHierarchyId) {
        this.ancestorHierarchyId = ancestorHierarchyId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getSsCode() {
        return ssCode;
    }

    public void setSsCode(String ssCode) {
        this.ssCode = ssCode;
    }
}
