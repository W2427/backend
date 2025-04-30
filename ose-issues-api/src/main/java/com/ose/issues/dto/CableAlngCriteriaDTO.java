package com.ose.issues.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CableAlngCriteriaDTO extends PageDTO {


    private static final long serialVersionUID = 801961677015427524L;

    @Schema(description = "电缆IDs")
    private List<Long> cableIDs;


    @Schema(description = "key word")
    private String keyWord;

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

    @Schema(description = "子系统")
    private List<String> subSystem;

    @Schema(description = "系统")
    private String system;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "托架OK")
    private String cableTrayStatus;

    @Schema(description = "敷设OK")
    private String cablePulledStatus;

    @Schema(description = "开始端接线OK")
    private String startTerminatedStatus;

    @Schema(description = "终止端接线OK")
    private String endTerminatedStatus;

    @Schema(description = "QCF OK")
    private String qcfStatus;

    @Schema(description = "memberId")
    private Long memberId;

    public List<String> getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(List<String> subSystem) {
        this.subSystem = subSystem;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public List<Long> getCableIDs() {
        return cableIDs;
    }

    public void setCableIDs(List<Long> cableIDs) {
        this.cableIDs = cableIDs;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCableTrayStatus() {
        return cableTrayStatus;
    }

    public void setCableTrayStatus(String cableTrayStatus) {
        this.cableTrayStatus = cableTrayStatus;
    }

    public String getCablePulledStatus() {
        return cablePulledStatus;
    }

    public void setCablePulledStatus(String cablePulledStatus) {
        this.cablePulledStatus = cablePulledStatus;
    }

    public String getStartTerminatedStatus() {
        return startTerminatedStatus;
    }

    public void setStartTerminatedStatus(String startTerminatedStatus) {
        this.startTerminatedStatus = startTerminatedStatus;
    }

    public String getEndTerminatedStatus() {
        return endTerminatedStatus;
    }

    public void setEndTerminatedStatus(String endTerminatedStatus) {
        this.endTerminatedStatus = endTerminatedStatus;
    }

    public String getQcfStatus() {
        return qcfStatus;
    }

    public void setQcfStatus(String qcfStatus) {
        this.qcfStatus = qcfStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
