package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptPatchForSPMDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "仓库ID")
    private Integer whId;
    //
//    @Schema(description = "仓库")
//    private String whCode;
//
    @Schema(description = "库位ID")
    private Integer locId;
//
//    @Schema(description = "库位")
//    private String locCode;


    @Schema(description = "发布日期")
    private Date issueDate;

    @Schema(description = "发料人")
    private String issueBy;

    @Schema(description = "施工单位ID")
    private String companyId;

    @Schema(description = "施工单位")
    private String companyCode;

    @Schema(description = "短描述")
    private String shortDesc;

    @Schema(description = "长描述")
    private String description;

    @Schema(description = "SPM数据库反写标识")
    private Boolean spmFlag;

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public Boolean getSpmFlag() {
        return spmFlag;
    }

    public void setSpmFlag(Boolean spmFlag) {
        this.spmFlag = spmFlag;
    }
}
