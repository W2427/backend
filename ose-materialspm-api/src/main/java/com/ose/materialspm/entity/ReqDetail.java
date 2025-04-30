package com.ose.materialspm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;

import com.ose.dto.BaseDTO;

/**
 * 请购单详情实体类。
 */
@SqlResultSetMapping
    (
        name = "ReqDetailSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = ReqDetail.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "identCode", column = "identCode"),
                    @FieldResult(name = "commodityCode", column = "commodityCode"),
                    @FieldResult(name = "totalReleaseQty", column = "totalReleaseQty"),
                    @FieldResult(name = "shortDesc", column = "shortDesc"),
                    @FieldResult(name = "increasePty", column = "increasePty"),
                    @FieldResult(name = "unitCode", column = "unitCode"),
                    @FieldResult(name = "rliPos", column = "rliPos")
                }
            )
        }
    )
@Entity
public class ReqDetail extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;

    @Id
    private String identCode;

    private String commodityCode;

    private String totalReleaseQty;

    private String shortDesc;

    private String increasePty;

    private String unitCode;

    private String rliPos;

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getTotalReleaseQty() {
        return totalReleaseQty;
    }

    public void setTotalReleaseQty(String totalReleaseQty) {
        this.totalReleaseQty = totalReleaseQty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getIncreasePty() {
        return increasePty;
    }

    public void setIncreasePty(String increasePty) {
        this.increasePty = increasePty;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getRliPos() {
        return rliPos;
    }

    public void setRliPos(String rliPos) {
        this.rliPos = rliPos;
    }

}
