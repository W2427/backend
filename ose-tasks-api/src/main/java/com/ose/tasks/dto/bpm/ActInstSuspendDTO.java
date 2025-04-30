package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.util.Set;

/**
 * 任务查询条件数据传输对象类。
 */
public class ActInstSuspendDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "待撤回的流程实例")
    private BpmActivityInstanceBase actInst;

    @Schema(description = "待撤回的流程实例")
    private BpmActivityInstanceState actInstState;

    @Schema(description = "taskDefKey 任务节点KEY")
    private String taskDefKey;

    @Schema(description = "bpmProcess 工序")
    private BpmProcess bpmProcess;

    @Schema(description = "task ids, 待撤回的task Ids, 123456123456123456")
    private Set<Long> taskIds;

    @Schema(description = "task type 任务类型")
    private String taskType;

    @Schema(description = "metaData")
    private Map<String, Object> metaData;

    @Schema(description = "父级 TaskId")
    private String parentTaskId;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public BpmProcess getBpmProcess() {
        return bpmProcess;
    }

    public void setBpmProcess(BpmProcess bpmProcess) {
        this.bpmProcess = bpmProcess;
    }

    public Set<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Set<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public BpmActivityInstanceState getActInstState() {
        return actInstState;
    }

    public void setActInstState(BpmActivityInstanceState actInstState) {
        this.actInstState = actInstState;
    }
}
