package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 任务处理流程case库
 */
public interface BpmExecuteCaseRepositoryCustom {

    /**
     * 任务处理case分页查询。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @param pageable          分页参数
     * @return
     */
    Page<BpmExecuteCase> list(BpmExecuteCaseDTO bpmExecuteCaseDTO, Pageable pageable);


    /**
     * 查找特殊的任务处理case。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    List<BpmExecuteCase> search(BpmExecuteCaseDTO bpmExecuteCaseDTO);
}
