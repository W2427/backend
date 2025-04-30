package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.entity.BatchTaskBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 批处理任务状态记录查询操作接口。
 */
public interface BatchTaskBasicRepositoryCustom {

    /**
     * 查询批处理任务。
     *
     * @param companyId   公司 ID
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页设置
     * @return 批处理任务查询结果分页数据
     */
    Page<BatchTaskBasic> search(
        Long companyId,
        Long orgId,
        Long projectId,
        BatchTaskCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 查询图纸导入任务。
     *
     * @param companyId 公司 ID
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchTaskCriteriaDTO
     * @param pageable  分页参数
     * @return 图纸导入任务记录
     */
    Page<BatchTaskBasic> searchDrawing(
        Long companyId,
        Long orgId,
        Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        Pageable pageable
    );

}
