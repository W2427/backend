package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.RatedTimeUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(
    name = "rated_times",
    indexes = {
        @Index(columnList = "processStageId, processId, entitySubTypeId")
    }
)
public class RatedTime extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8557924268764561456L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "工序阶段 ID")
    @Column
    private Long processStageId;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "定额工时查询字段 ID")
    @Column
    private Long ratedTimeCriterionId;

    @Schema(description = "实体类型 ID")
    @Column
    private Long entitySubTypeId;

    @Schema(description = "单位")
    @Column
    @Enumerated(EnumType.STRING)
    private RatedTimeUnit unit;

    @Schema(description = "管径")
    @Column
    private Double pipeDiameter;

    @Schema(description = "管子最小厚度")
    @Column
    private Double minPipeThickness;

    @Schema(description = "管子最大厚度")
    @Column
    private Double maxPipeThickness;

    @Schema(description = "材质")
    @Column
    private String material;

    @Schema(description = "最小试压")
    @Column
    private Double minTestPressure;

    @Schema(description = "最大试压")
    @Column
    private Double maxTestPressure;

    @Schema(description = "介质")
    @Column
    private String medium;

    @Schema(description = "清洁介质")
    @Column
    private String cleaningMedium;

    @Schema(description = "人工时")
    @Column
    private Double hourNorm;

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

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public RatedTimeUnit getUnit() {
        return unit;
    }

    public void setUnit(RatedTimeUnit unit) {
        this.unit = unit;
    }

    public void setUnit(String unit) {
        this.unit = RatedTimeUnit.valueOf(unit);
    }

    public Double getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(Double pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public Double getMinPipeThickness() {
        return minPipeThickness;
    }

    public void setMinPipeThickness(Double minPipeThickness) {
        this.minPipeThickness = minPipeThickness;
    }

    public Double getMaxPipeThickness() {
        return maxPipeThickness;
    }

    public void setMaxPipeThickness(Double maxPipeThickness) {
        this.maxPipeThickness = maxPipeThickness;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getMinTestPressure() {
        return minTestPressure;
    }

    public void setMinTestPressure(Double minTestPressure) {
        this.minTestPressure = minTestPressure;
    }

    public Double getMaxTestPressure() {
        return maxTestPressure;
    }

    public void setMaxTestPressure(Double maxTestPressure) {
        this.maxTestPressure = maxTestPressure;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getCleaningMedium() {
        return cleaningMedium;
    }

    public void setCleaningMedium(String cleaningMedium) {
        this.cleaningMedium = cleaningMedium;
    }

    public Double getHourNorm() {
        return hourNorm;
    }

    public void setHourNorm(Double hourNorm) {
        this.hourNorm = hourNorm;
    }

    public Long getRatedTimeCriterionId() {
        return ratedTimeCriterionId;
    }

    public void setRatedTimeCriterionId(Long ratedTimeCriterionId) {
        this.ratedTimeCriterionId = ratedTimeCriterionId;
    }

    @JsonProperty(value = "processId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getProcessReference() {
        return this.processId == null ? null : new ReferenceData(this.processId);
    }

    @JsonProperty(value = "processStageId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getProcessStageReference() {
        return this.processStageId == null ? null : new ReferenceData(this.processStageId);
    }

    @JsonProperty(value = "entitySubTypeId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getEntitySubTypeReference() {
        return this.entitySubTypeId == null ? null : new ReferenceData(this.entitySubTypeId);
    }
}
