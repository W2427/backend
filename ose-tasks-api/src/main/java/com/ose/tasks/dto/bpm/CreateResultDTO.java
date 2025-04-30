package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.vo.BpmTaskType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 工作流创建结果 数据传输对象
 */
public class CreateResultDTO extends BaseDTO {

    private static final long serialVersionUID = -7073339323410445314L;

    /**
     * 构造函数
     */
    public CreateResultDTO(
        String taskType,
        boolean startNextTask,
        TodoTaskExecuteDTO nextTodoTaskDTO) {
        this.taskType = taskType;
        this.startNextTask = startNextTask;
        this.nextTodoTaskDTO = nextTodoTaskDTO;
    }

    public CreateResultDTO() {
    }

    @Schema(description = "组织ID")
    private Long orgId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "环境变量")
    private ContextDTO context;

    @Schema(description = "工作流流程实例")
    private BpmActivityInstanceBase actInst;

    @Schema(description = "工作流流程实例状态")
    private BpmActivityInstanceState actInstState;

    @Schema(description = "创建流程的DTO")
    private ActivityInstanceDTO actInstDTO;

    @Schema(description = "流程创建变量 ")
    private Map<String, Object> variables;

    @Schema(description = "工序定义的ID，工序ID")
    private BpmProcess process;

    @Schema(description = "创建流程实例") //是否继续创建流程实例
    private boolean createResult = true;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "是否启动下一个任务节点") //当需要继续启动下一个任务节点时，将此字段置为 true
    private boolean startNextTask = false;

    @Schema(description = "启动下一个任务节点 的 todoTaskDTO")
    private TodoTaskExecuteDTO nextTodoTaskDTO;

    @Schema(description = "启动下一个任务节点的ID")
    private Long nextTaskId;

    @Schema(description = "报错信息")
    private String errorDesc;

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

    public boolean isStartNextTask() {
        return startNextTask;
    }

    public void setStartNextTask(boolean startNextTask) {
        this.startNextTask = startNextTask;
    }

    public TodoTaskExecuteDTO getNextTodoTaskDTO() {
        return nextTodoTaskDTO;
    }

    public void setNextTodoTaskDTO(TodoTaskExecuteDTO nextTodoTaskDTO) {
        this.nextTodoTaskDTO = nextTodoTaskDTO;
    }

    public Long getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(Long nextTaskId) {
        this.nextTaskId = nextTaskId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public ActivityInstanceDTO getActInstDTO() {
        return actInstDTO;
    }

    public void setActInstDTO(ActivityInstanceDTO actInstDTO) {
        this.actInstDTO = actInstDTO;
    }

    public BpmProcess getProcess() {
        return process;
    }

    public void setProcess(BpmProcess process) {
        this.process = process;
    }

    public boolean isCreateResult() {
        return createResult;
    }

    public void setCreateResult(boolean createResult) {
        this.createResult = createResult;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public BpmActivityInstanceState getActInstState() {
        return actInstState;
    }

    public void setActInstState(BpmActivityInstanceState actInstState) {
        this.actInstState = actInstState;
    }
}
