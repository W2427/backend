package com.ose.tasks.entity.trident;

import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.tasks.vo.trident.CheckSheetType;

import jakarta.persistence.*;

@Entity
@Table( name ="bpm_check_sheet" )
public class CheckSheet extends BaseEntity {


    private static final long serialVersionUID = -4755937443586047163L;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "check_sheet_id")
    private Integer checkSheetId;

    @Column(name = "check_sheet_stage" ) //A or B or M
    @Enumerated(EnumType.STRING)
    private CheckSheetStage checkSheetStage;


    @Column(name = "check_sheet_trident_tbl" ) //A or B
    @Enumerated(EnumType.STRING)
    private CheckSheetTridentTbl checkSheetTridentTbl;

    @Column(name = "check_sheet_type" ) //check list / record
    @Enumerated(EnumType.STRING)
    private CheckSheetType checkSheetType;

    @Column(name = "no")
    private String no;

    @Column(name = "description")
    private String description;

    @Column(name = "discipline" )
    private String discipline;

    @Column
    private Long checkSheetFileId;

    @Column
    private String checkSheetFilePath;


    @Column
    private Boolean isInTrident;

    public Integer getCheckSheetId() {
        return checkSheetId;
    }

    public void setCheckSheetId(Integer checkSheetId) {
        this.checkSheetId = checkSheetId;
    }

    public CheckSheetStage getCheckSheetStage() {
        return checkSheetStage;
    }

    public void setCheckSheetStage(CheckSheetStage checkSheetStage) {
        this.checkSheetStage = checkSheetStage;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Boolean getInTrident() {
        return isInTrident;
    }

    public void setInTrident(Boolean inTrident) {
        isInTrident = inTrident;
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

    public CheckSheetTridentTbl getCheckSheetTridentTbl() {
        return checkSheetTridentTbl;
    }

    public void setCheckSheetTridentTbl(CheckSheetTridentTbl checkSheetTridentTbl) {
        this.checkSheetTridentTbl = checkSheetTridentTbl;
    }

    public Long getCheckSheetFileId() {
        return checkSheetFileId;
    }

    public void setCheckSheetFileId(Long checkSheetFileId) {
        this.checkSheetFileId = checkSheetFileId;
    }

    public String getCheckSheetFilePath() {
        return checkSheetFilePath;
    }

    public void setCheckSheetFilePath(String checkSheetFilePath) {
        this.checkSheetFilePath = checkSheetFilePath;
    }
}
