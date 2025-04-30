package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.*;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WBS 条目数据。
 */
@MappedSuperclass
public abstract class WBSEntryBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -720703054253660608L;

    private static final Pattern WBS_PATTERN = Pattern.compile("^(.+\\.)?([^.]+)$");

    // 四级计划提前开始小时数默认值
    public static final int DEFAULT_START_BEFORE_HOURS = 60 * 24;

    // 支持的专业
    public static final Set<String> SUPPORTED_FUNC_PARTS = new HashSet<>(Arrays.asList("ENGINEERING","PIPING", "STRUCTURE","ELECTRICAL"));

    static {
        SUPPORTED_FUNC_PARTS.add("PIPING");
    }

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

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

    @Schema(description = "任务名称1")
    @Column
    private String name1;

    @Schema(description = "任务名称2")
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
    private Integer startBeforeHours = DEFAULT_START_BEFORE_HOURS;

    @Schema(description = "任务可提前开始的 Unix 时间")
    @Column
    private Double startUnixTime;

    @Schema(description = "所属模块层级节点 ID")
    @Column
    private Long moduleHierarchyNodeId;

    @Schema(description = "功能块 设计 管道等")
    @Column
    private String funcPart;

    /**
     * 从 P6 导出文件解析计划条目的 WBS 信息。
     *
     * @param task 从 P6 导出文件解析的任务节点信息
     * @return WBS 信息
     */

    public static String repairWBS(Task task) {

        Task parent = task.getParentTask();
        String wbs = task.getWBS();

        // 若存在上级任务且当前任务的 WBS 不以上级任务的 WBS 开始则重新设置当前任务的 WBS
        // 使当前任务的 WBS 以其上级任务的 WBS 开始（P6 v8.x 导出文件 BUG 容错）
        if (parent != null && !wbs.startsWith(parent.getWBS())) {

            // 设置上级任务的 WBS（递归）
            parent.setWBS(repairWBS(parent));

            List<String> parentWBS = new ArrayList<>(Arrays.asList(parent.getWBS().split("\\.")));
            List<String> segments = new ArrayList<>();
            String prefix = null;

            // 从当前任务的 WBS 的前部取得与其上级任务的 WBS 的后部重合的部分
            for (int i = parentWBS.size() - 1; i > 1; i--) {
                segments.add(0, parentWBS.get(i));
                if (wbs.startsWith(String.join(".", segments))) {
                    prefix = String.join(".", segments);
                }
            }

            // 若当前任务的 WBS 存在与其上级任务的 WBS 存在重合的部分
            // 则将当前任务的 WBS 中与其上级任务的 WBS 重合的部分替换为其上级任务的 WBS
            if (prefix != null) {
                task.setWBS(parent.getWBS() + wbs.substring(prefix.length()));
            }
        }

        return task.getWBS();
    }

    public WBSEntryBase() {
        super();
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

        String wbs = (String) task.getWBS();//TaskField.TEXT1);

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

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    /**
     * 专业是否被系统支持。
     *
     * @return 是否被系统支持
     */
    @JsonIgnore
    public boolean isFuncPartSupported() {
        return funcPart != null && SUPPORTED_FUNC_PARTS.contains(funcPart);
    }

    public Double getEstimatedManHours() {
        return estimatedManHours;
    }

    public void setEstimatedManHours(Double estimatedManHours) {
        this.estimatedManHours = estimatedManHours;
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
