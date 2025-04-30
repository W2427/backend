package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmExInspConfirm;
import com.ose.tasks.entity.bpm.BpmExInspUploadHistory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ExInspReportInterface {

    /**
     * 查询外检报告上传历史记录
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param pageDTO    分页DTO
     * @param operatorId
     * @return
     */
    Page<BpmExInspUploadHistory> externalInspectionUploadHistories(Long orgId, Long projectId,
                                                                   ExInspUploadHistorySearchDTO pageDTO, Long operatorId);


    /**
     * 查询报检记录
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    Page<ExInspReportsDTO> externalInspectionReports(Long orgId, Long projectId, PageDTO pageDTO,
                                                     ExInspReportCriteriaDTO criteriaDTO);

    Page<BpmExInspConfirm> uploadFileConfirmDetails(Long orgId, Long projectId, Long id, PageDTO pageDTO);


    /**
     * 处理上传的报告，返回二维码 对应 页数编号列表 的map
     *
     * @param pdfPerPages 上传文件的记录的列表
     * @return 返回二维码 对应 页数编号列表 的map
     */
    Map<String, List<String>> handleUploadedReport(Long orgId, Long projectId, List<String> pdfPerPages);

}
