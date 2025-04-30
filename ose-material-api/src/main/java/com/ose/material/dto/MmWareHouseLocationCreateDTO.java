package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.WareHouseLocationType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 仓库货架创建DTO
 */
public class MmWareHouseLocationCreateDTO extends BaseDTO {


    private static final long serialVersionUID = 8139155477893665117L;
    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "仓库类型")
    public WareHouseLocationType type;

    @Schema(description = "仓库名")
    private String name;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "仓库地址")
    private String address;

    @Schema(description = "父级仓库ID")
    public Long parentWareHouseId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public WareHouseLocationType getType() {
        return type;
    }

    public void setType(WareHouseLocationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getParentWareHouseId() {
        return parentWareHouseId;
    }

    public void setParentWareHouseId(Long parentWareHouseId) {
        this.parentWareHouseId = parentWareHouseId;
    }
}
