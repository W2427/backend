package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.tasks.vo.qc.NDEType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 批处理任务查询条件数据传输对象类。
 */
public class TodoBatchTaskCriteriaDTO extends BaseBatchTaskCriteriaDTO {


    private static final long serialVersionUID = -3893257377765122482L;

    @Schema(description = "工序分类")
    private Long processCategoryId;

    @Schema(description = "实体类型")
    private Long entitySubTypeId;

    @Schema(description = "工序阶段")
    private Long processStageId;

    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "assignees，执行人列表")
    private List<Long> assignees;

    @Schema(description = "模块")
    private String entityModuleNames;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "工作组")
    private String teamName;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "任务状态")
    private String suspensionState;

    @Schema(description = "是否分页，默认true")
    private boolean pageable = true;

    @Schema(description = "客户端类型")
    private String clientType;

    @Schema(description = "ndeType")
    private NDEType ndeType;

    @Schema(description = "二维码")
    private String qrcode;

    @Schema(description = "生成报告类型")
    private GenerateReportType genType;

    @Schema(description = "任务节点DefKey")
    private String taskDefKey;

    @Schema(description = "开始时间")
    private String createDateFrom;

    @Schema(description = "结束时间")
    private String createDateUntil;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateFromTime;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateUntilTime;

    @Schema(description = "状态检索：ACTIVE-进行中，SUSPEND-已挂起")
    private String stateSearch;

    @Schema(description = "是否需要 bpm 的图形的详细信息")
    private boolean bpmInfoRequired;

    public String getStateSearch() {
        return stateSearch;
    }

    public void setStateSearch(String stateSearch) {
        this.stateSearch = stateSearch;
    }

    public boolean isActiveSearch() {
        return "ACTIVE".equals(stateSearch);
    }

    public boolean isSuspendSearch() {
        return "SUSPEND".equals(stateSearch);
    }

    public String getTaskNode() {
        return taskNode;
    }

    public void setTaskNode(String taskNode) {
        this.taskNode = taskNode;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(String suspensionState) {
        this.suspensionState = suspensionState;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }

    public String getEntityModuleNames() {
        return entityModuleNames;
    }

    public void setEntityModuleNames(String entityModuleNames) {
        this.entityModuleNames = entityModuleNames;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public NDEType getNdeType() {
        return ndeType;
    }

    public void setNdeType(NDEType ndeType) {
        this.ndeType = ndeType;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public enum GenerateReportType {
        OTHER("OTHER"),
        REPAIR("REPAIR"),
        PENALTY("PENALTY");

        private String name;

        GenerateReportType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public GenerateReportType getGenType() {
        return genType;
    }

    public void setGenType(GenerateReportType genType) {
        this.genType = genType;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(String createDateFrom) {
        this.createDateFrom = createDateFrom;
        this.createDateFromTime = parseDate(createDateFrom);
    }

    public String getCreateDateUntil() {
        return createDateUntil;
    }

    public void setCreateDateUntil(String createDateUntil) {
        this.createDateUntil = createDateUntil;
        this.createDateUntilTime = parseDate(createDateUntil);
    }

    private Date parseDate(String dateStr) {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getCreateDateFromTime() {
        return createDateFromTime;
    }

    public void setCreateDateFromTime(Date createDateFromTime) {
        this.createDateFromTime = createDateFromTime;
    }

    public Date getCreateDateUntilTime() {
        return createDateUntilTime;
    }

    public void setCreateDateUntilTime(Date createDateUntilTime) {
        this.createDateUntilTime = createDateUntilTime;
    }

    public List<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Long> assignees) {
        this.assignees = assignees;
    }

    @Override
    public boolean isBpmInfoRequired() {
        return bpmInfoRequired;
    }

    @Override
    public void setBpmInfoRequired(boolean bpmInfoRequired) {
        this.bpmInfoRequired = bpmInfoRequired;
    }
}
