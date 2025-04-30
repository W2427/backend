package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ReportStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class QCReportCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "报告编号")
    private String seriesNo;

    @Schema(description = "担当者")
    private String operatorName;

//    @Schema(description = "报告状态")
//    private ReportStatus reportStatus;

    @Schema(description = "报告状态集合")
    private List<ReportStatus> reportStatusItemList;

    @Schema(description = "NDE类型")
    private String ndeType;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

//    public ReportStatus getReportStatus() {
//        return reportStatus;
//    }
//
//    public void setReportStatus(ReportStatus reportStatus) {
//        this.reportStatus = reportStatus;
//    }


    public List<ReportStatus> getReportStatusItemList() {
        return reportStatusItemList;
    }

    public void setReportStatusItemList(List<ReportStatus> reportStatusItemList) {
        this.reportStatusItemList = reportStatusItemList;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getNdeType() {
        return ndeType;
    }

    public void setNdeType(String ndeType) {
        this.ndeType = ndeType;
    }

}
