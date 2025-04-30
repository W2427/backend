package com.ose.test.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Demo查询结果DTO
 */
public class BomNodeResultsDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "BOM NODE LN ID")
    private String lnId;

    @Schema(description = "BOM NODE LN CODE")
    private String lnCode;

    @Schema(description = "BOM NODE bompath")
    private String bompath;

    @Schema(description = "BOM NODE LIST")
    private List<BomNodeResultsDTO> children;

    public String getLnId() {
        return lnId;
    }

    public void setLnId(String lnId) {
        this.lnId = lnId;
    }

    public String getLnCode() {
        return lnCode;
    }

    public void setLnCode(String lnCode) {
        this.lnCode = lnCode;
    }

    public String getBompath() {
        return bompath;
    }

    public void setBompath(String bompath) {
        this.bompath = bompath;
    }

    public List<BomNodeResultsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<BomNodeResultsDTO> children) {
        this.children = children;
    }

}
