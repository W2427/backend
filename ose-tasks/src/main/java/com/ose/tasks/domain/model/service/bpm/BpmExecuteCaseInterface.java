package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseCreateDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 任务处理case接口
 */
public interface BpmExecuteCaseInterface {

    /**
     * 查找任务处理case列表。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @param pageDTO           分页参数
     * @return
     */
    Page<BpmExecuteCase> list(BpmExecuteCaseDTO bpmExecuteCaseDTO, PageDTO pageDTO);

    /**
     * 查找特殊任务处理case。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    List<BpmExecuteCase> search(BpmExecuteCaseDTO bpmExecuteCaseDTO);

    /**
     * 添加任务处理case。
     *
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @param operatorDTO             操作人员
     * @return
     */
    BpmExecuteCase add(BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO, OperatorDTO operatorDTO);

    /**
     * 查找任务处理case详情。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    BpmExecuteCase detail(Long orgId, Long projectId, Long bpmExecuteCaseId);

    /**
     * 编辑任务处理case详情。
     *
     * @param bpmExecuteCaseId        任务处理caseid
     * @param bpmExecuteCaseCreateDTO 数据传输对象
     * @param operatorDTO             操作人
     * @return
     */
    BpmExecuteCase edit(Long bpmExecuteCaseId, BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO, OperatorDTO operatorDTO);

    /**
     * 删除任务处理case详情。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @param operatorDTO      操作人
     * @return
     */
    BpmExecuteCase delete(Long orgId, Long projectId, Long bpmExecuteCaseId, OperatorDTO operatorDTO);
}
