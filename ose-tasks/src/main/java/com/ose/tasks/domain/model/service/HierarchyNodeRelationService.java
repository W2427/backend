package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.HierarchyRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;

@Component
public class HierarchyNodeRelationService implements HierarchyNodeRelationInterface {

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final HierarchyRepository hierarchyRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    @Autowired
    public HierarchyNodeRelationService(
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        HierarchyRepository hierarchyRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService) {
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
    }

    /**
     * 创建HierarchyNodeRelation信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        ContextDTO contextDTO
    ) {
        int BATCH_SIZE = 100;


































































































        Project project = projectService.get(orgId, projectId);

        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO,
            project,
            BatchTaskCode.HIERARCHY,
            batchTask -> {

                int pageNo = 0;
                int pageSize;

                while (true) {


                    List<Tuple> hierarchyInfos = hierarchyRepository
                        .getHierarchyInfosByPage(
                            projectId,
                            pageNo * BATCH_SIZE,
                            BATCH_SIZE
                        );
                    if (hierarchyInfos == null) {
                        pageSize = 0;
                    } else {
                        pageSize = hierarchyInfos.size();
                    }

                    if (pageSize == 0) {
                        break;
                    }


                    for (Tuple hierarchyInfo : hierarchyInfos) {
                        String hierarchyPathStr = (String) hierarchyInfo.get("path");
                        List<Long> hierarchyAncestorIds = new ArrayList<>();



                        hierarchyAncestorIds = LongUtils.change2LongArr(hierarchyPathStr, "/");

                        Long moduleHierarchyNodeId = null;

                        int depth = (Integer) hierarchyInfo.get("depth");
                        Long hierarchyId = ((BigInteger) hierarchyInfo.get("hierarchyId")).longValue();
                        hierarchyAncestorIds.add(hierarchyId);

                        int ancestorDepth = 0;
                        for (Long hierarchyAncestorId : hierarchyAncestorIds) {
                            if (moduleHierarchyNodeId == null) {
                                Long tmpId = hierarchyNodeRelationRepository.findModuleHierarchyNodeId(projectId, hierarchyAncestorId);
                                if (tmpId != null) {
                                    moduleHierarchyNodeId = tmpId;
                                }
                            }
                            HierarchyNodeRelation hierarchyNodeRelation = hierarchyNodeRelationRepository.
                                findByHierarchyIdAndHierarchyAncestorId(hierarchyId, hierarchyAncestorId);
                            if (hierarchyNodeRelation == null) {

                                hierarchyNodeRelation = new HierarchyNodeRelation();
                                hierarchyNodeRelation.setOrgId(orgId);
                                hierarchyNodeRelation.setProjectId(projectId);
                                hierarchyNodeRelation.setHierarchyId(hierarchyId);
                                hierarchyNodeRelation.setHierarchyAncestorId(hierarchyAncestorId);
                                hierarchyNodeRelation.setModuleHierarchyNodeId(moduleHierarchyNodeId);

                            }
                            if (hierarchyNodeRelation.getNodeId() != null) {
                                continue;
                            }

                            hierarchyNodeRelation.setDepth(ancestorDepth++);

                            HierarchyNode hn = hierarchyRepository.findById(hierarchyId).orElse(null);
                            if (hn != null && hn.getNode() != null) {
                                hierarchyNodeRelation.setNodeId(hn.getNode().getId());
                                hierarchyNodeRelation.setEntityId(hn.getNode().getEntityId());
                                if (hn.getNode().getEntityType() != null) {
                                    hierarchyNodeRelation.setEntityType(hn.getNode().getEntityType());
                                }
                                hierarchyNodeRelation.setEntitySubType(hn.getNode().getEntitySubType());
                            }

                            HierarchyNode ahn = hierarchyRepository.findById(hierarchyAncestorId).orElse(null);
                            if (ahn != null) {
                                hierarchyNodeRelation.setNodeAncestorId(ahn.getNode().getId());
                                hierarchyNodeRelation.setAncestorEntityType(ahn.getEntityType() == null ? null : ahn.getEntityType());
                                hierarchyNodeRelation.setAncestorEntityId(ahn.getEntityId());
                            }

                            hierarchyNodeRelationRepository.save(hierarchyNodeRelation);
                        }
                        if (depth + 1 != ancestorDepth) {
                            System.out.println("Depth Wrong");
                        }

                    }

                    pageNo++;

                }
                return new BatchResultDTO();
            });
    }


    /**
     * 将 hierarchy path 转换成 扁平结构 存储
     *
     * @param projectId
     */
    @Transactional
    public void saveHierarchyPath(Long orgId, Long projectId, HierarchyNode node) {
        if (node == null || StringUtils.isEmpty(node.getPath())) {
            return;
        }

        String path = node.getPath();
        Long hierarchyId = node.getId();
        String entitySubType = node.getNode() == null ? null:node.getNode().getEntitySubType();

        Long moduleHierarchyNodeId = null;
        List<Long> hierarchyAncestorIds = LongUtils.change2LongArr(path, "/");
        hierarchyAncestorIds.add(hierarchyId);

        for (int i = 0; i < hierarchyAncestorIds.size(); i++) {

            HierarchyNodeRelation hierarchyNodeRelation =
                hierarchyNodeRelationRepository.findByHierarchyIdAndHierarchyAncestorId(hierarchyId, hierarchyAncestorIds.get(i));
            if (hierarchyNodeRelation == null) {
                if (moduleHierarchyNodeId == null) {
                    Long tmpId = hierarchyNodeRelationRepository.findModuleHierarchyNodeId(projectId, hierarchyAncestorIds.get(i));
                    if (tmpId != null) {
                        moduleHierarchyNodeId = tmpId;
                    }
                }

                hierarchyNodeRelation = new HierarchyNodeRelation();
                hierarchyNodeRelation.setOrgId(orgId);
                hierarchyNodeRelation.setProjectId(projectId);
                hierarchyNodeRelation.setDepth(i);
                hierarchyNodeRelation.setHierarchyAncestorId(hierarchyAncestorIds.get(i));
                hierarchyNodeRelation.setHierarchyId(hierarchyId);
                hierarchyNodeRelation.setNodeId(node.getNode().getId());
                hierarchyNodeRelation.setEntityType(node.getEntityType() == null ? null : node.getEntityType());
                hierarchyNodeRelation.setEntitySubType(node.getEntitySubType());
                hierarchyNodeRelation.setModuleHierarchyNodeId(moduleHierarchyNodeId);
                hierarchyNodeRelation.setEntityId(node.getEntityId());
                HierarchyNode ahn = hierarchyRepository.findById(hierarchyAncestorIds.get(i)).orElse(null);
                if (ahn != null && ahn.getNode() != null) {
                    ProjectNode apn = ahn.getNode();
                    hierarchyNodeRelation.setNodeAncestorId(apn.getId());
                    hierarchyNodeRelation.setAncestorEntityType(apn.getEntityType() == null ? null : apn.getEntityType());
                    hierarchyNodeRelation.setAncestorEntityId(apn.getEntityId());
                }
                hierarchyNodeRelationRepository.save(hierarchyNodeRelation);

            } else if (hierarchyNodeRelation.getDepth() != i ||
                (entitySubType == null && hierarchyNodeRelation.getEntitySubType() != null) ||
                (entitySubType != null && !entitySubType.equalsIgnoreCase(hierarchyNodeRelation.getEntitySubType()))
                ) {
                if (moduleHierarchyNodeId == null) {
                    Long tmpId = hierarchyNodeRelationRepository.findModuleHierarchyNodeId(projectId, hierarchyAncestorIds.get(i));
                    if (tmpId != null) {
                        moduleHierarchyNodeId = tmpId;
                    }
                }
                hierarchyNodeRelation.setDepth(i);
                hierarchyNodeRelation.setNodeId(node.getNode().getId());
                hierarchyNodeRelation.setEntityType(node.getEntityType());
                hierarchyNodeRelation.setEntitySubType(node.getEntitySubType());
                HierarchyNode ahn = hierarchyRepository.findById(hierarchyAncestorIds.get(i)).orElse(null);
                if (ahn != null && ahn.getNode() != null) {
                    ProjectNode apn = ahn.getNode();
                    hierarchyNodeRelation.setNodeAncestorId(apn.getId());
                    hierarchyNodeRelation.setAncestorEntityType(apn.getEntityType() == null ? null : apn.getEntityType());
                    hierarchyNodeRelation.setAncestorEntityId(apn.getEntityId());
                }
                hierarchyNodeRelationRepository.save(hierarchyNodeRelation);
            }


        }

    }


}
