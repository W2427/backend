package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class PlainCheckDTO extends BaseDTO {
    private static final long serialVersionUID = 4749150391523580046L;

    @Schema(description = "状态")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
