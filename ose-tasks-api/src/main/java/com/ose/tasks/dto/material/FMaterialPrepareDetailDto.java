package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料准备单DTO
 */
public class FMaterialPrepareDetailDto extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料准备详情单ID")
    private Long fmpdId;

    @Schema(description = "材料准备详情单描述")
    private String mpdCode;

    @Schema(description = "数量表示值")
    private String qtyDisplay;

    @Schema(description = "材料接收人")
    private String recvPerson;

    @Schema(description = "材料接收地点")
    private String recvSite;

    public Long getFmpdId() {
        return fmpdId;
    }

    public void setFmpdId(Long fmpdId) {
        this.fmpdId = fmpdId;
    }

    public String getMpdCode() {
        return mpdCode;
    }

    public void setMpdCode(String mpdCode) {
        this.mpdCode = mpdCode;
    }

    public String getRecvPerson() {
        return recvPerson;
    }

    public void setRecvPerson(String recvPerson) {
        this.recvPerson = recvPerson;
    }

    public String getRecvSite() {
        return recvSite;
    }

    public void setRecvSite(String recvSite) {
        this.recvSite = recvSite;
    }

    public String getQtyDisplay() {
        return qtyDisplay;
    }

    public void setQtyDisplay(String qtyDisplay) {
        this.qtyDisplay = qtyDisplay;
    }

}
