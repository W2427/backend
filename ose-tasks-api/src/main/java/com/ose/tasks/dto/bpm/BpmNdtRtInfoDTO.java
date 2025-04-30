package com.ose.tasks.dto.bpm;

import java.util.Date;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.DefectTypes;

import io.swagger.v3.oas.annotations.media.Schema;


public class BpmNdtRtInfoDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "流程实例id")
    private Long actInstId;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "片位号")
    private String filmNo;

    @Schema(description = "W.L.(mm)")
    private Integer wl;

    @Schema(description = "D.L. 缺陷长度(mm)")
    private Integer dl;

    @Schema(description = "D.L. 缺陷长度(mm) 符号")
    private String dlSymbol;

    @Schema(description = "D.L. 缺陷长度(mm) 参数1")
    private Integer dlParmFirst;

    @Schema(description = "D.L. 缺陷长度(mm) 参数2")
    private Integer dlParmSecond;

    @Schema(description = "D.L. 缺陷长度(mm) 文本表示")
    private String dlText;

    @Schema(description = "透度计指数")
    private String iqi;

    @Schema(description = "黑度")
    private String density;

    @Schema(description = "缺陷类型")
    private DefectTypes defectType;

    @Schema(description = "是否合格")
    private Boolean qualified;

    @Schema(description = "日期")
    private Date judgeDate;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
    }

    public Boolean getQualified() {
        return qualified;
    }

    public void setQualified(Boolean qualified) {
        this.qualified = qualified;
    }

    public String getIqi() {
        return iqi;
    }

    public void setIqi(String iqi) {
        this.iqi = iqi;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public DefectTypes getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectTypes defectType) {
        this.defectType = defectType;
    }

    public Date getJudgeDate() {
        return judgeDate;
    }

    public void setJudgeDate(Date judgeDate) {
        this.judgeDate = judgeDate;
    }

    public Integer getWl() {
        return wl;
    }

    public void setWl(Integer wl) {
        this.wl = wl;
    }

    public Integer getDl() {
        return dl;
    }

    public void setDl(Integer dl) {
        this.dl = dl;
    }

    public String getDlText() {
        return dlText;
    }

    public void setDlText(String dlText) {
        this.dlText = dlText;
    }

    public String getDlSymbol() {
        return dlSymbol;
    }

    public void setDlSymbol(String dlSymbol) {
        this.dlSymbol = dlSymbol;
    }

    public Integer getDlParmFirst() {
        return dlParmFirst;
    }

    public void setDlParmFirst(Integer dlParmFirst) {
        this.dlParmFirst = dlParmFirst;
    }

    public Integer getDlParmSecond() {
        return dlParmSecond;
    }

    public void setDlParmSecond(Integer dlParmSecond) {
        this.dlParmSecond = dlParmSecond;
    }

}
