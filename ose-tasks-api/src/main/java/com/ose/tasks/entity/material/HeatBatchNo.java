package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 材料二维码管理表。
 */
@Entity
@Table(name = "heat_batch_no",
indexes = {
    @Index(columnList = "entityNo")
})
public class HeatBatchNo extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 9180609574865607960L;

    @Schema(description = "实体")
    private String entityNo;

    @Schema(description = "炉号")
    private String heatNo;

    @Schema(description = "批号")
    private String batchNo;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
