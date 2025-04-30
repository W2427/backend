package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WBSScreenResultDTO extends BaseDTO {

    private static final long serialVersionUID = 8332350690407011757L;

    @Schema(description = "模块ID")
    private List<Long> moduleIdList;

    @Schema(description = "模块号")
    private List<String> moduleNoList;

    @Schema(description = "任务包ID")
    private List<String> taskPackageIdList;

    @Schema(description = "任务包名称")
    private List<String> taskPackageNameList;

    @Schema(description = "工序ID")
    private List<String> processIdList;

    @Schema(description = "工序")
    private List<String> processList;

    @Schema(description = "工作场地")
    private List<String> addressList;

    @Schema(description = "运行状态")
    private List<String> statusList;

    @Schema(description = "阶段")
    private List<String> stageList;

    public List<Long> getModuleIdList() {
        return moduleIdList;
    }

    public void setModuleIdList(List<Long> moduleIdList) {
        this.moduleIdList = moduleIdList;
    }

    public List<String> getModuleNoList() {
        return moduleNoList;
    }

    public void setModuleNoList(List<String> moduleNoList) {
        this.moduleNoList = moduleNoList;
    }

    public List<String> getTaskPackageIdList() {
        return taskPackageIdList;
    }

    public void setTaskPackageIdList(List<String> taskPackageIdList) {
        this.taskPackageIdList = taskPackageIdList;
    }

    public List<String> getTaskPackageNameList() {
        return taskPackageNameList;
    }

    public void setTaskPackageNameList(List<String> taskPackageNameList) {
        this.taskPackageNameList = taskPackageNameList;
    }

    public List<String> getProcessIdList() {
        return processIdList;
    }

    public void setProcessIdList(List<String> processIdList) {
        this.processIdList = processIdList;
    }

    public List<String> getProcessList() {
        return processList;
    }

    public void setProcessList(List<String> processList) {
        this.processList = processList;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }

    public List<String> getStageList() {
        return stageList;
    }

    public void setStageList(List<String> stageList) {
        this.stageList = stageList;
    }
}
