package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库清单详情DTO
 */
public class FMaterialIssueReceiptDetailPostDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "专业")
    private String dpCode;

    @Schema(description = "材料清单BOM的编号")
    private String lpId;

    @Schema(description = "SPM 领料单详情ID")
    private String spmFaDetailId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "单位")
    private String unitCode;

    @Schema(description = "出库数量")
    private Double issueQty = 0.0;

    @Schema(description = "超额出库数量")
    private Double overIssueQty = 0.0;

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getLpId() {
        return lpId;
    }

    public void setLpId(String lpId) {
        this.lpId = lpId;
    }

    public String getSpmFaDetailId() {
        return spmFaDetailId;
    }

    public void setSpmFaDetailId(String spmFaDetailId) {
        this.spmFaDetailId = spmFaDetailId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Double issueQty) {
        this.issueQty = issueQty;
    }

    public Double getOverIssueQty() {
        return overIssueQty;
    }

    public void setOverIssueQty(Double overIssueQty) {
        this.overIssueQty = overIssueQty;
    }

}
