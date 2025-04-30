package com.ose.tasks.dto.deliverydoc;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 文档包生成参数DTO。
 */
public class DeliveryDocModulesDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "模块名")
    private String module;

    public DeliveryDocModulesDTO(String module) {
        super();
        this.module = module;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

}
