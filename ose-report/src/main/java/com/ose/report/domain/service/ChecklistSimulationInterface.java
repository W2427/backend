package com.ose.report.domain.service;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ChecklistSimulationDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.ChecklistSimulation;
import org.springframework.data.domain.Page;

/**
 * 模拟检查单服务接口
 */
public interface ChecklistSimulationInterface {

    /**
     * 查询模拟检查单
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param pagination 分页信息
     * @return 模拟检查单数据
     */
    Page<ChecklistSimulation> searchSimulation(Long orgId, Long projectId, PageDTO pagination);

    /**
     * 查询单条模拟检查单数据
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 单条模拟检查单数据
     */

    ChecklistSimulation searchSimulationInfo(Long orgId, Long projectId, Long simulationId);

    /**
     * 创建模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param checklistSimulationDTO 模拟检查单数据
     * @return 创建完成的模拟检查单
     */
    ChecklistSimulation createSimulation(Long orgId, Long projectId, ChecklistSimulationDTO checklistSimulationDTO);

    /**
     * 更新模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param simulationId           模拟检查单ID
     * @param checklistSimulationDTO 模拟检查单数据
     * @return 更新完成的模拟检查单
     */
    ChecklistSimulation updateSimulation(Long orgId, Long projectId, Long simulationId, ChecklistSimulationDTO checklistSimulationDTO);

    /**
     * 更新模拟检查单生成的文件路径
     *
     * @param simulationId  模拟检查单ID
     * @param generatedFile 检查单生成的文件路径, fileId
     * @return 更新模拟检查单生成的文件路径
     */
    ChecklistSimulation updateGeneratedFile(Long simulationId, Long generatedFile);

    /**
     * 删除模拟检查单
     *
     * @param simulationId 模拟检查单ID
     */
    void deleteSimulation(Long simulationId);

    /**
     * 组装检查单报表数据
     *
     * @param simulationId 模拟检查单ID
     * @return 检查单报表数据
     */
    ChecklistReportDTO assembleSimulationReportData(Long simulationId);
}
