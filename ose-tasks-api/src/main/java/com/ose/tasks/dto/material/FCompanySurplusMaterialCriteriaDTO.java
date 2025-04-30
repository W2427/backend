package com.ose.tasks.dto.material;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class FCompanySurplusMaterialCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 6789005018457223699L;

    @Schema(description = "IDENT码")
    private String ident;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "材料接收状态")
    private boolean receivedFlg = true;

    @Schema(description = "NPS")
    private Double nps;

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public boolean isReceivedFlg() {
        return receivedFlg;
    }

    public void setReceivedFlg(boolean receivedFlg) {
        this.receivedFlg = receivedFlg;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }
}
