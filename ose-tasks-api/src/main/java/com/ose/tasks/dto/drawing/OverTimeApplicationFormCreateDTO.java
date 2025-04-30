package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import java.util.List;

/**
 * 传输对象
 */
public class OverTimeApplicationFormCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -3907609959237350019L;

    @Schema(description = "加班项目名")
    private String projectName;

    @Schema(description = "加班项目id")
    private Long projectId;

    @Schema(description = "加班任务")
    private List<String> task;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;


    private Double overTimeHours;

    private String taskDescription;


    private Double mealTimeHours;

    @Column
    private String startTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Double getOverTimeHours() {
        return overTimeHours;
    }

    public void setOverTimeHours(Double overTimeHours) {
        this.overTimeHours = overTimeHours;
    }

    public Double getMealTimeHours() {
        return mealTimeHours;
    }

    public void setMealTimeHours(Double mealTimeHours) {
        this.mealTimeHours = mealTimeHours;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<String> getTask() {
        return task;
    }

    public void setTask(List<String> task) {
        this.task = task;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
