package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmRuTask;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 工作流任务执行 数据传输对象
 */
public class TaskExecDTO extends BaseDTO {

    private static final long serialVersionUID = -6322696481183656890L;

    @Schema(description = "组织ID")
    private Long orgId;

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "任务传输数据DTO TODOTaskDTO")
    private TodoTaskExecuteDTO todoTaskDTO;

    @Schema(description = "环境变量")
    private ContextDTO context;

    @Schema(description = "工作流流程实例")
    private BpmActivityInstanceBase actInst;

    @Schema(description = "执行结果")
    private boolean execResult;

    @Schema(description = "任务执行结果返回信息")
    private TaskCompleteResponseDTO complete;

    @Schema(description = "任务执行结果的返回详细信息")
    private TaskCompleteResponseDTO completeInfo;

    @Schema(description = "执行中的任务 tasks.bpm_ru_task")
    private BpmRuTask ruTask;

    @Schema(description = "流程执行变量 ")
    private Map<String, Object> variables;

    public TaskCompleteResponseDTO getComplete() {
        return complete;
    }

    public void setComplete(TaskCompleteResponseDTO complete) {
        this.complete = complete;
    }

    public TaskCompleteResponseDTO getCompleteInfo() {
        return completeInfo;
    }

    public void setCompleteInfo(TaskCompleteResponseDTO completeInfo) {
        this.completeInfo = completeInfo;
    }

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public boolean isExecResult() {
        return execResult;
    }

    public void setExecResult(boolean execResult) {
        this.execResult = execResult;
    }

    public BpmRuTask getRuTask() {
        return ruTask;
    }

    public void setRuTask(BpmRuTask ruTask) {
        this.ruTask = ruTask;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    //
//    @Schema(description = "流程定义")
//    private BpmActReProcdef procDef;
//
//    @Schema(description = "运行的任务")
//    private BpmActRuTask actRuTask;
//
//    @Schema(description = "task表中的运行的任务")
//    private BpmRuTask ruTask;
//
//
//    @Schema(description = "当前流程实例")
//    private BpmActivityInstance actInst;
//
//    @Schema(description = "当前项目信息")
//    private Project project;
//
//    @Schema(description = "流程任务定义的名称")
//    private String processKey;
//
//    @Schema(description = "todoTaskDTO")
//    private TodoTaskExecuteDTO todoTaskDTO;
//
//    @Schema(description = "task Type 任务类型")
//    private BpmTaskType taskType;
//
//    public BpmActReProcdef getProcDef() {
//        return procDef;
//    }
//
//    public void setProcDef(BpmActReProcdef procDef) {
//        this.procDef = procDef;
//    }
//
//    public BpmActRuTask getActRuTask() {
//        return actRuTask;
//    }
//
//    public void setActRuTask(BpmActRuTask actRuTask) {
//        this.actRuTask = actRuTask;
//    }
//
//    public BpmRuTask getRuTask() {
//        return ruTask;
//    }
//
//    public void setRuTask(BpmRuTask ruTask) {
//        this.ruTask = ruTask;
//    }
//
//    public BpmActivityInstance getActInst() {
//        return actInst;
//    }
//
//    public void setActInst(BpmActivityInstance actInst) {
//        this.actInst = actInst;
//    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }
//
    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
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

    public TodoTaskExecuteDTO getTodoTaskDTO() {
        return todoTaskDTO;
    }

    public void setTodoTaskDTO(TodoTaskExecuteDTO todoTaskDTO) {
        this.todoTaskDTO = todoTaskDTO;
    }
//    public String getProcessKey() {
//        return processKey;
//    }
//
//    public void setProcessKey(String processKey) {
//        this.processKey = processKey;
//    }
//
//    public TodoTaskExecuteDTO getTodoTaskDTO() {
//        return todoTaskDTO;
//    }
//
//    public void setTodoTaskDTO(TodoTaskExecuteDTO todoTaskDTO) {
//        this.todoTaskDTO = todoTaskDTO;
//    }
//
//    public BpmTaskType getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(BpmTaskType taskType) {
//        this.taskType = taskType;
//    }
}
