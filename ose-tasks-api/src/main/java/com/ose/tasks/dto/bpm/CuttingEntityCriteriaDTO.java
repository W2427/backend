package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class CuttingEntityCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "是否套料")
    private Boolean nested;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "余料领料单号")
    private String matSurplusReceiptsNo;

    @Schema(description = "出库单code")
    private String matIssueCode;

    @Schema(description = "材料描述（多个以;分隔）")
    private String materialCode;

    @Schema(description = "材料编码")
    private List<String> tagNumber;

    @Schema(description = "材质")
    private List<String> nps;

    public String getMatIssueCode() {
        return matIssueCode;
    }

    public void setMatIssueCode(String matIssueCode) {
        this.matIssueCode = matIssueCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public List<String> getNps() {
        return nps;
    }

    public void setNps(List<String> nps) {
        this.nps = nps;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public List<String> getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(List<String> tagNumber) {
        this.tagNumber = tagNumber;
    }

    public Boolean getNested() {
        return nested;
    }

    public void setNested(Boolean nested) {
        this.nested = nested;
    }

    public String getMatSurplusReceiptsNo() {
        return matSurplusReceiptsNo;
    }

    public void setMatSurplusReceiptsNo(String matSurplusReceiptsNo) {
        this.matSurplusReceiptsNo = matSurplusReceiptsNo;
    }
}
