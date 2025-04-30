package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class PropertyDTO extends BaseDTO {

    private static final long serialVersionUID = 7553710845063489451L;

    @Schema(description = "自定义属性定义 ID")
    @NotBlank
    private Long propertyDefinitionId;

    @Schema(description = "设置的属性值")
    private List<String> values;

    public Long getPropertyDefinitionId() {
        return propertyDefinitionId;
    }

    public void setPropertyDefinitionId(Long propertyDefinitionId) {
        this.propertyDefinitionId = propertyDefinitionId;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
