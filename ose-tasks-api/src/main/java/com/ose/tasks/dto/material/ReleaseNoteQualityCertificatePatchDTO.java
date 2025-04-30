package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新质量证明书DTO
 */
public class ReleaseNoteQualityCertificatePatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "质量证明书编号")
    private String certCode;

    @Schema(description = "页码")
    private String pageNumber;

    @Schema(description = "描述")
    private String certDesc;

    @Schema(description = "临时文件")
    private String temporaryFileName;

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getCertDesc() {
        return certDesc;
    }

    public void setCertDesc(String certDesc) {
        this.certDesc = certDesc;
    }

    public String getTemporaryFileName() {
        return temporaryFileName;
    }

    public void setTemporaryFileName(String temporaryFileName) {
        this.temporaryFileName = temporaryFileName;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
