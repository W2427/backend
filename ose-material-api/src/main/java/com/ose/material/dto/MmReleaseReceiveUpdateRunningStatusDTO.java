package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 材料入库单状态更新DTO
 */
public class MmReleaseReceiveUpdateRunningStatusDTO extends BaseDTO {

    private static final long serialVersionUID = -5276092812586394809L;

    @Schema(description = "运行状态")
    private EntityStatus runningStatus;

    @Schema(description = "是否完成入库")
    private Boolean receiveFinished;

    public Boolean getReceiveFinished() {
        return receiveFinished;
    }

    public void setReceiveFinished(Boolean receiveFinished) {
        this.receiveFinished = receiveFinished;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }
}
