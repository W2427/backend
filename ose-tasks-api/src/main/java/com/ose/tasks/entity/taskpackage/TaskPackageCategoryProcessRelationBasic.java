package com.ose.tasks.entity.taskpackage;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 任务包类型-实体工序关系实体。
 */
@Entity
@Table(
    name = "task_package_category_process_relation",
    indexes = {
        @Index(columnList = "orgId,projectId,categoryId"),
        @Index(columnList = "categoryId,processId", unique = true)
    }
)
public class TaskPackageCategoryProcessRelationBasic extends BaseBizEntity {

    private static final long serialVersionUID = 5470330638238972519L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包分类 ID")
    @Column(nullable = false)
    private Long categoryId;

    @Schema(description = "工序 ID")
    @Column(nullable = false)
    private Long processId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setEntitySubTypeId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

}
