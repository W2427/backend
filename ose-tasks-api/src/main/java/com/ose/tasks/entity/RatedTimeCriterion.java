package com.ose.tasks.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(
    name = "rated_time_criteria",
    indexes = {
        @Index(
            columnList = "processStageId, processId, entitySubTypeId"
        )
    }
)
public class RatedTimeCriterion extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 939018451502135254L;

    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "工序阶段ID")
    @Column
    private Long processStageId;

    @Schema(description = "工序ID")
    @Column
    private Long processId;

    @Schema(description = "实体子类型ID")
    @Column
    private Long entitySubTypeId;

    @Schema(description = "是否包含米单位")
    @Column
    private Boolean unitM = false;

    @Schema(description = "是否包含个单位")
    @Column
    private Boolean unitN = false;

    @Schema(description = "是否包含直径")
    @Column
    private Boolean pipeDiameter = false;

    @Schema(description = "是否包含管子厚度")
    @Column
    private Boolean pipeThickness = false;

    @Schema(description = "是否包含材料")
    @Column
    private Boolean material = false;

    @Schema(description = "是否包含测试压力")
    @Column
    private Boolean testPressure = false;

    @Schema(description = "是否包含介质")
    @Column
    private Boolean medium = false;

    @Schema(description = "是否包含清洁介质")
    @Column
    private Boolean cleaningMedium = false;

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

    public Boolean getUnitM() {
        return unitM;
    }

    public void setUnitM(Boolean unitM) {
        this.unitM = unitM;
    }

    public Boolean getUnitN() {
        return unitN;
    }

    public void setUnitN(Boolean unitN) {
        this.unitN = unitN;
    }

    public Boolean getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(Boolean pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public Boolean getPipeThickness() {
        return pipeThickness;
    }

    public void setPipeThickness(Boolean pipeThickness) {
        this.pipeThickness = pipeThickness;
    }

    public Boolean getMaterial() {
        return material;
    }

    public void setMaterial(Boolean material) {
        this.material = material;
    }

    public Boolean getTestPressure() {
        return testPressure;
    }

    public void setTestPressure(Boolean testPressure) {
        this.testPressure = testPressure;
    }

    public Boolean getMedium() {
        return medium;
    }

    public void setMedium(Boolean medium) {
        this.medium = medium;
    }

    public Boolean getCleaningMedium() {
        return cleaningMedium;
    }

    public void setCleaningMedium(Boolean cleaningMedium) {
        this.cleaningMedium = cleaningMedium;
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
