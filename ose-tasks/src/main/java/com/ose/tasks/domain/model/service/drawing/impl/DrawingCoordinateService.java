package com.ose.tasks.domain.model.service.drawing.impl;

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
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingCoordinateInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateEntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.RatedTime;
import com.ose.tasks.entity.RatedTimeCriterion;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.RelationReturnEnum;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class DrawingCoordinateService extends StringRedisService implements DrawingCoordinateInterface {
    private final static Logger logger = LoggerFactory.getLogger(DrawingCoordinateService.class);

    /**
     * 工序  操作仓库
     */
    private final BpmProcessRepository processRepository;
    /**
     * 实体类型-坐标  操作仓库
     */
    private final EntitySubTypeCoordinateRelationRepository relationRepository;
    /**
     * 实体类型 操作仓库
     */
    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final BpmEntityTypeRepository entityTypeRepository;

    private final BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository;

    private final DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository;

    private final DrawingCoordinateRepository drawingCoordinateRepository;

    private final UploadFeignAPI uploadFeignAPI;


    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingCoordinateService(
        BpmProcessRepository processRepository,
        EntitySubTypeCoordinateRelationRepository relationRepository,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        BpmEntityTypeRepository entityTypeRepository,
        BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository,
        DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository,
        DrawingCoordinateRepository drawingCoordinateRepository,
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
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    public Page<DrawingCoordinate> getList(
        Long orgId,
        Long projectId,
        DrawingCoordinateCriteriaDTO criteriaDTO
    ) {
        return drawingCoordinateRepository.search(orgId, projectId, criteriaDTO);
    }

    @Override
    public DrawingCoordinate create(Long orgId, Long projectId, DrawingCoordinateDTO coordinateDTO) {

        DrawingCoordinate existedDrawingCoordinate = drawingCoordinateRepository.findByOrgIdAndProjectIdAndNameAndStatus(orgId, projectId, coordinateDTO.getName(), EntityStatus.ACTIVE);

        if (existedDrawingCoordinate != null){
            throw new BusinessError("drawing coordinate name existed!");
        }

        DrawingCoordinate drawingCoordinate = BeanUtils.copyProperties(coordinateDTO, new DrawingCoordinate());
        drawingCoordinate.setOrgId(orgId);
        drawingCoordinate.setProjectId(projectId);
        drawingCoordinate.setStatus(EntityStatus.ACTIVE);
        drawingCoordinate.setCreatedAt();

        if (coordinateDTO.getFileName() != null && !coordinateDTO.getFileName().isEmpty()) {
            logger.error("坐标 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), coordinateDTO.getFileName(),
                new FilePostDTO());
            logger.error("坐标 保存docs服务->结束");
            FileES fileES = fileESResBody.getData();
            drawingCoordinate.setFileId(LongUtils.parseLong(fileES.getId()));
            drawingCoordinate.setFilePath(fileES.getPath());
            drawingCoordinate.setFileName(fileES.getName());
        }

        return drawingCoordinateRepository.save(drawingCoordinate);
    }

    @Override
    public DrawingCoordinate update(
        Long orgId,
        Long projectId,
        Long id,
        DrawingCoordinateDTO coordinateDTO
    ) {
        Optional<DrawingCoordinate> optional = drawingCoordinateRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingCoordinate coordinate = optional.get();
        coordinate.setDrawingPositionX(coordinateDTO.getDrawingPositionX());
        coordinate.setDrawingPositionY(coordinateDTO.getDrawingPositionY());
        coordinate.setFileName(coordinateDTO.getFileName());
        coordinate.setName(coordinateDTO.getName());
        coordinate.setGraphWidth(coordinateDTO.getGraphWidth());
        coordinate.setGraphHeight(coordinateDTO.getGraphHeight());
        coordinate.setDrawingCoordinateType(coordinateDTO.getDrawingCoordinateType());
        coordinate.setDrawingCoverHeight(coordinateDTO.getDrawingCoverHeight());
        coordinate.setDrawingCoverWidth(coordinateDTO.getDrawingCoverWidth());
        coordinate.setLastModifiedAt(new Date());

        if (coordinateDTO.getFileName() != null && !coordinateDTO.getFileName().isEmpty()) {
            logger.error("坐标 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), coordinateDTO.getFileName(),
                new FilePostDTO());
            logger.error("坐标 保存docs服务->结束");
            FileES fileES = fileESResBody.getData();
            coordinate.setFileId(LongUtils.parseLong(fileES.getId()));
            coordinate.setFilePath(fileES.getPath());
        }

        return drawingCoordinateRepository.save(coordinate);
    }

    @Override
    public void deleteCoordinate(
        Long orgId,
        Long projectId,
        Long id
    ) {
        Optional<DrawingCoordinate> optional = drawingCoordinateRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingCoordinate coordinate = optional.get();
        coordinate.setStatus(EntityStatus.DELETED);
        coordinate.setLastModifiedAt();
        drawingCoordinateRepository.save(coordinate);
    }

    /**
     * 获取工序对应的实体类型列表。
     */
    @Override
    public Page<BpmEntitySubType> getEntitySubTypeList(Long id, Long projectId, Long orgId, DrawingCoordinateEntitySubTypeCriteriaDTO criteriaDTO) {
        if(LongUtils.isEmpty(criteriaDTO.getEntityTypeId())) {
            return relationRepository.findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgIdasPage(id, projectId, orgId, criteriaDTO.toPageable());
        }

        return relationRepository.findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgIdAndTypeId(id, projectId, orgId,
            criteriaDTO.getEntityTypeId(), criteriaDTO.toPageable());
    }

    /**
     * 添加实体类型。
     *
     * @param drawingCoordinateId        坐标id
     * @param entitySubTypeId 实体分类id
     * @return 工序
     */
    @Override
    public RelationReturnEnum addEntitySubType(Long drawingCoordinateId, Long entitySubTypeId, Long projectId, Long orgId) {
        Optional<BpmEntityTypeCoordinateRelation> result = relationRepository.findByDrawingCoordinateIdAndEntitySubTypeIdAndStatus(drawingCoordinateId, entitySubTypeId, EntityStatus.ACTIVE);
        if (!result.isPresent()) {
            BpmEntityTypeCoordinateRelation map = new BpmEntityTypeCoordinateRelation();
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
            Optional<DrawingCoordinate> optionalDrawingCoordinate = drawingCoordinateRepository.findById(drawingCoordinateId);
            if (optionalDrawingCoordinate.isPresent()) {
                map.setDrawingCoordinate(optionalDrawingCoordinate.get());
            } else {
                return RelationReturnEnum.COORDINATE_NOT_FOUND;
            }
            if (map.getDrawingCoordinate() != null && map.getEntitySubType() != null) {
                relationRepository.save(map);
                return RelationReturnEnum.SAVE_SUCCESS;
            }
        }
        return RelationReturnEnum.RELATION_EXIST;
    }

    /**
     * 删除实体类型。
     *
     * @param drawingCoordinateId        坐标id
     * @param entitySubTypeId 实体id
     * @return 工序
     */
    @Override
    public RelationReturnEnum deleteEntitySubType(Long drawingCoordinateId, Long entitySubTypeId, Long projectId, Long orgId) {
        Optional<BpmEntityTypeCoordinateRelation> result = relationRepository.findByDrawingCoordinateIdAndEntitySubTypeIdAndStatus(drawingCoordinateId, entitySubTypeId, EntityStatus.ACTIVE);
        if (result.isPresent()) {
            BpmEntityTypeCoordinateRelation map = result.get();
            map.setStatus(EntityStatus.DELETED);
            map.setLastModifiedAt();
            relationRepository.save(map);
            return RelationReturnEnum.DELETE_SUCCESS;
        }
        return RelationReturnEnum.RELATION_NOT_EXIST;
    }

    /**
     * 查询坐标详细信息。
     *
     * @param id        坐标id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    @Override
    public DrawingCoordinate get(Long id, Long projectId, Long orgId) {
        Optional<DrawingCoordinate> drawingCoordinate = drawingCoordinateRepository.findById(id);
        if (drawingCoordinate.isPresent()) {
            DrawingCoordinate result = drawingCoordinate.get();
            return result;
        }
        return null;
    }

    @Override
    public File saveDownloadFile(Long orgId, Long projectId, Long drawingCoordinateId, Long operatorId) {
        DrawingCoordinate drawingCoordinate = drawingCoordinateRepository.findByIdAndStatus(drawingCoordinateId, EntityStatus.ACTIVE);

        if (drawingCoordinate != null){
            File coordinateFile = new File(protectedDir, drawingCoordinate.getFilePath());
            return coordinateFile;
        }

        return null;
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

    /**
     * 取得工序对应的实体类型列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public List<BpmEntitySubType> getEntitySubTypeByDrawingCoordinateId(Long id, Long projectId, Long orgId) {
        return relationRepository.findEntitySubTypeByDrawingCoordinateIdAndProjectIdAndOrgId(id, projectId, orgId);
    }

}
