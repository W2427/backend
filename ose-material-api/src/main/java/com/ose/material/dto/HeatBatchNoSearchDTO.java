package com.ose.material.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 炉批号查询DTO
 */
public class HeatBatchNoSearchDTO extends PageDTO {

    private static final long serialVersionUID = -3611089106320372155L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "搜索关键字")
    public String heatNo;

    @Schema(description = "搜索关键字")
    public String batchNo;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
