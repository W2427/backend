package com.ose.tasks.entity.wbs;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 焊口实体数据实体。
 */
@Entity
@Table(name = "weld_log",
    indexes = {
        @Index(columnList = "deleted,orgId,projectId")
    })
public class WeldLog extends BaseVersionedBizEntity {


    private static final long serialVersionUID = 8534990129916796662L;

    @Schema(description = "焊口ID")
    @Column
    private Long entityId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "所属组织ID")
    private Long orgId;

    @Schema(description = "焊接程序编号，逗号分割")
    @Column
    private String wpsNo;

    @Schema(description = "焊接程序ID，逗号分隔")
    @Column
    private String wpsId;

    @Schema(description = "组建#1备注")
    @Column
    private String remark;

    @Schema(description = "最新焊后热处理结果 ID")
    @Column
    private Long pwhtId;

    @Schema(description = "最新硬度测试结果 ID")
    @Column
    private Long hdId;

    @Schema(description = "最新材质分析结果 ID")
    @Column
    private Long pmiId;

    @Schema(description = "最新焊接结果 ID")
    @Column
    private Long vTId;

    @Schema(description = "最新坡口结果 ID")
    @Column
    private Long bevelId;

    @Schema(description = "最新组对结果 ID")
    @Column
    private Long fitupId;

    @Schema(description = "最新补漆结果 ID")
    @Column
    private Long touchUpId;

    @Schema(description = "焊工ID，逗号分隔")
    @Column
    private String welderId;

    @Schema(description = "rt检验Id")
    @Column
    private String rtId;

    @Schema(description = "ut检验Id")
    @Column
    private String utId;

    @Schema(description = "mt检验Id")
    @Column
    private String mtId;

    @Schema(description = "pt检验Id")
    @Column
    private String ptId;

    @Schema(description = "fn检验Id")
    @Column
    private String fnId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public String getWpsId() {
        return wpsId;
    }

    public void setWpsId(String wpsId) {
        this.wpsId = wpsId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getPwhtId() {
        return pwhtId;
    }

    public void setPwhtId(Long pwhtId) {
        this.pwhtId = pwhtId;
    }

    public Long getHdId() {
        return hdId;
    }

    public void setHdId(Long hdId) {
        this.hdId = hdId;
    }

    public Long getPmiId() {
        return pmiId;
    }

    public void setPmiId(Long pmiId) {
        this.pmiId = pmiId;
    }

    public Long getvTId() {
        return vTId;
    }

    public void setvTId(Long vTId) {
        this.vTId = vTId;
    }

    public Long getBevelId() {
        return bevelId;
    }

    public void setBevelId(Long bevelId) {
        this.bevelId = bevelId;
    }

    public Long getFitupId() {
        return fitupId;
    }

    public void setFitupId(Long fitupId) {
        this.fitupId = fitupId;
    }

    public Long getTouchUpId() {
        return touchUpId;
    }

    public void setTouchUpId(Long touchUpId) {
        this.touchUpId = touchUpId;
    }

    public String getWelderId() {
        return welderId;
    }

    public void setWelderId(String welderId) {
        this.welderId = welderId;
    }

    public String getRtId() {
        return rtId;
    }

    public void setRtId(String rtId) {
        this.rtId = rtId;
    }

    public String getUtId() {
        return utId;
    }

    public void setUtId(String utId) {
        this.utId = utId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getPtId() {
        return ptId;
    }

    public void setPtId(String ptId) {
        this.ptId = ptId;
    }

    public String getFnId() {
        return fnId;
    }

    public void setFnId(String fnId) {
        this.fnId = fnId;
    }
}
