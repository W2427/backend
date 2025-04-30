package com.ose.issues.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义属性定义数据实体。
 */
@Entity
@Table(name = "issue_property_definitions")
public class IssuePropertyDefinition extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -5796483696477615534L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "问题类型")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    @Schema(description = "类型")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private CustomPropertyType propertyType;

    @Schema(description = "遗留问题属性分类")
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private IssuePropertyCategory propertyCategory;

    @Schema(description = "属性名称")
    @Column
    private String name;

    @Schema(description = "短描述，标题")
    @Column
    private String shortDesc;

    @Schema(description = "可选值列表")
    @Column
    private String options;

    @Schema(description = "是否是类属性")
    @Column
    private Boolean isIssueField;


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

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public CustomPropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(CustomPropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Boolean getIssueField() {
        return isIssueField;
    }

    public void setIssueField(Boolean issueField) {
        isIssueField = issueField;
    }

    @JsonIgnore
    public String getOptions() {
        return options;
    }

    @JsonSetter
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * 设置选项列表。
     *
     * @param options 选项列表
     */
    public void setOptions(Collection<String> options) {

        if (options == null || options.size() == 0) {
            return;
        }

        this.options = String.join("\r\n", options);
    }

    /**
     * 取得选项列表。
     *
     * @return 选项列表
     */
    @JsonProperty(value = "options", access = JsonProperty.Access.READ_ONLY)
    public List<String> getOptionList() {

        List<String> options = new ArrayList<>();

        if (this.options != null) {
            for (String option : this.options.split("\r\n")) {
                if (StringUtils.isEmpty(option)) {
                    continue;
                }
                options.add(option);
            }
        }

        return options;
    }

    public IssuePropertyCategory getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(IssuePropertyCategory propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

}
