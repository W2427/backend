package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 修复外检流程卡住的数据。
 */
public class RepairExinspDTO extends BaseDTO {

    private static final long serialVersionUID = -8018593983915308054L;

    @Schema(description = "流水号")
    private String seriesNo;

    @Schema(description = "专业")
    private String discipline;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
