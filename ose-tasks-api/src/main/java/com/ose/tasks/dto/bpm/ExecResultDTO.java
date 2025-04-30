package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作流执行结果 数据传输对象
 */
public class ExecResultDTO extends BaseDTO {

    private static final long serialVersionUID = -7073339323410445314L;

    public ExecResultDTO() {
    }

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

    @Schema(description = "工作流流程实例")
    private BpmActivityInstanceState actInstState;

    @Schema(description = "执行结果")
    private boolean execResult;
    private String comments;

//    @Schema(description = "任务执行结果返回信息")
//    private TaskCompleteResponseDTO complete;

    public List<ActReportDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ActReportDTO> attachments) {
        this.attachments = attachments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    private List<ActReportDTO> attachments;

    private List<ActReportDTO> pics;


//    @Schema(description = "任务执行结果的返回详细信息")
//    private TaskCompleteResponseDTO completeInfo;

    @Schema(description = "执行中的任务 tasks.bpm_ru_task")
    private BpmRuTask ruTask;

    @Schema(description = "流程执行变量 ")
    private Map<String, Object> variables;

    @Schema(description = "工序定义的ID，工序ID")
    private Long processId;

    @Schema(description = "后续任务节点s")
    private List<ExecResultDTO> nextTasks;

    @Schema(description = "是否自动启动任务节点") //当需要继续自动启动任务节点时，将此字段置为 true
    private boolean autoStart = false;

    @Schema(description = "工序完整信息")
    private BpmProcess bpmProcess;

    @Schema(description = "ERROR DESC")
    private String errorDesc;

    @Schema(description = "跳过代理,如果是True 不执行代理")
    private Boolean isOverrideDelegate;

    @Schema(description = "跳过代理,如果是True 不执行POST代理")
    private Boolean isOverridePostDelegate;

    @Schema(description = "Parent Task Id")
    private Long parentTaskId;

    @Schema(description = "exeVersion")
    private Long exeVersion;

    private boolean isNotFinished = false;
    private Set<NextTaskDTO> nextTaskNodes;


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

    public TodoTaskExecuteDTO getTodoTaskDTO() {
        return todoTaskDTO;
    }

    public void setTodoTaskDTO(TodoTaskExecuteDTO todoTaskDTO) {
        this.todoTaskDTO = todoTaskDTO;
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

    public boolean isExecResult() {
        return execResult;
    }

    public void setExecResult(boolean execResult) {
        this.execResult = execResult;
    }

//    public TaskCompleteResponseDTO getComplete() {
//        return complete;
//    }
//
//    public void setComplete(TaskCompleteResponseDTO complete) {
//        this.complete = complete;
//    }
//
//    public TaskCompleteResponseDTO getCompleteInfo() {
//        return completeInfo;
//    }
//
//    public void setCompleteInfo(TaskCompleteResponseDTO completeInfo) {
//        this.completeInfo = completeInfo;
//    }

    public BpmRuTask getRuTask() {
        return ruTask;
    }

    public void setRuTask(BpmRuTask ruTask) {
        this.ruTask = ruTask;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public List<ExecResultDTO> getNextTasks() {
        return nextTasks;
    }

    public void setNextTasks(List<ExecResultDTO> nextTasks) {
        this.nextTasks = nextTasks;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public BpmProcess getBpmProcess() {
        return bpmProcess;
    }

    public void setBpmProcess(BpmProcess bpmProcess) {
        this.bpmProcess = bpmProcess;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public Boolean getOverrideDelegate() {
        return isOverrideDelegate;
    }

    public void setOverrideDelegate(Boolean overrideDelegate) {
        isOverrideDelegate = overrideDelegate;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Boolean getOverridePostDelegate() {
        return isOverridePostDelegate;
    }

    public void setOverridePostDelegate(Boolean overridePostDelegate) {
        isOverridePostDelegate = overridePostDelegate;
    }

    public Long getExeVersion() {
        return exeVersion;
    }

    public void setExeVersion(Long exeVersion) {
        this.exeVersion = exeVersion;
    }

    public BpmActivityInstanceState getActInstState() {
        return actInstState;
    }

    public void setActInstState(BpmActivityInstanceState actInstState) {
        this.actInstState = actInstState;
    }

    public Set<NextTaskDTO> getNextTaskNodes() {
        return nextTaskNodes;
    }

    public void setNextTaskNodes(Set<NextTaskDTO> nextTaskNodes) {
        this.nextTaskNodes = nextTaskNodes;
    }

    public List<ActReportDTO> getPics() {
        return pics;
    }

    public void setPics(List<ActReportDTO> pics) {
        this.pics = pics;
    }


    public boolean isNotFinished() {
        return isNotFinished;
    }

    public void setNotFinished(boolean notFinished) {
        isNotFinished = notFinished;
    }
}
