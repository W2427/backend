package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmProcess;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实体管理 批处理 任务 数据传输对象
 */
public class TodoBatchTaskDTO<T, U, V> extends BaseDTO {


    private static final long serialVersionUID = 1344645346380958061L;

    //orgId
    private Long orgId;

    //projectId
    private Long projectId;

    //执行结果
    private boolean batchExecuteResult;

    //任务流程ids
    private List<String> procInsts;

    //当前任务节点id
    private List<String> actTaskIds;

    private BpmActivityInstanceBase actInst;

    private List<BpmActivityInstanceBase> batchActInsts;

    private Map<Long,BpmActivityInstanceState> batchActInstStateMap;

    //工序分类
    private String processCategory;

    //工序名称 en
    private String process;

    //工序阶段
    private String processStage;

    // 实体类型
    private String entitySubType;

    //流程图
    private String diagramResource;

    private List<Long> entityIds;

    private BpmProcess bpmProcess;

    //任务节点
    private String taskDefKey;

    //任务类型
    private String taskType;

    //网关flag
    private boolean mutiSelectFlag;

    private List<ActReportDTO> attachments;

    private List<ActReportDTO> pics;

    //节点命令
    private List<TaskGatewayDTO> gateway;

    //流程变量-可编辑
    private List<Map<String, Object>> variables;

    //流程变量-不可编辑
    private List<Map<String, Object>> variablesDisplay;

    //其他辅助信息
    private Map<String, Object> metaData;

    //执行前后 taskId的映射 map
    private Map<String, Set<String>> completeTasksMap;

    //网关
    private Map<String, Object> command;

    //主列表
    private Page<T> mainList;

    //辅助列表
    private Page<U> auxList;

    //其他列表
    private Page<V> miscList;

    private String entityType;


    @Schema(description = "待分配权限DTO")
    private TaskNodePrivilegeDTO category;

    @Schema(description = "工作组Id")
    private Long teamId;

    @Schema(description = "工作组")
    private String teamName;

    @Schema(description = "工作场地Id")
    private Long workSiteId;

    @Schema(description = "工作场地")
    private String workSiteName;

    @Schema(description = "任务包名")
    private String taskPackageName;

    @Schema(description = "任务包Id")
    private Long taskPackageId;

    @Schema(description = "处理按钮是否可以点击")
    private Boolean enable;

    @Schema(description = "流程备注")
    private String memo;

    @Schema(description = "发起人姓名")
    private String ownerName;

    @Schema(description = "是否可以批量生成")
    private Boolean canBatchGenerate = false;

    @Schema(description = "是否能被选中")
    private Boolean canSelected = true;

    @Schema(description = "ERROR DESC")
    private String errorDesc;

    public List<String> getProcInsts() {
        return procInsts;
    }

    public void setProcInsts(List<String> procInsts) {
        this.procInsts = procInsts;
    }

    public List<String> getActTaskIds() {
        return actTaskIds;
    }

    public void setActTaskIds(List<String> actTaskIds) {
        this.actTaskIds = actTaskIds;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public List<Long> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        this.entityIds = entityIds;
    }

    public BpmProcess getBpmProcess() {
        return bpmProcess;
    }

    public void setBpmProcess(BpmProcess bpmProcess) {
        this.bpmProcess = bpmProcess;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public boolean isMutiSelectFlag() {
        return mutiSelectFlag;
    }

    public void setMutiSelectFlag(boolean mutiSelectFlag) {
        this.mutiSelectFlag = mutiSelectFlag;
    }

    public List<TaskGatewayDTO> getGateway() {
        return gateway;
    }

    public void setGateway(List<TaskGatewayDTO> gateway) {
        this.gateway = gateway;
    }

    public List<Map<String, Object>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, Object>> variables) {
        this.variables = variables;
    }

    public List<Map<String, Object>> getVariablesDisplay() {
        return variablesDisplay;
    }

    public void setVariablesDisplay(List<Map<String, Object>> variablesDisplay) {
        this.variablesDisplay = variablesDisplay;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(Map<String, Object> command) {
        this.command = command;
    }

    public Page<T> getMainList() {
        return mainList;
    }

    public void setMainList(Page<T> mainList) {
        this.mainList = mainList;
    }

    public Page<U> getAuxList() {
        return auxList;
    }

    public void setAuxList(Page<U> auxList) {
        this.auxList = auxList;
    }

    public Page<V> getMiscList() {
        return miscList;
    }

    public void setMiscList(Page<V> miscList) {
        this.miscList = miscList;
    }

    public TaskNodePrivilegeDTO getCategory() {
        return category;
    }

    public void setCategory(TaskNodePrivilegeDTO category) {
        this.category = category;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Boolean getCanBatchGenerate() {
        return canBatchGenerate;
    }

    public void setCanBatchGenerate(Boolean canBatchGenerate) {
        this.canBatchGenerate = canBatchGenerate;
    }

    public Boolean getCanSelected() {
        return canSelected;
    }

    public void setCanSelected(Boolean canSelected) {
        this.canSelected = canSelected;
    }

    public String getProcessCategory() {
        return processCategory;
    }

    public void setProcessCategory(String processCategory) {
        this.processCategory = processCategory;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public Map<String, Set<String>> getCompleteTasksMap() {
        return completeTasksMap;
    }

    public void setCompleteTasksMap(Map<String, Set<String>> completeTasksMap) {
        this.completeTasksMap = completeTasksMap;
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

    public boolean isBatchExecuteResult() {
        return batchExecuteResult;
    }

    public void setBatchExecuteResult(boolean batchExecuteResult) {
        this.batchExecuteResult = batchExecuteResult;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public List<BpmActivityInstanceBase> getBatchActInsts() {
        return batchActInsts;
    }

    public void setBatchActInsts(List<BpmActivityInstanceBase> batchActInsts) {
        this.batchActInsts = batchActInsts;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<Long, BpmActivityInstanceState> getBatchActInstStateMap() {
        return batchActInstStateMap;
    }

    public void setBatchActInstStateMap(Map<Long, BpmActivityInstanceState> batchActInstStateMap) {
        this.batchActInstStateMap = batchActInstStateMap;
    }

    public List<ActReportDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ActReportDTO> attachments) {
        this.attachments = attachments;
    }

    public List<ActReportDTO> getPics() {
        return pics;
    }

    public void setPics(List<ActReportDTO> pics) {
        this.pics = pics;
    }
}

