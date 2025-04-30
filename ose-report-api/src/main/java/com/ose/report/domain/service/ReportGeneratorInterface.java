package com.ose.report.domain.service;

import java.io.OutputStream;

import com.ose.report.dto.report.BaseReportDTO;
import com.ose.report.exception.ReportGeneratingException;

/**
 * 报表生成器接口
 */
public interface ReportGeneratorInterface<T extends BaseReportDTO> {

    /**
     * 根据报表Mapping内容生成报表
     *
     * @param reportContent 报表Mapping内容
     * @return OutputStream 报表输出流
     * @throws ReportGeneratingException 动态报表构建器创建异常
     */
    OutputStream generateReport(T reportContent) throws ReportGeneratingException;
}
