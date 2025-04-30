package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SystemAlngTransferDTO extends BaseDTO {


    private static final long serialVersionUID = -4963176032546579348L;

    @Schema(description = "子系统ID")
    private List<Long> subSystemIds;

    @Schema(description = "托架确认者ID")
    private Long cableTrayConfirmPersonId;

    @Schema(description = "敷设确认者ID")
    private Long cablePulledConfirmPersonId;

    @Schema(description = "盘柜端/开始端接线完成确认者ID")
    private Long startTerminatedConfirmPersonId;

    @Schema(description = "设备端/终止端接线完成确认者ID")
    private Long endTerminatedConfirmPersonId;

    @Schema(description = "QCF确认者ID")
    private Long qcfOkConfirmPersonId;


    @Schema(description = "Comments")
    private String comment;

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

    public Long getStartTerminatedConfirmPersonId() {
        return startTerminatedConfirmPersonId;
    }

    public void setStartTerminatedConfirmPersonId(Long startTerminatedConfirmPersonId) {
        this.startTerminatedConfirmPersonId = startTerminatedConfirmPersonId;
    }

    public Long getEndTerminatedConfirmPersonId() {
        return endTerminatedConfirmPersonId;
    }

    public void setEndTerminatedConfirmPersonId(Long endTerminatedConfirmPersonId) {
        this.endTerminatedConfirmPersonId = endTerminatedConfirmPersonId;
    }

    public Long getQcfOkConfirmPersonId() {
        return qcfOkConfirmPersonId;
    }

    public void setQcfOkConfirmPersonId(Long qcfOkConfirmPersonId) {
        this.qcfOkConfirmPersonId = qcfOkConfirmPersonId;
    }

    public List<Long> getSubSystemIds() {
        return subSystemIds;
    }

    public void setSubSystemIds(List<Long> subSystemIds) {
        this.subSystemIds = subSystemIds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
