package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.BpmActTaskConfigDTO;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;
import org.springframework.data.domain.Page;

/**
 * 任务代理设置库
 */
public interface BpmActTaskConfigRepositoryCustom {


    /**
     * 查找任务代理设置清单。
     *
     * @param bpmActTaskConfigDTO 查询参数
     * @return
     */
    Page<BpmActTaskConfig> search(Long orgId, Long projectId, BpmActTaskConfigDTO bpmActTaskConfigDTO, PageDTO pageDTO);
}
