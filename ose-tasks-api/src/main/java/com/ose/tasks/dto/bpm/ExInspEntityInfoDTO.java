package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class ExInspEntityInfoDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "外检报验单号")
    private String reportNo;

    @Schema(description = "工序英文名")
    private String process;

    @Schema(description = "工序阶段英文名")
    private String processStage;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "外检报验单id")
    private Long reportId;

    @Schema(description = "层级")
    private String layer;

    @Schema(description = "试压包")
    private String pressureTestPackage;

    @Schema(description = "区域")
    private String area;

    @Schema(description = "子系统")
    private String subSystem;

    @Schema(description = "清洁包")
    private String cleanPackage;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getCleanPackage() {
        return cleanPackage;
    }

    public void setCleanPackage(String cleanPackage) {
        this.cleanPackage = cleanPackage;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }


}
