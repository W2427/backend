package com.ose.tasks.dto.bpm;

import java.util.List;
import java.util.Map;

import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmExInspConfirm;
import com.ose.vo.EntityStatus;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 传输对象
 */
public class BpmExInspConfirmsDTO extends BaseBatchTaskCriteriaDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "确认的id数组")
    private List<Long> confirmIdList;

    @Schema(description = "确认的 bpmExInspUploadConfirm 集合")
    private List<BpmExInspConfirm> exInspUploadConfirms;

    @Schema(description = "状态：REJECTED，APPROVED ")
    private EntityStatus status;

    @Schema(description = "QC 报告集合")
    private Map<Long, QCReport> reports;

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public List<Long> getConfirmIdList() {
        return confirmIdList;
    }

    public void setConfirmIdList(List<Long> confirmIdList) {
        this.confirmIdList = confirmIdList;
    }

    public List<BpmExInspConfirm> getExInspUploadConfirms() {
        return exInspUploadConfirms;
    }

    public void setExInspUploadConfirms(List<BpmExInspConfirm> exInspUploadConfirms) {
        this.exInspUploadConfirms = exInspUploadConfirms;
    }

    public Map<Long, QCReport> getReports() {
        return reports;
    }

    public void setReports(Map<Long, QCReport> reports) {
        this.reports = reports;
    }
}
