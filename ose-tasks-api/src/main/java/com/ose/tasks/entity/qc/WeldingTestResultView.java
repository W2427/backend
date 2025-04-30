package com.ose.tasks.entity.qc;


import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "welding_test_result")
public class WeldingTestResultView extends BaseEntity {

    private static final long serialVersionUID = -6230989791349840920L;

    @Column
    @Schema(name = "组织ID")
    private Long orgId;

    @Column
    @Schema(name = "项目ID")
    private Long projectId;

    @Column
    @Schema(description = "所属ISO号")
    private String isoNo;

    @Column
    @Schema(description = "实施位置类型")
    private String shopField;

    @Column
    @Schema(description = "母材信息1")
    private String baseMetal1;

    @Column
    @Schema(description = "母材信息2")
    private String baseMetal2;

    @Column
    @Schema(description = "焊口类型代码")
    private String weldType;

    @Column
    @Schema(description = "焊工ID")
    private String welderId;

    @Column
    @Schema(description = "WPS编号")
    private String wpsNo;

    @Column
    @Schema(description = "PWHT最新检测结果")
    private String pwhtResult;

    @Column
    @Schema(description = "PWHT ID")
    private Long pwhtId;

    @Column
    @Schema(description = "PWHT 结果备注")
    private String pwhtComment;

    @Column
    @Schema(description = "HD最新检测结果")
    private String hdResult;

    @Column
    @Schema(description = "HD ID")
    private Long hdId;

    @Column
    @Schema(description = "HD 结果备注")
    private String hdComment;

    @Column
    @Schema(description = "NDT最新检测结果")
    private String ndtResult;

    @Column
    @Schema(description = "NDT ID")
    private Long ndtId;

    @Column
    @Schema(description = "NDT 结果备注")
    private String ndtComment;

    @Column
    @Schema(description = "PMI最新检测结果")
    private String pmiResult;

    @Column
    @Schema(description = "PMI ID")
    private Long pmiId;

    @Column
    @Schema(description = "PMI 结果备注")
    private String pmiComment;

    @Column
    @Schema(description = "焊接 ID")
    private Long weldingId;

    @Column
    @Schema(description = "焊接 结果")
    private String weldingResult;

    @Column
    @Schema(description = "最新焊接结果备注")
    private String weldingComment;

    @Column
    @Schema(description = "最新焊口配对结果")
    private String pairResult;

    @Column
    @Schema(description = "最新焊口配对ID")
    private Long pairId;

    @Column
    @Schema(description = "组对 结果备注")
    private String pairComment;

    @Column
    @Schema(description = "最新坡口打磨结果")
    private String grooveGrindingResult;

    @Column
    @Schema(description = "最新坡口打磨ID")
    private Long grooveGrindingId;

    @Column
    @Schema(description = "坡口 结果备注")
    private String grooveGrindingComment;

    @Column
    @Schema(description = "最新补漆结果")
    private String paintRepairResult;

    @Column
    @Schema(description = "最新补漆结果 ID")
    private Long paintRepairId;

    @Column
    @Schema(description = "最新补漆结果备注")
    private String paintRepairComment;

    @Transient
    @Schema(description = "焊工名称")
    private List<String> welders;

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

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getShopField() {
        return shopField;
    }

    public void setShopField(String shopField) {
        this.shopField = shopField;
    }

    public String getBaseMetal1() {
        return baseMetal1;
    }

    public void setBaseMetal1(String baseMetal1) {
        this.baseMetal1 = baseMetal1;
    }

    public String getBaseMetal2() {
        return baseMetal2;
    }

    public void setBaseMetal2(String baseMetal2) {
        this.baseMetal2 = baseMetal2;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getWelderId() {
        return welderId;
    }

    public void setWelderId(String welderId) {
        this.welderId = welderId;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public String getPwhtResult() {
        return pwhtResult;
    }

    public void setPwhtResult(String pwhtResult) {
        this.pwhtResult = pwhtResult;
    }

    public String getHdResult() {
        return hdResult;
    }

    public void setHdResult(String hdResult) {
        this.hdResult = hdResult;
    }

    public String getNdtResult() {
        return ndtResult;
    }

    public void setNdtResult(String ndtResult) {
        this.ndtResult = ndtResult;
    }

    public String getPmiResult() {
        return pmiResult;
    }

    public void setPmiResult(String pmiResult) {
        this.pmiResult = pmiResult;
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

    public Long getNdtId() {
        return ndtId;
    }

    public void setNdtId(Long ndtId) {
        this.ndtId = ndtId;
    }

    public Long getPmiId() {
        return pmiId;
    }

    public void setPmiId(Long pmiId) {
        this.pmiId = pmiId;
    }

    public Long getWeldingId() {
        return weldingId;
    }

    public void setWeldingId(Long weldingId) {
        this.weldingId = weldingId;
    }

    public String getWeldingResult() {
        return weldingResult;
    }

    public void setWeldingResult(String weldingResult) {
        this.weldingResult = weldingResult;
    }

    public String getWeldingComment() {
        return weldingComment;
    }

    public void setWeldingComment(String weldingComment) {
        this.weldingComment = weldingComment;
    }

    public String getPairResult() {
        return pairResult;
    }

    public void setPairResult(String pairResult) {
        this.pairResult = pairResult;
    }

    public Long getPairId() {
        return pairId;
    }

    public void setPairId(Long pairId) {
        this.pairId = pairId;
    }

    public String getGrooveGrindingResult() {
        return grooveGrindingResult;
    }

    public void setGrooveGrindingResult(String grooveGrindingResult) {
        this.grooveGrindingResult = grooveGrindingResult;
    }

    public Long getGrooveGrindingId() {
        return grooveGrindingId;
    }

    public void setGrooveGrindingId(Long grooveGrindingId) {
        this.grooveGrindingId = grooveGrindingId;
    }

    public String getPwhtComment() {
        return pwhtComment;
    }

    public void setPwhtComment(String pwhtComment) {
        this.pwhtComment = pwhtComment;
    }

    public String getHdComment() {
        return hdComment;
    }

    public void setHdComment(String hdComment) {
        this.hdComment = hdComment;
    }

    public String getNdtComment() {
        return ndtComment;
    }

    public void setNdtComment(String ndtComment) {
        this.ndtComment = ndtComment;
    }

    public String getPmiComment() {
        return pmiComment;
    }

    public void setPmiComment(String pmiComment) {
        this.pmiComment = pmiComment;
    }

    public String getPairComment() {
        return pairComment;
    }

    public void setPairComment(String pairComment) {
        this.pairComment = pairComment;
    }

    public String getGrooveGrindingComment() {
        return grooveGrindingComment;
    }

    public void setGrooveGrindingComment(String grooveGrindingComment) {
        this.grooveGrindingComment = grooveGrindingComment;
    }

    public String getPaintRepairResult() {
        return paintRepairResult;
    }

    public void setPaintRepairResult(String paintRepairResult) {
        this.paintRepairResult = paintRepairResult;
    }

    public Long getPaintRepairId() {
        return paintRepairId;
    }

    public void setPaintRepairId(Long paintRepairId) {
        this.paintRepairId = paintRepairId;
    }

    public String getPaintRepairComment() {
        return paintRepairComment;
    }

    public void setPaintRepairComment(String paintRepairComment) {
        this.paintRepairComment = paintRepairComment;
    }

    public List<String> getWelders() {
        return welders;
    }

    public void setWelders(List<String> welders) {
        this.welders = welders;
    }
}
