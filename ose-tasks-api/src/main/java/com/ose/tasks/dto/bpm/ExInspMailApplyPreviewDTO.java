package com.ose.tasks.dto.bpm;

import java.util.List;
import java.util.Map;

import com.ose.tasks.entity.bpm.BpmExInspMailApplication;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象
 */
public class ExInspMailApplyPreviewDTO extends BaseBatchTaskCriteriaDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "外检处理列表")
    private List<Long> externalInspectionApplyScheduleIds;

    @Schema(description = "协调有无区分")
    private String coordinateCategory;

    private Map<String, BpmExInspMailApplication> inspectionMailApplications;

    public List<Long> getExternalInspectionApplyScheduleIds() {
        return externalInspectionApplyScheduleIds;
    }

    public void setExternalInspectionApplyScheduleIds(List<Long> externalInspectionApplyScheduleIds) {
        this.externalInspectionApplyScheduleIds = externalInspectionApplyScheduleIds;
    }

    public String getCoordinateCategory() {
        return coordinateCategory;
    }

    public void setCoordinateCategory(String coordinateCategory) {
        this.coordinateCategory = coordinateCategory;
    }

    public Map<String, BpmExInspMailApplication> getInspectionMailApplications() {
        return inspectionMailApplications;
    }

    public void setInspectionMailApplications(
        Map<String, BpmExInspMailApplication> inspectionMailApplications) {
        this.inspectionMailApplications = inspectionMailApplications;
    }

}
