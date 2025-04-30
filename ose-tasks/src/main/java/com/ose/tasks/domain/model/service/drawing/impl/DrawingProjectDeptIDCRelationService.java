package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.util.BeanUtils;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.drawing.DrawingProjectDeptIDCRelationInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.*;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

@Component

public class DrawingProjectDeptIDCRelationService implements DrawingProjectDeptIDCRelationInterface {

    private final DrawingProjectDeptIDCRelationRepository drawingProjectDeptIDCRelationRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingProjectDeptIDCRelationService(
        DrawingProjectDeptIDCRelationRepository drawingProjectDeptIDCRelationRepository
    ) {
        this.drawingProjectDeptIDCRelationRepository = drawingProjectDeptIDCRelationRepository;
    }

    /**
     * 创建
     */
    @Override
    public DrawingProjectDeptIDCRelation create(Long orgId, Long projectId, Long userId, DrawingProjectDeptIDCRelationDTO dto) {
        Optional<DrawingProjectDeptIDCRelation> searchOptional = drawingProjectDeptIDCRelationRepository.findByProjectIdAndDrawingIdAndDepartmentIdAndDeletedIsFalse(
            projectId,
            dto.getDrawingId(),
            dto.getDepartmentId()
        );
        if (searchOptional.isPresent()) {
            throw new BusinessError("Corresponding configuration already exists!");
        }

        DrawingProjectDeptIDCRelation relation = new DrawingProjectDeptIDCRelation();
        BeanUtils.copyProperties(dto, relation);
        relation.setOrgId(orgId);
        relation.setProjectId(projectId);
        relation.setStatus(EntityStatus.ACTIVE);
        relation.setCreatedBy(userId);
        relation.setCreatedAt();
        relation.setLastModifiedAt();
        relation.setLastModifiedBy(userId);
        drawingProjectDeptIDCRelationRepository.save(relation);
        return relation;
    }

    /**
     * 修改
     */
    @Override
    public DrawingProjectDeptIDCRelation modify(Long orgId, Long projectId, Long id, Long userid,
                                                DrawingProjectDeptIDCRelationDTO dto) {
        Optional<DrawingProjectDeptIDCRelation> relationOptional = drawingProjectDeptIDCRelationRepository.findById(id);
        if (relationOptional.isPresent()) {
            DrawingProjectDeptIDCRelation relation = relationOptional.get();
            BeanUtils.copyProperties(dto, relation);
            relation.setLastModifiedBy(userid);
            relation.setLastModifiedAt();
            drawingProjectDeptIDCRelationRepository.save(relation);
        }
        return relationOptional.get();
    }

    /**
     * 获取
     */
    @Override
    public List<DrawingProjectDeptIDCRelation> getList(Long orgId, Long projectId,
                                                       DrawingProjectDeptIDCRelationSearchDTO dto) {
        return drawingProjectDeptIDCRelationRepository.search(orgId, projectId, dto);
    }

    /**
     * 获取
     */
    @Override
    public List<DrawingProjectDeptIDCRelationDTO> getListByDrawingId(Long orgId, Long projectId,Long drawingId) {
        List<DrawingProjectDeptIDCRelationDTO> result = new ArrayList<>();
        List<Map<String, Object>> list = drawingProjectDeptIDCRelationRepository.findUserIdAndUsernameByDrawingId(drawingId);
        list.forEach(data ->{
            DrawingProjectDeptIDCRelationDTO dto = new DrawingProjectDeptIDCRelationDTO();
            dto.setUserId((Long) data.get("userId"));
            dto.setUsername((String) data.get("username"));
            result.add(dto);
        });
        return result;
    }


    /**
     * 删除图纸清单条目
     */
    @Override
    public boolean delete(Long orgId, Long projectId, Long id, OperatorDTO operatorDTO) {
        Optional<DrawingProjectDeptIDCRelation> relationOptional = drawingProjectDeptIDCRelationRepository.findById(id);
        if (relationOptional.isPresent()) {
            DrawingProjectDeptIDCRelation relation = relationOptional.get();
            relation.setStatus(EntityStatus.DELETED);
            relation.setDeleted(true);
            relation.setDeletedAt();
            relation.setLastModifiedAt();
            relation.setLastModifiedBy(operatorDTO.getId());
            drawingProjectDeptIDCRelationRepository.save(relation);
        }
        return true;
    }

}
