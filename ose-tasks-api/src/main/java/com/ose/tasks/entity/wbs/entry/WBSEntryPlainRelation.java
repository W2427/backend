package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import com.ose.tasks.vo.wbs.WBSEntryType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(name = "wbs_entry_plain_relation"
    ,
    indexes = {
        @Index(columnList = "wbsEntryId,wbsEntryAncestorId"),
        @Index(columnList = "wbsEntryId"),
        @Index(columnList = "wbsEntryAncestorId,type"),
        @Index(columnList = "orgId,projectId,nodeId")
    }
    )
public class WBSEntryPlainRelation extends BaseEntity {


    private static final long serialVersionUID = 4092419876740120905L;
    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "层级ID wbsEntryId")
    @Column
    private Long wbsEntryId;

    @Schema(description = "层级 祖先ID wbsEntryId ancestor Id")
    @Column
    private Long wbsEntryAncestorId;

    @Schema(description = "Project Node Id")
    @Column
    private Long nodeId;

    @Schema(description = "WBS Entry Type")
    @Column
    @Enumerated(EnumType.STRING)
    private WBSEntryType type;

    @Schema(description = " Entity Id")
    @Column
    private Long entityId;

    @Schema(description = "深度 层次 depth")
    @Column
    private int depth;

    @Schema(description = "module No")
    @Column
    private String moduleNo;

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

    public Long getWbsEntryId() {
        return wbsEntryId;
    }

    public void setWbsEntryId(Long wbsEntryId) {
        this.wbsEntryId = wbsEntryId;
    }

    public Long getWbsEntryAncestorId() {
        return wbsEntryAncestorId;
    }

    public void setWbsEntryAncestorId(Long wbsEntryAncestorId) {
        this.wbsEntryAncestorId = wbsEntryAncestorId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public WBSEntryType getType() {
        return type;
    }

    public void setType(WBSEntryType type) {
        this.type = type;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }
}
