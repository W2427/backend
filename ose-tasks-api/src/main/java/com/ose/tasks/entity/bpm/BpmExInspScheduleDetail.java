package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import com.ose.vo.InspectParty;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 外检协调详细。 com.ose.tasks.entity.bpm.BpmExInspScheduleDetail
 */
@Entity
@Table(name = "bpm_external_inspection_schedule_detail")
public class BpmExInspScheduleDetail extends BaseVersionedBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    @Schema(description = "ORG ID")
    @Column
    private Long orgId;

    @Schema(description = "PROJECT ID")
    @Column
    private Long projectId;

    @Schema(description = "报检人")
    @Column
    private Long operator;

    @Schema(description = "报检安排的ID")
    @Column
    private Long scheduleId;

    @Schema(description = "状态")
    @Column
    @Enumerated(EnumType.STRING)
    private InspectParty inspectParty;

    @Schema(description = "状态")
    @Column
    @Enumerated(EnumType.STRING)
    private ExInspApplyStatus applyStatus;


    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public InspectParty getInspectParty() {
        return inspectParty;
    }

    public void setInspectParty(InspectParty inspectParty) {
        this.inspectParty = inspectParty;
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

    public ExInspApplyStatus getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(ExInspApplyStatus applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

}
