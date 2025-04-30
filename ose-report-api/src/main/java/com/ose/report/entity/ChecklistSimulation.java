package com.ose.report.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 检查单模拟实体类
 */
@Entity
@Table(name = "report_checklist_simulations")
public class ChecklistSimulation extends BaseEntity {

    private static final long serialVersionUID = -4231100569010628814L;

    // 组织ID
    @Column(nullable = false)
    @NotNull(message = "checklist's org id is required")
    private Long orgId;

    // 项目ID
    @Column(nullable = false)
    @NotNull(message = "checklist simulation's project id is required")
    private Long projectId;

    // 名称
    @Column(nullable = false, length = 64)
    @NotNull(message = "checklist simulation's name is required")
    private String name;

    // 报表分类（检查单，周报，......）
    @Column(nullable = false)
    @NotNull(message = "checklist simulation's checklist id is required")
    @JsonIgnore
    private Long checklistId;

    // 模拟数据
    @Lob
    @Column(columnDefinition = "TEXT")
    private String simulationData;

    // 模拟报表文件
    @Column(length = 256)
    private Long generatedFile;

    /**
     * 默认构造方法
     */
    public ChecklistSimulation() {
        super();
    }

    /**
     * 默认方法
     *
     * @param id 检查单模拟ID
     */
    public ChecklistSimulation(Long id) {
        super(id);
    }

    /**
     * Gets the value of orgId.
     *
     * @return the value of orgId
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * Sets the orgId.
     *
     * <p>You can use getOrgId() to get the value of orgId</p>
     *
     * @param orgId orgId
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * Gets the value of projectId.
     *
     * @return the value of projectId
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * Sets the projectId.
     *
     * <p>You can use getProjectId() to get the value of projectId</p>
     *
     * @param projectId projectId
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the value of name.
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of checklistId.
     *
     * @return the value of checklistId
     */
    public Long getChecklistId() {
        return checklistId;
    }

    /**
     * Sets the checklistId.
     *
     * <p>You can use getChecklistId() to get the value of checklistId</p>
     *
     * @param checklistId checklistId
     */
    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }

    /**
     * Gets the value of simulationData.
     *
     * @return the value of simulationData
     */
    public String getSimulationData() {
        return simulationData;
    }

    /**
     * Sets the simulationData.
     *
     * <p>You can use getSimulationData() to get the value of simulationData</p>
     *
     * @param simulationData simulationData
     */
    public void setSimulationData(String simulationData) {
        this.simulationData = simulationData;
    }

    /**
     * Gets the value of generatedFile.
     *
     * @return the value of generatedFile
     */
    public Long getGeneratedFile() {
        return generatedFile;
    }

    /**
     * Sets the generatedFile.
     *
     * <p>You can use getGeneratedFile() to get the value of generatedFile</p>
     *
     * @param generatedFile generatedFile
     */
    public void setGeneratedFile(Long generatedFile) {
        this.generatedFile = generatedFile;
    }

    /**
     * 关联信息（检查单ID）
     *
     * @return $ref id
     */
    @JsonProperty(value = "checklistId", access = READ_ONLY)
    public ReferenceData getChecklistIdByRef() {
        return this.checklistId == null
            ? null
            : new ReferenceData(this.checklistId);
    }
}
