package com.ose.tasks.dto;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class SubconDTO extends BaseDTO {

    private static final long serialVersionUID = -6746346381569049871L;

    @Schema(description = "分包商名称")
    private String name;

    @Schema(description = "分包商全称")
    private String fullName;

    @Schema(description = "分包商地址")
    private String address;

    @Schema(description = "分包商网址")
    private String website;

    @Schema(description = "分包商资质")
    private String intelligence;

    @Schema(description = "分包商图标")
    private String subContractorLogo;

    public String getSubContractorLogo() {
        return subContractorLogo;
    }

    public void setSubContractorLogo(String subContractorLogo) {
        this.subContractorLogo = subContractorLogo;
    }

    public String getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(String intelligence) {
        this.intelligence = intelligence;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
