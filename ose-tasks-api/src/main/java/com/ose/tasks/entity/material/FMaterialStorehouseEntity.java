package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 库位实体类。
 */
@Entity
@Table(name = "mat_f_material_warehouse_location_relation")
public class FMaterialStorehouseEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 8282076110158516825L;

    @Column(length = 16)
    private Long orgId;

    @Column(length = 16)
    private Long projectId;

    @Column
    private String whCode;

    @Column
    private Integer whId;

    @Column
    private String locCode;

    @Column
    private Integer locId;

    @Column
    private String goodsShelf;

    @Column
    private String memo;

    @Column
    private String qrCode;

    @Column
    private int printCount;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
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
}
