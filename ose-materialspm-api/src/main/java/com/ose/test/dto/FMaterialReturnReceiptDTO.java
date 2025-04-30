package com.ose.test.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

/**
 * 退库清单DTO
 */
public class FMaterialReturnReceiptDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "默认值：N")
    private String esiStatus;

    @Schema(description = "退库单号")
    private String rtiNumber;

    @Schema(description = "版本")
    private String revisionId;

    @Schema(description = "单据创建时间")
    private Date rtiCreateDate;

    @Schema(description = "发布时间")
    private Date rtiPostDate;

    @Schema(description = "默认值：N")
    private String poplIshByProc;

    @Schema(description = "SPM PROJECT ID")
    private String projId;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    @Schema(description = "施工单位ID")
    private String companyId;

    private List<FMaterialReturnReceiptDetailDTO> details;

    public String getEsiStatus() {
        return esiStatus;
    }

    public void setEsiStatus(String esiStatus) {
        this.esiStatus = esiStatus;
    }

    public String getRtiNumber() {
        return rtiNumber;
    }

    public void setRtiNumber(String rtiNumber) {
        this.rtiNumber = rtiNumber;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(String revisionId) {
        this.revisionId = revisionId;
    }

    public Date getRtiCreateDate() {
        return rtiCreateDate;
    }

    public void setRtiCreateDate(Date rtiCreateDate) {
        this.rtiCreateDate = rtiCreateDate;
    }

    public Date getRtiPostDate() {
        return rtiPostDate;
    }

    public void setRtiPostDate(Date rtiPostDate) {
        this.rtiPostDate = rtiPostDate;
    }

    public String getPoplIshByProc() {
        return poplIshByProc;
    }

    public void setPoplIshByProc(String poplIshByProc) {
        this.poplIshByProc = poplIshByProc;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<FMaterialReturnReceiptDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<FMaterialReturnReceiptDetailDTO> details) {
        this.details = details;
    }
}
