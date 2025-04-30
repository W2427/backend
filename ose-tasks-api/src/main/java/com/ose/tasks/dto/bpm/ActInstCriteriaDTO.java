package com.ose.tasks.dto.bpm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.dto.PageDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 任务查询条件数据传输对象类。
 */
public class ActInstCriteriaDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "流程当前节点执行人")
    private String currentExecutor;

    @Schema(description = "工作组名称")
    private String teamName;

    @Schema(description = "任务包名称")
    private String taskPackageName;

    @Schema(description = "function名称")
    private String function;

    @Schema(description = "type名称")
    private String type;

    @Schema(description = "流程责任人")
    private String ownerName;

    @Schema(description = "工序分类Id")
    private Long processCategoryId;

    @Schema(description = "实体类型分类Id")
    private Long entityTypeId;

    @Schema(description = "实体类型Id")
    private Long entitySubTypeId;

    @Schema(description = "工序分类Id")
    private Long processStageId;

    @Schema(description = "工序Id")
    private Long processId;

    @Schema(description = "工序")
    private String processName;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "工作场地地址")
    private String workSiteAddress;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体编号")
    private Long entityId;

    @Schema(description = "完成状态")
    private String finishState;

    @Schema(description = "运行状态")
    private String suspensionState;

    @Schema(description = "流程定义id")
    private String procDefId;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "开始时间")
    private String createDateFrom;

    @Schema(description = "结束时间")
    private String createDateUntil;

    @Schema(description = "TASK DEF KEY")
    private String taskDefKey;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateFromTime;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateUntilTime;

    private Boolean pageable = false;

    private List<Long> actInstIds;

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
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

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public String getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(String suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public String getCurrentExecutor() {
        return currentExecutor;
    }

    public void setCurrentExecutor(String currentExecutor) {
        this.currentExecutor = currentExecutor;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
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

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public Boolean getPageable() {
        return pageable;
    }

    public void setPageable(Boolean pageable) {
        this.pageable = pageable;
    }

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
