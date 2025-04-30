package com.ose.tasks.entity.bpm;

import jakarta.persistence.*;

import org.springframework.data.annotation.Id;

import com.ose.entity.BaseBizEntity;

/**
 * 实体工序管理 实体类。
 */
@Entity
@Table(
    name = "bpm_entity_type_process_relation",
    indexes = {
        @Index(columnList = "process_id,status,entity_sub_type_id")
    }
)
public class BpmEntityTypeProcessRelation extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //项目id
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    //组织id
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    // 实体id
    @Id
    @ManyToOne
    @JoinColumn(name = "entity_sub_type_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private BpmEntitySubType entitySubType;


    // 工序id
    @Id
    @ManyToOne
    @JoinColumn(name = "process_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private BpmProcess process;

    @Column(name = "func_part_id", nullable = true)
    private Long funcPartId;

    @Column(name = "func_part", nullable = true)
    private String funcPart;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public BpmEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(BpmEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public BpmProcess getProcess() {
        return process;
    }

    public void setProcess(BpmProcess process) {
        this.process = process;
    }

    public Long getFuncPartId() {
        return funcPartId;
    }

    public void setFuncPartId(Long funcPartId) {
        this.funcPartId = funcPartId;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
