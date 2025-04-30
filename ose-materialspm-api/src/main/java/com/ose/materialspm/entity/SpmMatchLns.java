package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * SpmMatchLnNode 查询DTO
 */

@SqlResultSetMapping
    (
        name = "SpmMatchLnsSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = SpmMatchLns.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "materialCode", column = "material_code"),
                    @FieldResult(name = "materialDesc", column = "material_desc"),
                    @FieldResult(name = "bomQuantity", column = "bom_quantity"),
                    @FieldResult(name = "matchedQuantity", column = "matched_quantity"),
                    @FieldResult(name = "matchPercent", column = "match_percent")
                }
            )
        }
    )
@Entity
public class SpmMatchLns extends BaseDTO {

    @Schema(description = "材料代码")
    @Id
    private String materialCode;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "bom数量")
    private Double bomQuantity;

    @Schema(description = "已匹配数量")
    private Double matchedQuantity;

    @Schema(description = "匹配率")
    private Double matchPercent;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public Double getBomQuantity() {
        return bomQuantity;
    }

    public void setBomQuantity(Double bomQuantity) {
        this.bomQuantity = bomQuantity;
    }

    public Double getMatchedQuantity() {
        return matchedQuantity;
    }

    public void setMatchedQuantity(Double matchedQuantity) {
        this.matchedQuantity = matchedQuantity;
    }

    public Double getMatchPercent() {
        return matchPercent;
    }

    public void setMatchPercent(Double matchPercent) {
        this.matchPercent = matchPercent;
    }
}
