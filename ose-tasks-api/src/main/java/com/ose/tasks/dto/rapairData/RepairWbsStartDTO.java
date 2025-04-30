package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 修改项目数据接口。
 */
public class RepairWbsStartDTO extends BaseDTO {

    private static final long serialVersionUID = -1769417010831339131L;

    @Schema(description = "日志")
    private List<Long> websIds ;

    public List<Long> getWebsIds() {
        return websIds;
    }

    public void setWebsIds(List<Long> websIds) {
        this.websIds = websIds;
    }
}
