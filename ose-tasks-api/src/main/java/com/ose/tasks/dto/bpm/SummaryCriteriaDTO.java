package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class SummaryCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 8089543734405362177L;

    @Schema(description = "工序阶段")
    private Long processStageId;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "工作场地地址")
    private String workSiteAddress;

    @Schema(description = "工作场地地址")
    private String taskDefKey;

    @Schema(description = "完成状态")
    private String finishState;

    @Schema(description = "创建时间")
    private Date createdAt;

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
