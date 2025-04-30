package com.ose.tasks.domain.model.service.drawing;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.DrawingProjectDeptIDCRelation;

import java.util.List;

/**
 * service接口
 */
public interface DrawingProjectDeptIDCRelationInterface {

    /**
     * 创建
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param userId     用户ID
     * @param dto
     * @return
     */
    DrawingProjectDeptIDCRelation create(Long orgId, Long projectId, Long userId, DrawingProjectDeptIDCRelationDTO dto);

    /**
     * 修改
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id         id
     * @param userId     用户ID
     * @param dto
     * @return Drawing
     */
    DrawingProjectDeptIDCRelation modify(Long orgId, Long projectId, Long id, Long userId, DrawingProjectDeptIDCRelationDTO dto);


    List<DrawingProjectDeptIDCRelation> getList(Long orgId, Long projectId, DrawingProjectDeptIDCRelationSearchDTO dto);


    List<DrawingProjectDeptIDCRelationDTO> getListByDrawingId(Long orgId, Long projectId, Long drawingId);


    /**
     * 删除
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        ID
     * @return boolean
     */
    boolean delete(Long orgId, Long projectId, Long id, OperatorDTO operatorDTO);
}
