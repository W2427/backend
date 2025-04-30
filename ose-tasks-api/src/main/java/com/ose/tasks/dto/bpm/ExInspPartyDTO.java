package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


public class ExInspPartyDTO extends BaseDTO {

    private static final long serialVersionUID = -154894200248197147L;
    /**
     *
     */


    @Schema(description = "报检方")
    private String partyName;

    @Schema(description = "taskid")
    private String taskId;

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
