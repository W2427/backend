package com.ose.tasks.domain.model.service.bpm;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.DrawingCoordinateRepository;
import com.ose.tasks.domain.model.repository.DrawingSignatureCoordinateRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.ose.tasks.dto.bpm.BatchAddRelationDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeDTO;
import com.ose.tasks.dto.bpm.EntitySubTypeProcessCriteriaDTO;
import com.ose.tasks.dto.bpm.SortDTO;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.tasks.vo.RelationReturnEnum;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;

@Component
public class EntitySubTypeService extends StringRedisService implements EntitySubTypeInterface {
    private final static Logger logger = LoggerFactory.getLogger(EntitySubTypeService.class);

    /**
     * 工序  操作仓库
     */
    private final BpmProcessRepository processRepository;
    /**
     * 实体类型-工序  操作仓库
     */
    private final EntitySubTypeProcessRelationRepository relationRepository;
    /**
     * 实体类型 操作仓库
     */
    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final BpmEntityTypeRepository entityTypeRepository;

    private final BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository;

    private final EntitySubTypeCoordinateRelationRepository coordinateRelationRepository;

    private final DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository;

    private final DrawingCoordinateRepository drawingCoordinateRepository;

    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     */
    @Autowired
    public EntitySubTypeService(
        BpmProcessRepository processRepository,
        EntitySubTypeProcessRelationRepository relationRepository,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        BpmEntityTypeRepository entityTypeRepository,
        BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository,
        DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository,
        DrawingCoordinateRepository drawingCoordinateRepository,
        EntitySubTypeCoordinateRelationRepository coordinateRelationRepository,
        StringRedisTemplate stringRedisTemplate,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI) {
        super(stringRedisTemplate);
        this.processRepository = processRepository;
        this.relationRepository = relationRepository;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.bpmEntitySubTypeCheckListRepository = bpmEntitySubTypeCheckListRepository;
        this.drawingSignatureCoordinateRepository = drawingSignatureCoordinateRepository;
        this.drawingCoordinateRepository = drawingCoordinateRepository;
        this.coordinateRelationRepository = coordinateRelationRepository;
        this.uploadFeignAPI = uploadFeignAPI;
    }


    /**
     * 创建实体
     *
     * @return 创建的实体
     */
    @Override
    public BpmEntitySubType create(EntitySubTypeDTO entityCategoryDTO, Long projectId, Long orgId) {

        long count = entitySubTypeRepository.count();

        BpmEntitySubType entity = BeanUtils.copyProperties(entityCategoryDTO, new BpmEntitySubType());

        Optional<BpmEntityType> typeOp = entityTypeRepository.findById(entityCategoryDTO.getEntityTypeId());
        if (typeOp.isPresent()) {
            entity.setEntityType(typeOp.get());
        }

        entity.setSubDrawingFlg(entityCategoryDTO.getSubDrawingFlg());

        entity.setProjectId(projectId);
        entity.setOrgId(orgId);
        entity.setCreatedAt();
        entity.setStatus(EntityStatus.ACTIVE);
        entity.setLastModifiedAt();
        entity.setOrderNo((int) (count + 1));
        entitySubTypeRepository.save(entity);

        if (entityCategoryDTO.getCheckListFileName() != null
            && !entityCategoryDTO.getCheckListFileName().isEmpty()) {
            for (String tempName : entityCategoryDTO.getCheckListFileName()) {
                logger.error("实体类型1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), tempName,
                    new FilePostDTO());
                logger.error("实体类型1 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {

                    BpmEntitySubTypeCheckList checkList = new BpmEntitySubTypeCheckList();
                    checkList.setCreatedAt();
                    checkList.setStatus(EntityStatus.ACTIVE);
                    checkList.setFileId(LongUtils.parseLong(fileEs.getId()));
                    checkList.setFileName(fileEs.getName());
                    checkList.setEntitySubTypeId(entity.getId());
                    bpmEntitySubTypeCheckListRepository.save(checkList);
                }
            }
        }

        return entity;
    }

    /**
     * 获取实体类型列表
     *
     * @param page 分页信息
     * @return 实体类型列表
     */
    @Override
    public Page<BpmEntitySubType> getList(EntitySubTypeCriteriaDTO page, Long projectId, Long orgId) {
        return entitySubTypeRepository.getList(orgId, projectId, page);
    }

    /**
     * 删除实体类型
     *
     * @param id 实体类型id
     * @return 操作是否成功
     */
    @Override
    public boolean delete(Long id, Long projectId, Long orgId) {
        Optional<BpmEntitySubType> optionalEntity = entitySubTypeRepository.findById(id);
        if (optionalEntity.isPresent()) {
            BpmEntitySubType entity = optionalEntity.get();
            entity.setStatus(EntityStatus.DELETED);
            entity.setLastModifiedAt();
            entitySubTypeRepository.save(entity);
            return true;
        }
        return false;
    }

    /**
     * 编辑实体类型
     */
    @Override
    public BpmEntitySubType modify(Long id, EntitySubTypeDTO entityCategoryDTO, Long projectId, Long orgId) {
        Optional<BpmEntitySubType> result = entitySubTypeRepository.findById(id);
        if (result.isPresent()) {
            BpmEntitySubType entity = BeanUtils.copyProperties(entityCategoryDTO, result.get());
            Optional<BpmEntityType> typeOp = entityTypeRepository.findById(entityCategoryDTO.getEntityTypeId());
            if (typeOp.isPresent()) {
                entity.setEntityType(typeOp.get());
            }
            entity.setLastModifiedAt();

            List<BpmEntitySubTypeCheckList> listDB = bpmEntitySubTypeCheckListRepository.findByEntitySubTypeId(entity.getId());

            if (entityCategoryDTO.getCheckListFileName() != null
                && !entityCategoryDTO.getCheckListFileName().isEmpty()) {

                for (BpmEntitySubTypeCheckList cl : listDB) {
                    if (!entityCategoryDTO.getCheckListFileName().contains(cl.getFileId() == null ? "" : cl.getFileId().toString())) {
                        bpmEntitySubTypeCheckListRepository.delete(cl);
                    } else {
                        entityCategoryDTO.getCheckListFileName().remove(cl.getFileId() == null ? "" : cl.getFileId().toString());
                    }
                }
                for (String tempName : entityCategoryDTO.getCheckListFileName()) {
                    logger.error("实体类型2 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), tempName,
                        new FilePostDTO());
                    logger.error("实体类型2 保存docs服务->结束");
                    FileES fileEs = fileESResBody.getData();
                    if (fileEs != null && fileEs.getId() != null) {

                        BpmEntitySubTypeCheckList checkList = new BpmEntitySubTypeCheckList();
                        checkList.setCreatedAt();
                        checkList.setStatus(EntityStatus.ACTIVE);
                        checkList.setFileId(LongUtils.parseLong(fileEs.getId()));
                        checkList.setFileName(fileEs.getName());
                        checkList.setEntitySubTypeId(entity.getId());
                        bpmEntitySubTypeCheckListRepository.save(checkList);
                    }
                }
            } else {
                for (BpmEntitySubTypeCheckList cl : listDB) {
                    bpmEntitySubTypeCheckListRepository.delete(cl);
                }
            }

            return entitySubTypeRepository.save(entity);
        }
        return null;
    }


    /**
     * 获取实体类型对应的工序列表
     *
     * @param id 实体类型id
     * @return 工序列表
     */
    @Override
    public Page<BpmEntityTypeProcessRelation> getProcessList(Long id, Long projectId, Long orgId, EntitySubTypeProcessCriteriaDTO criteriaDTO) {
        if (criteriaDTO.getProcessStageId() == null) {
            return relationRepository.findProcessByEntitySubTypeIdAndProjectIdAndOrgId(id, projectId, orgId, criteriaDTO.toPageable());
        }
        return relationRepository.findProcessByEntitySubTypeIdAndProjectIdAndOrgIdAndProcessStageId(id, projectId, orgId, criteriaDTO.getProcessStageId(), criteriaDTO.toPageable());
    }


    /**
     * 添加工序
     *
     * @param entitySubTypeId 实体类型id
     * @param processId       工序id
     * @return 工序列表
     */
    @Override
    public RelationReturnEnum addProcess(Long entitySubTypeId, Long processId, Long projectId, Long orgId) {
        Optional<BpmEntityTypeProcessRelation> result = relationRepository.findByProcessIdAndEntitySubTypeIdAndStatus(processId, entitySubTypeId, EntityStatus.ACTIVE);
        if (!result.isPresent()) {
            BpmEntityTypeProcessRelation map = new BpmEntityTypeProcessRelation();
            map.setProjectId(projectId);
            map.setOrgId(orgId);
            map.setCreatedAt();
            map.setStatus(EntityStatus.ACTIVE);

            Optional<BpmEntitySubType> optionalEntity = entitySubTypeRepository.findById(entitySubTypeId);
            if (optionalEntity.isPresent()) {
                map.setEntitySubType(optionalEntity.get());
            } else {
                return RelationReturnEnum.ENTITY_CATEGORY_NOT_FOUND;
            }
            Optional<BpmProcess> optionalProcess = processRepository.findById(processId);
            if (optionalProcess.isPresent()) {
                map.setProcess(optionalProcess.get());
            } else {
                return RelationReturnEnum.PROCESS_NOT_FOUND;
            }
            if (map.getProcess() != null && map.getEntitySubType() != null) {
                relationRepository.save(map);
                return RelationReturnEnum.SAVE_SUCCESS;
            }
        }
        return RelationReturnEnum.RELATION_EXIST;
    }


    /**
     * 删除工序
     */
    @Override
    public RelationReturnEnum deleteProcess(Long entitySubTypeId, Long processId, Long projectId, Long orgId) {
        Optional<BpmEntityTypeProcessRelation> result = relationRepository.findByProcessIdAndEntitySubTypeIdAndStatus(processId, entitySubTypeId, EntityStatus.ACTIVE);
        if (result.isPresent()) {
            BpmEntityTypeProcessRelation map = result.get();
            map.setStatus(EntityStatus.DELETED);
            map.setLastModifiedAt();
            relationRepository.save(map);
            return RelationReturnEnum.DELETE_SUCCESS;
        }
        return RelationReturnEnum.RELATION_NOT_EXIST;
    }


    /**
     * 查询实体详细信息
     */
    @Override
    public BpmEntitySubType getEntity(Long id, Long projectId, Long orgId) {
        Optional<BpmEntitySubType> optionalEntity = entitySubTypeRepository.findById(id);
        if (optionalEntity.isPresent()) {
            BpmEntitySubType entity = optionalEntity.get();
            List<BpmEntitySubTypeCheckList> checkList = bpmEntitySubTypeCheckListRepository.findByEntitySubTypeId(entity.getId());
            entity.setCheckList(checkList);
            return entity;
        }
        return null;
    }


    /**
     * 获取全部工序
     *
     * @param projectId 项目id
     * @param orgId     组织id
     */
    @Override
    public List<BpmProcess> getProcessList(Long projectId, Long orgId) {
        return processRepository.findByStatusAndProjectIdAndOrgId(EntityStatus.ACTIVE, projectId, orgId);
    }


    /**
     * 查询实体对应的工序关系
     */
    @Override
    public List<BpmEntityTypeProcessRelation> getRelationByEntitySubTypeId(Long id) {
        List<BpmEntityTypeProcessRelation> l = relationRepository.findByEntitySubTypeIdAndStatus(id, EntityStatus.ACTIVE);
        if (l != null && l.size() > 0) {
            return l;
        }
        return null;
    }

    /**
     * 批量添加工序
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dto
     * @return
     */
    @Override
    public boolean addProcessBatch(Long orgId, Long projectId, Long id, BatchAddRelationDTO dto) {
        for (Long processId : dto.getIds()) {
            Optional<BpmEntityTypeProcessRelation> result = relationRepository.findByProcessIdAndEntitySubTypeIdAndStatus(processId, id, EntityStatus.ACTIVE);
            if (!result.isPresent()) {
                BpmEntityTypeProcessRelation map = new BpmEntityTypeProcessRelation();
                map.setProjectId(projectId);
                map.setOrgId(orgId);
                map.setCreatedAt();
                map.setStatus(EntityStatus.ACTIVE);

                Optional<BpmEntitySubType> optionalEntity = entitySubTypeRepository.findById(id);
                if (optionalEntity.isPresent()) {
                    map.setEntitySubType(optionalEntity.get());
                }
                Optional<BpmProcess> optionalProcess = processRepository.findById(processId);
                if (optionalProcess.isPresent()) {
                    map.setProcess(optionalProcess.get());
                }
                if (map.getProcess() != null && map.getEntitySubType() != null) {
                    relationRepository.save(map);
                }
            }
        }
        return true;
    }


    /**
     * 获取实体对应的工序阶段列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    @Override
    public List<BpmProcessStage> getProcessStageList(Long orgId, Long projectId, Long id) {
        return relationRepository.findProcessStageByEntitySubTypeIdAndProjectIdAndOrgId(id, projectId, orgId);
    }


    /**
     * 根据英文名称查询实体类型
     */
    @Override
    public BpmEntitySubType findByOrgIdAndProjectIdAndNameEn(Long orgId, Long projectId, String nameEn) {
        return entitySubTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, nameEn, EntityStatus.ACTIVE).orElse(null);
    }


    /**
     * 批量排序
     */
    @Override
    public boolean sort(List<SortDTO> sortDTOs, Long projectId, Long orgId) {
        for (SortDTO dto : sortDTOs) {
            Optional<BpmEntitySubType> optionalEntitySubType = entitySubTypeRepository.findById(dto.getId());
            if (optionalEntitySubType.isPresent()) {
                BpmEntitySubType entitySubType = optionalEntitySubType.get();
                entitySubType.setOrderNo((short) dto.getOrderNo());
                entitySubType.setLastModifiedAt();
                entitySubTypeRepository.save(entitySubType);
            }
        }
        return true;
    }


    /**
     * 获取全部实体类型对应的实体类型分类列表
     */
    @Override
    public List<BpmEntityType> getEntityTypeList(Long projectId, Long orgId, String type) {
        if (CategoryTypeTag.READONLY.name().equalsIgnoreCase(type)) {
            return entitySubTypeRepository.findEntityTypes(projectId, type);
        } else if (CategoryTypeTag.BUSINESS.name().equalsIgnoreCase(type)) {
            return entitySubTypeRepository.findEntityBusinessTypes(projectId, type);
        }

        return null;
    }

    @Override
    public List<DrawingSignatureCoordinate> getSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long id
    ) {
        return drawingSignatureCoordinateRepository
            .findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatus(
                orgId,
                projectId,
                id,
                EntityStatus.ACTIVE
            );
    }

    @Override
    public Page<DrawingCoordinate> getBarCodeCoordinate(
        Long orgId,
        Long projectId,
        Long id
    ) {
        List<DrawingCoordinateType> drawingCoordinateTypes = new ArrayList<>();
        drawingCoordinateTypes.add(DrawingCoordinateType.BARCODE_HORIZONTAL);
        drawingCoordinateTypes.add(DrawingCoordinateType.BARCODE_VERTICAL);

        List<BpmEntityTypeCoordinateRelation> relations = coordinateRelationRepository.findByEntitySubTypeIdAndStatus(id, EntityStatus.ACTIVE);

        if (relations != null && !relations.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            relations.forEach(relation -> ids.add(relation.getDrawingCoordinate().getId()));
            return drawingCoordinateRepository.findByOrgIdAndProjectIdAndStatusAndDrawingCoordinateTypeInAndIdIn(orgId, projectId, EntityStatus.ACTIVE, drawingCoordinateTypes, ids, new PageDTO().toPageable());
        } else {
            return null;
        }
    }

    @Override
    public DrawingSignatureCoordinate addSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long id,
        DrawingSignatureCoordinateDTO coordinateDTO
    ) {
        DrawingSignatureCoordinate coordinate = new DrawingSignatureCoordinate();
        coordinate.setOrgId(orgId);
        coordinate.setProjectId(projectId);
        coordinate.setEntitySubTypeId(id);
        coordinate.setDrawingPositionX(coordinateDTO.getDrawingPositionX());
        coordinate.setDrawingPositionY(coordinateDTO.getDrawingPositionY());
        coordinate.setDrawingScaleToFit(coordinateDTO.getDrawingScaleToFit());
        coordinate.setDrawingSignatureType(coordinateDTO.getDrawingSignatureType());

        coordinate.setCreatedAt();
        coordinate.setStatus(EntityStatus.ACTIVE);
        return drawingSignatureCoordinateRepository.save(coordinate);
    }

    @Override
    public DrawingSignatureCoordinate updateSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id,
        DrawingSignatureCoordinateDTO coordinateDTO
    ) {
        DrawingSignatureCoordinate coordinate = drawingSignatureCoordinateRepository
            .findById(id)
            .orElse(null);
        if (coordinate == null) {
            throw new NotFoundError();
        }
        coordinate.setDrawingPositionX(coordinateDTO.getDrawingPositionX());
        coordinate.setDrawingPositionY(coordinateDTO.getDrawingPositionY());
        coordinate.setDrawingScaleToFit(coordinateDTO.getDrawingScaleToFit());
        coordinate.setDrawingSignatureType(coordinateDTO.getDrawingSignatureType());
        coordinate.setLastModifiedAt();

        return drawingSignatureCoordinateRepository.save(coordinate);
    }

    @Override
    public DrawingCoordinate updateBarCodeCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id,
        DrawingCoordinateDTO coordinateDTO
    ) {
        DrawingCoordinate coordinate = drawingCoordinateRepository
            .findById(id)
            .orElse(null);
        if (coordinate == null) {
            throw new NotFoundError();
        }

        coordinate.setDrawingPositionX(coordinateDTO.getDrawingPositionX());
        coordinate.setDrawingPositionY(coordinateDTO.getDrawingPositionY());
        coordinate.setFileId(coordinateDTO.getFileId());
        coordinate.setFileName(coordinateDTO.getFileName());
        coordinate.setFilePath(coordinateDTO.getFilePath());
        coordinate.setName(coordinateDTO.getName());
        coordinate.setGraphWidth(coordinateDTO.getGraphWidth());
        coordinate.setGraphHeight(coordinateDTO.getGraphHeight());
        coordinate.setDrawingCoordinateType(coordinateDTO.getDrawingCoordinateType());
        coordinate.setDrawingCoverHeight(coordinateDTO.getDrawingCoverHeight());
        coordinate.setDrawingCoverWidth(coordinateDTO.getDrawingCoverWidth());
        coordinate.setLastModifiedAt(new Date());

        return drawingCoordinateRepository.save(coordinate);
    }

    @Override
    public void deleteSignatureCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id
    ) {
        DrawingSignatureCoordinate coordinate = drawingSignatureCoordinateRepository
            .findById(id)
            .orElse(null);
        if (coordinate == null) {
            throw new NotFoundError();
        }
        coordinate.setStatus(EntityStatus.DELETED);
        drawingSignatureCoordinateRepository.save(coordinate);
    }

    @Override
    public void deleteCoordinate(
        Long orgId,
        Long projectId,
        Long entitySubTypeId,
        Long id
    ) {
        BpmEntityTypeCoordinateRelation coordinateRelation = coordinateRelationRepository.findByDrawingCoordinateIdAndEntitySubTypeIdAndStatus(id, entitySubTypeId, EntityStatus.ACTIVE).orElse(null);;

//        DrawingCoordinate coordinate = drawingCoordinateRepository
//            .findById(id)
//            .orElse(null);
        if (coordinateRelation == null) {
            throw new NotFoundError();
        }
        coordinateRelationRepository.delete(coordinateRelation);

    }

    /**
     * 设置返回结果引用数据。
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>      返回结果类型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> entityCategoryIDs = new ArrayList<>();

        for (T entity : entities) {

            if (entity instanceof RatedTime && ((RatedTime) entity).getEntitySubTypeId() != null) {
                entityCategoryIDs.add(((RatedTime) entity).getEntitySubTypeId());
            }

            if (entity instanceof RatedTimeCriterion && ((RatedTimeCriterion) entity).getEntitySubTypeId() != null) {
                entityCategoryIDs.add(((RatedTimeCriterion) entity).getEntitySubTypeId());
            }
        }

        List<BpmEntitySubType> entityCategories = entitySubTypeRepository.findByIdIn(entityCategoryIDs);

        for (BpmEntitySubType entitySubType : entityCategories) {
            included.put(entitySubType.getId(), entitySubType);
        }

        return included;
    }

    @Override
    public BpmEntitySubType getEntitySubType(Long projectId, String entitySubType) {
        String entitySubTypeKey = String.format(RedisKey.ENTITY_SUB_TYPE.getDisplayName(), projectId, entitySubType);
        String entitySubTypeStr = getRedisKey(entitySubTypeKey);

        if (StringUtils.isEmpty(entitySubTypeStr)) {
            BpmEntitySubType best = null;
            best = entitySubTypeRepository.findById(projectId).orElse(null);
            if (best != null) {
                entitySubTypeStr = StringUtils.toJSON(best);
                setRedisKey(entitySubTypeKey, entitySubTypeStr);
            }
            return best;

        } else {
            return StringUtils.decode(entitySubTypeStr, new TypeReference<BpmEntitySubType>() {
            });
        }
    }

    @Override
    public BpmEntitySubType getEntitySubTypeByWbs(Long projectId, String typeName, String entitySubType) {
        return entitySubTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, entitySubType, EntityStatus.ACTIVE).orElse(null);
    }

    @Override
    public List<DrawingCoordinate> getBarCodeCoordinateListByEntitySubType(Long orgId, Long projectId, String entitySubType) {

        return drawingCoordinateRepository.searchDrawingCoordinateByEntitySubType(orgId, projectId, entitySubType);
    }

}
