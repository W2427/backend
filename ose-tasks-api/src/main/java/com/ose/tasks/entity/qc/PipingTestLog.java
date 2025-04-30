package com.ose.tasks.entity.qc;

import com.ose.tasks.vo.bpm.ExInspStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 管道专业 实施记录。
 */
@Entity
@Table(name = "piping_construction_log",
    indexes = {
        @Index(columnList = "entityId,actInstId,deleted", unique = true),
        @Index(columnList = "actInstId,testResult,deleted"),
        @Index(columnList = "processId")
    })
public final class PipingTestLog extends BaseConstructionLog {


    private static final long serialVersionUID = -6192230578276350872L;


    @Schema(description = "材料1 炉批号 Id")
    @Column
    private Long heatNoId1;

    @Schema(description = "材料2 炉批号Id")
    @Column
    private Long heatNoId2;

    @Schema(description = "焊后热处理编号")
    @Column
    private String pwhtCode;

    @Schema(description = "WPS Nos,逗号分隔")
    @Column
    private String wpsNos;

    @Schema(description = "外检结果")
    @Column
    @Enumerated(EnumType.STRING)
    private ExInspStatus exInspStatus;

    @Schema(description = "射线测试胶片质量")
    @Column
    private String rtFilmQuantity;

    @Schema(description = "探伤长度")
    @Column
    private Integer inspectionLength;

    @Schema(description = "SPOOL ID")
    @Column
    private Long spoolEntityId;

    @Schema(description = "ISO ID")
    @Column
    private Long isoEntityId;

    public PipingTestLog() {
        super();
    }

    public String getRtFilmQuantity() {
        return rtFilmQuantity;
    }

    public void setRtFilmQuantity(String rtFilmQuantity) {
        this.rtFilmQuantity = rtFilmQuantity;
    }

    public Integer getInspectionLength() {
        return inspectionLength;
    }

    public void setInspectionLength(Integer inspectionLength) {
        this.inspectionLength = inspectionLength;
    }

    public ExInspStatus getExInspStatus() {
        return exInspStatus;
    }

    public void setExInspStatus(ExInspStatus exInspStatus) {
        this.exInspStatus = exInspStatus;
    }

    public String getPwhtCode() {
        return pwhtCode;
    }

    public void setPwhtCode(String pwhtCode) {
        this.pwhtCode = pwhtCode;
    }

    public Long getSpoolEntityId() {
        return spoolEntityId;
    }

    public void setSpoolEntityId(Long spoolEntityId) {
        this.spoolEntityId = spoolEntityId;
    }

    public Long getIsoEntityId() {
        return isoEntityId;
    }

    public void setIsoEntityId(Long isoEntityId) {
        this.isoEntityId = isoEntityId;
    }

    public String getWpsNos() {
        return wpsNos;
    }

    public void setWpsNos(String wpsNos) {
        this.wpsNos = wpsNos;
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
