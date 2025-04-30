package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.BizCodeDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.vo.BpmTaskType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 实体管理 数据传输对象
 */
public class TodoTaskDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    //任务流程id
    private Long actInstId;

    // 的task_id 121212323123123
    private Long taskId;

    private String taskDefKey;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "当前执行人")
    private Long assignee;

    @Schema(description = "当前执行人")
    private String taskAssignName;

    private String version;

    private NDEType ndeType;

    // 实体编号
    private String entityNo;

    private String entityNo1;

    private String entityNo2;

    // 实体编号
    private String drawingTitle;

    // 安装图纸编号
    private String installationDrawingNo;

    // 安装图纸编号
    private String installationDrawingName;

    // 实体类型
    private String entitySubType;

    // 实体类型
    private String entitySubTypeCn;

    //流程图
    private String diagramResource;

    private boolean subDrawingFlg;

    private Long entityId;

    private String processCategory;

    // 工序分类
    private String processStage;

    // 工序
    private String process;

    //任务节点
    private String taskNode;

    //任务创建时间
    private Date taskCreatedTime;

    //网关flag
    private boolean mutiSelectFlag;

    //节点命令
    private List<TaskGatewayDTO> gateway;

    //运行状态
    private SuspensionState suspensionState;

    //流程变量-可编辑
    private List<Map<String, Object>> variables;

    //流程变量-不可编辑
    private List<Map<String, Object>> variablesDisplay;

    private List<ActReportDTO> documents;

    //外检时间
    private Date externalInspectionTime;

    //内检时间
    private Date internalInspectionTime;

    //业主外检
    private List<ExternalStatusDTO> customerExternal;

    //第三方外检
    private List<ExternalStatusDTO> thirdPartyExternal;

    //其他外检
    private List<ExternalStatusDTO> otherExternal;

    //外检清单
    private List<ActReportDTO> externalApplyList;

    //外检报告
    private List<ActReportDTO> externalReport;

    //节点命令
    private List<BizCodeDTO> externalInspectionStatusList;

    //材料DTO
    private TaskMaterialDTO materialDTO;

    @Schema(description = "待分配权限DTO")
    private TaskNodePrivilegeDTO category;

    @Schema(description = "待分配权限DTOs")
    private List<TaskNodePrivilegeDTO> categories;

    @Schema(description = "工作组Id")
    private Long teamId;

    @Schema(description = "工作组")
    private String teamName;

    @Schema(description = "工作场地Id")
    private Long workSiteId;

    @Schema(description = "工作场地")
    private String workSiteName;

    @Schema(description = "工作地址")
    private String workSiteAddress;

    @Schema(description = "任务包名")
    private String taskPackageName;

    @Schema(description = "任务包Id")
    private Long taskPackageId;

    @Schema(description = "流程内驳回次数")
    private int unAcceptCount;

    @Schema(description = "已焊接次数")
    private Integer weldCount = 0;

    @Schema(description = "子流程列表")
    private List<BpmActivityInstanceBase> subActInsts;

    @Schema(description = "处理按钮是否可以点击")
    private Boolean enable;

    @Schema(description = "焊口实体信息")
    private WeldEntity weldEntity;

    @Schema(description = "流程备注")
    private String memo;

    @Schema(description = "焊口返修次数")
    private Integer weldRepairCount = 0;

    @Schema(description = "焊口扩口次数")
    private Integer weldPenaltyCount = 0;

    @Schema(description = "发起人姓名")
    private String ownerName;

    @Schema(description = "是否有rt信息")
    private Boolean hasRtInfo = false;

    @Schema(description = "是否可以批量生成")
    private Boolean canBatchGenerate = false;

    @Schema(description = "是否能被选中")
    private Boolean canSelected = true;

    @Schema(description = "焊口NPS")
    private String weldNpsText;

    @Schema(description = "焊口壁厚等级")
    private String weldThickness;

    @Schema(description = "处理状态")
    private Boolean isHandling;

    @Schema(description = "专业")
    private String discipline;

    private String ndtBatch;

    private String welderNo;

    private String ratio;

    private String weldLength;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public Date getTaskCreatedTime() {
        return taskCreatedTime;
    }

    public void setTaskCreatedTime(Date taskCreatedTime) {
        this.taskCreatedTime = taskCreatedTime;
    }

    public List<TaskGatewayDTO> getGateway() {
        return gateway;
    }

    public void setGateway(List<TaskGatewayDTO> gateway) {
        this.gateway = gateway;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
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

    public boolean isMutiSelectFlag() {
        return mutiSelectFlag;
    }

    public void setMutiSelectFlag(boolean mutiSelectFlag) {
        this.mutiSelectFlag = mutiSelectFlag;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public Date getInternalInspectionTime() {
        return internalInspectionTime;
    }

    public void setInternalInspectionTime(Date internalInspectionTime) {
        this.internalInspectionTime = internalInspectionTime;
    }

    public List<ActReportDTO> getExternalReport() {
        return externalReport;
    }

    public void setExternalReport(List<ActReportDTO> externalReport) {
        this.externalReport = externalReport;
    }

    public List<ExternalStatusDTO> getCustomerExternal() {
        return customerExternal;
    }

    public void setCustomerExternal(List<ExternalStatusDTO> customerExternal) {
        this.customerExternal = customerExternal;
    }

    public List<ExternalStatusDTO> getThirdPartyExternal() {
        return thirdPartyExternal;
    }

    public void setThirdPartyExternal(List<ExternalStatusDTO> thirdPartyExternal) {
        this.thirdPartyExternal = thirdPartyExternal;
    }

    public List<ExternalStatusDTO> getOtherExternal() {
        return otherExternal;
    }

    public void setOtherExternal(List<ExternalStatusDTO> otherExternal) {
        this.otherExternal = otherExternal;
    }

    public List<BizCodeDTO> getExternalInspectionStatusList() {
        return externalInspectionStatusList;
    }

    public void setExternalInspectionStatusList(List<BizCodeDTO> externalInspectionStatusList) {
        this.externalInspectionStatusList = externalInspectionStatusList;
    }

    public List<ActReportDTO> getExternalApplyList() {
        return externalApplyList;
    }

    public void setExternalApplyList(List<ActReportDTO> externalApplyList) {
        this.externalApplyList = externalApplyList;
    }

    public List<ActReportDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ActReportDTO> documents) {
        this.documents = documents;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public TaskMaterialDTO getMaterialDTO() {
        return materialDTO;
    }

    public void setMaterialDTO(TaskMaterialDTO materialDTO) {
        this.materialDTO = materialDTO;
    }

    public String getProcessCategory() {
        return processCategory;
    }

    public void setProcessCategory(String processCategory) {
        this.processCategory = processCategory;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public boolean isSubDrawingFlg() {
        return subDrawingFlg;
    }

    public void setSubDrawingFlg(boolean subDrawingFlg) {
        this.subDrawingFlg = subDrawingFlg;
    }

    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public List<BpmActivityInstanceBase> getSubActInsts() {
        return subActInsts;
    }

    public void setSubActInsts(List<BpmActivityInstanceBase> subActInsts) {
        this.subActInsts = subActInsts;
    }

    public TaskNodePrivilegeDTO getCategory() {
        return category;
    }

    public void setCategory(TaskNodePrivilegeDTO category) {
        this.category = category;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public NDEType getNdeType() {
        return ndeType;
    }

    public void setNdeType(NDEType ndeType) {
        this.ndeType = ndeType;
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
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

    public int getUnAcceptCount() {
        return unAcceptCount;
    }

    public void setUnAcceptCount(int unAcceptCount) {
        this.unAcceptCount = unAcceptCount;
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

    public Integer getWeldCount() {
        return weldCount;
    }

    public void setWeldCount(Integer weldCount) {
        this.weldCount = weldCount;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public WeldEntity getWeldEntity() {
        return weldEntity;
    }

    public void setWeldEntity(WeldEntity weldEntity) {
        this.weldEntity = weldEntity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getWeldRepairCount() {
        return weldRepairCount;
    }

    public void setWeldRepairCount(Integer weldRepairCount) {
        this.weldRepairCount = weldRepairCount;
    }

    public Integer getWeldPenaltyCount() {
        return weldPenaltyCount;
    }

    public void setWeldPenaltyCount(Integer weldPenaltyCount) {
        this.weldPenaltyCount = weldPenaltyCount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Boolean getHasRtInfo() {
        return hasRtInfo;
    }

    public void setHasRtInfo(Boolean hasRtInfo) {
        this.hasRtInfo = hasRtInfo;
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

    public String getWeldNpsText() {
        return weldNpsText;
    }

    public void setWeldNpsText(String weldNpsText) {
        this.weldNpsText = weldNpsText;
    }

    public String getWeldThickness() {
        return weldThickness;
    }

    public void setWeldThickness(String weldThickness) {
        this.weldThickness = weldThickness;
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

    public Boolean getHandling() {
        return isHandling;
    }

    public void setHandling(Boolean handling) {
        isHandling = handling;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getNdtBatch() {
        return ndtBatch;
    }

    public void setNdtBatch(String ndtBatch) {
        this.ndtBatch = ndtBatch;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public List<TaskNodePrivilegeDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<TaskNodePrivilegeDTO> categories) {
        this.categories = categories;
    }

    public String getInstallationDrawingNo() {
        return installationDrawingNo;
    }

    public void setInstallationDrawingNo(String installationDrawingNo) {
        this.installationDrawingNo = installationDrawingNo;
    }

    public String getInstallationDrawingName() {
        return installationDrawingName;
    }

    public void setInstallationDrawingName(String installationDrawingName) {
        this.installationDrawingName = installationDrawingName;
    }

    public String getTaskAssignName() {
        return taskAssignName;
    }

    public void setTaskAssignName(String taskAssignName) {
        this.taskAssignName = taskAssignName;
    }

    public String getEntitySubTypeCn() {
        return entitySubTypeCn;
    }

    public void setEntitySubTypeCn(String entitySubTypeCn) {
        this.entitySubTypeCn = entitySubTypeCn;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public String getEntityNo1() {
        return entityNo1;
    }

    public void setEntityNo1(String entityNo1) {
        this.entityNo1 = entityNo1;
    }

    public String getEntityNo2() {
        return entityNo2;
    }

    public void setEntityNo2(String entityNo2) {
        this.entityNo2 = entityNo2;
    }
}
