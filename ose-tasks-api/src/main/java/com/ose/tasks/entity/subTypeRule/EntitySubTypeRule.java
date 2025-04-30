package com.ose.tasks.entity.subTypeRule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.wbs.EntityTypeRule;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 管线实体数据实体。
 */
@Entity
@Table(name = "entity_category_rule")
public class EntitySubTypeRule extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -4302591077326324446L;

    @Schema(description = "公司 ID")
    @Column(nullable = false)
    private Long companyId;

    @Schema(description = "组织 ID")
    @Column(nullable = false)
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column(nullable = false)
    private Long projectId;

    @Schema(description = "实体大类型")
    @Column(nullable = false, length = 64)
    private String entityType;

    @Schema(description = "实体子类型")
    @Column(nullable = false, length = 64)
    private String subType;

    @Schema(description = "实体子类型ID")
    @Column(nullable = false)
    private Long entitySubTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "orgId", referencedColumnName = "orgId"),
        @JoinColumn(updatable = false, insertable = false, name = "projectId", referencedColumnName = "projectId"),
        @JoinColumn(updatable = false, insertable = false, name = "entitySubTypeId", referencedColumnName = "id")
    })
    private BpmEntitySubType entitySubType;


    @Schema(description = "实体类型规则")
    @Column(nullable = false, length = 128)
    @Enumerated(EnumType.STRING)
    private EntityTypeRule ruleType;

    @Schema(description = "实体类型规则优先顺序")
    @Column(nullable = false, length = 3)
    private Integer ruleOrder;

    @Schema(description = "条件值1")
    @Column(nullable = false)
    private String value1;

    @Schema(description = "条件值2")
    @Column
    private String value2;

    @Schema(description = "父级类型")
    @Column
    private String parentType;

    @Schema(description = "是否需要保温厚度信息")
    @Column(nullable = false)
    private Boolean thicknessRequired;

    @JsonCreator
    public EntitySubTypeRule() {
        this(null);
        this.setStatus(EntityStatus.ACTIVE);
    }

    /**
     * 构造函数。
     *
     * @param project 项目信息
     */
    public EntitySubTypeRule(Project project) {
        if (project != null) {
            this.companyId = project.getCompanyId();
            this.orgId = project.getOrgId();
            this.projectId = project.getId();
        }
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public BpmEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(BpmEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public EntityTypeRule getRuleType() {
        return ruleType;
    }

    public void setRuleType(EntityTypeRule ruleType) {
        this.ruleType = ruleType;
    }

    public Integer getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(Integer ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        if (!StringUtils.isEmpty(parentType)) {
            this.parentType = parentType;
        }
    }

    public Boolean getThicknessRequired() {
        return thicknessRequired;
    }

    public void setThicknessRequired(Boolean thicknessRequired) {
        this.thicknessRequired = thicknessRequired;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
