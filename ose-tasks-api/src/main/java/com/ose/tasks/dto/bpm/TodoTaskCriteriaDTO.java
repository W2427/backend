package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 任务查询条件数据传输对象类。
 */
public class TodoTaskCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "工序分类")
    private Long processCategoryId;

    @Schema(description = "实体类型")
    private Long entitySubTypeId;

    @Schema(description = "工序阶段")
    private Long processStageId;

    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "工序英文名")
    private String processName;

    @Schema(description = "任务节点")
    private String taskNode;

    @Schema(description = "模块")
    private String entityModuleNames;

    @Schema(description = "任务包ID列表")
    private List<String> taskPackageList;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "工作场地地址")
    private String workSiteAddress;

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

    @Schema(description = "处理状态")
    private Boolean isHandling;

    @Schema(description = "状态检索：ACTIVE-进行中，SUSPEND-已挂起")
    private String stateSearch;

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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public Boolean getHandling() {
        return isHandling;
    }

    public void setHandling(Boolean handling) {
        isHandling = handling;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public List<String> getTaskPackageList() {
        return taskPackageList;
    }

    public void setTaskPackageList(List<String> taskPackageList) {
        this.taskPackageList = taskPackageList;
    }
}
