package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class CuttingCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "编号，名称")
    private String keyword;


    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


}
