package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmsDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;

public interface ExInspReHandleReportInterface {


    /**
     * 确认上传报告的结果，执行响应动作
     *
     * @param context
     * @param orgId
     * @param projectId
     * @param confirmDTO
     * @param operator
     * @return
     */
    BpmExInspConfirmResponseDTO handleReport(
        ContextDTO context,
        Long orgId,
        Long projectId,
        BpmExInspConfirmsDTO confirmDTO,
        OperatorDTO operator
    );

}
