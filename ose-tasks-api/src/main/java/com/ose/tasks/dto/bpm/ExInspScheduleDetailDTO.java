package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 外检报告上传
 */
public class ExInspScheduleDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;


    @Schema(description = "外检协调明细id")
    private Long scheduleDetailId;

    // 报告号
    @Schema(description = "编号")
    private String reportSeriesNo;

    // 实体类型分类
    @Schema(description = "实体类型分类")
    private String entityType;

    // 实体类型
    @Schema(description = "实体类型")
    private String entitySubType;

    // 工序阶段
    @Schema(description = "工序阶段")
    private String processStage;

    // 工序
    @Schema(description = "工序")
    private String process;

    @Schema(description = "报表")
    private List<ActReportDTO> reports;

    @Schema(description = "实体明细")
    private List<ExInspReportHandleDTO> data;

    public Long getScheduleDetailId() {
        return scheduleDetailId;
    }

    public void setScheduleDetailId(Long scheduleDetailId) {
        this.scheduleDetailId = scheduleDetailId;
    }

    public String getReportSeriesNo() {
        return reportSeriesNo;
    }

    public void setReportSeriesNo(String reportSeriesNo) {
        this.reportSeriesNo = reportSeriesNo;
    }

    public String getEntitType() {
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

    public List<ActReportDTO> getReports() {
        return reports;
    }

    public void setReports(List<ActReportDTO> reports) {
        this.reports = reports;
    }

    public List<ExInspReportHandleDTO> getData() {
        return data;
    }

    public void setData(List<ExInspReportHandleDTO> data) {
        this.data = data;
    }

}
