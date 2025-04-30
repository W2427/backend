package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 放行单物品关联二维码 实体类。
 */
@Entity
@Table(name = "mat_release_note_item_detail_qr_code",
indexes = {
    @Index(columnList = "orgId,projectId,relnId"),
    @Index(columnList = "orgId,projectId,heatNoId")
})
public class ReleaseNoteItemDetailQrCodeEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "放行单表ID")
    private Long relnId;

    @Schema(description = "放行单详情表ID")
    private Long relnItemId;

    @Schema(description = "放行单详情关联炉批号表ID")
    private Long relnItemDetailId;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "一类一码物品的件数")
    private int cnt;

    @Schema(description = "流水号")
    private Integer seqNumber;

    @Schema(description = "炉批号 ID")
    @Column
    private Long heatNoId;

    public Long getRelnItemDetailId() {
        return relnItemDetailId;
    }

    public void setRelnItemDetailId(Long relnItemDetailId) {
        this.relnItemDetailId = relnItemDetailId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
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

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public Long getRelnItemId() {
        return relnItemId;
    }

    public void setRelnItemId(Long relnItemId) {
        this.relnItemId = relnItemId;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }
}
