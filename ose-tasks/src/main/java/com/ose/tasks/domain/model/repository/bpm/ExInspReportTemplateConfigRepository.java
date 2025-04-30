package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmExInspReportTemplateConfig;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 外检报告与模版号对应关系 CRUD 操作接口。
 */
@Transactional
public interface ExInspReportTemplateConfigRepository
    extends PagingAndSortingRepository<BpmExInspReportTemplateConfig, Long> {

    List<BpmExInspReportTemplateConfig> findByProjectIdAndReportNameAndStatus(
        Long projectId,
        String reportName,
        EntityStatus status);

}
