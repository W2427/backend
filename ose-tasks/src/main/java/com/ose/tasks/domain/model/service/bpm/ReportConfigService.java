package com.ose.tasks.domain.model.service.bpm;

import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.qc.ReportConfigRepository;
import com.ose.tasks.dto.bpm.ReportConfigCriteriaDTO;
import com.ose.tasks.dto.bpm.ReportConfigDTO;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReportConfigService implements ReportConfigInterface {

    /**
     * 报告配置 操作仓库
     */
    private final ReportConfigRepository reportConfigRepository;

    /**
     * 构造方法
     *
     * @param reportConfigRepository
     */
    @Autowired
    public ReportConfigService(ReportConfigRepository reportConfigRepository) {
        this.reportConfigRepository = reportConfigRepository;
    }

    /**
     * 获取报告配置列表
     *
     * @param pagination 分页信息
     * @return 报告配置列表
     */
    @Override
    public Page<ReportConfig> getList(ReportConfigCriteriaDTO pagination, Long projectId, Long orgId, Long processId) {
        return reportConfigRepository.findByStatusAndProjectIdAndOrgIdANDProcessId(EntityStatus.ACTIVE, projectId, orgId, processId, pagination.toPageable());
    }

    /**
     * 创建报告配置。
     *
     * @param reportConfigDTO 报告配置信息
     * @param projectId  报告配置分类id
     * @return 报告配置
     */
    @Override
    public ReportConfig create(ReportConfigDTO reportConfigDTO, Long projectId, Long orgId) {



        ReportConfig reportConfig = BeanUtils.copyProperties(reportConfigDTO, new ReportConfig());
        if (reportConfig.getReportCode() == null || "".equals(reportConfig.getReportCode())) {
            throw new BusinessError("", "business-error: ReportSubType is  not null.");
        }
        if (existsByReportCode(reportConfig.getReportCode(), projectId, orgId, reportConfig.getProcessId(), EntityStatus.ACTIVE)) {
            throw new BusinessError("", "business-error: reportCode ALREADY EXISTS.");
        }
        if (reportConfig.getReportSubType() == null) {
            throw new BusinessError("", "business-error: ReportSubType is  not null.");
        }
        if (reportConfig.getReportType() == null) {
            throw new BusinessError("", "business-error: getReportType is  not null.");
        }
        if (reportConfig.getReportGenerateClass() == null || "".equals(reportConfig.getReportGenerateClass())) {
            throw new BusinessError("", "business-error: getReportGenerateClass is  not null.");
        }
        if (reportConfig.getTemplateName() == null || "".equals(reportConfig.getTemplateName())) {
            throw new BusinessError("", "business-error: templateName is  not null.");
        }
        reportConfig.setProjectId(projectId);
        reportConfig.setOrgId(orgId);
        reportConfig.setCreatedAt();
        reportConfig.setStatus(EntityStatus.ACTIVE);


        reportConfigRepository.save(reportConfig);
        return reportConfig;
    }

    /**
     * 编辑报告配置。
     *
     * @param reportConfigDTO 报告配置信息
     * @param projectId  报告配置id
     * @return 工序
     */
    @Override
    public ReportConfig modify(Long id, ReportConfigDTO reportConfigDTO, Long projectId, Long orgId) {
        Optional<ReportConfig> result = reportConfigRepository.findById(id);
        if (result.isPresent()) {
            ReportConfig reportConfig = result.get();
            if (reportConfig.getReportSubType() == null) {
                throw new BusinessError("", "business-error: ReportSubType is  not null.");
            }
            if (reportConfig.getReportType() == null) {
                throw new BusinessError("", "business-error: getReportType is  not null.");
            }
            if (reportConfig.getReportGenerateClass() == null || "".equals(reportConfig.getReportGenerateClass())) {
                throw new BusinessError("", "business-error: getReportGenerateClass is  not null.");
            }
            reportConfig.setReportName(reportConfigDTO.getReportName());
            reportConfig.setReportGenerateClass(reportConfigDTO.getReportGenerateClass());
            reportConfig.setReportCode(reportConfigDTO.getReportCode());
            reportConfig.setReportSubType(reportConfigDTO.getReportSubType());
            reportConfig.setReportType(reportConfigDTO.getReportType());
            reportConfig.setProcessId(reportConfigDTO.getProcessId());
            reportConfig.setCover(reportConfigDTO.getCover());
            reportConfig.setTemplateName(reportConfigDTO.getTemplateName());
            reportConfig.setLastModifiedAt();

            return reportConfigRepository.save(reportConfig);
        }
        return null;
    }

    /**
     * 删除报告配置。
     */
    @Override
    public boolean delete(Long id, Long projectId, Long orgId) {
        Optional<ReportConfig> reportConfigOptional = reportConfigRepository.findById(id);
        if (reportConfigOptional.isPresent()) {
            ReportConfig reportConfig = reportConfigOptional.get();
            reportConfig.setStatus(EntityStatus.DELETED);
            reportConfig.setLastModifiedAt();
            reportConfigRepository.save(reportConfig);
            return true;
        }
        return false;
    }

    /**
     * 查询报告配置详细信息。
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    @Override
    public ReportConfig get(Long id, Long projectId, Long orgId) {
        Optional<ReportConfig> reportConfig = reportConfigRepository.findById(id);
        if (reportConfig.isPresent()) {
            ReportConfig result = reportConfig.get();
            return result;
        }
        return null;
    }

    /**
     * 判断报告配置是否已经存在。
     *
     *  @param reportCode        节点编号
     *  @param projectId 项目 ID
     *  @param orgId 组织 ID
     *  @param processId 工序 ID
     *  @param status 状态
     *  @return 报告配置 是否存在  存在 ：true；不存在：false；
     */
    @Override
    public boolean existsByReportCode(String reportCode, Long projectId, Long orgId, Long processId, EntityStatus status) {

        if (reportConfigRepository.existsByReportCodeAndProjectIdAndOrgIdAndProcessIdAndStatus(reportCode, projectId, orgId, processId, status)) {
            return true;
        }

        return false;
    }
}
