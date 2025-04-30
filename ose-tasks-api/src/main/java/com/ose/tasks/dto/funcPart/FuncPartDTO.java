package com.ose.tasks.dto.funcPart;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 业务代码创建数据传输对象。
 */
public class FuncPartDTO extends BaseDTO {

    private static final long serialVersionUID = 4729732497347995648L;
    @Schema(description = "功能分块")
    private String funcPart;

    @Schema(description = "功能说明")
    private String description;

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
