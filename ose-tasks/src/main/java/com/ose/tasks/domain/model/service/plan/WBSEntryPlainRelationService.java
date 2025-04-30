package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryBlob;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlainRelation;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.wbs.WBSEntryType;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class WBSEntryPlainRelationService implements WBSEntryPlainRelationInterface {

    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;

    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryBlobRepository wbsEntryBlobRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    // EntityManager
    private final EntityManager entityManager;

    @Autowired
    public WBSEntryPlainRelationService(
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        WBSEntryRepository wbsEntryRepository,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        EntityManager entityManager
    ) {
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.entityManager = entityManager;
    }

    /**
     * 创建WBSEntryPlainRelation信息。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        ContextDTO context
    ) {
        int BATCH_SIZE =200;//50;

//        new Thread(() -> {
//            int pageNo = 0;
//            int pageSize;
//            while (true) {
//
//                // 取得已达可启动时间的四级计划
//                List<Tuple> wbsEntryInfos = wbsEntryRepository //hierarchyId, path, depth
//                    .getWBSInfosByPage(
//                        projectId,
//                        pageNo * BATCH_SIZE,
//                        BATCH_SIZE
//                    );
//                if(wbsEntryInfos == null) {
//                    pageSize = 0;
//                } else {
//                    pageSize = wbsEntryInfos.size();
//                }
//
//                if (pageSize == 0) {
//                    break;
//                }
//
//                // 尝试启动每一个四级计划
//                for (Tuple wbsEntryInfo : wbsEntryInfos) {
//                    String wbsEntryPathStr = (String) wbsEntryInfo.get("path");
//                    List<Long> wbsEntryAncestorIds = new ArrayList<>();
//                    wbsEntryAncestorIds = LongUtils.change2LongArr(wbsEntryPathStr,"/");
//                    String moduleNo = (String) wbsEntryInfo.get("sector");
//
//                    int depth = (Integer) wbsEntryInfo.get("depth");
//                    Long wbsEntryId = ((BigInteger)  wbsEntryInfo.get("wbsEntryId")).longValue();
//                    wbsEntryAncestorIds.add(wbsEntryId);
//                    BigInteger bgNodeId = (BigInteger) wbsEntryInfo.get("nodeId");
//                    BigInteger bgEntityId = (BigInteger) wbsEntryInfo.get("entityId");
//                    Long entityId = bgEntityId == null?null:bgEntityId.longValue();
//                    Long nodeId = bgNodeId == null?null:bgNodeId.longValue();
//                    String typeStr = (String) wbsEntryInfo.get("type");
//                    WBSEntryType type = StringUtils.isEmpty(typeStr)?null:WBSEntryType.valueOf(typeStr);
//
//                    int ancestorDepth = 0;
//                    for(Long wbsEntryAncestorId : wbsEntryAncestorIds) {
//                        WBSEntryPlainRelation wbsEntryPlainRelation = wbsEntryPlainRelationRepository.
//                            findByWbsEntryIdAndWbsEntryAncestorId(wbsEntryId, wbsEntryAncestorId);
//                        if(wbsEntryPlainRelation == null) {
//
//                            wbsEntryPlainRelation = new WBSEntryPlainRelation();
//                            wbsEntryPlainRelation.setOrgId(orgId);
//                            wbsEntryPlainRelation.setProjectId(projectId);
//                            wbsEntryPlainRelation.setWbsEntryId(wbsEntryId);
//                            wbsEntryPlainRelation.setWbsEntryAncestorId(wbsEntryAncestorId);
//                            wbsEntryPlainRelation.setNodeId(nodeId);
//                            wbsEntryPlainRelation.setType(type);
//                            wbsEntryPlainRelation.setModuleNo(moduleNo);
//                            wbsEntryPlainRelation.setEntityId(entityId);
//
//                        }
//
//                        wbsEntryPlainRelation.setDepth(ancestorDepth++);
//
//                        wbsEntryPlainRelationRepository.save(wbsEntryPlainRelation);
//                    }
//                    if((depth + 1) != ancestorDepth) {
//                        System.out.println("Depth Wrong");
//                    }
//
//                }
//
//                pageNo++;
//
//            }
//
//        }).start();

        Project project = projectService.get(orgId, projectId);

        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.WBS_ENTRY,
            false,
            context,
            batchTask -> {
                int pageNo = 0;
                int pageSize;
                while (true) {

                    // 取得已达可启动时间的四级计划
                    List<Tuple> wbsEntryInfos = wbsEntryRepository //hierarchyId, path, depth
                        .getWBSInfosByPage(
                            projectId,
                            pageNo * BATCH_SIZE,
                            BATCH_SIZE
                        );
                    if(wbsEntryInfos == null) {
                        pageSize = 0;
                    } else {
                        pageSize = wbsEntryInfos.size();
                    }

                    if (pageSize == 0) {
                        break;
                    }

                    // 尝试启动每一个四级计划
                    for (Tuple wbsEntryInfo : wbsEntryInfos) {
                        String wbsEntryPathStr = (String) wbsEntryInfo.get("path");
                        List<Long> wbsEntryAncestorIds = new ArrayList<>();
                        wbsEntryAncestorIds = LongUtils.change2LongArr(wbsEntryPathStr,"/");
                        String moduleNo = (String) wbsEntryInfo.get("sector");

                        int depth = (Integer) wbsEntryInfo.get("depth");
                        Long wbsEntryId = ((BigInteger)  wbsEntryInfo.get("wbsEntryId")).longValue();
                        wbsEntryAncestorIds.add(wbsEntryId);
                        BigInteger bgNodeId = (BigInteger) wbsEntryInfo.get("nodeId");
                        BigInteger bgEntityId = (BigInteger) wbsEntryInfo.get("entityId");
                        Long entityId = bgEntityId == null?null:bgEntityId.longValue();
                        Long nodeId = bgNodeId == null?null:bgNodeId.longValue();
                        String typeStr = (String) wbsEntryInfo.get("type");
                        WBSEntryType type = StringUtils.isEmpty(typeStr)?null:WBSEntryType.valueOf(typeStr);

                        int ancestorDepth = 0;
                        for(Long wbsEntryAncestorId : wbsEntryAncestorIds) {
                            WBSEntryPlainRelation wbsEntryPlainRelation = wbsEntryPlainRelationRepository.
                                findByWbsEntryIdAndWbsEntryAncestorId(wbsEntryId, wbsEntryAncestorId);
                            if(wbsEntryPlainRelation == null) {

                                wbsEntryPlainRelation = new WBSEntryPlainRelation();
                                wbsEntryPlainRelation.setOrgId(orgId);
                                wbsEntryPlainRelation.setProjectId(projectId);
                                wbsEntryPlainRelation.setWbsEntryId(wbsEntryId);
                                wbsEntryPlainRelation.setWbsEntryAncestorId(wbsEntryAncestorId);
                                wbsEntryPlainRelation.setNodeId(nodeId);
                                wbsEntryPlainRelation.setType(type);
                                wbsEntryPlainRelation.setModuleNo(moduleNo);
                                wbsEntryPlainRelation.setEntityId(entityId);

                            }

                            wbsEntryPlainRelation.setDepth(ancestorDepth++);

                            wbsEntryPlainRelationRepository.save(wbsEntryPlainRelation);
                        }
                        if((depth + 1) != ancestorDepth) {
                            System.out.println("Depth Wrong");
                        }

                    }

                    pageNo++;

                }
                return new BatchResultDTO();
            });
    }

    /**
     * 将 wbsEntry path 转换成 扁平结构 存储
     * @param projectId
     */
    @Override
    public void saveWBSEntryPath(Long orgId, Long projectId, WBSEntry wbsEntry) {
        if(wbsEntry == null) {
            return;
        }
        WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
        if(wbsEntryBlob == null || StringUtils.isEmpty(wbsEntryBlob.getPath())) return;

        String path = wbsEntryBlob.getPath();
        Long wbsEntryId = wbsEntry.getId();

        List<Long> wbsEntryAncestorIds = LongUtils.change2LongArr(path,"/");
        if(!CollectionUtils.isEmpty(wbsEntryAncestorIds) && !wbsEntryAncestorIds.getLast().equals(wbsEntryId)) {
            wbsEntryAncestorIds.add(wbsEntryId);
        }


        for(int i = 0; i <  wbsEntryAncestorIds.size(); i++) {

//            if(entityManager.getTransaction() != null) {
//                entityManager.clear();
//            }
//            System.out.println("wbsEntryId" + wbsEntryId + " wbsEntryAncestorIds.get(i): " + wbsEntryAncestorIds.get(i) );

            WBSEntryPlainRelation wbsEntryPlainRelation =
                wbsEntryPlainRelationRepository.findByWbsEntryIdAndWbsEntryAncestorId(wbsEntryId, wbsEntryAncestorIds.get(i));
            if(wbsEntryPlainRelation == null) {

                wbsEntryPlainRelation = new WBSEntryPlainRelation();
                wbsEntryPlainRelation.setOrgId(orgId);
                wbsEntryPlainRelation.setProjectId(projectId);
                wbsEntryPlainRelation.setWbsEntryId(wbsEntryId);
                wbsEntryPlainRelation.setWbsEntryAncestorId(wbsEntryAncestorIds.get(i));
                wbsEntryPlainRelation.setNodeId(wbsEntry.getProjectNodeId());
                wbsEntryPlainRelation.setType(wbsEntry.getType());
                wbsEntryPlainRelation.setModuleNo(wbsEntry.getSector());
                wbsEntryPlainRelation.setDepth(i);
                wbsEntryPlainRelation.setEntityId(wbsEntry.getEntityId());

            } else if (wbsEntryPlainRelation.getDepth() != i) {
                wbsEntryPlainRelation = new WBSEntryPlainRelation();
                wbsEntryPlainRelation.setOrgId(orgId);
                wbsEntryPlainRelation.setProjectId(projectId);
                wbsEntryPlainRelation.setWbsEntryId(wbsEntryId);
                wbsEntryPlainRelation.setWbsEntryAncestorId(wbsEntryAncestorIds.get(i));
                wbsEntryPlainRelation.setNodeId(wbsEntry.getProjectNodeId());
                wbsEntryPlainRelation.setType(wbsEntry.getType());
                wbsEntryPlainRelation.setModuleNo(wbsEntry.getSector());
                wbsEntryPlainRelation.setDepth(i);
                wbsEntryPlainRelation.setEntityId(wbsEntry.getEntityId());

            }

            wbsEntryPlainRelationRepository.save(wbsEntryPlainRelation);

        }

    }


}
