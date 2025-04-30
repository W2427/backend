package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;

public interface ExInspUploadReportInterface {


    /**
     * 上传报告
     *
     * @return
     */
    BpmExInspConfirmResponseDTO uploadReport(
        Long orgId,
        Long projectId,
        DrawingUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        boolean secondUpload,
        ContextDTO context
    );

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
    BpmExInspConfirmResponseDTO confirmUploadedReport(
        ContextDTO context,
        Long orgId,
        Long projectId,
        BpmExInspConfirmsDTO confirmDTO,
        OperatorDTO operator
    );

    /**
     * 跳过上传报告
     *
     * @param orgId
     * @param projectId
     * @param skipDTO
     * @param contextDTO
     */
    void skipUpload(
        Long orgId, Long projectId, DrawingUploadDTO skipDTO, ContextDTO contextDTO
    );

   void externalInspectionConfirm(
       Long orgId,
       Long projectId,
       ContextDTO contextDTO,
       ExInspReportHandleSearchDTO exInspReportUploadDTO
    );

    void  externalInspectionReHandle(
        Long orgId,
        Long projectId,
        ContextDTO contextDTO,
        ExInspReportHandleSearchDTO uploadDTO
    );
}
