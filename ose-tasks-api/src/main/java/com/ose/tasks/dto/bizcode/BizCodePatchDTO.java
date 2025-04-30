package com.ose.tasks.dto.bizcode;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;

/**
 * 业务代码创建数据传输对象。
 */
public class BizCodePatchDTO extends BaseDTO {

    private static final long serialVersionUID = 5440738780124735840L;

    @Schema(description = "业务代码")
    @Size(max = 64)
    private String code;

    @Schema(description = "业务代码名称")
    @Size(max = 255)
    private String name;

    @Schema(description = "业务代码描述")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
