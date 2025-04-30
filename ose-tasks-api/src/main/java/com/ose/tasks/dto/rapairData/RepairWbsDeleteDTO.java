package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 修改项目数据接口。
 */
public class RepairWbsDeleteDTO extends BaseDTO {

    private static final long serialVersionUID = -4793287714029452116L;

    @Schema(description = "四级计划IDs")
    private List<Long> wbsIds;

    public List<Long> getWbsIds() {
        return wbsIds;
    }

    public void setWbsIds(List<Long> wbsIds) {
        this.wbsIds = wbsIds;
    }
}
