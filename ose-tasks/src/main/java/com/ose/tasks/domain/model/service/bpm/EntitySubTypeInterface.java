package com.ose.tasks.domain.model.service.bpm;

import java.util.List;

import com.ose.service.EntityInterface;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import org.springframework.data.domain.Page;

import com.ose.tasks.dto.bpm.BatchAddRelationDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeProcessCriteriaDTO;
import com.ose.tasks.dto.bpm.SortDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityTypeProcessRelation;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.vo.RelationReturnEnum;

/**
 * 实体管理service接口
 */
public interface EntitySubTypeInterface extends EntityInterface {

    /**
     * 创建实体
     */
    BpmEntitySubType create(EntitySubTypeDTO entityCategoryDTO, Long projectId, Long orgId);

    /**
     * 获取实体列表
     *
     * @param page      分页信息
     * @param projectId 项目id
     * @param orgId     组织id
     */
    Page<BpmEntitySubType> getList(EntitySubTypeCriteriaDTO page, Long projectId, Long orgId);

    /**
     * 删除实体
     *
     * @param id        实体id
     * @param projectId 项目id
     * @param orgId     组织id
     */
    boolean delete(Long id, Long projectId, Long orgId);

    /**
     * 编辑实体
     */
    BpmEntitySubType modify(Long id, EntitySubTypeDTO entityCategoryDTO, Long projectId, Long orgId);

    /**
     * 获取实体对应的工序列表
     *
     * @param id          实体id
     * @param projectId   项目id
     * @param orgId       组织id
     * @param criteriaDTO
     */
    Page<BpmEntityTypeProcessRelation> getProcessList(Long id, Long projectId, Long orgId, EntitySubTypeProcessCriteriaDTO criteriaDTO);

    /**
     * 添加工序
     *
     * @param entityId  实体id
     * @param projectId 项目id
     * @param orgId     组织id
     */
    RelationReturnEnum addProcess(Long entityId, Long stepId, Long projectId, Long orgId);

    /**
     * 删除工序
     *
     * @param entityId  实体id
     * @param projectId 项目id
     * @param orgId     组织id
     */
    RelationReturnEnum deleteProcess(Long entityId, Long stepId, Long projectId, Long orgId);

    /**
     * 查询实体详细信息
     */
    BpmEntitySubType getEntity(Long id, Long projectId, Long orgId);

    BpmEntitySubType getEntitySubType(Long projectId, String entitySubType);

    BpmEntitySubType getEntitySubTypeByWbs(Long projectId, String typeName, String entitySubType);

    /**
     * 获取全部工序
     */
    List<BpmProcess> getProcessList(Long projectId, Long orgId);

    /**
     * 查询实体对应的工序关系
     *
     * @param id
     * @return
     */
    List<BpmEntityTypeProcessRelation> getRelationByEntitySubTypeId(Long id);

    /**
     * 批量添加工序
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dto
     * @return
     */
    boolean addProcessBatch(Long orgId, Long projectId, Long id, BatchAddRelationDTO dto);

    /**
     * 获取实体对应的工序阶段列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    List<BpmProcessStage> getProcessStageList(Long orgId, Long projectId, Long id);

    /**
     * 根据英文名称查询实体类型
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param nameEn
     * @return
     */
    BpmEntitySubType findByOrgIdAndProjectIdAndNameEn(Long orgId, Long projectId, String nameEn);

    /**
     * 批量排序
     *
     * @param sortDTOs
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    boolean sort(List<SortDTO> sortDTOs, Long projectId, Long orgId);

    List<BpmEntityType> getEntityTypeList(Long projectId, Long orgId, String type);

    List<DrawingCoordinate> getBarCodeCoordinateListByEntitySubType(Long orgId, Long projectId, String entitySubType);
    /**
     * 根据实体类型ID查询电子签名坐标
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    List<DrawingSignatureCoordinate> getSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long id
    );
    /**
     * 根据实体类型ID查询电子签名坐标
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    Page<DrawingCoordinate> getBarCodeCoordinate(
        Long orgId,
        Long projectId,
        Long id
    );

    /**
     * 添加电子签名坐标
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param id
     * @param coordinateDTO
     * @return
     */
    DrawingSignatureCoordinate addSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long id,
        DrawingSignatureCoordinateDTO coordinateDTO
    );

    /**
     * 修改电子签名坐标
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param entitySubTypeId
     * @param id
     * @param coordinateDTO
     * @return
     */
    DrawingSignatureCoordinate updateSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id,
        DrawingSignatureCoordinateDTO coordinateDTO
    );

    /**
     * 修改条形码坐标
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param entitySubTypeId
     * @param id
     * @param coordinateDTO
     * @return
     */
    DrawingCoordinate updateBarCodeCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id,
        DrawingCoordinateDTO coordinateDTO
    );

    /**
     * 删除电子签名坐标
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param entitySubTypeId
     * @param id
     */
    void deleteSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id
    );

    /**
     * 删除坐标
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param entitySubTypeId
     * @param id
     */
    void deleteCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id
    );
}
