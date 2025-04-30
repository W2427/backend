package com.ose.issues.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SystemAlngCriteriaDTO extends ExperienceCriteriaDTO {


    private static final long serialVersionUID = -7576385874221026521L;
    @Schema(description = "子系统")
    private String subSystem;

    @Schema(description = "系统")
    private String system;

    @Schema(description = "负责人 memberId")
    private Long memberId;

    @Schema(description = "托架确认人ID")
    private Long cableTrayConfirmPersonId;

    @Schema(description = "电缆敷设确认人ID")
    private Long cablePulledConfirmPersonId;

    @Schema(description = "电缆开始端接线确认人ID")
    private Long cableStartTeminatedConfirmPersonId;

    @Schema(description = "电缆终止端接线确认人ID")
    private Long cableEndTerminatedConfirmPersonId;

    @Schema(description = "电缆QCF确认人ID")
    private Long cableQcfConfirmPersonId;

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Long getCableTrayConfirmPersonId() {
        return cableTrayConfirmPersonId;
    }

    public void setCableTrayConfirmPersonId(Long cableTrayConfirmPersonId) {
        this.cableTrayConfirmPersonId = cableTrayConfirmPersonId;
    }

    public Long getCablePulledConfirmPersonId() {
        return cablePulledConfirmPersonId;
    }

    public void setCablePulledConfirmPersonId(Long cablePulledConfirmPersonId) {
        this.cablePulledConfirmPersonId = cablePulledConfirmPersonId;
    }

    public Long getCableStartTeminatedConfirmPersonId() {
        return cableStartTeminatedConfirmPersonId;
    }

    public void setCableStartTeminatedConfirmPersonId(Long cableStartTeminatedConfirmPersonId) {
        this.cableStartTeminatedConfirmPersonId = cableStartTeminatedConfirmPersonId;
    }

    public Long getCableEndTerminatedConfirmPersonId() {
        return cableEndTerminatedConfirmPersonId;
    }

    public void setCableEndTerminatedConfirmPersonId(Long cableEndTerminatedConfirmPersonId) {
        this.cableEndTerminatedConfirmPersonId = cableEndTerminatedConfirmPersonId;
    }

    public Long getCableQcfConfirmPersonId() {
        return cableQcfConfirmPersonId;
    }

    public void setCableQcfConfirmPersonId(Long cableQcfConfirmPersonId) {
        this.cableQcfConfirmPersonId = cableQcfConfirmPersonId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
