package com.ose.tasks.dto.structureWbs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Structure Weld 实体数据传输对象。 TODO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureWeldEntryInsertDTO extends BaseDTO {


    private static final long serialVersionUID = 2247769167333176745L;

    @Schema(description = "Work Package 01 No")
    private String wp01No;

    @Schema(description = "Work Package 01 Id")
    private Long wp01Id;

    @Schema(description = "Work Package 02 No")
    private String wp02No;

    @Schema(description = "Work Package 02 Id")
    private Long wp02Id;

    @Schema(description = "Work Package 03 No")
    private String wp03No;

    @Schema(description = "Work Package 03 Id")
    private Long wp03Id;

    @Schema(description = "Work Package 04 No")
    private String wp04No;

    @Schema(description = "Work Package 04 Id")
    private Long wp04Id;

    @Schema(description = "焊口No")
    private String no;

    private String weldClass;

    @Schema(description = "实施阶段，F(abrication),A(ssemble),E(rection),C(ompletion)")
    private String stage;

    @Schema(description = "焊口实体类型")
    private String weldType;

    @Schema(description = "焊接图纸")
    private String weldMapNo;

    @Schema(description = "焊口版本")
    private String revision;

    @Schema(description = "焊接程序编号")
    private String wpsNo;

    @Schema(description = "是否执行硬度测试")
    private Boolean hardnessTest;

    @Schema(description = "焊口长度")
    private Double length;

    @Schema(description = "焊口长度文本")
    private String lengthText;

    @Schema(description = "焊口长度单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "组件#1标签")
    private String tagNo1;

    @Schema(description = "组件#1材质代码")
    private String materialCode1;

    @Schema(description = "组件#1材质描述")
    private String material1;

    @Schema(description = "壁厚等级1")
    private String thickness1;

    @Schema(description = "组件#2标签")
    private String tagNo2;

    @Schema(description = "组件#2材质代码")
    private String materialCode2;

    @Schema(description = "组件#2材质描述")
    private String material2;

    @Schema(description = "壁厚等级2")
    private String thickness2;

    @Schema(description = "油漆代码")
    private String paintCode;

    @Schema(description = "备注")
    private String remark;
    @Schema(description = "层级标识")
    private String hierachyParent;

    @Schema(description = "MT比例")
    private Integer mtRatio;

    private Integer ptRatio;

    private Integer utRatio;

    private Integer rtRatio;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWp01No() {
        return wp01No;
    }

    public void setWp01No(String wp01No) {
        this.wp01No = wp01No;
    }

    public Long getWp01Id() {
        return wp01Id;
    }

    public void setWp01Id(Long wp01Id) {
        this.wp01Id = wp01Id;
    }

    public String getWp02No() {
        return wp02No;
    }

    public void setWp02No(String wp02No) {
        this.wp02No = wp02No;
    }

    public Long getWp02Id() {
        return wp02Id;
    }

    public void setWp02Id(Long wp02Id) {
        this.wp02Id = wp02Id;
    }

    public String getWp03No() {
        return wp03No;
    }

    public void setWp03No(String wp03No) {
        this.wp03No = wp03No;
    }

    public Long getWp03Id() {
        return wp03Id;
    }

    public void setWp03Id(Long wp03Id) {
        this.wp03Id = wp03Id;
    }

    public String getWp04No() {
        return wp04No;
    }

    public void setWp04No(String wp04No) {
        this.wp04No = wp04No;
    }

    public Long getWp04Id() {
        return wp04Id;
    }

    public void setWp04Id(Long wp04Id) {
        this.wp04Id = wp04Id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getWeldClass() {
        return weldClass;
    }

    public void setWeldClass(String weldClass) {
        this.weldClass = weldClass;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getWeldMapNo() {
        return weldMapNo;
    }

    public void setWeldMapNo(String weldMapNo) {
        this.weldMapNo = weldMapNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Boolean getHardnessTest() {
        return hardnessTest;
    }

    public void setHardnessTest(Boolean hardnessTest) {
        this.hardnessTest = hardnessTest;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public String getTagNo1() {
        return tagNo1;
    }

    public void setTagNo1(String tagNo1) {
        this.tagNo1 = tagNo1;
    }

    public String getMaterialCode1() {
        return materialCode1;
    }

    public void setMaterialCode1(String materialCode1) {
        this.materialCode1 = materialCode1;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getThickness1() {
        return thickness1;
    }

    public void setThickness1(String thickness1) {
        this.thickness1 = thickness1;
    }

    public String getTagNo2() {
        return tagNo2;
    }

    public void setTagNo2(String tagNo2) {
        this.tagNo2 = tagNo2;
    }

    public String getMaterialCode2() {
        return materialCode2;
    }

    public void setMaterialCode2(String materialCode2) {
        this.materialCode2 = materialCode2;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getThickness2() {
        return thickness2;
    }

    public void setThickness2(String thickness2) {
        this.thickness2 = thickness2;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHierachyParent() {
        return hierachyParent;
    }

    public void setHierachyParent(String hierachyParent) {
        this.hierachyParent = hierachyParent;
    }

    public Integer getMtRatio() {
        return mtRatio;
    }

    public void setMtRatio(Integer mtRatio) {
        this.mtRatio = mtRatio;
    }

    public Integer getPtRatio() {
        return ptRatio;
    }

    public void setPtRatio(Integer ptRatio) {
        this.ptRatio = ptRatio;
    }

    public Integer getUtRatio() {
        return utRatio;
    }

    public void setUtRatio(Integer utRatio) {
        this.utRatio = utRatio;
    }

    public Integer getRtRatio() {
        return rtRatio;
    }

    public void setRtRatio(Integer rtRatio) {
        this.rtRatio = rtRatio;
    }
}
