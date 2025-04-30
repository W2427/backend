package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.dto.HierarchyNodeWithErrorDTO;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 项目层级结构节点数据实体。
 */
@Entity
@Table(
    name = "hierarchy_node",
    indexes = {
        @Index(columnList = "hierarchyType,deleted,node_id"),
        @Index(columnList = "hierarchyType,deleted,path"),
        @Index(columnList = "projectId,deleted,depth DESC"),
        @Index(columnList = "id,deleted"),
        @Index(columnList = "projectId,node_id,deleted"),
        @Index(columnList = "projectId,deleted,id"),
        @Index(columnList = "node_id,deleted"),
        @Index(columnList = "parentId,hierarchyType"),
        @Index(columnList = "deleted,path,node_id")
    }
)
public class HierarchyNode extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -7772310838084455865L;

    @Schema(description = "所属公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务节点路径")
    @Column(nullable = false)
    private String path;

    //fftj,如/ISO,/ISO/SPOOL,/ISO/SPOOL/PIPE_PIECE,/ISO/SPOOL/SBW
//    @Schema(description = "实体子类型路径")
//    @Column(nullable = false)
//    private String entitySubTypePath;

    @Schema(description = "上级节点 ID")
    @Column
    private Long parentId;

    @JsonIgnore
    @Transient
    private HierarchyNode parentHierarchyNode = null;

    @Schema(description = "项目节点 ID")
    @Transient
    private Long projectNodeId;

    @Schema(description = "节点信息")
    @ManyToOne
    @JoinColumn(name = "node_id")
    private ProjectNode node = new ProjectNode();

    @Schema(description = "层级深度")
    @Column(nullable = false)
    private int depth;

    @Schema(description = "排序顺序")
    @Column(nullable = false)
    private int sort;

    @Schema(description = "层级类型")
    @Column(length = 45, nullable = false)
    private String hierarchyType;

    @Schema(description = "图纸类型")
    @Column
    private String drawingType;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "下级节点列表")
    @Transient
    private List<HierarchyNode> children = new ArrayList<>();

    @Schema(description = "是否为新节点")
    @Transient
    private boolean isNew = false;

    @Schema(description = "整体完成进度")
    @Column
    private Double overallFinishedScore;

    @Schema(description = "所占整体进度")
    @Column
    private Double overallTotalScore;

    @Schema(description = "整体计划工时")
    @Column
    private Double overallPlanHours;

    @Schema(description = "整体实际工时")
    @Column
    private Double overallActualHours;

    @Schema(description = "安装完成进度")
    @Column
    private Double installationFinishedScore;

    @Schema(description = "安装整体进度")
    @Column
    private Double installationTotalScore;

    @Schema(description = "安装计划工时")
    @Column
    private Double installationPlanHours;

    @Schema(description = "安装实际工时")
    @Column
    private Double installationActualHours;

    @Schema(description = "试压包完成进度")
    @Column
    private Double ptpFinishedScore;

    @Schema(description = "试压包整体进度")
    @Column
    private Double ptpTotalScore;

    @Schema(description = "试压包计划工时")
    @Column
    private Double ptpPlanHours;

    @Schema(description = "试压包实际共识")
    @Column
    private Double ptpActualHours;

    @Schema(description = "清洁完成进度")
    @Column
    private Double cleannessFinishedScore;

    @Schema(description = "清洁整体进度")
    @Column
    private Double cleannessTotalScore;

    @Schema(description = "清洁计划工时")
    @Column
    private Double cleannessPlanHours;

    @Schema(description = "清洁实际工时")
    @Column
    private Double cleannessActualHours;

    @Schema(description = "子系统完成进度")
    @Column
    private Double mcFinishedScore;

    @Schema(description = "子系统整体进度")
    @Column
    private Double mcTotalScore;

    @Schema(description = "子系统计划工时")
    @Column
    private Double mcPlanHours;

    @Schema(description = "子系统实际工时")
    @Column
    private Double mcActualHours;

    @Schema(description = "导入数据传输对象")
    @Transient
    private HierarchyNodeWithErrorDTO dto;

    public HierarchyNode() {
        super();
    }

    public HierarchyNode(HierarchyNode parentNode) {
        this();
        setParentNode(parentNode);
    }

    public HierarchyNode(HierarchyNode parentNode, String discipline) {
        this();
        node.setDiscipline(discipline);
        setParentNode(parentNode);
    }

    public HierarchyNode(HierarchyNode parentNode, ProjectNode node) {
        this(parentNode);

        setNode(node);
    }

    private void setHierarchyType() {

        if (node == null || parentHierarchyNode == null) {
            return;
        }
        hierarchyType = parentHierarchyNode.getHierarchyType();

    }

    @JsonIgnore
    public void setParentNode(HierarchyNode parentNode) {
        parentHierarchyNode = parentNode;
        setCompanyId(parentNode.getCompanyId());
        setOrgId(parentNode.getOrgId());
        setProjectId(parentNode.getProjectId());
        setParentId(parentNode.getId());
        String parentPath = (parentNode.getPath() == null ? "" : parentNode.getPath()) +
            (parentNode.getId() == null ? "" : parentNode.getId().toString());
        setPath(parentPath + "/");
        setDepth(parentNode.getDepth() + 1);
        setStatus(EntityStatus.ACTIVE);
        setHierarchyType(parentNode.getHierarchyType());
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
        node.setCompanyId(companyId);
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
        node.setOrgId(orgId);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
        node.setProjectId(projectId);
    }

    @JsonIgnore
    public String getNo() {
        return node.getNo();
    }

    @JsonIgnore
    public void setNo(String no, String parentNo) {
        node.setNo(no, parentNo);
    }

    @JsonIgnore
    public void setNo(String no) {
        // 注意：非实体类型使用
        node.setNo(no);
    }

    @JsonIgnore
    public String getEntityType() {
        return node.getEntityType();
    }

    @JsonIgnore
    public void setEntitySubType(String entitySubType) {
        node.setEntitySubType(entitySubType);
    }

    @JsonIgnore
    public String getEntitySubType() {
        return node.getEntitySubType();
    }

    @JsonIgnore
    public void setEntityType(String entityType) {
        node.setEntityType(entityType);
    }

    @JsonIgnore
    public Long getEntityId() {
        return node.getEntityId();
    }

    @JsonIgnore
    public void setEntityId(Long entityId) {
        node.setEntityId(entityId);
    }

    @JsonIgnore
    public String getModuleType() {
        return node.getModuleType();
    }

    @JsonIgnore
    public void setModuleType(String moduleType) {
        node.setModuleType(moduleType);
    }

    @JsonIgnore
    public String getBatchNo() {
        return node.getBatchNo();
    }

    @JsonIgnore
    public void setBatchNo(String batchNo) {
        node.setBatchNo(batchNo);
    }

    @JsonIgnore
    public boolean isPack() {
        return node.isPack();
    }

    @JsonIgnore
    public void setPack(boolean isPack) {
        node.setPack(isPack);
    }

    public String getPath() {
        return path;
    }

    @JsonIgnore
    public List<Long> getParentIDs() {

        List<String> parentIDs = new ArrayList<>(
            Arrays.asList(this.getPath().split("/"))
        );

        parentIDs.removeIf(StringUtils::isEmpty);

        return LongUtils.change2Str(parentIDs);
    }

    @JsonProperty(value = "parents", access = JsonProperty.Access.READ_ONLY)
    public List<ReferenceData> getParentRefs() {

        List<ReferenceData> parents = new ArrayList<>();

        for (Long parentId : getParentIDs()) {
            parents.add(new ReferenceData(parentId));
        }

        return parents;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getProjectNodeId() {
        return this.projectNodeId;
    }

    @JsonSetter
    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public void setProjectNodeId(ProjectNode projectNode) {
        this.projectNodeId = projectNode == null ? null : projectNode.getId();
    }

    public ProjectNode getNode() {
        return node;
    }

    public void setNode(ProjectNode node) {
        this.node = node == null ? this.node : node;
        setStatus(EntityStatus.ACTIVE);
        setHierarchyType();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<HierarchyNode> getChildren() {
        return children;
    }

    public void setChildren(List<HierarchyNode> children) {
        this.children = children;
    }

    public void addChild(HierarchyNode child) {
        children.add(child);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Double getOverallFinishedScore() {
        return overallFinishedScore;
    }

    public void setOverallFinishedScore(Double overallFinishedScore) {
        this.overallFinishedScore = overallFinishedScore;
    }

    public Double getOverallTotalScore() {
        return overallTotalScore;
    }

    public void setOverallTotalScore(Double overallTotalScore) {
        this.overallTotalScore = overallTotalScore;
    }

    public Double getOverallPlanHours() {
        return overallPlanHours;
    }

    public void setOverallPlanHours(Double overallPlanHours) {
        this.overallPlanHours = overallPlanHours;
    }

    public Double getOverallActualHours() {
        return overallActualHours;
    }

    public void setOverallActualHours(Double overallActualHours) {
        this.overallActualHours = overallActualHours;
    }

    public Double getInstallationFinishedScore() {
        return installationFinishedScore;
    }

    public void setInstallationFinishedScore(Double installationFinishedScore) {
        this.installationFinishedScore = installationFinishedScore;
    }

    public Double getInstallationTotalScore() {
        return installationTotalScore;
    }

    public void setInstallationTotalScore(Double installationTotalScore) {
        this.installationTotalScore = installationTotalScore;
    }

    public Double getInstallationPlanHours() {
        return installationPlanHours;
    }

    public void setInstallationPlanHours(Double installationPlanHours) {
        this.installationPlanHours = installationPlanHours;
    }

    public Double getInstallationActualHours() {
        return installationActualHours;
    }

    public void setInstallationActualHours(Double installationActualHours) {
        this.installationActualHours = installationActualHours;
    }

    public Double getPtpFinishedScore() {
        return ptpFinishedScore;
    }

    public void setPtpFinishedScore(Double ptpFinishedScore) {
        this.ptpFinishedScore = ptpFinishedScore;
    }

    public Double getPtpTotalScore() {
        return ptpTotalScore;
    }

    public void setPtpTotalScore(Double ptpTotalScore) {
        this.ptpTotalScore = ptpTotalScore;
    }

    public Double getPtpPlanHours() {
        return ptpPlanHours;
    }

    public void setPtpPlanHours(Double ptpPlanHours) {
        this.ptpPlanHours = ptpPlanHours;
    }

    public Double getPtpActualHours() {
        return ptpActualHours;
    }

    public void setPtpActualHours(Double ptpActualHours) {
        this.ptpActualHours = ptpActualHours;
    }

    public Double getCleannessFinishedScore() {
        return cleannessFinishedScore;
    }

    public void setCleannessFinishedScore(Double cleannessFinishedScore) {
        this.cleannessFinishedScore = cleannessFinishedScore;
    }

    public Double getCleannessTotalScore() {
        return cleannessTotalScore;
    }

    public void setCleannessTotalScore(Double cleannessTotalScore) {
        this.cleannessTotalScore = cleannessTotalScore;
    }

    public Double getCleannessPlanHours() {
        return cleannessPlanHours;
    }

    public void setCleannessPlanHours(Double cleannessPlanHours) {
        this.cleannessPlanHours = cleannessPlanHours;
    }

    public Double getCleannessActualHours() {
        return cleannessActualHours;
    }

    public void setCleannessActualHours(Double cleannessActualHours) {
        this.cleannessActualHours = cleannessActualHours;
    }

    public Double getMcFinishedScore() {
        return mcFinishedScore;
    }

    public void setMcFinishedScore(Double mcFinishedScore) {
        this.mcFinishedScore = mcFinishedScore;
    }

    public Double getMcTotalScore() {
        return mcTotalScore;
    }

    public void setMcTotalScore(Double mcTotalScore) {
        this.mcTotalScore = mcTotalScore;
    }

    public Double getMcPlanHours() {
        return mcPlanHours;
    }

    public void setMcPlanHours(Double mcPlanHours) {
        this.mcPlanHours = mcPlanHours;
    }

    public Double getMcActualHours() {
        return mcActualHours;
    }

    public void setMcActualHours(Double mcActualHours) {
        this.mcActualHours = mcActualHours;
    }

    public HierarchyNodeWithErrorDTO getDto() {
        return dto;
    }

    public void setDto(HierarchyNodeWithErrorDTO dto) {
        this.dto = dto;
    }

    @Override
    public void setCreatedAt(Date createdAt) {

        super.setCreatedAt(createdAt);

        if (node != null && node.getCreatedAt() == null) {
            node.setCreatedAt(createdAt);
        }

    }

    @Override
    public void setCreatedBy(Long createdBy) {

        super.setCreatedBy(createdBy);

        if (node != null && node.getCreatedBy() == null) {
            node.setCreatedBy(createdBy);
        }

    }

    @Override
    public void setLastModifiedAt(Date lastModifiedAt) {

        super.setLastModifiedAt(lastModifiedAt);

        if (node != null && node.getLastModifiedAt() == null) {
            node.setLastModifiedAt(lastModifiedAt);
        }

    }

    @Override
    public void setLastModifiedAt() {

        super.setLastModifiedAt();

        if (node != null) {
            node.setLastModifiedAt();
        }

    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {

        super.setLastModifiedBy(lastModifiedBy);

        if (node != null && node.getLastModifiedBy() == null) {
            node.setLastModifiedBy(lastModifiedBy);
        }

    }

    @Override
    public void setStatus(EntityStatus status) {

        super.setStatus(status);

        if (node != null && node.getStatus() == null) {
            node.setStatus(status);
        }

    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }
}
