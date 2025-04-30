package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptPostForObirDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM入库单ID")
    private String mrrId;

    @Schema(description = "SPM入库单")
    private String mrrNumber;

    @Schema(description = "SPM入库单版本")
    private String revisionId;

    @Schema(description = "SPM入库单类型")
    private String recvType;

    @Schema(description = "SPM入库单仓库")
    private String whCode;

    @Schema(description = "SPM入库单仓库中的位置")
    private String locCode;

    @Schema(description = "SPM 专业")
    private String dpCode;

    @Schema(description = "SPM 描述")
    private String shortDesc;

    @Schema(description = "开箱检验单ID")
    private Long obirId;

    public String getMrrId() {
        return mrrId;
    }

    public void setMrrId(String mrrId) {
        this.mrrId = mrrId;
    }

    public String getMrrNumber() {
        return mrrNumber;
    }

    public void setMrrNumber(String mrrNumber) {
        this.mrrNumber = mrrNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public String getRecvType() {
        return recvType;
    }

    public void setRecvType(String recvType) {
        this.recvType = recvType;
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

    public Long getObirId() {
        return obirId;
    }

    public void setObirId(Long obirId) {
        this.obirId = obirId;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

}
