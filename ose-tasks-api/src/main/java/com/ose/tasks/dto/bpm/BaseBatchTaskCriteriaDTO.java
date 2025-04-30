package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import com.ose.vo.BpmTaskType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 批处理任务查询条件数据传输对象基础类。
 */
public class BaseBatchTaskCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = -3893257377765122482L;


    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "任务节点DefKey")
    private String taskDefKey;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "是否需要 bpm 的图形的详细信息")
    private boolean bpmInfoRequired;

    @Schema(description = "网关 map")
    private Map<String, Object> command;

    @Schema(description = "任务Id的集合")
    private List<Long> taskIds;

    private double costHour = 0;

    @Schema(description = "实体No的集合")
    private List<String> entityNos;

    private String attachFiles;//逗号分割 文件名

    private String picFiles;//逗号分割 图片名

    @Schema(description = "任务Id对应的command")
    private Map<Long, Map<String, Object>> taskCommands;

    @Schema(description = "是否为跳过上传报告")
    private boolean skipUploadFlag = false;

    @Schema(description = "exeVersion")
    private Long exeVersion;

    //外检时间
    private Date externalInspectionTime;

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public boolean isBpmInfoRequired() {
        return bpmInfoRequired;
    }

    public void setBpmInfoRequired(boolean bpmInfoRequired) {
        this.bpmInfoRequired = bpmInfoRequired;
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(Map<String, Object> command) {
        this.command = command;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        try {
            BpmTaskType.valueOf(taskType);
            this.taskType = taskType;
        } catch (Exception e) {
            this.taskType = null;
        }
    }

    public Map<Long, Map<String, Object>> getTaskCommands() {
        return taskCommands;
    }

    public void setTaskCommands(Map<Long, Map<String, Object>> taskCommands) {
        this.taskCommands = taskCommands;
    }

    public boolean isSkipUploadFlag() {
        return skipUploadFlag;
    }

    public void setSkipUploadFlag(boolean skipUploadFlag) {
        this.skipUploadFlag = skipUploadFlag;
    }

    public List<String> getEntityNos() {
        return entityNos;
    }

    public void setEntityNos(List<String> entityNos) {
        this.entityNos = entityNos;
    }

    public Long getExeVersion() {
        return exeVersion;
    }

    public void setExeVersion(Long exeVersion) {
        this.exeVersion = exeVersion;
    }

    public String getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(String attachFiles) {
        this.attachFiles = attachFiles;
    }

    public String getPicFiles() {
        return picFiles;
    }

    public void setPicFiles(String picFiles) {
        this.picFiles = picFiles;
    }

    public double getCostHour() {
        return costHour;
    }

    public void setCostHour(double costHour) {
        this.costHour = costHour;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }
}
