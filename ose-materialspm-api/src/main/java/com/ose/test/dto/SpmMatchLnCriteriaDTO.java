package com.ose.test.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SpmMatchLns 查询DTO
 */
public class SpmMatchLnCriteriaDTO extends PageDTO {

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM BOM Match节点 ID 集合")
    private String lnIdsString;// List 不能传递，需要转为 String

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getLnIdsString() {
        return lnIdsString;
    }

    public void setLnIdsString(String lnIdsString) {
        this.lnIdsString = lnIdsString;
    }
}
