package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.vo.material.NestGateWay;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务包-实体关系实体。
 */
@Entity
@Table(
    name = "task_package_entity_relation",
    indexes = {
        @Index(columnList = "taskPackageId,entityId,entityType"),
        @Index(columnList = "projectId,entityId")
    }
)
public class TaskPackageEntityRelation extends BaseBizEntity {

    private static final long serialVersionUID = 5494102692255724460L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包 ID")
    @Column(nullable = false)
    private Long taskPackageId;

    @Schema(description = "层级节点 ID")
    @Column(nullable = false, length = 32)
    private String entityType;

    @Schema(description = "实体 ID")
    @Column(nullable = false)
    private Long entityId;

    @Schema(description = "实体子类型")
    @Column(length = 64)
    private String entitySubType;

    @Schema(description = "实体编号")
    @Column(nullable = false)
    private String entityNo;

    @Schema(description = "管材已领用")
    @Column
    private Boolean isUsedInPipe =false;

    @Schema(description = "内场管附件已领用")
    @Column
    private Boolean isUsedInShopComponent =false;

    @Schema(description = "外场管附件已领用")
    @Column
    private Boolean isUsedInFieldComponent =false;

    @Schema(description = "套料状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private NestGateWay nestGateWay = NestGateWay.NONE;

    public TaskPackageEntityRelation() {
    }

    public TaskPackageEntityRelation(TaskPackage taskPackage, ProjectNode entity) {
        this.setOrgId(taskPackage.getOrgId());
        this.setProjectId(taskPackage.getProjectId());
        this.setTaskPackageId(taskPackage.getId());
        this.setEntityType(entity.getEntityType());
        this.setEntityId(entity.getId());
        this.setEntitySubType(entity.getEntitySubType());
        this.setEntityNo(entity.getNo());
        this.setStatus(EntityStatus.ACTIVE);
    }

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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public Boolean getUsedInPipe() {
        return isUsedInPipe;
    }

    public void setUsedInPipe(Boolean usedInPipe) {
        isUsedInPipe = usedInPipe;
    }

    public Boolean getUsedInShopComponent() {
        return isUsedInShopComponent;
    }

    public void setUsedInShopComponent(Boolean usedInShopComponent) {
        isUsedInShopComponent = usedInShopComponent;
    }

    public Boolean getUsedInFieldComponent() {
        return isUsedInFieldComponent;
    }

    public void setUsedInFieldComponent(Boolean usedInFieldComponent) {
        isUsedInFieldComponent = usedInFieldComponent;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }
}
