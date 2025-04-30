package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PropertyDefinitionCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 8346931005703393791L;

    @Schema(description = "问题类型")
    @NotNull
    private IssueType issueType;

    @Schema(description = "属性类型")
    @NotNull
    private CustomPropertyType propertyType;

    @Schema(description = "属性分类")
    private IssuePropertyCategory propertyCategory;

    @Schema(description = "属性名称")
    @NotBlank
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
