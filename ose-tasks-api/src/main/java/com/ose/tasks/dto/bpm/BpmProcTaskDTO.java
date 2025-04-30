package com.ose.tasks.dto.bpm;
import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.vo.BpmTaskType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 工作流 和任务 数据传输对象
 */
public class BpmProcTaskDTO extends BaseDTO {

    private static final long serialVersionUID = 1468558517192719629L;

    @Schema(description = "环境变量")
    private ContextDTO context;

    @Schema(description = "流程定义")
    private BpmReDeployment bpmReDeployment;

    @Schema(description = "task表中的运行的任务")
    private BpmRuTask ruTask;

    @Schema(description = "当前流程实例")
    private BpmActivityInstanceBase actInst;

    @Schema(description = "当前项目信息")
    private Project project;

    @Schema(description = "流程任务定义的ID ")
    private Long processId;

    @Schema(description = "todoTaskDTO")
    private TodoTaskExecuteDTO todoTaskDTO;

    @Schema(description = "task Type 任务类型")
    private String taskType;

    @Schema(description = "bpm process 实例")
    private BpmProcess bpmProcess;

    @Schema(description = "跳过代理不执行")
    private Boolean isOverrideDelegate;

    @Schema(description = "跳过Post代理不执行")
    private Boolean isOverridePostDelegate;

    public BpmReDeployment getBpmReDeployment() {
        return bpmReDeployment;
    }

    public void setBpmReDeployment(BpmReDeployment bpmReDeployment) {
        this.bpmReDeployment = bpmReDeployment;
    }

    public BpmRuTask getRuTask() {
        return ruTask;
    }

    public void setRuTask(BpmRuTask ruTask) {
        this.ruTask = ruTask;
    }

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public TodoTaskExecuteDTO getTodoTaskDTO() {
        return todoTaskDTO;
    }

    public void setTodoTaskDTO(TodoTaskExecuteDTO todoTaskDTO) {
        this.todoTaskDTO = todoTaskDTO;
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

    public BpmProcess getBpmProcess() {
        return bpmProcess;
    }

    public void setBpmProcess(BpmProcess bpmProcess) {
        this.bpmProcess = bpmProcess;
    }

    public Boolean getOverrideDelegate() {
        return isOverrideDelegate;
    }

    public void setOverrideDelegate(Boolean overrideDelegate) {
        isOverrideDelegate = overrideDelegate;
    }

    public Boolean getOverridePostDelegate() {
        return isOverridePostDelegate;
    }

    public void setOverridePostDelegate(Boolean overridePostDelegate) {
        isOverridePostDelegate = overridePostDelegate;
    }

}
