package com.ose.tasks.entity.taskpackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 任务包列表 包含 百分比信息。
 * 1. 在 taskPackage 中增加 total count，finished count, total work load, finished work load
 * 2. 当 wbs entry approved 的时候，更新 finished count, finished work load
 * 3. 当生成 wbs entry（关系到 task package）的时候，更新 total count, total work load
 *         - 生成 4级计划时候 增加
 *         - 向工作包添加实体的时候 重新计算
 * 4. 当删除 wbs entry的时候，更新 total count, finished count, total work load, finished work load
 *         - 删除实体的时候 计算
 *         - 工作包移除实体的时候重新计算
 */
@Entity
@Table(
    name = "task_package_percent"
)
public class TaskPackagePercent extends BaseVersionedBizEntity implements TaskPackageEntity {


    private static final long serialVersionUID = 5621224690658013444L;

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

    @Schema(description = "任务包排序")
    @Column
    private Integer sortOrder;

    @Schema(description = "完成的个数")
    @Column
    private Integer finishedCount;

    @Schema(description = "总个数")
    @Column
    private Integer totalCount;

    @Schema(description = "完成个数百分比")
    @Column
    private Double percentCount;

    @Schema(description = "完成的物量")
    @Column
    private Double finishedWorkLoad;

    @Schema(description = "总物量")
    @Column
    private Double totalWorkLoad;

    @Schema(description = "完成物量百分比")
    @Column
    private Double percentWorkLoad;


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

    public void setEntitySubTypeId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @JsonSetter
    public void setEntitySubTypeId(ReferenceData categoryRef) {
        this.categoryId = categoryRef.get$ref();
    }

    @Schema(description = "任务包分类 ID")
    @JsonSetter
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

    public Double getPercentCount() {
        return percentCount;
    }

    public void setPercentCount(Double percentCount) {
        this.percentCount = percentCount;
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

    public Double getPercentWorkLoad() {
        return percentWorkLoad;
    }

    public void setPercentWorkLoad(Double percentWorkLoad) {
        this.percentWorkLoad = percentWorkLoad;
    }
}
