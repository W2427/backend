package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class PwhtAndHardnessTestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -5834522188974336387L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "日期")
    private Date date;

    @Schema(description = "单位/区域")
    private String area;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    @Schema(description = "需求保温温度")
    private String requiredHoldingTemp;

    @Schema(description = "需求保温时间")
    private String requiredHoldingTime;

    @Schema(description = "需求升温速率")
    private String requiredHeatingRate;

    @Schema(description = "需求冷却速率")
    private String requiredCoolingRate;

    @Schema(description = "实际保温温度")
    private String actualHoldingTemp;

    @Schema(description = "实际保温时间")
    private String actualHoldingTime;

    @Schema(description = "实际升温速率")
    private String actualHeatingRate;

    @Schema(description = "实际冷却速率")
    private String actualCoolingRate;

    @Schema(description = "设备描述")
    private String equipmentDescription;

    @Schema(description = "进卡速度")
    private String chartSpeed;

    @Schema(description = "记录单号")
    private String recorderNo;

    @Schema(description = "校验日期")
    private Date calibrationDate;

    @Schema(description = "硬度测试仪类型")
    private String hardnessTester;

    @Schema(description = "硬度测试序列号")
    private String hardnessTestSerialNo;

    @Schema(description = "硬度测试方法")
    private String hardnessTestMethod;

    @Schema(description = "硬度测试校验日期")
    private Date hardnessTestCalibrationDate;

    private List<PwhtAndHardnessTestItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }

    public String getRequiredHoldingTemp() {
        return requiredHoldingTemp;
    }

    public void setRequiredHoldingTemp(String requiredHoldingTemp) {
        this.requiredHoldingTemp = requiredHoldingTemp;
    }

    public String getRequiredHoldingTime() {
        return requiredHoldingTime;
    }

    public void setRequiredHoldingTime(String requiredHoldingTime) {
        this.requiredHoldingTime = requiredHoldingTime;
    }

    public String getRequiredHeatingRate() {
        return requiredHeatingRate;
    }

    public void setRequiredHeatingRate(String requiredHeatingRate) {
        this.requiredHeatingRate = requiredHeatingRate;
    }

    public String getRequiredCoolingRate() {
        return requiredCoolingRate;
    }

    public void setRequiredCoolingRate(String requiredCoolingRate) {
        this.requiredCoolingRate = requiredCoolingRate;
    }

    public String getActualHoldingTemp() {
        return actualHoldingTemp;
    }

    public void setActualHoldingTemp(String actualHoldingTemp) {
        this.actualHoldingTemp = actualHoldingTemp;
    }

    public String getActualHoldingTime() {
        return actualHoldingTime;
    }

    public void setActualHoldingTime(String actualHoldingTime) {
        this.actualHoldingTime = actualHoldingTime;
    }

    public String getActualHeatingRate() {
        return actualHeatingRate;
    }

    public void setActualHeatingRate(String actualHeatingRate) {
        this.actualHeatingRate = actualHeatingRate;
    }

    public String getActualCoolingRate() {
        return actualCoolingRate;
    }

    public void setActualCoolingRate(String actualCoolingRate) {
        this.actualCoolingRate = actualCoolingRate;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    public String getChartSpeed() {
        return chartSpeed;
    }

    public void setChartSpeed(String chartSpeed) {
        this.chartSpeed = chartSpeed;
    }

    public String getRecorderNo() {
        return recorderNo;
    }

    public void setRecorderNo(String recorderNo) {
        this.recorderNo = recorderNo;
    }

    public Date getCalibrationDate() {
        return calibrationDate;
    }

    public void setCalibrationDate(Date calibrationDate) {
        this.calibrationDate = calibrationDate;
    }

    public String getHardnessTester() {
        return hardnessTester;
    }

    public void setHardnessTester(String hardnessTester) {
        this.hardnessTester = hardnessTester;
    }

    public String getHardnessTestSerialNo() {
        return hardnessTestSerialNo;
    }

    public void setHardnessTestSerialNo(String hardnessTestSerialNo) {
        this.hardnessTestSerialNo = hardnessTestSerialNo;
    }

    public String getHardnessTestMethod() {
        return hardnessTestMethod;
    }

    public void setHardnessTestMethod(String hardnessTestMethod) {
        this.hardnessTestMethod = hardnessTestMethod;
    }

    public Date getHardnessTestCalibrationDate() {
        return hardnessTestCalibrationDate;
    }

    public void setHardnessTestCalibrationDate(Date hardnessTestCalibrationDate) {
        this.hardnessTestCalibrationDate = hardnessTestCalibrationDate;
    }

    @Override
    public List<PwhtAndHardnessTestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PwhtAndHardnessTestItemDTO> items) {
        this.items = items;
    }
}
