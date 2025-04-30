package com.ose.tasks.dto.trident;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.tasks.vo.trident.CheckSheetType;

import java.util.Date;


public class ItrCreateDTO extends BaseDTO {


    private static final long serialVersionUID = -2590316633633886883L;
    private Integer tridentCheckSheetId;

    private Date tridentCompletedDate;

    private Integer tridentTagId;

    private Integer tridentItrId;

    private String tridentFileRef;

    private Integer tridentProjPhaseId;

    private Long projectId;

    private Long fileId;

    private String itrCheckTemplate;

    private Long entityId;

    private String entityNo;

    private Long inChargePerson;

    private CheckSheetStage checkSheetStage;

    private CheckSheetTridentTbl checkSheetTridentTbl;

    private CheckSheetType checkSheetType;

    private Boolean isInTrident;

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

    public Integer getTridentItrId() {
        return tridentItrId;
    }

    public void setTridentItrId(Integer tridentItrId) {
        this.tridentItrId = tridentItrId;
    }

    public CheckSheetTridentTbl getCheckSheetTridentTbl() {
        return checkSheetTridentTbl;
    }

    public void setCheckSheetTridentTbl(CheckSheetTridentTbl checkSheetTridentTbl) {
        this.checkSheetTridentTbl = checkSheetTridentTbl;
    }
}
