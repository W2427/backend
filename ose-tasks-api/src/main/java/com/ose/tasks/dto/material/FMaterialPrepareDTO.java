package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料准备单DTO
 */
public class FMaterialPrepareDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "材料准备单号")
    private String mpCode;

    @Schema(description = "材料准备单描述")
    private String mpDesc;

    @Schema(description = "网关，MATERIAL_ISSUE_PIPE（管材首次出库），MATERIAL_REISSUE_PIPE(管材补领出库),MATERIAL_ISSUE_FITTING_SHOP（内场管附件出库），MATERIAL_ISSUE_FITTING_FILED（外场管附件出库）")
    private String gateway;

    @Schema(description = "网关名称")
    private String gatewayName;

    @Schema(description = "施工单位ID")
    private String companyId;

    @Schema(description = "施工单位")
    private String companyCode;

    @Schema(description = "任务包ID")
    private Long taskPackageId;

    public String getMpCode() {
        return mpCode;
    }

    public void setMpCode(String mpCode) {
        this.mpCode = mpCode;
    }

    public String getMpDesc() {
        return mpDesc;
    }

    public void setMpDesc(String mpDesc) {
        this.mpDesc = mpDesc;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
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

    public Long getTaskPackageId() {
        return taskPackageId;
    }

    public void setTaskPackageId(Long taskPackageId) {
        this.taskPackageId = taskPackageId;
    }
}
