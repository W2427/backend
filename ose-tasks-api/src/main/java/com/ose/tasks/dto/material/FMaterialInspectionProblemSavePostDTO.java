package com.ose.tasks.dto.material;

import java.util.Map;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 创建开箱检验清单DTO
 */
public class FMaterialInspectionProblemSavePostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "网关")
    private Map<String, Object> commandMap;

    public Map<String, Object> getCommandMap() {
        return commandMap;
    }

    public void setCommandMap(Map<String, Object> commandMap) {
        this.commandMap = commandMap;
    }

}
