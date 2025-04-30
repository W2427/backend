package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 更新退库清单DTO
 */
public class FMaterialReturnPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    @Schema(description = "网关")
    private Map<String, Object> commandMap;

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getCommandMap() {
        return commandMap;
    }

    public void setCommandMap(Map<String, Object> commandMap) {
        this.commandMap = commandMap;
    }
}
