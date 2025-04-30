package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.MaterialReceiveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 发货单创建DTO
 */
public class MmShippingCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 9181731155282528264L;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "发货单名称")
    private String name;

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "附件")
    private String attachments;

    @Schema(description = "临时文件名")
    private String fileName;

    @Schema(description = "请购单ID")
    @NotNull
    private Long mmPurchasePackageId;

    @Schema(description = "发货单类型")
    private MaterialReceiveType type;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MaterialReceiveType getType() {
        return type;
    }

    public void setType(MaterialReceiveType type) {
        this.type = type;
    }

    public Long getMmPurchasePackageId() {
        return mmPurchasePackageId;
    }

    public void setMmPurchasePackageId(Long mmPurchasePackageId) {
        this.mmPurchasePackageId = mmPurchasePackageId;
    }
}
