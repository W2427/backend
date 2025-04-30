package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExInspActInstHandleHistoryDTO extends PageDTO {

    private static final long serialVersionUID = -5077307251955130306L;

    @Schema(description = "查找关键字")
    private String keyword;

    @Schema(description = "运行状态")
    private String runningStatus;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
    }
}
