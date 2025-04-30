package com.ose.tasks.domain.model.service;

import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.service.bpm.EntityTypeInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 项目节点管理服务。
 */
@Component
public class ProjectNodeService implements ProjectNodeInterface {

    private final EntityTypeInterface entityTypeService;

    private ProjectNodeRepository projectNodeRepository;


    private BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> subSystemEntityService;


    @Autowired
    public ProjectNodeService(
        EntityTypeInterface entityTypeService, ProjectNodeRepository projectNodeRepository,
        BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> subSystemEntityService) {
        this.entityTypeService = entityTypeService;
        this.projectNodeRepository = projectNodeRepository;
        this.subSystemEntityService = subSystemEntityService;
    }

    @Override
    public List<SubDrawing> getDrawingInfo(Long orgId, Long projectId, Long entityId) {
        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).orElse(null);

        if (projectNode == null || projectNode.getEntityType() == null) {
            throw new BusinessError("There no such Type Entity in Project Nodes");
        }
        BaseWBSEntityInterface wbsEntityService = entityTypeService.getEntityInterface(projectId, projectNode.getEntityType());

        return wbsEntityService.getMaterialInfo(orgId, projectId, entityId);
    }

    @Override
    public List<SubDrawing> getDrawingsInfo(Long orgId, Long projectId, Set<Long> entityIds) {
        if (CollectionUtils.isEmpty(entityIds)) return new ArrayList<>();
        List<SubDrawing> subDrawings = new ArrayList<>();

        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityIds.iterator().next()).orElse(null);

        if (projectNode == null || projectNode.getEntityType() == null) {
            return subDrawings;

        }

        BaseWBSEntityInterface wbsEntityService = entityTypeService.getEntityInterface(projectId, projectNode.getEntityType());// getEntityService(projectNode.getEntityType());

        entityIds.forEach(entityId -> {
            List<SubDrawing> subDwgs = wbsEntityService.getDrawingInfo( orgId, projectId,entityId,null);
            if(CollectionUtils.isEmpty(subDwgs)) return;
            subDrawings.addAll(
                subDwgs
            );

        });

        return subDrawings;
    }


    @Override
    public List<MaterialInfoDTO> getMaterialInfo(Long orgId, Long projectId, Long entityId) {

        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityId).orElse(null);

        if (projectNode == null || projectNode.getEntityType() == null) {
            throw new BusinessError("There no such Type Entity in Project Nodes");
        }
        BaseWBSEntityInterface wbsEntityService = entityTypeService.getEntityInterface(projectId, projectNode.getEntityType());

        return wbsEntityService.getDrawingInfo(
            orgId,
            projectId,
            entityId,
            null);
    }

    @Override
    public List<MaterialInfoDTO> getMaterialsInfo(Long orgId, Long projectId, Set<Long> entityIds) {
        if (CollectionUtils.isEmpty(entityIds)) return new ArrayList<>();
        List<MaterialInfoDTO> materialInfos = new ArrayList<>();
        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(projectId, entityIds.iterator().next()).orElse(null);

        if (projectNode == null || projectNode.getEntityType() == null) {
            throw new BusinessError("There no such Type Entity in Project Nodes");
        }
        BaseWBSEntityInterface wbsEntityService = entityTypeService.getEntityInterface(projectId, projectNode.getEntityType());

        entityIds.forEach(entityId -> {
            materialInfos.addAll(
                wbsEntityService.getDrawingInfo(
                    orgId,
                    projectId,
                    entityId,
                    null)
            );
        });

        return materialInfos;
    }


}
