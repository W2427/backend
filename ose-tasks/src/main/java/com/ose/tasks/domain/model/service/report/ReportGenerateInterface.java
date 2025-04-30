package com.ose.tasks.domain.model.service.report;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.report.dto.BaseListReportDTO;
import com.ose.report.dto.BaseReportListItemDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.tasks.entity.bpm.BpmActivityInstanceReport;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;

import java.util.List;
import java.util.Map;

public interface ReportGenerateInterface {

    /**
     * 生成 报告 接口
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param actTaskIds     任务IDs
     * @param listReportDTO  报告传输数据DTO
     * @param actInstList    流程实例 列表
     * @param reportMetaData 报告 元数据
     * @param operatorDTO    操作者DTO
     * @return
     */
    <T extends BaseListReportDTO> ReportHistory generateReport(Long orgId,
                                                               Long projectId,
                                                               String[] actTaskIds,
                                                               List<BpmActivityInstanceReport> actInstList,
                                                               T listReportDTO,
                                                               Map<String, Object> reportMetaData,
                                                               OperatorDTO operatorDTO,
                                                               ContextDTO contextDTO);


    /**
     * 生成 原生的 XLSX 报告 接口
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param actTaskIds     任务IDs
     * @param listReportDTO  报告传输数据DTO
     * @param actInstList    流程实例 列表
     * @param reportMetaData 报告 元数据
     * @param operatorDTO    操作者DTO
     * @return
     */
    default <T extends BaseListReportDTO> ReportHistory generateXlsReport(Long orgId,
                                                               Long projectId,
                                                               String[] actTaskIds,
                                                               List<BpmActivityInstanceReport> actInstList,
                                                               T listReportDTO,
                                                               Map<String, Object> reportMetaData,
                                                               OperatorDTO operatorDTO){
        return new ReportHistory();
    }

    /**
     * 生成报告封面
     *
     * @param orgId
     * @param projectId
     * @param inspectionContents
     * @param listReportDTO
     * @param reportMetaData
     * @param actInstList
     * @param <T                 extends BaseListReportDTO>
     * @return
     */
    <T extends BaseListReportDTO> ReportHistory generateReportCover(Long orgId,
                                                                    Long projectId,
                                                                    String inspectionContents,
                                                                    T listReportDTO,
                                                                    Map<String, Object> reportMetaData,
                                                                    List<BpmActivityInstanceReport> actInstList);

    /**
     * 生成报告号
     *
     * @param orgId
     * @param projectId
     * @return
     */
    <T extends BaseListReportDTO> T generateReportDTO(Long orgId,
                                                      Long projectId,
                                                      Map<String, Object> reportMetaData,
                                                      List<BpmActivityInstanceBase> actInstList);

    /**
     * 生成报告号,外检申请用
     *
     * @param orgId
     * @param projectId
     * @return
     */
    <T extends BaseListReportDTO> T generateReportDTO(Long orgId,
                                                      Long projectId,
                                                      Map<String, Object> reportMetaData,
                                                      List<BpmActivityInstanceBase> activityInstances,
                                                      boolean inspApplication);


    /**
     * 返回报告的类型
     *
     * @param orgId
     * @param projectId
     * @param actInst
     * @return 报告类型字符串
     */
    String getReportType(Long orgId, Long projectId, BpmActivityInstanceBase actInst);

    /**
     * 返回报告数据内容map。
     *
     * @return
     */
    List<Map<String, Object>> exportDto(Long orgId,
                                        Long projectId,
                                        List<BpmActivityInstanceState> actInstList);

    /**
     * 返回报告数据字段map。
     *
     * @param listReportDTO
     * @param <T>
     * @return
     */
    <T extends BaseReportListItemDTO> Map<String, Object> exportDtoKeys(
        T listReportDTO);

    /**
     * 返回报告数据字段map。generateReportDTO
     *
     * @param <T>
     * @return
     */
    <T extends BaseReportListItemDTO> T getReportItemDto();

}
