package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.TimeUnit;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 项目 WBS 层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "上级条目 ID")
    private Long parentId;

    @Schema(description = "WBS 条目类型")
    private WBSEntryType type;

    @Schema(description = "管线编号")
    private String isoNo;

    @Schema(description = "编号（简）")
    private String no = "";

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "开始时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date startAt;

    @Schema(description = "截止时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date finishAt;

    @Schema(description = "历时")
    private Double duration;

    @Schema(description = "历时单位")
    private TimeUnit durationUnit;

    @Schema(description = "是否活跃")
    private Boolean active;

    @Schema(description = "子条目权重总和")
    private double totalScore;

    @Schema(description = "已完成的子条目权重总和")
    private double finishedScore;

    @Schema(description = "实际开始时间（工作流启动时间）")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date startedAt;

    @Schema(description = "工作流启动者 ID")
    private String startedBy;

    @Schema(description = "实际结束时间")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date finishedAt;

    @Schema(description = "实际工时")
    private Double actualDuration;

    @Schema(description = "任务是否已完成")
    private boolean finished;

    @Schema(description = "四级计划运行状态")
    private WBSEntryRunningStatus runningStatus;

    @Schema(description = "工作组 ID")
    @JsonIgnore
    private Long teamId;

    @Schema(description = "工作场地名称")
    private String workSiteName;

    @Schema(description = "前置任务列表")
    @JsonIgnore
    private List<WBSEntryPredecessor> predecessors;

    @Schema(description = "后置任务列表")
    @JsonIgnore
    private List<WBSEntrySuccessor> successors;

    @Schema(description = "层级节点列表")
    private List<WBSEntryDTO> children = null;

    @Schema(description = "所属模块层级节点 ID")
    private Long moduleHierarchyNodeId;

    @Schema(description = "模块类型")
    private String moduleType;

    @Schema(description = "模块编号")
    @JsonProperty(value = "moduleNo")
    private String sector;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "工序阶段")
    private String stage;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "WBS 条目排序权重")
    private Long sort;

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public WBSEntryDTO() {
        super();
    }

    public WBSEntryDTO(WBSEntry entry) {
        this();
        BeanUtils.copyProperties(entry, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public WBSEntryType getType() {
        return type;
    }

    public void setType(WBSEntryType type) {
        this.type = type;
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
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

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public double getFinishedScore() {
        return finishedScore;
    }

    public void setFinishedScore(double finishedScore) {
        this.finishedScore = finishedScore;
    }

    @JsonProperty("progress")
    public double getProgress() {
        return totalScore == 0.0 ? 0.0 : (Math.round(finishedScore * 10000 / totalScore) / 100.0);
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Double getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Double actualDuration) {
        this.actualDuration = actualDuration;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getWorkSiteName() {
        return workSiteName;
    }

    public void setWorkSiteName(String workSiteName) {
        this.workSiteName = workSiteName;
    }

    public List<WBSEntryPredecessor> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<WBSEntryPredecessor> predecessors) {
        this.predecessors = predecessors;
    }

    public List<WBSEntrySuccessor> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<WBSEntrySuccessor> successors) {
        this.successors = successors;
    }

    public List<WBSEntryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<WBSEntryDTO> children) {
        this.children = children;
    }

    public void addChild(WBSEntryDTO child) {

        if (child == null) {
            return;
        }

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    private <T extends WBSEntryRelationBasic> List<WBSRelationDTO> getRelations(List<T> entries) {

        if (entries == null || entries.size() == 0) {
            return null;
        }

        List<WBSRelationDTO> relations = new ArrayList<>();
        WBSRelationDTO relation;

        for (T entry : entries) {

            relation = new WBSRelationDTO(entry);

            if (!relation.getValid()) {
                continue;
            }

            relations.add(relation);
        }

        return relations.size() == 0 ? null : relations;
    }

    @JsonProperty(value = "predecessors", access = READ_ONLY)
    public List<WBSRelationDTO> getPredecessorRelations() {
        return getRelations(this.predecessors);
    }

    @JsonProperty(value = "successors", access = READ_ONLY)
    public List<WBSRelationDTO> getSuccessorRelations() {
        return getRelations(this.successors);
    }

    @Override
    public Set<Long> relatedOrgIDs() {
        return teamId == null
            ? new HashSet<>()
            : new HashSet<>(Collections.singletonList(teamId));
    }

    @Schema(description = "工作组信息")
    @JsonProperty(value = "team", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getTeamRef() {
        return teamId == null ? null : new ReferenceData(teamId);
    }

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public boolean isDisciplineSupported() {
        return discipline != null && WBSEntryBase.SUPPORTED_FUNC_PARTS.contains(discipline);
    }

    public boolean isWbsValid() {
        return !StringUtils.isEmpty(sector) && !StringUtils.isEmpty(stage) && !StringUtils.isEmpty(process);
    }
}
