package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.vo.unit.AreaUnit;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * SECTION 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wp02EntryUpdateDTO extends BaseDTO {


    private static final long serialVersionUID = -5363617246971981704L;
    @Schema(description = "Parent No")
    private String moduleNo;

    @Schema(description = "Entity No")
    private String no;

    @Schema(description = "分段简写")
    private String shortCode;

    @Schema(description = "图纸编号")
    private String dwgNo;

    @Schema(description = "版本")
    private String revision;

    @Schema(description = "页数")
    private Integer sheetNo;

    @Schema(description = "总页数")
    private Integer sheetTotal;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "weight text")
    private String weightText;

    @Schema(description = "weight 单位")
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Schema(description = "weight")
    private Double weight;

    @Schema(description = "长度 文本")
    private String lengthText;

    @Schema(description = "长度 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "length")
    private Double length;

    @Schema(description = "宽度 文本")
    private String widthText;

    @Schema(description = "宽度 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit widthUnit;

    @Schema(description = "width")
    private Double width;

    @Schema(description = "高度厚度 文本")
    private String heightText;

    @Schema(description = "高度厚度 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit heightUnit;

    @Schema(description = "height")
    private Double height;

    @Schema(description = "totalArea")
    private Double totalArea;

    @Schema(description = "总面积单位")
    @Enumerated(EnumType.STRING)
    private AreaUnit totalAreaUnit;

    @Schema(description = "总面积文本")
    private String totalAreaText;

    @Schema(description = "Painting Area")
    private Double paintingArea;

    @Schema(description = "油漆面积单位")
    @Enumerated(EnumType.STRING)
    private AreaUnit PaintingAreaUnit;

    @Schema(description = "油漆面积文本")
    private String paintingAreaText;


    @Schema(description = "pfpArea")
    private Double pfpArea;

    @Schema(description = "pfp单位")
    @Enumerated(EnumType.STRING)
    private AreaUnit pfpAreaUnit;

    @Schema(description = "pfp文本")
    private String pfpAreaText;

    @Schema(description = "是否分段油漆")
    private Boolean paintFlag;

    @Schema(description = "油漆代码")
    private String paintCode;

    @Schema(description = "备注")
    private String remarks;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }


    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }


    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getWidthText() {
        return widthText;
    }

    public void setWidthText(String widthText) {
        this.widthText = widthText;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public String getHeightText() {
        return heightText;
    }

    public void setHeightText(String heightText) {
        this.heightText = heightText;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public LengthUnit getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(LengthUnit widthUnit) {
        this.widthUnit = widthUnit;
    }

    public LengthUnit getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(LengthUnit heightUnit) {
        this.heightUnit = heightUnit;
    }


    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public AreaUnit getTotalAreaUnit() {
        return totalAreaUnit;
    }

    public void setTotalAreaUnit(AreaUnit totalAreaUnit) {
        this.totalAreaUnit = totalAreaUnit;
    }

    public String getTotalAreaText() {
        return totalAreaText;
    }

    public void setTotalAreaText(String totalAreaText) {
        this.totalAreaText = totalAreaText;
    }

    public Double getPaintingArea() {
        return paintingArea;
    }

    public void setPaintingArea(Double paintingArea) {
        this.paintingArea = paintingArea;
    }

    public AreaUnit getPaintingAreaUnit() {
        return PaintingAreaUnit;
    }

    public void setPaintingAreaUnit(AreaUnit paintingAreaUnit) {
        PaintingAreaUnit = paintingAreaUnit;
    }

    public String getPaintingAreaText() {
        return paintingAreaText;
    }

    public void setPaintingAreaText(String paintingAreaText) {
        this.paintingAreaText = paintingAreaText;
    }

    public Double getPfpArea() {
        return pfpArea;
    }

    public void setPfpArea(Double pfpArea) {
        this.pfpArea = pfpArea;
    }

    public AreaUnit getPfpAreaUnit() {
        return pfpAreaUnit;
    }

    public void setPfpAreaUnit(AreaUnit pfpAreaUnit) {
        this.pfpAreaUnit = pfpAreaUnit;
    }

    public String getPfpAreaText() {
        return pfpAreaText;
    }

    public void setPfpAreaText(String pfpAreaText) {
        this.pfpAreaText = pfpAreaText;
    }

    public Boolean getPaintFlag() {
        return paintFlag;
    }

    public void setPaintFlag(Boolean paintFlag) {
        this.paintFlag = paintFlag;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }
}
