package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 入库清单DTO
 */
public class FMaterialReceiveReceiptPatchForSPMDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "仓库ID")
    private String whId;

    @Schema(description = "仓库")
    private String whCode;

    @Schema(description = "库位ID")
    private String locId;

    @Schema(description = "库位")
    private String locCode;

    @Schema(description = "材料状态ID")
    private String smstId;

    @Schema(description = "材料状态")
    private String smstCode;

    @Schema(description = "材料接收日期")
    private Date matlRecvDate;

    @Schema(description = "接收人（可空）")
    private String recvBy;

    @Schema(description = "运输人（可空）")
    private String shipper;

    @Schema(description = "运输单号（可空）")
    private String shipperRefNo;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    public String getWhId() {
        return whId;
    }

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getSmstId() {
        return smstId;
    }

    public void setSmstId(String smstId) {
        this.smstId = smstId;
    }

    public String getSmstCode() {
        return smstCode;
    }

    public void setSmstCode(String smstCode) {
        this.smstCode = smstCode;
    }

    public Date getMatlRecvDate() {
        return matlRecvDate;
    }

    public void setMatlRecvDate(Date matlRecvDate) {
        this.matlRecvDate = matlRecvDate;
    }

    public String getRecvBy() {
        return recvBy;
    }

    public void setRecvBy(String recvBy) {
        this.recvBy = recvBy;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperRefNo() {
        return shipperRefNo;
    }

    public void setShipperRefNo(String shipperRefNo) {
        this.shipperRefNo = shipperRefNo;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
