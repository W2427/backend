package com.ose.tasks.dto.trident;

import com.ose.dto.PageDTO;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.tasks.vo.trident.CheckSheetType;


public class CheckSheetCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = -6389574327722068864L;
    private Integer checkSheetId;

    private CheckSheetStage checkSheetStage;


    private CheckSheetTridentTbl checkSheetTridentTbl;

    private CheckSheetType checkSheetType;

    private String no;

    private String keyWord;

    private String description;

    private String discipline;

    private Long checkSheetFileId;

    private String checkSheetFilePath;

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

    public Boolean getInTrident() {
        return isInTrident;
    }

    public void setInTrident(Boolean inTrident) {
        isInTrident = inTrident;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
