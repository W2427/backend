package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WarehouseLocationDTO extends BaseDTO {

    private static final long serialVersionUID = 5345458041535990031L;

    @Schema(description = "详细")
    private List<WarehouseLoactionDetailDTO> datas;

    public List<WarehouseLoactionDetailDTO> getDatas() {
        return datas;
    }

    public void setDatas(List<WarehouseLoactionDetailDTO> datas) {
        this.datas = datas;
    }
}
