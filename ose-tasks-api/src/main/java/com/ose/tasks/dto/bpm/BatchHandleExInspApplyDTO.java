package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象
 */
public class BatchHandleExInspApplyDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "业主外检处理列表")
    private List<Map<String, Object>> ownerExternalInspection;

    @Schema(description = "第三方外检处理列表")
    private List<Map<String, Object>> thirdExternalInspection;

    @Schema(description = "其他外检处理列表")
    private List<Map<String, Object>> otherExternalInspection;

    @Schema(description = "业主外检时间")
    private Date ownerExternalInspectionTime;

    @Schema(description = "第三方外检时间")
    private Date thirdExternalInspectionTime;

    @Schema(description = "其他外检时间")
    private Date otherExternalInspectionTime;

    @Schema(description = "检查者")
    private String inspector;

    @Schema(description = "检查项名称")
    private String inspectionItemName;

    @Schema(description = "专业")
    private String discipline;

    public List<Map<String, Object>> getOwnerExternalInspection() {
        return ownerExternalInspection;
    }

    public void setOwnerExternalInspection(List<Map<String, Object>> ownerExternalInspection) {
        this.ownerExternalInspection = ownerExternalInspection;
    }

    public List<Map<String, Object>> getThirdExternalInspection() {
        return thirdExternalInspection;
    }

    public void setThirdExternalInspection(List<Map<String, Object>> thirdExternalInspection) {
        this.thirdExternalInspection = thirdExternalInspection;
    }

    public List<Map<String, Object>> getOtherExternalInspection() {
        return otherExternalInspection;
    }

    public void setOtherExternalInspection(List<Map<String, Object>> otherExternalInspection) {
        this.otherExternalInspection = otherExternalInspection;
    }

    public Date getOwnerExternalInspectionTime() {
        return ownerExternalInspectionTime;
    }

    public void setOwnerExternalInspectionTime(Date ownerExternalInspectionTime) {
        this.ownerExternalInspectionTime = ownerExternalInspectionTime;
    }

    public Date getThirdExternalInspectionTime() {
        return thirdExternalInspectionTime;
    }

    public void setThirdExternalInspectionTime(Date thirdExternalInspectionTime) {
        this.thirdExternalInspectionTime = thirdExternalInspectionTime;
    }

    public Date getOtherExternalInspectionTime() {
        return otherExternalInspectionTime;
    }

    public void setOtherExternalInspectionTime(Date otherExternalInspectionTime) {
        this.otherExternalInspectionTime = otherExternalInspectionTime;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }


}
