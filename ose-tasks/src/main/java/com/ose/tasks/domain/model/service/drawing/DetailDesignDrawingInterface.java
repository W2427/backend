package com.ose.tasks.domain.model.service.drawing;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DetailDesignDrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.drawing.DetailDesignDrawing;
import com.ose.tasks.entity.drawing.DetailDesignDrawingDetail;

/**
 * 详细设计图纸service接口。
 */
public interface DetailDesignDrawingInterface {

    /**
     * 根据批处理id更新导入文件id。
     *
     * @param batchTaskId 批处理任务id
     * @param fileId      文件id
     * @return
     */
    boolean updateFileId(Long batchTaskId, Long fileId);

    /**
     * 批量导入详细设计清单。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param operator  操作者
     * @param batchTask 批处理任务
     * @param importDTO 导入DTO
     * @return
     */
    BatchResultDTO importDetailDesign(Long orgId, Long projectId, OperatorDTO operator, BatchTask batchTask,
                                      DrawingImportDTO importDTO);

    /**
     * 查询详细设计清单列表
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return
     */
    Page<DetailDesignDrawing> searchDetailDesignList(Long orgId, Long projectId, PageDTO page,
                                                     DetailDesignDrawingCriteriaDTO criteriaDTO);

    /**
     * 查询详细设计清单明细
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        详细设计图纸id
     * @return
     */
    List<DetailDesignDrawingDetail> searchDetailDesignListDetail(Long orgId, Long projectId, Long id);

}
