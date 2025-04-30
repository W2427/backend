package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateEntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.RelationReturnEnum;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;

/**
 * 实体管理service接口
 */
public interface DrawingCoordinateInterface extends EntityInterface {

    /**
     * 根据实体类型ID查询条形码坐标
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    Page<DrawingCoordinate> getList(
        Long orgId,
        Long projectId,
        DrawingCoordinateCriteriaDTO criteriaDTO
    );

    /**
     * 创建坐标
     *
     * @param coordinateDTO 工序信息
     * @param projectId  项目id
     * @param orgId      组织id
     */
    DrawingCoordinate create(
        Long projectId,
        Long orgId,
        DrawingCoordinateDTO coordinateDTO
    );


    /**
     * 修改条形码坐标
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param id
     * @param coordinateDTO
     * @return
     */
    DrawingCoordinate update(
        Long orgId,
        Long projectId,
        Long id,
        DrawingCoordinateDTO coordinateDTO
    );

    /**
     * 删除坐标
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param id
     */
    void deleteCoordinate(
        Long orgId,
        Long projectId,
        Long id
    );

    /**
     * 获取工序对应的实体类型列表
     *
     * @param id
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    Page<BpmEntitySubType> getEntitySubTypeList(Long id, Long projectId, Long orgId, DrawingCoordinateEntitySubTypeCriteriaDTO criteriaDTO);

    /**
     * 添加实体类型
     *
     * @param coordinateId    坐标id
     * @param entitySubTypeId  实体子类型id
     * @param projectId 项目id
     * @param orgId     组织id
     * @return
     */
    RelationReturnEnum addEntitySubType(Long coordinateId, Long entitySubTypeId, Long projectId, Long orgId);

    /**
     * 删除实体类型
     *
     * @param coordinateId    坐标id
     * @param entitySubTypeId  实体子类型id
     * @param projectId 项目ID
     * @param orgId     组织id
     * @return
     */
    RelationReturnEnum deleteEntitySubType(Long coordinateId, Long entitySubTypeId, Long projectId, Long orgId);

    /**
     * 查询坐标详细信息
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    DrawingCoordinate get(Long id, Long projectId, Long orgId);

    File saveDownloadFile(Long orgId, Long projectId, Long drawingCoordinateId, Long operatorId);

    List<BpmEntitySubType> getEntitySubTypeByDrawingCoordinateId(Long id, Long projectId, Long orgId);
}
