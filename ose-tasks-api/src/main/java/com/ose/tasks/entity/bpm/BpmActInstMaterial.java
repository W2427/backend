package com.ose.tasks.entity.bpm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_act_inst_material",
indexes = {
    @Index(columnList = "entityId,actInstId"),
    @Index(columnList = "heatNoId1,heatNoId2"),
    @Index(columnList = "entityQrCode1"),
    @Index(columnList = "entityQrCode2")
})
public class BpmActInstMaterial extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = " 实体id")
    @Column
    private Long entityId;

    //
    @Schema(description = " 流程实例id")
    @Column
    private Long actInstId;

    //
    @Schema(description = " 任务id")
    @Column
    private String actTaskId;

    //
    @Schema(description = " 管子二维码1")
    @Column
    private String entityQrCode1;

    //
    @Schema(description = " tagNumber1")
    @Column
    private String tagNumber1;

    //
    @Schema(description = " tagNumber2")
    @Column
    private String tagNumber2;

    //
    @Schema(description = " 管子二维码2")
    @Column
    private String entityQrCode2;

    @Schema(description = " 炉批号1")
    @Column
    private String heatNoCode1;

    @Schema(description = " 短描述1")
    @Column
    private String shortDesc1;

    @Schema(description = " 炉批号2")
    @Column
    private String heatNoCode2;

    @Schema(description = "炉批号1Id")
    @Column
    private Long heatNoId1;

    @Schema(description = " 炉批号2Id")
    @Column
    private Long heatNoId2;

    //
    @Schema(description = " 短描述2")
    @Column
    private String shortDesc2;

    @Schema(description = " 1是否打印")
    @Column(columnDefinition = "bit default b'0'")
    private Boolean printFlag1;

    @Schema(description = " 2是否打印")
    @Column(columnDefinition = "bit default b'0'")
    private Boolean printFlag2;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getActTaskId() {
        return actTaskId;
    }

    public void setActTaskId(String actTaskId) {
        this.actTaskId = actTaskId;
    }

    public String getHeatNoCode1() {
        return heatNoCode1;
    }

    public void setHeatNoCode1(String heatNoCode1) {
        this.heatNoCode1 = heatNoCode1;
    }

    public String getShortDesc1() {
        return shortDesc1;
    }

    public void setShortDesc1(String shortDesc1) {
        this.shortDesc1 = shortDesc1;
    }

    public String getHeatNoCode2() {
        return heatNoCode2;
    }

    public void setHeatNoCode2(String heatNoCode2) {
        this.heatNoCode2 = heatNoCode2;
    }

    public String getShortDesc2() {
        return shortDesc2;
    }

    public void setShortDesc2(String shortDesc2) {
        this.shortDesc2 = shortDesc2;
    }

    public String getEntityQrCode1() {
        return entityQrCode1;
    }

    public void setEntityQrCode1(String entityQrCode1) {
        this.entityQrCode1 = entityQrCode1;
    }

    public String getEntityQrCode2() {
        return entityQrCode2;
    }

    public void setEntityQrCode2(String entityQrCode2) {
        this.entityQrCode2 = entityQrCode2;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTagNumber1() {
        return tagNumber1;
    }

    public void setTagNumber1(String tagNumber1) {
        this.tagNumber1 = tagNumber1;
    }

    public String getTagNumber2() {
        return tagNumber2;
    }

    public void setTagNumber2(String tagNumber2) {
        this.tagNumber2 = tagNumber2;
    }

    public Boolean getPrintFlag1() {
        return printFlag1;
    }

    public void setPrintFlag1(Boolean printFlag1) {
        this.printFlag1 = printFlag1;
    }

    public Boolean getPrintFlag2() {
        return printFlag2;
    }

    public void setPrintFlag2(Boolean printFlag2) {
        this.printFlag2 = printFlag2;
    }

    public Long getHeatNoId1() {
        return heatNoId1;
    }

    public void setHeatNoId1(Long heatNoId1) {
        this.heatNoId1 = heatNoId1;
    }

    public Long getHeatNoId2() {
        return heatNoId2;
    }

    public void setHeatNoId2(Long heatNoId2) {
        this.heatNoId2 = heatNoId2;
    }
}
