package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class TaskPackageDTO extends BaseDTO {

    private static final long serialVersionUID = 7715190625320094916L;

    @Schema(description = "任务包ID")
    private Long taskPackageId;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }
}
