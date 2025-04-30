package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.vo.unit.AreaUnit;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 结构 构件 实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wp04EntryUpdateDTO extends BaseDTO {


    private static final long serialVersionUID = 1229192276801557046L;
    @Schema(description = "Project/Area")
    private String moduleNo;

    @Schema(description = "模块号")
    private String sectionNo;

    @Schema(description = "版本号")
    private String panelNo;

    @Schema(description = "部件号")
    private String no;

    @Schema(description = "构件简写")
    private String shortCode;

    @Schema(description = "图纸编号")
    private String dwgNo;

    @Schema(description = "页数")
    private Integer sheetNo;

    @Schema(description = "总页数")
    private Integer sheetTotal;

    @Schema(description = "数量")
    private Integer qty;

    @Schema(description = "部件型号")
    private String memberSize;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "版本")
    private String revision;

    @Schema(description = "无损探伤类型")
    @Enumerated(EnumType.STRING)
    private NDEType nde;

    @Schema(description = "无损探伤抽检百分比")
    private Integer ndeRatio;

    @Schema(description = "unit weight text")
    private String unitWeightText;

    @Schema(description = "unit weight 单位")
    @Enumerated(EnumType.STRING)
    private WeightUnit unitWeightUnit;

    @Schema(description = "unit weight")
    private Double unitWeight;

    @Schema(description = "unit area text")
    private String unitAreaText;

    @Schema(description = "unit area 单位")
    @Enumerated(EnumType.STRING)
    private AreaUnit unitAreaUnit;

    @Schema(description = "unit area")
    private Double unitArea;

    @Schema(description = "component Type")
    private String componentType;

    @Schema(description = "散件发运")
    private String stickBuilt;

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

    @Schema(description = "区间")
    private String area;


    @Schema(description = "是否片体油漆")
    private Boolean paintFlag;

    @Schema(description = "油漆类型代码")
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

    public String getSectionNo() {
        return sectionNo;
    }

    public void setSectionNo(String sectionNo) {
        this.sectionNo = sectionNo;
    }

    public String getPanelNo() {
        return panelNo;
    }

    public void setPanelNo(String panelNo) {
        this.panelNo = panelNo;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(String memberSize) {
        this.memberSize = memberSize;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }


    public int getNdeRatio() {
        return ndeRatio;
    }

    public void setNdeRatio(int ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public Double getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(Double unitWeight) {
        this.unitWeight = unitWeight;
    }


    public String getStickBuilt() {
        return stickBuilt;
    }

    public void setStickBuilt(String stickBuilt) {
        this.stickBuilt = stickBuilt;
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


    public NDEType getNde() {
        return nde;
    }

    public void setNde(NDEType nde) {
        this.nde = nde;
    }

    public void setNdeRatio(Integer ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public String getUnitWeightText() {
        return unitWeightText;
    }

    public void setUnitWeightText(String unitWeightText) {
        this.unitWeightText = unitWeightText;
    }

    public WeightUnit getUnitWeightUnit() {
        return unitWeightUnit;
    }

    public void setUnitWeightUnit(WeightUnit unitWeightUnit) {
        this.unitWeightUnit = unitWeightUnit;
    }

    public String getUnitAreaText() {
        return unitAreaText;
    }

    public void setUnitAreaText(String unitAreaText) {
        this.unitAreaText = unitAreaText;
    }

    public AreaUnit getUnitAreaUnit() {
        return unitAreaUnit;
    }

    public void setUnitAreaUnit(AreaUnit unitAreaUnit) {
        this.unitAreaUnit = unitAreaUnit;
    }

    public Double getUnitArea() {
        return unitArea;
    }

    public void setUnitArea(Double unitArea) {
        this.unitArea = unitArea;
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

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
