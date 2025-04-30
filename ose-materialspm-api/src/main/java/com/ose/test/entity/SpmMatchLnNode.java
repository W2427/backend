package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * SPM中 BOM 节点 匹配率的 实体 ftjftj
 */
@SqlResultSetMapping
    (
        name = "SpmMatchLnNodeSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = SpmMatchLnNode.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "lnId", column = "lnId"),
                    @FieldResult(name = "lnCode", column = "lnCode"),
                    @FieldResult(name = "lastModified", column = "lastModified"),
                    @FieldResult(name = "matchPercent", column = "matchPercent")
                }
            )
        }
    )
@Entity
public class SpmMatchLnNode extends BaseDTO {


    @Id
    private BigDecimal lnId;

    private String lnCode;

    private String lastModified;

    private Float matchPercent;

    public BigDecimal getLnId() {
        return lnId;
    }

    public void setLnId(BigDecimal lnId) {
        this.lnId = lnId;
    }

    public String getLnCode() {
        return lnCode;
    }

    public void setLnCode(String lnCode) {
        this.lnCode = lnCode;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Float getMatchPercent() {
        return matchPercent;
    }

    public void setMatchPercent(Float matchPercent) {
        this.matchPercent = matchPercent;
    }
}
