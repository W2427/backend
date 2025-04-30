package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 传输对象
 */
public class DrawingDetailQueryDTO extends BaseDTO {

    private static final long serialVersionUID = -8493337269647543095L;
    @Schema(description = "状态")
    private List<EntityStatus> status;

    public List<EntityStatus> getStatus() {
        return status;
    }

    public void setStatus(List<EntityStatus> status) {
        this.status = status;
    }
}
