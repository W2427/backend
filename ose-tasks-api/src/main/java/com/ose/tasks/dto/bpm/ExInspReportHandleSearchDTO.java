package com.ose.tasks.dto.bpm;

import java.util.List;

/**
 * 外检报告上传 处理
 */
public class ExInspReportHandleSearchDTO extends BaseBatchTaskCriteriaDTO {

    private static final long serialVersionUID = -34948993806220141L;

    private List<ExInspReportHandleDTO> exInspReportHandleDTOS = null;

    public List<ExInspReportHandleDTO> getExInspReportHandleDTOS() {
        return exInspReportHandleDTOS;
    }

    public void setExInspReportHandleDTOS(List<ExInspReportHandleDTO> exInspReportHandleDTOS) {
        this.exInspReportHandleDTOS = exInspReportHandleDTOS;
    }
}
