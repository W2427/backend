package com.ose.tasks.dto.workinghour;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 工时DTO
 */
public class ProjectWorkingHourPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "工时工序(图纸类型）")
    private String workingHoursTaskPackage;

    @Schema(description = "相关图纸（单个）")
    private String relatedDrawings;

    @Schema(description = "工作内容")
    private String workContent;

    @Schema(description = "工时时长")
    private Double workingHours;

    @Schema(description = "审核者ID")
    private Long approvalId;

    public String getWorkingHoursTaskPackage() {
        return workingHoursTaskPackage;
    }

    public void setWorkingHoursTaskPackage(String workingHoursTaskPackage) {
        this.workingHoursTaskPackage = workingHoursTaskPackage;
    }

    public String getRelatedDrawings() {
        return relatedDrawings;
    }

    public void setRelatedDrawings(String relatedDrawings) {
        this.relatedDrawings = relatedDrawings;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }
}
