package com.ose.report.domain.service.impl;

import com.ose.report.domain.repository.ChecklistItemRepository;
import com.ose.report.domain.repository.ChecklistRepository;
import com.ose.report.domain.repository.ReportTemplateRepository;
import com.ose.report.domain.service.ReportGeneratorInterface;
import com.ose.report.dto.report.BaseReportDTO;
import com.ose.report.exception.ReportGeneratingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

/**
 * 检查单服务
 */
@Component
public class ReportGeneratorService implements ReportGeneratorInterface {

    /**
     * 检查单 操作仓库
     */
    private final ChecklistRepository checklistRepository;

    /**
     * 检查项 操作仓库
     */
    private final ChecklistItemRepository checklistItemRepository;

    /**
     * 报表模板 操作仓库
     */
    private final ReportTemplateRepository reportTemplateRepository;

    /**
     * 构造方法
     *
     * @param checklistRepository      检查单操作仓库
     * @param checklistItemRepository  检查项操作仓库
     * @param reportTemplateRepository 报表模板操作仓库
     */
    @Autowired
    public ReportGeneratorService(ChecklistRepository checklistRepository,
                                  ChecklistItemRepository checklistItemRepository,
                                  ReportTemplateRepository reportTemplateRepository) {

        this.checklistRepository = checklistRepository;
        this.checklistItemRepository = checklistItemRepository;
        this.reportTemplateRepository = reportTemplateRepository;
    }

    @Override
    public OutputStream generateReport(BaseReportDTO reportContent) throws ReportGeneratingException {
        return null;
    }
}
