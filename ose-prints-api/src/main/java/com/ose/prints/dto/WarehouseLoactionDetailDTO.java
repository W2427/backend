package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WarehouseLoactionDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 7793443380694982831L;

    @Schema(description = "项目名")
    private String project;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "打印次数")
    private Integer printCount;

    @Schema(description = "库位")
    private String whCode;

    @Schema(description = "货位")
    private String locCode;

    @Schema(description = "货架层号")
    private String goodsShelf;

    @Schema(description = "备注")
    private String memo;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
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

    public String getGoodsShelf() {
        return goodsShelf;
    }

    public void setGoodsShelf(String goodsShelf) {
        this.goodsShelf = goodsShelf;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
