package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class ExperienceStatusDTO extends BaseDTO {

    private static final long serialVersionUID = 8787877050966304322L;

    @Schema(name = "部门状态")
    private EntityStatus suspendStatus;

    public EntityStatus getSuspendStatus() {
        return suspendStatus;
    }

    public void setSuspendStatus(EntityStatus suspendStatus) {
        this.suspendStatus = suspendStatus;
    }

}
