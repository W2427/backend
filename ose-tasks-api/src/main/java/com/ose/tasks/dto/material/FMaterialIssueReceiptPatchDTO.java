package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 领料单版本")
    private String spmRunNumber;

    @Schema(description = "SPM 描述")
    private String spmMirDesc;

    @Schema(description = "SPM 出库单ID")
    private String spmMirId;

    @Schema(description = "SPM 出库单")
    private String spmMirNumber;

    @Schema(description = "SPM 出库单版本")
    private String revisionId;

    @Schema(description = "SPM 发布类型")
    private String mirType;

    @Schema(description = "SPM 出库类型")
    private String issueType;

    @Schema(description = "SPM 仓库")
    private String whCode;

    @Schema(description = "SPM 仓库中的位置")
    private String locCode;

    public String getSpmMirDesc() {
        return spmMirDesc;
    }

    public void setSpmMirDesc(String spmMirDesc) {
        this.spmMirDesc = spmMirDesc;
    }

    public String getSpmMirId() {
        return spmMirId;
    }

    public void setSpmMirId(String spmMirId) {
        this.spmMirId = spmMirId;
    }

    public String getSpmMirNumber() {
        return spmMirNumber;
    }

    public void setSpmMirNumber(String spmMirNumber) {
        this.spmMirNumber = spmMirNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getMirType() {
        return mirType;
    }

    public void setMirType(String mirType) {
        this.mirType = mirType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSpmRunNumber() {
        return spmRunNumber;
    }

    public void setSpmRunNumber(String spmRunNumber) {
        this.spmRunNumber = spmRunNumber;
    }

}
