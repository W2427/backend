package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.numeric.NDERatioDTO;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

/**
 * weld实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeldEntryInsertDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;


    @Schema(description = "所属ISO号")
    private String isoNo;

    @Schema(description = "节点表示名")
    private String displayName = null;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "实施位置类型")
    private String shopField;

    @Schema(description = "焊口类型代码")
    private String weldType;

    @Schema(description = "页码")
    private Integer sheetNo;

    @Schema(description = "总页数")
    private Integer sheetTotal;

    @Schema(description = "焊口显示标签")
    private List<String> displayWeldMarks;

    @Schema(description = "焊接程序编号")
    private String wpsNo;

    @Schema(description = "无损探伤类型")
    private NDEType nde;

    @Schema(description = "无损探伤比例")
    private Integer ndeRatio;

    @Schema(description = "是否执行焊接后热处理")
    private Boolean pwht;

    @Schema(description = "是否执行硬度测试")
    private Boolean hardnessTest;

    @Schema(description = "材质分析抽取比例")
    private Integer pmiRatio;

    @Schema(description = "管道等级")
    private String pipeClass;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "壁厚等级")
    private String thickness;

    @Schema(description = "组件#1标签")
    private String tagNo1;

    @Schema(description = "组件#1材质代码")
    private String materialCode1;

    @Schema(description = "组件#1材质描述")
    private String material1;

    @Schema(description = "组建#1备注")
    private String remarks1;

    @Schema(description = "组件#2标签")
    private String tagNo2;

    @Schema(description = "组件#2材质代码")
    private String materialCode2;

    @Schema(description = "组件#2材质描述")
    private String material2;

    @Schema(description = "组建#2备注")
    private String remarks2;

    @Schema(description = "油漆类型")
    private String paintingCode;

    @JsonCreator
    public WeldEntryInsertDTO() {
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getShopField() {
        return shopField;
    }

    @JsonSetter
    public void setShopField(String shopField) {
        this.shopField = shopField;
    }

    public String getWeldType() {
        return weldType;
    }

    @JsonSetter
    public void setWeldType(String weldType) {
        this.weldType = weldType;
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

    public List<String> getDisplayWeldMarks() {
        return displayWeldMarks;
    }

    public void setDisplayWeldMarks(List<String> displayWeldMarks) {
        this.displayWeldMarks = displayWeldMarks;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public NDEType getNde() {
        return nde;
    }

    @JsonSetter
    public void setNde(NDEType nde) {
        this.nde = nde;
    }

    /**
     * 设定nde值。
     *
     * @param nde nde值
     */
    @JsonSetter
    public void setNde(String nde) {
        NDERatioDTO ratioDTO = new NDERatioDTO(nde);
        this.nde = ratioDTO.getType();
        this.ndeRatio = ratioDTO.getRatio();
    }

    public Integer getNdeRatio() {
        return ndeRatio;
    }

    public void setNdeRatio(Integer ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public Boolean getPwht() {
        return pwht;
    }

    public void setPwht(Boolean pwht) {
        this.pwht = pwht;
    }

    public Boolean getHardnessTest() {
        return hardnessTest;
    }

    public void setHardnessTest(Boolean hardnessTest) {
        this.hardnessTest = hardnessTest;
    }

    public Integer getPmiRatio() {
        return pmiRatio;
    }

    public void setPmiRatio(Integer pmiRatio) {
        this.pmiRatio = pmiRatio;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
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

    @JsonSetter
    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(String npsUnit) {
        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    @JsonSetter
    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        // 壁厚等级 修改为可以为空
        this.thickness = thickness;
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

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
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

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }
}
