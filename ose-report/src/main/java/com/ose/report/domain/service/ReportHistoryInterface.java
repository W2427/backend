package com.ose.report.domain.service;

import com.ose.dto.OperatorDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.service.EntityInterface;

/**
 * 检查单服务接口
 */
public interface ReportHistoryInterface extends EntityInterface {

    /**
     * 更新报告创建历史记录。
     *
     * @param operator      最后更新者信息
     * @param reportHistory 报告历史记录
     * @return 报告历史记录
     */
    ReportHistory save(OperatorDTO operator, ReportHistory reportHistory);

}
