package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 放行单查询DTO
 */
public class ReleaseNoteItemDetailPatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材质证书ID")
    private Long certId;

    @Schema(description = "材质证书编号")
    private String certCode;

    @Schema(description = "材质证书页码")
    private String certPageNumber;

    public Long getCertId() {
        return certId;
    }

    public void setCertId(Long certId) {
        this.certId = certId;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }

    public String getCertPageNumber() {
        return certPageNumber;
    }

    public void setCertPageNumber(String certPageNumber) {
        this.certPageNumber = certPageNumber;
    }
}
