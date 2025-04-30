package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class ProcessKeyDTO extends BaseDTO {

    private static final long serialVersionUID = -8231282655407852000L;
    /**
     * 工序阶段 - 工序 列表
     */


    @Schema(description = "工序阶段 - 工序")
    private String processKey;

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }
}
