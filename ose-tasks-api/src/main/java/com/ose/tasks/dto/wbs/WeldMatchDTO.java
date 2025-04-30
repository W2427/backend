package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.WPSMatchResult;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 焊口匹配结果DTO。
 */
public class WeldMatchDTO extends BaseDTO {
    private static final long serialVersionUID = -4485986784010634571L;

    @Schema(description = "实体 ID")
    private Long id;

    @Schema(description = "节点编号")
    private String no;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "壁厚等级")
    private String thickness;

    @Schema(description = "焊接程序编号")
    private String wpsNo;

    @Schema(description = "是否手动编辑过")
    private Boolean wpsEdited;

    @Schema(description = "组件#1材质")
    private String material1;

    //fftj
    @Schema(description = "组件#2材质")
    private String material2;

    @Schema(description = "焊口匹配结果")
    private WPSMatchResult result;

    @Schema(description = "焊接类型")
    private String weldType;

    /**
     * 取得数据实体 ID。
     *
     * @return 数据实体 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置数据实体 ID。
     *
     * @param id 数据实体 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Boolean getWpsEdited() {
        return wpsEdited;
    }

    public void setWpsEdited(Boolean wpsEdited) {
        this.wpsEdited = wpsEdited;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public WPSMatchResult getResult() {
        return result;
    }

    public void setResult(WPSMatchResult result) {
        this.result = result;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }
}
