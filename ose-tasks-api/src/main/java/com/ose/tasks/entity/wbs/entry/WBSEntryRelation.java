package com.ose.tasks.entity.wbs.entry;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import net.sf.mpxj.RelationType;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * WBS 条目关系数据。
 */
@Entity
@Table(
    name = "wbs_entry_relation",
    indexes = {
        @Index(
            columnList = "projectId,predecessorId,successorId",
            unique = true
        ),
        @Index(columnList = "projectId,predecessorId"),
        @Index(columnList = "projectId,successorId")
    }
)
public class WBSEntryRelation extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 6818376401769588113L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "前置任务 GUID")
    @Column(length = 48)
    private String predecessorId;

    @Schema(description = "后置任务 GUID")
    @Column(length = 48)
    private String successorId;

    @Schema(description = "关系类型")
    @Column
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    @Schema(
        name = "前置任务是否可选",
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

    public String getPredecessorId() {
        return predecessorId;
    }

    @JsonSetter
    public void setPredecessorId(String predecessorId) {
        this.predecessorId = predecessorId;
    }

    public void setPredecessorId(UUID predecessorId) {
        setPredecessorId(predecessorId.toString());
    }

    public String getSuccessorId() {
        return successorId;
    }

    public void setSuccessorId(String successorId) {
        this.successorId = successorId;
    }

    public void setSuccessorId(UUID successorId) {
        setSuccessorId(successorId.toString());
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }
}
