package com.ose.tasks.entity.repairData;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "pmi",
    indexes = {
        @Index(columnList = "entityId")
    })
public class PmiPart extends BaseEntity {


    private static final long serialVersionUID = 5302043794354196584L;

    @Schema(description = "iso")
    private String iso;

    @Schema(description = "joint no")
    private String jointNo;

    @Schema(description = "repair")
    private String repair;

    @Schema(description = "report_no")
    private String reportNo;

    @Schema(description = "date")
    private String date;

    @Schema(description = "no")
    private String no;

    @Schema(description = "entity Id")
    private Long entityId;

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

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
