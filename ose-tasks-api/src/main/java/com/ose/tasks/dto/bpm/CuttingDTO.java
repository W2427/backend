package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;


public class CuttingDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "出库单code")
    private String matIssueCode;

    @Schema(description = "余料领料单号")
    private String surplusNo;

    @Schema(description = "材料编码")
    private List<String> tagNumber;

    @Schema(description = "材质")
    private List<String> nps;

    @Schema(description = "下料实体Id列表")
    private List<Long> cuttingEntityIds;

    @Schema(description = "交接单名称")
    private String name;

    @Schema(description = "交接时间")
    private Date date;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "是否套料")
    private Boolean doNest;

    public List<Long> getCuttingEntityIds() {
        return cuttingEntityIds;
    }

    public void setCuttingEntityIds(List<Long> cuttingEntityIds) {
        this.cuttingEntityIds = cuttingEntityIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMatIssueCode() {
        return matIssueCode;
    }

    public void setMatIssueCode(String matIssueCode) {
        this.matIssueCode = matIssueCode;
    }

    public List<String> getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(List<String> tagNumber) {
        this.tagNumber = tagNumber;
    }

    public List<String> getNps() {
        return nps;
    }

    public void setNps(List<String> nps) {
        this.nps = nps;
    }

    public Boolean getDoNest() {
        return doNest;
    }

    public void setDoNest(Boolean doNest) {
        this.doNest = doNest;
    }

    public String getSurplusNo() {
        return surplusNo;
    }

    public void setSurplusNo(String surplusNo) {
        this.surplusNo = surplusNo;
    }
}
