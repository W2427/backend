package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.RelationType;

import jakarta.persistence.*;

/**
 * WBS 条目关系数据。
 */
@MappedSuperclass
public class WBSEntryRelationBasic extends BaseEntity {

    private static final long serialVersionUID = -2107640778703538764L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "关系类型")
    @Column
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    @Schema(description = "前置任务 GUID")
    @Column(length = 48)
    private String predecessorId;

    @Schema(description = "后置任务 GUID")
    @Column(length = 48)
    private String successorId;

    @Schema(description = "是否已被删除")
    @Column
    private Boolean deleted;

    @Schema(
        description = "前置任务可选时表示可以从前置任务推进到后续任务，但前置任务的启动不为后续任务启动的必要条件。"
            + "例如，从【NDT】可以推进到【焊接】，但是【焊接】的启动前提不包含【NDT】已启动。"
    )
    @Column
    private Boolean optional;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public String getPredecessorId() {
        return predecessorId;
    }

    public void setPredecessorId(String predecessorId) {
        this.predecessorId = predecessorId;
    }

    public String getSuccessorId() {
        return successorId;
    }

    public void setSuccessorId(String successorId) {
        this.successorId = successorId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }
}
