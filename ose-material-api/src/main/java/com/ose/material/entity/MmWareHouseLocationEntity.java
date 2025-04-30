package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.WareHouseLocationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 仓库货位
 */
@Entity
@Table(name = "mm_ware_house_location")
public class MmWareHouseLocationEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 5124872224099539746L;

    @Schema(description = "组织ID")
    @Column
    public Long orgId;

    @Schema(description = "项目ID")
    @Column
    public Long projectId;

    @Schema(description = "公司ID")
    @Column
    public Long companyId;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "仓库名")
    @Column
    public String name;

    @Schema(description = "仓库地址")
    @Column
    public String address;

    @Schema(description = "仓库类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public WareHouseLocationType type;

    @Schema(description = "父级仓库ID")
    @Column
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

//    public String getNo() {
//        return no;
//    }
//
//    public void setNo(String no) {
//        this.no = no;
//    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WareHouseLocationType getType() {
        return type;
    }

    public void setType(WareHouseLocationType type) {
        this.type = type;
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
