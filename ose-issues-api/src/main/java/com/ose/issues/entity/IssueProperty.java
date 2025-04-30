package com.ose.issues.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseBizEntity;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.List;

/**
 * 问题信息的自定义属性。
 */
@Entity
@Table(name = "issue_properties",
indexes = {
    @Index(columnList = "projectId,issueId,propertyId")
})
public class IssueProperty extends BaseBizEntity {

    private static final long serialVersionUID = 8943118626863679122L;

    @Schema(description = "问题 ID")
    @Column
    private Long issueId;

    @Schema(description = "Project ID")
    @Column
    private Long projectId;

    @Schema(description = "属性类型")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private CustomPropertyType propertyType;

    @Schema(description = "属性 ID")
    @Column
    private Long propertyId;

    @Schema(description = "属性名称")
    @Column
    private String propertyName;

    @Schema(description = "属性值")
    @Column(name = "value_options")
    private String values;

    public IssueProperty() {
    }

    /**
     * 构造方法。
     *
     * @param issueId            问题 ID
     * @param propertyDefinition 自定义属性定义
     * @param values             属性值
     */
    public IssueProperty(
        final Long issueId,
        final IssuePropertyDefinition propertyDefinition,
        final List<String> values
    ) {
        setIssueId(issueId);
        setPropertyId(propertyDefinition.getId());
        setPropertyName(propertyDefinition.getName());
        setPropertyType(propertyDefinition.getPropertyType());
        setValues(values);
        setCreatedAt();
        setLastModifiedAt();
        setStatus(EntityStatus.ACTIVE);
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public CustomPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(CustomPropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValues() {
        return values;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @JsonSetter
    public void setValues(String values) {
        this.values = values;
    }

    public void setValues(List<String> values) {

        if (values == null) {
            return;
        }

        setValues(String.join("\r\n", values));
    }

}
