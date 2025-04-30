package com.ose.report.domain.service.impl;

import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.report.domain.repository.ReportTemplateRepository;
import com.ose.report.domain.service.ReportTemplateInterface;
import com.ose.report.dto.TemplateDTO;
import com.ose.report.entity.Checklist;
import com.ose.report.entity.Template;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 报表模板服务
 */
@Component
public class ReportTemplateService implements ReportTemplateInterface {

    /**
     * 报表模板 操作仓库
     */
    private final ReportTemplateRepository reportTemplateRepository;

    /**
     * 构造方法
     *
     * @param reportTemplateRepository 报表模板 操作仓库
     */
    @Autowired
    public ReportTemplateService(ReportTemplateRepository reportTemplateRepository) {
        this.reportTemplateRepository = reportTemplateRepository;
    }

    /**
     * 检查报表模板名是否可用
     *
     * @param templateName 报表模板名
     * @return 报表模板名是否可用
     */
    @Override
    public Boolean isNameAvailable(String templateName) {
        return reportTemplateRepository.existsByName(templateName);
    }

    /**
     * 创建报表模板
     *
     * @param templateDTO 模板数据
     * @return 创建的报表模板
     */
    @Override
    public Template create(TemplateDTO templateDTO) {

        Template template = BeanUtils.copyProperties(templateDTO, new Template());
        return reportTemplateRepository.save(template);
    }

    /**
     * 根据报表分类和显示位置查询模板
     *
     * @param domain     模板分类（检查单，周报，......）
     * @param position   模板位置（Head，Title，Detail，......）
     * @param pagination 分页参数
     * @return 模板列表
     */
    @Override
    public Page<Template> search(Domain domain, Position position, PageDTO pagination) {

        return reportTemplateRepository.findByDomainAndPosition(domain, position, pagination.toPageable());
    }

    /**
     * 引用数据处理
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>      引用数据类型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(Map<Long, Object> included,
                                                             List<T> entities) {

        List<Long> templateIdList = new ArrayList<>();

        // 取得所有引用模板的 ID
        for (T entity : entities) {

            // 来源检查单
            if (entity instanceof Checklist) {
                Checklist checklist = (Checklist) entity;

                // 表头模板
                Long templateId = checklist.getHeaderTemplateId();

                if (templateId != null && !templateIdList.contains(templateId)) {
                    templateIdList.add(templateId);
                }

                // 签字栏模板
                templateId = checklist.getSignatureTemplateId();

                if (templateId != null && !templateIdList.contains(templateId)) {
                    templateIdList.add(templateId);
                }
            }
        }

        // 查询引用的检查单的基本信息
        List<Template> templates =
            (List<Template>) reportTemplateRepository.findAllById(templateIdList);

        // 将应用目标用户信息设置到 included 字典中
        for (Template template : templates) {
            included.put(template.getId(), template);
        }

        return included;
    }
}
