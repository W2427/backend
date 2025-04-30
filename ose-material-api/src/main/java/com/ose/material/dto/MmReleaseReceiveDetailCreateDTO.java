package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 材料入库单详情创建DTO
 */
public class MmReleaseReceiveDetailCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -7821510672164830311L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "入库单ID")
    public Long materialReceiveNoteId;

    @Schema(description = "大类")
    private String categoryFirst;

    @Schema(description = "小类")
    private String categorySecond;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "材料描述")
    private String desc;

    @Schema(description = "计量单位")
    private String unitCode;

    @Schema(description = "计划入库数量")
    private Integer planQty;

    @Schema(description = "盘点数量")
    private Integer inventoryQty;

    @Schema(description = "炉批号ID")
    public Long heatBatchId;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "类型（一类一码，一物一码）")
    private QrCodeType type;

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

    public Long getMaterialReceiveNoteId() {
        return materialReceiveNoteId;
    }

    public void setMaterialReceiveNoteId(Long materialReceiveNoteId) {
        this.materialReceiveNoteId = materialReceiveNoteId;
    }

    public String getCategoryFirst() {
        return categoryFirst;
    }

    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst;
    }

    public String getCategorySecond() {
        return categorySecond;
    }

    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getPlanQty() {
        return planQty;
    }

    public void setPlanQty(Integer planQty) {
        this.planQty = planQty;
    }

    public Integer getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(Integer inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public Long getHeatBatchId() {
        return heatBatchId;
    }

    public void setHeatBatchId(Long heatBatchId) {
        this.heatBatchId = heatBatchId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public QrCodeType getType() {
        return type;
    }

    public void setType(QrCodeType type) {
        this.type = type;
    }
}
