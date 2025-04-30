package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptPatchForConfirmDTO extends BaseDTO {

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
