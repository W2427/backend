package com.ose.tasks.dto.workinghour;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 工时DTO
 */
public class ProjectWorkingHourPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "工时日期(YYYY-MM-DD)")
    private Date projectWorkingHourDate;

    @Schema(description = "项目工时大分类")
    private Long projectWorkingHoursLargeCategory;

    @Schema(description = "项目工时小分类")
    private Long projectWorkingHoursSmallCategory;

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

    public Date getProjectWorkingHourDate() {
        return projectWorkingHourDate;
    }

    public void setProjectWorkingHourDate(Date projectWorkingHourDate) {
        this.projectWorkingHourDate = projectWorkingHourDate;
    }

    public Long getProjectWorkingHoursLargeCategory() {
        return projectWorkingHoursLargeCategory;
    }

    public void setProjectWorkingHoursLargeCategory(Long projectWorkingHoursLargeCategory) {
        this.projectWorkingHoursLargeCategory = projectWorkingHoursLargeCategory;
    }

    public Long getProjectWorkingHoursSmallCategory() {
        return projectWorkingHoursSmallCategory;
    }

    public void setProjectWorkingHoursSmallCategory(Long projectWorkingHoursSmallCategory) {
        this.projectWorkingHoursSmallCategory = projectWorkingHoursSmallCategory;
    }

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
