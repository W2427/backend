package com.ose.tasks.dto.rapairData;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 修改项目数据接口。
 */
public class RepairWbsSearchDTO extends PageDTO {

    private static final long serialVersionUID = 8535289621735231527L;

    @Schema(description = "工序")
    private String process;

    @Schema(description = "工序阶段")
    private String stage;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
