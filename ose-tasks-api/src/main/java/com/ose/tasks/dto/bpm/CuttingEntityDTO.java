package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.material.NestGateWay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class CuttingEntityDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    @Schema(description = "spoolEntityNo")
    private String spoolEntityNo;

    @Schema(description = "材料准备单id")
    private Long matPrepareId;

    @Schema(description = "材料准备单号")
    private String matPrepareCode;

    @Schema(description = "余料领料单号")
    private String matSurplusReceiptsNo;

    @Schema(description = "余料领料单id")
    private Long matSurplusReceiptsId;

    @Schema(description = "套料状态")
    @Enumerated(EnumType.STRING)
    private NestGateWay nestGateWay = NestGateWay.NONE;

    @Schema(description = "材料出库单id")
    private Long matIssueId;

    @Schema(description = "材料出库单编号")
    private String matIssueCode;

    @Schema(description = "nestingFile")
    private ActReportDTO report;

    @Schema(description = "是否套料")
    private Boolean isNested;

    private BpmRuTask ruTask;

    public BpmRuTask getRuTask() {
        return ruTask;
    }

    public void setRuTask(BpmRuTask ruTask) {
        this.ruTask = ruTask;
    }

    public Long getMatPrepareId() {
        return matPrepareId;
    }

    public void setMatPrepareId(Long matPrepareId) {
        this.matPrepareId = matPrepareId;
    }

    public String getMatPrepareCode() {
        return matPrepareCode;
    }

    public void setMatPrepareCode(String matPrepareCode) {
        this.matPrepareCode = matPrepareCode;
    }

    public Long getMatIssueId() {
        return matIssueId;
    }

    public void setMatIssueId(Long matIssueId) {
        this.matIssueId = matIssueId;
    }

    public String getMatIssueCode() {
        return matIssueCode;
    }

    public void setMatIssueCode(String matIssueCode) {
        this.matIssueCode = matIssueCode;
    }

    public String getSpoolEntityNo() {
        return spoolEntityNo;
    }

    public void setSpoolEntityNo(String spoolEntityNo) {
        this.spoolEntityNo = spoolEntityNo;
    }

    public ActReportDTO getReport() {
        return report;
    }

    public void setReport(ActReportDTO report) {
        this.report = report;
    }

    public Boolean getNested() {
        return isNested;
    }

    public void setNested(Boolean nested) {
        isNested = nested;
    }

    public String getMatSurplusReceiptsNo() {
        return matSurplusReceiptsNo;
    }

    public void setMatSurplusReceiptsNo(String matSurplusReceiptsNo) {
        this.matSurplusReceiptsNo = matSurplusReceiptsNo;
    }

    public Long getMatSurplusReceiptsId() {
        return matSurplusReceiptsId;
    }

    public void setMatSurplusReceiptsId(Long matSurplusReceiptsId) {
        this.matSurplusReceiptsId = matSurplusReceiptsId;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }
}
