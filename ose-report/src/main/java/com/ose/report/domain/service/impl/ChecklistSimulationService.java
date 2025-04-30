package com.ose.report.domain.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.report.domain.repository.ChecklistItemRepository;
import com.ose.report.domain.repository.ChecklistRepository;
import com.ose.report.domain.repository.ChecklistSimulationRepository;
import com.ose.report.domain.repository.ReportTemplateRepository;
import com.ose.report.domain.service.ChecklistSimulationInterface;
import com.ose.report.dto.ChecklistInfoDTO;
import com.ose.report.dto.ChecklistSimulationDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.Checklist;
import com.ose.report.entity.ChecklistItem;
import com.ose.report.entity.ChecklistSimulation;
import com.ose.report.entity.Template;
import com.ose.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 模拟检查单服务
 */
@Component
public class ChecklistSimulationService extends BaseChecklistService implements ChecklistSimulationInterface {

    /**
     * JSON 转换工具
     **/
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 检查单 操作仓库
     */
    private final ChecklistRepository checklistRepository;

    /**
     * 检查项 操作仓库
     */
    private final ChecklistItemRepository checklistItemRepository;

    /**
     * 模拟检查单 操作仓库
     */
    private final ChecklistSimulationRepository checklistSimulationRepository;

    /**
     * 报表模板 操作仓库
     */
    private final ReportTemplateRepository reportTemplateRepository;

    /**
     * 构造方法
     *
     * @param checklistRepository           检查单操作仓库
     * @param checklistItemRepository       检查项操作仓库
     * @param checklistSimulationRepository 模拟检查单操作仓库
     * @param reportTemplateRepository      报表模板操作仓库
     */
    @Autowired
    public ChecklistSimulationService(ChecklistRepository checklistRepository,
                                      ChecklistItemRepository checklistItemRepository,
                                      ChecklistSimulationRepository checklistSimulationRepository,
                                      ReportTemplateRepository reportTemplateRepository) {

        this.checklistRepository = checklistRepository;
        this.checklistItemRepository = checklistItemRepository;
        this.checklistSimulationRepository = checklistSimulationRepository;
        this.reportTemplateRepository = reportTemplateRepository;
    }

    /**
     * 查询模拟检查单
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param pagination 分页信息
     * @return 模拟检查单数据
     */
    @Override
    public Page<ChecklistSimulation> searchSimulation(Long orgId, Long projectId, PageDTO pagination) {

        return checklistSimulationRepository.findAllByOrgIdAndProjectId(orgId, projectId, pagination.toPageable());
    }

    /**
     * 查询单条模拟检查单数据
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 单条模拟检查单数据
     */
    @Override
    public ChecklistSimulation searchSimulationInfo(Long orgId, Long projectId, Long simulationId) {

        // 获取模拟检查单信息
        ChecklistSimulation checklistSimulation =
            checklistSimulationRepository.findById(simulationId).orElse(null);

        if (checklistSimulation == null) {
            return null;
        }

        return checklistSimulation;
    }

    /**
     * 创建模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param checklistSimulationDTO 模拟检查单数据
     * @return 创建完成的模拟检查单
     */
    @Override
    public ChecklistSimulation createSimulation(Long orgId, Long projectId,
                                                ChecklistSimulationDTO checklistSimulationDTO) {

        ChecklistSimulation checklistSimulation = BeanUtils.copyProperties(
            checklistSimulationDTO, new ChecklistSimulation());

        String checklistInfo;
        try {
            checklistInfo = objectMapper.writeValueAsString(checklistSimulationDTO.getSimulationData());
        } catch (JsonProcessingException e) {
            throw new BusinessError("");
        }
        checklistSimulation.setSimulationData(checklistInfo);
        checklistSimulation.setOrgId(orgId);
        checklistSimulation.setProjectId(projectId);

        return checklistSimulationRepository.save(checklistSimulation);
    }

    /**
     * 更新模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param simulationId           模拟检查单ID
     * @param checklistSimulationDTO 模拟检查单数据
     * @return 更新完成的模拟检查单
     */
    @Override
    public ChecklistSimulation updateSimulation(Long orgId, Long projectId,
                                                Long simulationId, ChecklistSimulationDTO checklistSimulationDTO) {

        ChecklistSimulation checklistSimulation = BeanUtils.copyProperties(
            checklistSimulationDTO, new ChecklistSimulation());

        checklistSimulation.setId(simulationId);
        String checklistInfo;

        try {
            checklistInfo = objectMapper.writeValueAsString(checklistSimulationDTO.getSimulationData());
        } catch (JsonProcessingException e) {
            throw new BusinessError("");
        }

        checklistSimulation.setSimulationData(checklistInfo);
        checklistSimulation.setOrgId(orgId);
        checklistSimulation.setProjectId(projectId);

        return checklistSimulationRepository.save(checklistSimulation);
    }

    /**
     * 更新模拟检查单生成的文件路径
     *
     * @param simulationId  模拟检查单ID
     * @param generatedFile 检查单生成的文件路径
     * @return 更新模拟检查单生成的文件路径
     */
    public ChecklistSimulation updateGeneratedFile(Long simulationId, Long generatedFile) {

        ChecklistSimulation checklistSimulation =
            checklistSimulationRepository.findById(simulationId).orElse(null);

        checklistSimulation.setGeneratedFile(generatedFile);

        return checklistSimulationRepository.save(checklistSimulation);
    }

    /**
     * 删除模拟检查单
     *
     * @param simulationId 模拟检查单ID
     */
    @Override
    public void deleteSimulation(Long simulationId) {
        checklistSimulationRepository.deleteById(simulationId);
    }

    /**
     * 组装检查单报表数据
     *
     * @param simulationId 模拟检查单ID
     * @return 检查单报表数据
     */
    @Override
    public ChecklistReportDTO assembleSimulationReportData(Long simulationId) {

        // 获取模拟检查单信息
        ChecklistSimulation checklistSimulation =
            checklistSimulationRepository.findById(simulationId).orElse(null);

        if (checklistSimulation == null) {
            return null;
        }

        // 获取检查单基本信息
        Long checklistId = checklistSimulation.getChecklistId();

        Checklist checklist =
            checklistRepository.findById(checklistId).orElse(null);

        if (checklist == null) {
            return null;
        }

        // 获取模板信息
        List<Template> templates = reportTemplateRepository.findByIdIn(new Long[]{
            checklist.getHeaderTemplateId(),
            checklist.getSignatureTemplateId()
        });

        // 模板未找到时处理结束
        if (templates == null || templates.size() == 0) {
            return null;
        }

        // 获取检查项获取数据
        List<ChecklistItem> checklistItems = checklistItemRepository.findAllByChecklistIdOrderByItemNo(checklistId);

        // 组装检查单基本信息
        ChecklistInfoDTO checklistInfoDTO;
        try {
            checklistInfoDTO = objectMapper.readValue(checklistSimulation.getSimulationData(), ChecklistInfoDTO.class);
        } catch (IOException e) {
            throw new BusinessError("");
        }

        // 组装数据
        return super.assembleReportData(checklist, templates, checklistItems, checklistInfoDTO);
    }
}
