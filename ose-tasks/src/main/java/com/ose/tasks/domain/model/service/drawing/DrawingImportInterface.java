package com.ose.tasks.domain.model.service.drawing;

import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import org.springframework.data.domain.Page;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BatchTaskBasic;

/**
 * service接口
 */
public interface DrawingImportInterface {

    /**
     * 导入管道生产设计图纸清单
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param operator   用户
     * @param batchTask  批处理
     * @param importDTO  导入的DTO
     * @param discipline 专业 discipline
     * @return 返回批处理DTO
     */
    BatchResultDTO importDrawingList(Long orgId, Long projectId, OperatorDTO operator, BatchTask batchTask,
                                     DrawingImportDTO importDTO, String discipline);

    /**
     * 查询导入记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchTaskCriteriaDTO
     * @param page      分页
     * @return 返回批处理DTO
     */
    Page<BatchTaskBasic> search(Long orgId, Long projectId, BatchTaskCriteriaDTO batchTaskCriteriaDTO, PageDTO page);

    /**
     * 根据batchTaskId更新importFileId
     *
     * @param batchTaskId 批处理ID
     * @param fileId      上传文件的ID
     * @return 是否成功
     */
    boolean updateFileId(Long batchTaskId, Long fileId);


}
