package com.ose.issues.dto;

import com.ose.annotation.NullableNotBlank;
import com.ose.dto.BaseDTO;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PropertyDefinitionUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -3815145654010162637L;

    @Schema(description = "问题类型")
    private IssueType issueType;

    @Schema(description = "属性类型")
    private CustomPropertyType propertyType;

    @Schema(description = "遗留问题属性分类")
    private IssuePropertyCategory propertyCategory;

    @Schema(description = "属性名称")
    @NullableNotBlank
    private String name;

    @Schema(description = "可选值（属性类型为 OPTIONS_SINGLE 或 OPTIONS_MULTIPLE 时）")
    private List<String> options;

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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public IssuePropertyCategory getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(IssuePropertyCategory propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

}
