package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.material.TagNumberType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Demo查询DTO
 */
public class ReleaseNoteInfoVo extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private Long actInstId;

    @Schema(description = "任务 ID, 123456")
    private String taskId;

    // SPM 放行单号
    @Schema(description = "放行单号")
    private String spmRelnNumber;

    @Schema(description = "计划放行单号")
    private String spmPlanRelnNumber;

    // 合同编号
    @Schema(description = "合同编号")
    private String poNumber;

    // 合同批次号
    @Schema(description = "合同批次号")
    private Integer poSupp;

    @Schema(description = "材料批次号")
    private String materialBatchNumber;

    // 专业
    @Schema(description = "专业")
    private String dpCode;

    // 材料编码
    @Schema(description = "材料编码")
    private String tagNumber;

    // 材料编码
    @Schema(description = "ident码")
    private String ident;

    // 材料编码类型
    @Schema(description = "材料编码类型")
    private TagNumberType tagNumberType;

    // 单位
    @Schema(description = "单位")
    private String qtyUnitCode;

    // 请购量
    @Schema(description = "请购量")
    private BigDecimal poliQty;

    // 放行量
    @Schema(description = "放行量")
    private BigDecimal relnQty;

    // 合计量
    @Schema(description = "合计量")
    private BigDecimal totalQty;

    // 描述
    @Schema(description = "描述")
    private String shortDesc;

    // 炉批号
    @Schema(description = "炉批号")
    private String heatNoCode;

    // 规格
    @Schema(description = "规格")
    private String spec;

    @Schema(description = "NPS")
    private String npsText;

    @Schema(description = "NPS 单位")
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    // 单位数量
    @Schema(description = "单位数量")
    private BigDecimal qty;

    // 个数
    @Schema(description = "个数")
    private Integer qtyCnt;

    // 验收报告的类型(管材料, 管附件, 阀件)
    @Schema(description = "验收报告的类型(管材料, 管附件, 阀件)")
    private ReportSubType reportSubType;

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getPoSupp() {
        return poSupp;
    }

    public void setPoSupp(Integer poSupp) {
        this.poSupp = poSupp;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(String qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public BigDecimal getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(BigDecimal poliQty) {
        this.poliQty = poliQty;
    }

    public BigDecimal getRelnQty() {
        return relnQty;
    }

    public void setRelnQty(BigDecimal relnQty) {
        this.relnQty = relnQty;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getHeatNoCode() {
        return heatNoCode;
    }

    public void setHeatNoCode(String heatNoCode) {
        this.heatNoCode = heatNoCode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(Integer qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getSpmPlanRelnNumber() {
        return spmPlanRelnNumber;
    }

    public void setSpmPlanRelnNumber(String spmPlanRelnNumber) {
        this.spmPlanRelnNumber = spmPlanRelnNumber;
    }
}
