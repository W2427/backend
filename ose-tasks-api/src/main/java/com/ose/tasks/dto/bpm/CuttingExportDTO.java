package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


public class CuttingExportDTO extends BaseDTO {

    private static final long serialVersionUID = -3236444790066188900L;

    @Schema(description = "下料单ids")
    private List<Long> cuttingIds;

    public List<Long> getCuttingIds() {
        return cuttingIds;
    }

    public void setCuttingIds(List<Long> cuttingIds) {
        this.cuttingIds = cuttingIds;
    }
}
