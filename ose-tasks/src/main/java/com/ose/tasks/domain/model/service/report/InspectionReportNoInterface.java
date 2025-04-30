package com.ose.tasks.domain.model.service.report;

/**
 * 报告编号接口
 */
public interface InspectionReportNoInterface {

    /**
     * 生成新的reportNo
     *
     * @param projectId  项目ID
     * @param process
     * @param type
     * @param moduleName
     * @param orgId      组织ID
     * @return
     */
    String generateNewReportNo(Long orgId, Long projectId, String process, String type, String moduleName);

    /**
     * 获取临时报告编号
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param process
     * @param ndeType
     * @param moduleName
     * @return
     */
    String generateTempReportNo(Long orgId, Long projectId, String process, String ndeType, String moduleName);

}
