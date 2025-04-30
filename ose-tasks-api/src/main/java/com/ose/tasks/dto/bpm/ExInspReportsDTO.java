package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmExInspConfirm;

/**
 * 外检报告 详情
 */
public class ExInspReportsDTO extends BaseDTO {

    private static final long serialVersionUID = -34948993806220141L;

    private QCReport qcReport;

    private List<BpmExInspConfirm> exInspConfirmDTOS;

    //节点命令
    private List<TaskGatewayDTO> gateway;

    public QCReport getQcReport() {
        return qcReport;
    }

    public void setQcReport(QCReport qcReport) {
        this.qcReport = qcReport;
    }

    public List<BpmExInspConfirm> getExInspConfirmDTOS() {
        return exInspConfirmDTOS;
    }

    public void setExInspConfirmDTOS(List<BpmExInspConfirm> exInspConfirmDTOS) {
        this.exInspConfirmDTOS = exInspConfirmDTOS;
    }

    public List<TaskGatewayDTO> getGateway() {
        return gateway;
    }

    public void setGateway(List<TaskGatewayDTO> gateway) {
        this.gateway = gateway;
    }

}
