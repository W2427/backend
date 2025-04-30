package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 项目 WBS ISO No 条目查询数据传输对象。
 */

@SqlResultSetMapping
    (
        name = "WBSEntryIsoDTOSqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = WBSEntryIsoDTO.class, //就是当前这个类的名字
                fields = {
                    @FieldResult(name = "isoNo", column = "iso_no"),
                    @FieldResult(name = "bomMatchPercent", column = "bom_match_percent"),
                    @FieldResult(name = "spmLnId", column = "spm_ln_id")
                }
            )
        }
    )
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryIsoDTO {

    @Schema(description = "iso no ")
    @Id
    private String isoNo;

    @Schema(description = "bom match percent")
    private Double bomMatchPercent;

    @Schema(description = "SPM LN_ID")
    private BigDecimal spmLnId;

//    public WBSEntryIsoDTO(String isoNo) {
//        this.isoNo = isoNo;
//    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public Double getBomMatchPercent() {
        return bomMatchPercent;
    }

    public void setBomMatchPercent(Double bomMatchPercent) {
        this.bomMatchPercent = bomMatchPercent;
    }

    public BigDecimal getSpmLnId() {
        return spmLnId;
    }

    public void setSpmLnId(BigDecimal spmLnId) {
        this.spmLnId = spmLnId;
    }
}
