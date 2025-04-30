package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务查询条件数据传输对象类。
 */
public class TodoTaskMobileCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;


    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "流程名英文")
    private String processNameEn;

    @Schema(description = "流程名中文")
    private String processNameCn;

    @Schema(description = "任务节点定义key")
    private String taskDefKey;

    @Schema(description = "阶段名英文")
    private String stageNameEn;

    @Schema(description = "阶段名中文")
    private String stageNameCn;

    @Schema(description = "检数")
    private int amount;

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessNameEn() {
        return processNameEn;
    }

    public void setProcessNameEn(String processNameEn) {
        this.processNameEn = processNameEn;
    }

    public String getProcessNameCn() {
        return processNameCn;
    }

    public void setProcessNameCn(String processNameCn) {
        this.processNameCn = processNameCn;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStageNameEn() {
        return stageNameEn;
    }

    public void setStageNameEn(String stageNameEn) {
        this.stageNameEn = stageNameEn;
    }

    public String getStageNameCn() {
        return stageNameCn;
    }

    public void setStageNameCn(String stageNameCn) {
        this.stageNameCn = stageNameCn;
    }

    public TodoTaskMobileCriteriaDTO() {

    }

    public TodoTaskMobileCriteriaDTO(Long processId, String taskNode, String processNameEn, String processNameCn,
                                     String taskDefKey, int amount) {
        super();
        this.processId = processId;
        this.taskNode = taskNode;
        this.processNameEn = processNameEn;
        this.processNameCn = processNameCn;
        this.taskDefKey = taskDefKey;
        this.amount = amount;
    }

}
