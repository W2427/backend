package com.ose.tasks.domain.model.service.bpm;

import java.util.List;

import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ProcessStageDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;

/**
 * 工序阶段管理service接口
 */
public interface ProcessStageInterface extends EntityInterface {

    /**
     * 创建新的工序阶段
     *
     * @param processStageDTO 工序阶段信息
     * @param projectId       项目ID
     * @param orgId           组织ID
     * @return
     */
    BpmProcessStage create(ProcessStageDTO processStageDTO, Long projectId, Long orgId);

    /**
     * 查询工序阶段列表
     *
     * @param page
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    Page<BpmProcessStage> getList(PageDTO page, Long projectId, Long orgId);

    /**
     * 删除指定的工序阶段
     *
     * @param id        工序阶段id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    boolean delete(Long id, Long projectId, Long orgId);

    /**
     * 修改工序阶段
     *
     * @param id              工序阶段id
     * @param processStageDTO 工序阶段信息
     * @param projectId       项目ID
     * @param orgId           组织ID
     * @return
     */
    BpmProcessStage modify(Long id, ProcessStageDTO processStageDTO, Long projectId, Long orgId);

    /**
     * 获取工序阶段信息
     *
     * @param id        工序阶段id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    BpmProcessStage getStage(Long id, Long projectId, Long orgId);

    /**
     * 根据工序阶段id查询工序列表
     *
     * @param id
     * @return
     */
    List<BpmProcess> getProcessesByStageId(Long id);

}
