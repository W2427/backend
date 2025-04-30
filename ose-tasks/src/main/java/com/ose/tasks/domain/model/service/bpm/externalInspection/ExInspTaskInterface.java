package com.ose.tasks.domain.model.service.bpm.externalInspection;

import java.util.List;

import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.vo.InspectResult;
import com.ose.tasks.vo.qc.ReportStatus;
import org.springframework.data.domain.Page;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExInspReportHandleDTO;
import com.ose.tasks.dto.bpm.ExInspReportHandleSearchDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;

public interface ExInspTaskInterface {

    /**
     * 查询需要再次处理的外检
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param pageDTO     分页DTO
     * @param criteriaDTO
     * @return  Page<QCReport>
     */
    Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO, ExInspViewCriteriaDTO criteriaDTO);

    /**
     * 需要再次处理外检明细
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    List<ExInspReportHandleDTO> externalInspectionViewsDetail(Long orgId, Long projectId, Long id);


    void externalInspectionAgainHandle(Long orgId, Long projectId,
                                       ExInspReportHandleSearchDTO uploadDTO);


    /**
     * 将外检报告放到entityDoc中
     *
     * @param exInspSchedule        外检申请
     * @param reports               报告s
     * @param reportStatus          报告类型
     * @param gateWayCommand        报告处理网关
     * @param operatorDTO           operatorDTO
     */
    void moveReportsToEntityDoc(BpmExInspSchedule exInspSchedule,
                                List<ActReportDTO> reports,
                                ReportStatus reportStatus,
                                String gateWayCommand,
                                OperatorDTO operatorDTO);


    /**
     * 将NG的交接实体释放掉。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO
     * @return
     */
    boolean releaseDeliveryEntities(Long orgId, Long projectId, ExInspReportHandleSearchDTO uploadDTO);

    /**
     * 取得外检结果、是否为最终外检 流程文档类型
     *
     * @param inspectResult
     * @param exInspFinal
     * @param actInstDocType
     * @param reportStatus
     * @param gateWayCommand
     */

    void getExInspResult(InspectResult inspectResult,
                         Boolean exInspFinal,
                         ActInstDocType actInstDocType,
                         ReportStatus reportStatus,
                         String gateWayCommand);


}
