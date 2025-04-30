package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class WorkStepFollowUpDTO extends BaseDTO {

    @Schema(description = "工作项代码")
    private String workCode;

    @Schema(description = "工作项代码描述-表头")
    private String workCodeDescription;

    @Schema(description = "WP01的名称")
    private String module;

    @Schema(description = "根据Tag的第一部分前缀取名字")
    private String deck;

    @Schema(description = "Tag")
    private String tag;

    @Schema(description = "tag描述")
    private String tagDescription;

    @Schema(description = "保留空白")
    private String tagRemarks;

    @Schema(description = "加权进度求和后的总进度，再除以100")
    private String eqvProg;

    @Schema(description = "设计导入的tag重量")
    private String qty;

    @Schema(description = "eqvProg乘以qty")
    private String eqvQty;

    private String cutting;

    private String fitUp;

    private String weld;

    private String ndt;

    private String release;

    @Schema(description = "cutting总重量")
    private String cuttingTotal;

    @Schema(description = "cutting已用重量")
    private String cuttingDone;

    @Schema(description = "cutting剩余重量")
    private String cuttingRemaining;

    @Schema(description = "fitUp总数量")
    private String fitUpTotal;

    @Schema(description = "fitUp已用数量")
    private String fitUpDone;

    @Schema(description = "fitUp剩余数量")
    private String fitUpRemaining;

    @Schema(description = "weld总数量")
    private String weldTotal;

    @Schema(description = "weld已用数量")
    private String weldDone;

    @Schema(description = "weld剩余数量")
    private String weldRemaining;

    @Schema(description = "ndt总数量")
    private String ndtTotal;

    @Schema(description = "ndt已用数量")
    private String ndtDone;

    @Schema(description = "ndt剩余数量")
    private String ndtRemaining;

    @Schema(description = "release总数量")
    private String releaseTotal;

    @Schema(description = "release已用数量")
    private String releaseDone;

    @Schema(description = "release剩余数量")
    private String releaseRemaining;

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getWorkCodeDescription() {
        return workCodeDescription;
    }

    public void setWorkCodeDescription(String workCodeDescription) {
        this.workCodeDescription = workCodeDescription;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public String getTagRemarks() {
        return tagRemarks;
    }

    public void setTagRemarks(String tagRemarks) {
        this.tagRemarks = tagRemarks;
    }

    public String getEqvProg() {
        return eqvProg;
    }

    public void setEqvProg(String eqvProg) {
        this.eqvProg = eqvProg;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getEqvQty() {
        return eqvQty;
    }

    public void setEqvQty(String eqvQty) {
        this.eqvQty = eqvQty;
    }

    public String getCutting() {
        return cutting;
    }

    public void setCutting(String cutting) {
        this.cutting = cutting;
    }

    public String getFitUp() {
        return fitUp;
    }

    public void setFitUp(String fitUp) {
        this.fitUp = fitUp;
    }

    public String getWeld() {
        return weld;
    }

    public void setWeld(String weld) {
        this.weld = weld;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getCuttingTotal() {
        return cuttingTotal;
    }

    public void setCuttingTotal(String cuttingTotal) {
        this.cuttingTotal = cuttingTotal;
    }

    public String getCuttingDone() {
        return cuttingDone;
    }

    public void setCuttingDone(String cuttingDone) {
        this.cuttingDone = cuttingDone;
    }

    public String getCuttingRemaining() {
        return cuttingRemaining;
    }

    public void setCuttingRemaining(String cuttingRemaining) {
        this.cuttingRemaining = cuttingRemaining;
    }

    public String getFitUpTotal() {
        return fitUpTotal;
    }

    public void setFitUpTotal(String fitUpTotal) {
        this.fitUpTotal = fitUpTotal;
    }

    public String getFitUpDone() {
        return fitUpDone;
    }

    public void setFitUpDone(String fitUpDone) {
        this.fitUpDone = fitUpDone;
    }

    public String getFitUpRemaining() {
        return fitUpRemaining;
    }

    public void setFitUpRemaining(String fitUpRemaining) {
        this.fitUpRemaining = fitUpRemaining;
    }

    public String getWeldTotal() {
        return weldTotal;
    }

    public void setWeldTotal(String weldTotal) {
        this.weldTotal = weldTotal;
    }

    public String getWeldDone() {
        return weldDone;
    }

    public void setWeldDone(String weldDone) {
        this.weldDone = weldDone;
    }

    public String getWeldRemaining() {
        return weldRemaining;
    }

    public void setWeldRemaining(String weldRemaining) {
        this.weldRemaining = weldRemaining;
    }

    public String getNdtTotal() {
        return ndtTotal;
    }

    public void setNdtTotal(String ndtTotal) {
        this.ndtTotal = ndtTotal;
    }

    public String getNdtDone() {
        return ndtDone;
    }

    public void setNdtDone(String ndtDone) {
        this.ndtDone = ndtDone;
    }

    public String getNdtRemaining() {
        return ndtRemaining;
    }

    public void setNdtRemaining(String ndtRemaining) {
        this.ndtRemaining = ndtRemaining;
    }

    public String getReleaseTotal() {
        return releaseTotal;
    }

    public void setReleaseTotal(String releaseTotal) {
        this.releaseTotal = releaseTotal;
    }

    public String getReleaseDone() {
        return releaseDone;
    }

    public void setReleaseDone(String releaseDone) {
        this.releaseDone = releaseDone;
    }

    public String getReleaseRemaining() {
        return releaseRemaining;
    }

    public void setReleaseRemaining(String releaseRemaining) {
        this.releaseRemaining = releaseRemaining;
    }
}
