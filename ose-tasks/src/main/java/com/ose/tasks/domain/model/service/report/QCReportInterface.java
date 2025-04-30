package com.ose.tasks.domain.model.service.report;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.QCReportPackageDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceReport;
import com.ose.tasks.dto.bpm.QCReportCriteriaDTO;
import com.ose.tasks.dto.bpm.ReportUploadDTO;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.InspectParty;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QCReportInterface {

    /**
     * 生成外检报告
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param bpmProcess  工序
     * @param operator    操作者
     * @param location    场地
     * @param taskIds     任务Ids
     * @param actInstList 流程列表
     */
    QCReport generateReport(Long orgId,
                            Long projectId,
                            BpmProcess bpmProcess,
                            OperatorDTO operator,
                            ContextDTO contextDTO,
                            String location,
                            List<Long> taskIds,
                            List<BpmActivityInstanceReport> actInstList,
                            List<InspectParty> inspectParties,
                            ReportConfig reportConfig,
                            String projectName,
                            Date inspectionTime,
                            List<Long> assignees,
                            List<String> userNames,
                            Set<String> entityNos,
                            Set<Long> actInstIds,
                            Long scheduleId);

    QCReport generateReport(Long orgId,
                            Long projectId,
                            BpmProcess bpmProcess,
                            OperatorDTO operator,
                            ContextDTO contextDTO,
                            String location,
                            List<Long> taskIds,
                            List<BpmActivityInstanceReport> actInstList,
                            List<InspectParty> inspectParties,
                            ReportConfig reportConfig,
                            String projectName,
                            Date inspectionTime,
                            List<Long> assignees,
                            List<String> userNames,
                            Set<String> entityNos,
                            Set<Long> actInstIds,
                            Long scheduleId,
                            Map<String, Integer> oldReportNumMap,
                            ReportStatus reportStatus);


    /**
     * 查询qcReport列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<QCReport> getList(Long orgId, Long projectId, PageDTO page, QCReportCriteriaDTO criteriaDTO);

    /**
     * 上传qcReport
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param uploadDTO   DTO
     * @param operatorDTO Operator
     */
    void uploadReport(ContextDTO contextDTO,
                      Long orgId,
                      Long projectId,
                      ReportUploadDTO uploadDTO,
                      OperatorDTO operatorDTO);

    /**
     * 补传report
     *
     * @param context
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param reportId
     * @param uploadDTO
     */
    void patchUploadReport(
        ContextDTO context,
        Long orgId,
        Long projectId,
        Long reportId,
        ReportUploadDTO uploadDTO
    );

    /**
     * 生成外检报告
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param bpmProcess         工序
     * @param operator           操作者
     * @param location           场地
     * @param taskIds            任务Ids
     * @param actInstList        流程列表
     * @param inspectionContents 检验内容
     */
    QCReport generateReportCover(Long orgId,
                                 Long projectId,
                                 BpmProcess bpmProcess,
                                 List<Long> assignees,
                                 List<String> userNames,
                                 OperatorDTO operator,
                                 String location,
                                 Set<String> entityNos,
                                 List<Long> taskIds,
                                 Set<Long> actInstIds,
                                 List<BpmActivityInstanceReport> actInstList,
                                 String inspectionContents,
                                 List<InspectParty> inspectParties,
                                 String projectName,
                                 Date inspectionTime,
                                 ReportConfig reportConfig,
                                 Long scheduleId);

    /**
     * 取得 工序上生成报告的设置
     *
     * @param bpmProcess       工序
     * @param subReportInfoMap reportType -> subReportType
     */
    Map<String, List<ReportConfig>> getReportConfig(BpmProcess bpmProcess, Map<String, String> subReportInfoMap);

    /**
     * 打包图纸文件。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operatorDTO 操作人员
     */
    void startZip(Long orgId,
                  Long projectId,
                  QCReportPackageDTO qcReportPackageDTO,
                  OperatorDTO operatorDTO,
                  ContextDTO contextDTO
    );
}
