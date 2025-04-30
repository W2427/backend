package com.ose.tasks.domain.model.repository.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.SubDrawingCriteriaDTO;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.vo.EntityStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


/**
 * Custom
 */
@Transactional
public interface SubDrawingRepositoryCustom {

    Page<SubDrawing> search(Long orgId, Long projectId, Long drawingId, PageDTO page,
                            SubDrawingCriteriaDTO criteriaDTO);

    List<SubDrawing> findByDrawingIdAndStatusAndFilePathNotNull(Long drawingId, EntityStatus status);

    /**
     * 查询图纸下的最新运行中的子图纸。
     *
     * @param id
     * @param status
     * @param revOrder
     * @return
     */
    List<SubDrawing> findByDrawingIdAndStatusAndRevOrderLE(Long id, EntityStatus status, Integer revOrder);

    /**
     * 查询图纸下的最新子图纸。
     *
     * @param id
     * @param revOrder
     * @return
     */
    List<SubDrawing> findByDrawingIdAndRevOrderLE(Long id, Integer revOrder);

    Page<SubDrawing> search(Long orgId, Long projectId, Long drawingId,
                            SubDrawingCriteriaDTO criteriaDTO, List<Long> subIds, Pageable pageable);

    /**
     * 查找最新图集下子图纸
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    Page<SubDrawing> searchLatestSubDrawing(Long orgId, Long projectId, Long drawingId,
                                            SubDrawingCriteriaDTO criteriaDTO, Pageable pageable);

    /**
     * 查找所有图集下子图纸
     *
     * @param orgId
     * @param projectId
     * @param criteriaDTO
     * @param pageable
     * @return
     */
    Page<SubDrawing> searchAllSubDrawing(Long orgId, Long projectId,
                                            SubDrawingCriteriaDTO criteriaDTO, Pageable pageable);

    List<SubDrawing> findByDrawingIdAndDrawingVersionAndStatus(
        Long orgId,
        Long projectId,
        Long drawingId,
        String drawingVersion,
        EntityStatus status);
}
