package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MmReleaseReceiveType;
import com.ose.vo.MaterialReceiveType;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 材料入库单创建DTO
 */
public class MmReleaseReceiveCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -5763254040880429003L;
    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "入库单名称")
    private String name;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "采购包ID")
    private Long mmPurchasePackageId;

    @Schema(description = "采购包编号")
    private String mmPurchasePackageNo;

    @Schema(description = "发货单ID")
    private Long mmShippingId;

    @Schema(description = "发货单编号")
    private String mmShippingNo;

    @Schema(description = "初始采购量为0")
    private MmReleaseReceiveType mmReleaseReceiveType;

    @Schema(description = "入库单类型")
    private MaterialReceiveType type;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getMmPurchasePackageId() {
        return mmPurchasePackageId;
    }

    public void setMmPurchasePackageId(Long mmPurchasePackageId) {
        this.mmPurchasePackageId = mmPurchasePackageId;
    }

    public String getMmPurchasePackageNo() {
        return mmPurchasePackageNo;
    }

    public void setMmPurchasePackageNo(String mmPurchasePackageNo) {
        this.mmPurchasePackageNo = mmPurchasePackageNo;
    }

    public Long getMmShippingId() {
        return mmShippingId;
    }

    public void setMmShippingId(Long mmShippingId) {
        this.mmShippingId = mmShippingId;
    }

    public String getMmShippingNo() {
        return mmShippingNo;
    }

    public void setMmShippingNo(String mmShippingNo) {
        this.mmShippingNo = mmShippingNo;
    }

    public MmReleaseReceiveType getMmReleaseReceiveType() {
        return mmReleaseReceiveType;
    }

    public void setMmReleaseReceiveType(MmReleaseReceiveType mmReleaseReceiveType) {
        this.mmReleaseReceiveType = mmReleaseReceiveType;
    }

    public MaterialReceiveType getType() {
        return type;
    }

    public void setType(MaterialReceiveType type) {
        this.type = type;
    }
}
