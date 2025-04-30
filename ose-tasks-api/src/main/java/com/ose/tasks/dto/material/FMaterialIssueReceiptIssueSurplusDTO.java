package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptIssueSurplusDTO extends BaseDTO {

    private static final long serialVersionUID = -2511265211986099741L;

    @Schema(description = "余料库入库、出库状态")
    private boolean issueSurplus = false;

    private List<Long> fMaterialIssueReceiptTotalEntityIds;

    public boolean isIssueSurplus() {
        return issueSurplus;
    }

    public void setIssueSurplus(boolean issueSurplus) {
        this.issueSurplus = issueSurplus;
    }

    public List<Long> getfMaterialIssueReceiptTotalEntityIds() {
        return fMaterialIssueReceiptTotalEntityIds;
    }

    public void setfMaterialIssueReceiptTotalEntityIds(List<Long> fMaterialIssueReceiptTotalEntityIds) {
        this.fMaterialIssueReceiptTotalEntityIds = fMaterialIssueReceiptTotalEntityIds;
    }
}
