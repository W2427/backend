package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务包类型实体。
 */
@Entity
@Table(
    name = "task_package_category"
)
public class TaskPackageCategory extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -3018780668051064667L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "名称")
    @Column(nullable = false)
    private String name;

    @Schema(description = "描述")
    @Column(nullable = false)
    private String description;

    @Schema(description = "实体类型")
    @Column(nullable = false, length = 32)
    private String entityType;

    @Schema(description = "功能分类")
    @Column(nullable = false, length = 32)
    private String funcPart;

    @Schema(description = "专业")
    @Column(nullable = false, length = 32)
    private String discipline;

    @Schema(description = "任务包类型备注")
    @Column(length = 500)
    private String memo;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
