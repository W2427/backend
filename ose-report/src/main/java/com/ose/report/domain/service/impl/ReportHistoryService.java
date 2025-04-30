package com.ose.report.domain.service.impl;

import com.ose.dto.OperatorDTO;
import com.ose.report.domain.repository.ReportHistoryRepository;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.entity.ReportHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报告服务
 */
@Component
public class ReportHistoryService implements ReportHistoryInterface {

    // 报告创建历史记录操作仓库
    private final ReportHistoryRepository reportHistoryRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ReportHistoryService(ReportHistoryRepository reportHistoryRepository) {
        this.reportHistoryRepository = reportHistoryRepository;
    }

    /**
     * 更新报告创建历史记录。
     *
     * @param operator      最后更新者信息
     * @param reportHistory 报告历史记录
     * @return 报告历史记录
     */
    @Override
    public ReportHistory save(OperatorDTO operator, ReportHistory reportHistory) {
        reportHistory.setLastModifiedAt();
        reportHistory.setLastModifiedBy(operator.getId());
        return reportHistoryRepository.save(reportHistory);
    }

}
