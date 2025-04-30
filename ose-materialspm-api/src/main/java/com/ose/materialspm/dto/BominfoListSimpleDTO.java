package com.ose.materialspm.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POH list查询DTO
 */
public class BominfoListSimpleDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "材料编码")
    private String tagNumber = "";

    @Schema(description = "bom路径")
    private String bomPath = "";

    @Schema(description = "描述")
    private String shortDesc = "";

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getBomPath() {
        return bomPath;
    }

    public void setBomPath(String bomPath) {
        this.bomPath = bomPath;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

}
