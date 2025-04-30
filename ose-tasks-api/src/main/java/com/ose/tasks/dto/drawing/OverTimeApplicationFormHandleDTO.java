package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class OverTimeApplicationFormHandleDTO extends BaseDTO {

    private static final long serialVersionUID = -6558971634301001384L;
    @Schema(description = "申请结果")
    private String result;

    @Schema(description = "备注")
    private String remark;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
