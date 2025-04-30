package com.ose.tasks.dto.deliverydoc;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 文档包生成参数DTO。
 */
public class GenerateDocDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "模块名")
    private String module;

    @Schema(description = "工序名")
    private String process;

    @Schema(description = "类型")
    private String type;

    public GenerateDocDTO() {
        super();
    }

    public GenerateDocDTO(String module, String process, String type) {
        super();
        this.module = module;
        this.process = process;
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
