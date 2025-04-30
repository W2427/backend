package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WBSModuleTaskPackageDTO extends BaseDTO {

    private static final long serialVersionUID = -1692627626261052112L;

    @Schema(description = "模块ID")
    private Long moduleId;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "任务包ID")
    private String taskPackageId;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "工序ID")
    private String processId;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "工作场地")
    private String address;

    @Schema(description = "运行状态")
    private String status;

    @Schema(description = "阶段")
    private String stage;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(String taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
