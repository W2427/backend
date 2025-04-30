package com.ose.tasks.entity.bpm;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.qc.DefectTypes;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_ndt_rt_info")
public class BpmNdtRtInfo extends BaseBizEntity {

    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "组织id")
    private Long orgId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "流程实例id")
    private Long actInstId;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "片位号")
    private Integer filmNo;

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
    @Enumerated(EnumType.STRING)
    private DefectTypes defectType;

    @Schema(description = "是否合格")
    @Column(nullable = true, columnDefinition = "bit default b'1'")
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

    public Integer getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(Integer filmNo) {
        this.filmNo = filmNo;
    }

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

    public String getDlText() {
        return dlText;
    }

    public void setDlText(String dlText) {
        this.dlText = dlText;
    }

}
