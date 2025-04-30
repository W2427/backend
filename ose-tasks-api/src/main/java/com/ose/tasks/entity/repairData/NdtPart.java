package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "ndt",
    indexes = {
        @Index(columnList = "entityId")
    })
public class NdtPart extends BaseEntity {

    private static final long serialVersionUID = -2467546275146123100L;
    @Schema(description = "iso")
    private String iso;

    @Schema(description = "spool no")
    private String spoolNo;

    @Schema(description = "weld no")
    private String weldNo;

    @Schema(description = "repair")
    private String repair;

    @Schema(description = "date")
    private String date;

    @Schema(description = "report_no")
    private String reportNo;

    @Schema(description = "result")
    private String result;

    @Schema(description = "ndt")
    private String ndt;

    @Schema(description = "no")
    private String no;

    @Schema(description = "entity Id")
    private Long entityId;

    private Integer memo;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getMemo() {
        return memo;
    }

    public void setMemo(Integer memo) {
        this.memo = memo;
    }
}
