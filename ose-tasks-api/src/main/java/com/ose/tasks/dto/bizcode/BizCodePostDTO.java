package com.ose.tasks.dto.bizcode;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 业务代码创建数据传输对象。
 */
public class BizCodePostDTO extends BizCodePatchDTO {

    private static final long serialVersionUID = -1158138505889603547L;

    @Schema(description = "业务代码分类名称")
    @NotEmpty
    @Size(max = 255)
    private String typeName;

    private String bizCodeType;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBizCodeType() {
        return bizCodeType;
    }

    public void setBizCodeType(String bizCodeType) {
        this.bizCodeType = bizCodeType;
    }
}
