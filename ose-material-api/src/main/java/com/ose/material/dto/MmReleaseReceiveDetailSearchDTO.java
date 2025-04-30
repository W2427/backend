package com.ose.material.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料入库单详情查询DTO
 */
public class MmReleaseReceiveDetailSearchDTO extends PageDTO {

    private static final long serialVersionUID = -9117084561204224748L;

    @Schema(description = "搜索关键字")
    public String keyword;

    @Schema(description = "是否是入库单详情")
    public Boolean releaseReceiveDetail;

    @Schema(description = "是否是小程序")
    public Boolean weChat;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "Ident码")
    private String identCode;

    @Schema(description = "安装码")
    private String installationCode;

    @Schema(description = "是否查询盘点量小于等于0的flag")
    @JsonProperty("inventory")
    private Boolean inventory;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Boolean getReleaseReceiveDetail() {
        return releaseReceiveDetail;
    }

    public void setReleaseReceiveDetail(Boolean releaseReceiveDetail) {
        this.releaseReceiveDetail = releaseReceiveDetail;
    }

    public Boolean getWeChat() {
        return weChat;
    }

    public void setWeChat(Boolean weChat) {
        this.weChat = weChat;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getInstallationCode() {
        return installationCode;
    }

    public void setInstallationCode(String installationCode) {
        this.installationCode = installationCode;
    }

    public Boolean getInventory() {
        return inventory;
    }

    public void setInventory(Boolean inventory) {
        this.inventory = inventory;
    }
}
