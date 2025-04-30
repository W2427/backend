package com.ose.tasks.entity.taskpackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 任务包实体。
 */
@Entity
@Table(
    name = "task_package"
)
public class TaskPackageBasic extends BaseVersionedBizEntity implements TaskPackageEntity {

    private static final long serialVersionUID = 103577754047589518L;

    @Schema(description = "所属组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "所属项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "任务包类型")
    @Column(nullable = false)
    @JsonIgnore
    private Long categoryId;

    @Schema(description = "任务包名称")
    @Column(nullable = false)
    private String name;

    @Schema(description = "操作者id")
    @Column
    private Long lastModifiedBy;

    @Schema(description = "操作者name")
    @Column
    private String lastModifiedName;

    @Schema(description = "专业")
    @Column(nullable = false)
    private String discipline;

    @Schema(description = "任务包排序")
    @Column
    private Integer sortOrder;

    @Schema(description = "完成的个数")
    @Column(columnDefinition = "int default 0")
    private Integer finishedCount = 0;

    @Schema(description = "总个数")
    @Column(columnDefinition = "int default 0")
    private Integer totalCount = 0;

    @Schema(description = "完成的物量")
    @Column(columnDefinition = "double default 0.0")
    private Double finishedWorkLoad = 0.0;

    @Schema(description = "总物量")
    @Column(columnDefinition = "double default 0.0")
    private Double totalWorkLoad = 0.0;

    @Schema(description = "备注")
    @Column(length = 500)
    private String memo;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @JsonSetter
    public void setCategoryId(ReferenceData categoryRef) {
        this.categoryId = categoryRef.get$ref();
    }

    @Schema(description = "任务包分类 ID")
    @JsonProperty(value = "categoryId")
    public ReferenceData getCategoryRef() {
        return new ReferenceData(this.categoryId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getFinishedWorkLoad() {
        return finishedWorkLoad;
    }

    public void setFinishedWorkLoad(Double finishedWorkLoad) {
        this.finishedWorkLoad = finishedWorkLoad;
    }

    public Double getTotalWorkLoad() {
        return totalWorkLoad;
    }

    public void setTotalWorkLoad(Double totalWorkLoad) {
        this.totalWorkLoad = totalWorkLoad;
    }

    @Override
    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedName() {
        return lastModifiedName;
    }

    public void setLastModifiedName(String lastModifiedName) {
        this.lastModifiedName = lastModifiedName;
    }
}
