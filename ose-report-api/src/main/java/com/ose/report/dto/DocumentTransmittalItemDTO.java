package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DocumentTransmittalItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4337403924650029604L;

    @Schema(description = "文件编号")
    private String documentNo;

    @Schema(description = "文件名称")
    private String documentDesc;

    @Schema(description = "版本")
    private String revision;

    @Schema(description = "备注")
    private String remark;

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String documentDesc) {
        this.documentDesc = documentDesc;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
