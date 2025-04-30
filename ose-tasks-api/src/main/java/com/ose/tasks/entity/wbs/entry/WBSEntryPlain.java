package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.*;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * WBS 条目数据。
 */
@Entity
@Table(
    name = "wbs_entries_plain"
)
public class WBSEntryPlain extends BaseVersionedBizEntity {


    private static final long serialVersionUID = 2818732168668878662L;

    private static Random random = new Random(System.currentTimeMillis());


    private static final Pattern WBS_PATTERN = Pattern.compile("^(.+\\.)?([^.]+)$");

    // 四级计划提前开始小时数默认值
    public static final int DEFAULT_START_BEFORE_HOURS = 60 * 24;

    // 支持的专业
    public static final Set<String> SUPPORTED_DISCIPLINES = new HashSet<>(Arrays.asList("PIPING", "STRUCTURE","ELECTRICAL"));

    static {
        SUPPORTED_DISCIPLINES.add("PIPING");
    }

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "项目 ID")
    @Column
    private Long sProjectId;

    @Schema(description = "上级 WBS ID")
    @Column
    private Long parentId;

    @Schema(description = "WBS 条目层级深度")
    @Column
    private Integer depth;

    @Schema(description = "WBS 条目排序权重")
    @Column
    private Long sort;

    @Schema(description = "WBS 条目类型")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private WBSEntryType type;

    @Schema(description = "编号（简）")
    @Column
    private String no;

    @Schema(description = "任务名称")
    @Column
    private String name;

    @Schema(description = "任务名称（1）")
    @Column
    private String name1;

    @Schema(description = "任务名称（2）")
    @Column
    private String name2;

    @Schema(description = "模块类型")
    @Column
    private String moduleType;

    @Schema(description = "所属区域/模块/子系统编号")
    @Column(length = 128)
    private String sector;

    @Schema(description = "所属层/包编号")
    @Column(length = 128)
    private String layerPackage;

    @Schema(description = "工序阶段")
    @Column(length = 32)
    private String stage;

    @Schema(description = "工序")
    @Column(length = 32)
    private String process;

    @Schema(description = "工作场地 ID")
    @Column
    private Long workSiteId;

    @Schema(description = "工作场地名称")
    @Column
    private String workSiteName;

    @Schema(description = "工作场地地址")
    @Column
    private String workSiteAddress;

    @Schema(description = "模块号")
    @Column
    private String moduleNo;

    @Schema(description = "开始时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date startAt;

    @Schema(description = "截止时间")
    @Column
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    private Date finishAt;

    @Schema(description = "历时")
    @Column
    private Double duration;

    @Schema(description = "历时单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private TimeUnit durationUnit;

    @Schema(description = "是否活跃")
    @Column(columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean active;

    @Schema(description = "GUID")
    @Column(nullable = false)
    private String guid;

    @Schema(description = "项目节点 ID")
    @Column
    private Long projectNodeId;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "实体类型")
    @Column(length = 32)

    private String entityType;

    @Schema(description = "实体子类型")
    @Column(length = 64)
    private String entitySubType;

    @Schema(description = "抽检比例")
    @Column
    private Integer proportion;

    @Schema(description = "计划工时(新)")
    @Column
    private Double estimatedManHours;

    @Schema(description = "四级计划提前自动启动的小时数")
    @Column
    private Integer startBeforeHours = 0;

    @Schema(description = "任务可提前开始的 Unix 时间")
    @Column
    private Double startUnixTime;

    @Schema(description = "所属模块层级节点 ID")
    @Column
    private Long moduleHierarchyNodeId;

    @Schema(description = "专业")
    @Column
    private String discipline;

    @Schema(description = "公司 ID")
    @Column
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "建造视图层级节点 ID")
    @Column
    private Long hierarchyNodeId;

    @Schema(description = "建造视图上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "抽检随机编号")
    @Column
    private Integer randomNo;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "工作流实例 ID")
    @Column(length = 16)
    private String processInstanceId;

    @Schema(description = "四级计划运行状态")
    @Column
    @Enumerated(EnumType.STRING)
    private WBSEntryRunningStatus runningStatus;

    @Schema(description = "备注")
    @Lob
    @Column(length = 4096)
    private String remarks;

    @Schema(description = "任务包 ID")
    @Column
    private Long taskPackageId;

    @Schema(description = "ISO 图纸编号")
    @Column
    private Integer dwgShtNo;

    @Schema(description = "工作量 物量")
    @Column
    private Integer workLoad;

    @Schema(description = "材料匹配率")
    private Double bomMatchPercent;

    @Schema(description = "图纸发放状态")
    private Boolean issueStatus;

    @Schema(description = "层级节点路径")
    private String hnPath;

    @Schema(description = "WBS 条目路径")
    @Column
    private String path;

    @Schema(description = "编号（全）")
    @Column
    private String wbs;

    @Schema(description = "图纸标题")
    @Column
    private String drawingTitle;


    /**
     * 构造方法。
     */
    public WBSEntryPlain() {
        super();
        randomNo = Math.abs(random.nextInt());
    }

    public String getDrawingTitle() {
        return drawingTitle;
    }

    public void setDrawingTitle(String drawingTitle) {
        this.drawingTitle = drawingTitle;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public Integer getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(Integer randomNo) {
        this.randomNo = randomNo;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }

    public Double getBomMatchPercent() {
        return bomMatchPercent;
    }

    public void setBomMatchPercent(Double bomMatchPercent) {
        this.bomMatchPercent = bomMatchPercent;
    }

    public Boolean getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Boolean issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getHnPath() {
        return hnPath;
    }

    public void setHnPath(String hnPath) {
        this.hnPath = hnPath;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public WBSEntryType getType() {
        return type;
    }

    @JsonSetter
    public void setType(WBSEntryType type) {
        this.type = type;
    }

    public void setType(TaskType type) {
        setType(WBSEntryType.getInstance(type.name()));
    }

    public String getNo() {
        return no;
    }

    @JsonSetter
    public void setNo(String no) {
        this.no = no;
    }

    public void setNo(Task task) {

        String wbs = (String) task.getWBS();//(TaskField.TEXT1);

        if (wbs == null) {
            wbs = task.getWBS();
        }

        Matcher matcher = WBS_PATTERN.matcher(wbs);

        if (matcher.matches()) {
            this.no = matcher.group(2);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
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

    public String getWorkSiteAddress() {
        return workSiteAddress;
    }

    public void setWorkSiteAddress(String workSiteAddress) {
        this.workSiteAddress = workSiteAddress;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
        setStartUnixTime();
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

    @JsonSetter
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setDuration(Duration duration) {

        if (duration == null) {
            return;
        }

        setDuration(duration.getDuration());
        setDurationUnit(duration.getUnits());
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getStartBeforeHours() {
        return (startBeforeHours == null || startBeforeHours < 0) ? DEFAULT_START_BEFORE_HOURS : startBeforeHours;
    }

    public void setStartBeforeHours(Integer startBeforeHours) {
        this.startBeforeHours = startBeforeHours;
        setStartUnixTime();
    }

    public Double getStartUnixTime() {
        return startUnixTime;
    }

    @JsonSetter
    public void setStartUnixTime(Double startUnixTime) {
        this.startUnixTime = startUnixTime;
    }

    public void setStartUnixTime() {

        if (getStartAt() == null) {
            return;
        }

        double startBeforeSeconds = getStartBeforeHours() * 3600;
        double startAt = Math.floor(getStartAt().getTime() / 1000.0);

        this.startUnixTime = startAt - startBeforeSeconds;
    }

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
    }

    public String getLayerPackage() {
        return layerPackage;
    }

    public void setLayerPackage(String layerPackage) {
        this.layerPackage = layerPackage;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline == null ? "" : discipline.toUpperCase();
    }

    public Double getEstimatedManHours() {
        return estimatedManHours;
    }

    public void setEstimatedManHours(Double estimatedManHours) {
        this.estimatedManHours = estimatedManHours;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    public WBSEntryRunningStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(WBSEntryRunningStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public Long getsProjectId() {
        return sProjectId;
    }

    public void setsProjectId(Long sProjectId) {
        this.sProjectId = sProjectId;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    @Schema(description = "")
    @JsonProperty(value = "taskPackageId", access = READ_ONLY)
    public ReferenceData getTaskPackageIdRef() {
        return this.taskPackageId == null
            ? null
            : new ReferenceData(this.taskPackageId);
    }

    @Schema(description = "")
    @JsonProperty(value = "workSiteId", access = READ_ONLY)
    public ReferenceData getWorkSiteIddRef() {
        return this.workSiteId == null
            ? null
            : new ReferenceData(this.workSiteId);
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
