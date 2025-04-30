package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.PageDTO;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * 项目 WBS 条目查询数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryPlainQueryDTO extends PageDTO {


    private static final long serialVersionUID = -1475173349024925899L;

    @Schema(description = "条目类型")
    private List<WBSEntryType> entryTypes;

    /**
     * 计划开始时间，包含此时间及晚于此时间的计划
     */
    @Schema(description = "计划开始时间（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startAt = null;


    @Schema(description = "计划完成时间（yyyy-MM-ddTHH:mm:ss.SSSZ）")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date finishAt = null;


    //前台做成一个下拉框，显示班组名称
    @Schema(description = "实施班组 ID")
    private List<Long> teamIds = null;

    @Schema(description = "四级计划名称")
    private String name = null;

    //前台做成一个下拉框，显示场地名称
    @Schema(description = "施工场地 ID")
    private List<String> workSiteIds = null;

    @Schema(description = "场地名称")
    @Column(nullable = false)
    private String workSiteName;

    @Schema(description = "场地地址")
    @Column
    private String workSiteAddress;

    //S M 等
    @Schema(description = "模块类型")
    private List<String> moduleTypes = null;

    @Schema(description = "所属模块层级节点 ID")
    private List<String> moduleIds = null;

    @Schema(description = "模块号")
    private  String moduleNo;

    @Schema(description = "工序阶段")
    private List<String> stages = null;

    @Schema(description = "工序")
    private List<String> processes = null;

    @Schema(description = "实体编号（前部匹配）")
    private String entityNo = null;

    @Schema(description = "实体类型")
    private List<String> entityTypes = null;

    @Schema(description = "实体子类型")
    private List<String> entitySubTypes = null;

    @Schema(description = "是否已启用（默认查询启用的条目）")
    private Boolean active = true;

    @Schema(description = "是否已结束（默认查询全部）")
    private Boolean finished = null;

    @Schema(description = "任务执行状态（默认查询全部）")
    private List<WBSEntryRunningStatus> runningStatus = null;

    @Schema(description = "工作包 ID")
    private List<Long> taskPackageIds;

    @Schema(description = "材料到货匹配率")
    private Double bomMatchPercent;

    @Schema(description = "物量 工作量")
    private Integer workLoad;

    @Schema(description = "图纸状态")
    private Boolean issueStatus;

    @Schema(description = "前置任务全部完成")
    private Boolean predecessorDone;

    public List<WBSEntryType> getEntryTypes() {
        return entryTypes;
    }

    public void setEntryTypes(List<WBSEntryType> entryTypes) {
        this.entryTypes = entryTypes;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public List<String> getWorkSiteIds() {
        return workSiteIds;
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

    public void setWorkSiteIds(List<String> workSiteIds) {
        this.workSiteIds = workSiteIds;
    }

    public List<String> getModuleTypes() {
        return moduleTypes;
    }

    public void setModuleTypes(List<String> moduleTypes) {
        this.moduleTypes = moduleTypes;
    }

    public List<String> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<String> moduleIds) {
        this.moduleIds = moduleIds;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public List<String> getProcesses() {
        return processes;
    }

    public void setProcesses(List<String> processes) {
        this.processes = processes;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public List<String> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<String> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public List<String> getEntitySubTypes() {
        return entitySubTypes;
    }

    public void setEntitySubTypes(List<String> entitySubTypes) {
        this.entitySubTypes = entitySubTypes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<WBSEntryRunningStatus> getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(List<WBSEntryRunningStatus> runningStatus) {
        this.runningStatus = runningStatus;
    }


    public List<Long> getTaskPackageIds() {
        return taskPackageIds;
    }

    public void setTaskPackageIds(List<Long> taskPackageIds) {
        this.taskPackageIds = taskPackageIds;
    }

    public Double getBomMatchPercent() {
        return bomMatchPercent;
    }

    public void setBomMatchPercent(Double bomMatchPercent) {
        this.bomMatchPercent = bomMatchPercent;
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public Boolean getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Boolean issueStatus) {
        this.issueStatus = issueStatus;
    }

    public Boolean getPredecessorDone() {
        return predecessorDone;
    }

    public void setPredecessorDone(Boolean predecessorDone) {
        this.predecessorDone = predecessorDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
