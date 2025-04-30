package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.MmHeatBatchNoEntity;
import com.ose.material.entity.MmImportBatchTask;
import org.springframework.data.domain.Page;

/**
 * 炉批号接口
 */
public interface HeatBatchNoInterface {

    /**
     * 获取炉批号列表。
     */
    Page<MmHeatBatchNoEntity> search(
        Long orgId,
        Long projectId,
        HeatBatchNoSearchDTO heatBatchNoSearchDTO
    );

    /**
     * 创建炉批号。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchNoCreateDTO
     * @return
     */
    MmHeatBatchNoEntity create(
        Long orgId,
        Long projectId,
        HeatBatchNoCreateDTO heatBatchNoCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新炉批号。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchNoCreateDTO
     * @return
     */
    MmHeatBatchNoEntity update(
        Long orgId,
        Long projectId,
        Long heatBatchId,
        HeatBatchNoCreateDTO heatBatchNoCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 炉批号详情。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchId
     * @return
     */
    MmHeatBatchNoEntity detail(
        Long orgId,
        Long projectId,
        Long heatBatchId
    );

    /**
     * 删除炉批号。
     *
     * @param orgId
     * @param projectId
     * @param heatBatchId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long heatBatchId,
        ContextDTO contextDTO
    );

    /**
     * 导入炉批号详情。
     *
     * @param orgId
     * @param projectId
     * @param operator
     * @param batchTask
     * @param importDTO
     * @return
     */
    MmImportBatchResultDTO importHeatBatch(Long orgId,
                                           Long projectId,
                                           OperatorDTO operator,
                                           MmImportBatchTask batchTask,
                                           MmImportBatchTaskImportDTO importDTO
    );

}
