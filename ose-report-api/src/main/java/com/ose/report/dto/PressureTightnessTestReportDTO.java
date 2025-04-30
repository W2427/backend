package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class PressureTightnessTestReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -8916778860538887128L;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "实验日期")
    private Date date;

    @Schema(description = "仪器1编号")
    private String equipmentNo1;

    @Schema(description = "仪器1量程")
    private String equipmentRange1;

    @Schema(description = "鉴定有效期1")
    private Date validDate1;

    @Schema(description = "仪器2编号")
    private String equipmentNo2;

    @Schema(description = "仪器2量程")
    private String equipmentRange2;

    @Schema(description = "鉴定有效期2")
    private Date validDate2;

    @Schema(description = "试验介质")
    private String testMedium;

    @Schema(description = "环境温度")
    private String ambientTemp;

    @Schema(description = "介质温度")
    private String mediumTemp;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<PressureTightnessTestItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEquipmentNo1() {
        return equipmentNo1;
    }

    public void setEquipmentNo1(String equipmentNo1) {
        this.equipmentNo1 = equipmentNo1;
    }

    public String getEquipmentRange1() {
        return equipmentRange1;
    }

    public void setEquipmentRange1(String equipmentRange1) {
        this.equipmentRange1 = equipmentRange1;
    }

    public Date getValidDate1() {
        return validDate1;
    }

    public void setValidDate1(Date validDate1) {
        this.validDate1 = validDate1;
    }

    public String getEquipmentNo2() {
        return equipmentNo2;
    }

    public void setEquipmentNo2(String equipmentNo2) {
        this.equipmentNo2 = equipmentNo2;
    }

    public String getEquipmentRange2() {
        return equipmentRange2;
    }

    public void setEquipmentRange2(String equipmentRange2) {
        this.equipmentRange2 = equipmentRange2;
    }

    public Date getValidDate2() {
        return validDate2;
    }

    public void setValidDate2(Date validDate2) {
        this.validDate2 = validDate2;
    }

    public String getTestMedium() {
        return testMedium;
    }

    public void setTestMedium(String testMedium) {
        this.testMedium = testMedium;
    }

    public String getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(String ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    public String getMediumTemp() {
        return mediumTemp;
    }

    public void setMediumTemp(String mediumTemp) {
        this.mediumTemp = mediumTemp;
    }

    @Override
    public List<PressureTightnessTestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PressureTightnessTestItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
