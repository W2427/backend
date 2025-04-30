package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExInspActInstHandleHistoryDTO;
import com.ose.tasks.dto.bpm.ExInspReportHandleDTO;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.vo.InspectParty;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ExInspScheduleCriteriaDTO;
import com.ose.tasks.dto.bpm.ExInspScheduleDTO;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmExInspScheduleDetail;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExInspScheduleInterface {

    /**
     * 创建信息的报检记录
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param scheduleDTO
     * @param operator
     * @return
     */
    BpmExInspSchedule createExternalInspectionSchedule(Long orgId, Long projectId, ExInspScheduleDTO scheduleDTO,
                                                       OperatorDTO operator, Boolean isSingleReport, Boolean isSendEmail);

    /**
     * 创建新的报检详情记录
     */
    List<BpmExInspScheduleDetail> createExternalInspectionScheduleDetail(Long orgId, Long projectId,
                                                                         BpmExInspSchedule schedule, OperatorDTO operator,
                                                                         Set<InspectParty> inspectParties);

    /**
     * 删除报检记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param context   上下文
     * @param id
     * @param command   网关
     * @return
     */
    boolean deleteExternalInspectionSchedule(ContextDTO context, Long orgId, Long projectId, Long id, Map<String, Object> command);

    /**
     * 修改报检记录
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param id          exInspSchedule 的ID
     * @param scheduleDTO 外检安排DTO
     * @return
     */
    BpmExInspSchedule modifyExternalInspectionSchedule(Long orgId, Long projectId, Long id, ExInspScheduleDTO scheduleDTO);


    /**
     * 查询外检任务明细
     */
    List<ExInspReportHandleDTO> getScheduleDetail(Long orgId, Long projectId, QCReport qcReport);

    /**
     * 保存外检协调记录
     *
     * @param schedule 外检安排
     * @return
     */
    BpmExInspSchedule saveBpmExternalInspectionSchedule(BpmExInspSchedule schedule);


    /**
     * 获取报检记录详细信息
     *
     * @param id
     * @return
     */
    BpmExInspSchedule getExternalInspectionSchedule(Long id);


    /**
     * 获取报检记录详细信息
     *
     * @param id
     * @return
     */
    List<BpmExInspScheduleDetail> getExternalInspectionScheduleDetails(Long id);

    /**
     * 获取报检记录列表
     */
    Page<BpmExInspSchedule> getExternalInspectionScheduleList(Long orgId, Long projectId,
                                                              PageDTO page, ExInspScheduleCriteriaDTO criteriaDTO);

    List<ActReportDTO> getActReports(List<QCReport> qcReports);

    List<QCReport> getExternalInspectionScheduleReport(Long orgId, Long projectId, Long scheduleId);

    Page<ExInspActInstHandleHistory> getExInspActInstHandleHistory(
        Long orgId,
        Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO
    );
}
