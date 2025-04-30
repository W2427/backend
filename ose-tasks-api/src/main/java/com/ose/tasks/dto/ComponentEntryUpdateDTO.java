package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.util.StringUtils;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Component实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "父级实体号")
    private String parentNodeNo;

    @Schema(description = "组件短代码")
    private String shortCode;

    @Schema(description = "材料描述")
    private String material;

    @Schema(description = "组件材料编码")
    private String materialCode;

    @Schema(description = "组件数量")
    private Integer qty;

    @Schema(description = "组件数量单位")
    private String qtyUnit;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "壁厚等级")
    private String thickness;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "管道等级")
    private String pipeClass;

    @Schema(description = "绝缘代号")
    private String insulationCode;

    @Schema(description = "页码")
    private Integer sheetNo;

    @Schema(description = "总页数")
    private Integer sheetTotal;

    @Schema(description = "ISO 图纸号 ")
    private String isoDrawing;

    @Schema(description = "坐标 X ")
    private String coordinateX;

    @Schema(description = "坐标 Y ")
    private String coordinateY;

    @Schema(description = "坐标 Z ")
    private String coordinateZ;

    @Schema(description = "预制类别")
    private String fabricated;

    @Schema(description = "是否表面处理")
    private Boolean surfaceTreatment;

    @Schema(description = "油漆代码")
    private String paintingCode;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "批量删除标记")
    private String remarks2;

    @Schema(description = "位置标记")
    private String materialWithPositionalMark;

    @JsonCreator
    public ComponentEntryUpdateDTO() {
    }

    public String getParentNodeNo() {
        return parentNodeNo;
    }

    public void setParentNodeNo(String parentNodeNo) {
        this.parentNodeNo = parentNodeNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @JsonGetter
    public String getNpsText() {
        return npsText;
    }

    @JsonSetter
    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    @JsonGetter
    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    /**
     * 设定nps单位值。
     *
     * @param npsUnit nps单位
     */
    @JsonSetter
    public void setNpsUnit(String npsUnit) {
        if (StringUtils.isEmpty(npsUnit)) {
            return;
        }

        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    @JsonSetter
    public void setNps(String nps) {
        // 管件的NPS可以为空
        if (StringUtils.isEmpty(nps)) {
            this.npsText = nps;
            this.npsUnit = null;
            this.nps = null;
            return;
        }

        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }

    public String getInsulationCode() {
        return insulationCode;
    }

    public void setInsulationCode(String insulationCode) {
        this.insulationCode = insulationCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Integer getSheetTotal() {
        return sheetTotal;
    }

    public void setSheetTotal(Integer sheetTotal) {
        this.sheetTotal = sheetTotal;
    }


    public String getIsoDrawing() {
        return isoDrawing;
    }

    public void setIsoDrawing(String isoDrawing) {
        this.isoDrawing = isoDrawing;
    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }

    public String getCoordinateZ() {
        return coordinateZ;
    }

    public void setCoordinateZ(String coordinateZ) {
        this.coordinateZ = coordinateZ;
    }

    public String getFabricated() {
        return fabricated;
    }

    public void setFabricated(String fabricated) {
        this.fabricated = fabricated;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getMaterialWithPositionalMark() {
        return materialWithPositionalMark;
    }

    public void setMaterialWithPositionalMark(String materialWithPositionalMark) {
        this.materialWithPositionalMark = materialWithPositionalMark;
    }

    public Boolean getSurfaceTreatment() {
        return surfaceTreatment;
    }

    public void setSurfaceTreatment(Boolean surfaceTreatment) {
        this.surfaceTreatment = surfaceTreatment;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
