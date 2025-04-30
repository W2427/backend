package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class CuttingEntitySearchConditionDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    @Schema(description = "出库单code")
    private List<String> matIssueCodeList;

    @Schema(description = "余料领料单编号")
    private List<String> matSurplusReceiptsNoList;

    @Schema(description = "材料编码")
    private List<String> tagNumberList;

    @Schema(description = "材质")
    private List<String> npsList;

    public List<String> getMatIssueCodeList() {
        return matIssueCodeList;
    }

    public void setMatIssueCodeList(List<String> matIssueCodeList) {
        this.matIssueCodeList = matIssueCodeList;
    }

    public List<String> getTagNumberList() {
        return tagNumberList;
    }

    public void setTagNumberList(List<String> tagNumberList) {
        this.tagNumberList = tagNumberList;
    }

    public List<String> getNpsList() {
        return npsList;
    }

    public void setNpsList(List<String> npsList) {
        this.npsList = npsList;
    }

    public List<String> getMatSurplusReceiptsNoList() {
        return matSurplusReceiptsNoList;
    }

    public void setMatSurplusReceiptsNoList(List<String> matSurplusReceiptsNoList) {
        this.matSurplusReceiptsNoList = matSurplusReceiptsNoList;
    }
}
