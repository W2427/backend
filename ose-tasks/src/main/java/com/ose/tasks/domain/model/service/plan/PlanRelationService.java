package com.ose.tasks.domain.model.service.plan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.EntityHierarchyParentRepository;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.process.BpmnTaskRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.service.bpm.EntityTypeInterface;
import com.ose.tasks.domain.model.service.bpm.EntityTypeRelationInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.plan.business.PlanBusinessInterface;
import com.ose.tasks.domain.model.service.plan.relation.PlanRelationDelegateInterface;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.EntityHierarchyParent;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.SpringContextUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import net.sf.mpxj.RelationType;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * 计划管理服务。
 */
@Component
public class PlanRelationService extends StringRedisService implements PlanRelationInterface {

    // 区域层级节点类型
    private static final String[] AREA_ENTITY_TYPES = new String[]{
        "AREA",
        "SUB_AREA",
        "WP01",
        "SECTOR",
        "SUB_SECTOR",
        "SYSTEM",
        "WP02"
    };

    private static final String STAGE_PROCESS_ENTITY_TYPE = "STAGE_PROCESS_ENTITY_TYPE";


    //先检查 REDIS中是否存在 (orgId + projectId + moduleProcessDefinitionId + category) -> bpmnTaskRelation
    private static final String BPMN_TR_BY_CATEGORY_AT_REDIS_KEY = "BPMN_TR:%s:%s:%s:%s:BY_CATEGORY";

    //先检查 REDIS中是否存在 (orgId + projectId + entitySubType) -> bpmEntityRule
    private static final String ENTITY_CATEGORY_RULE_AT_REDIS_KEY = "ENTITY_RULE:%s:%s:%s:BY_SUB_ENTITY_TYPE";

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    // 需要检验的工序的集合（同一上级下所有实体的工序必须完成才能开始的工序）
    private static final Set<String> INSPECTION_PROCESSES = new HashSet<>();

    // 结构上级实体类型集合
    private static final Set<String> STRUCTURE_PARENT_ENTITY_TYPES = new HashSet<>();

    // 结构上级实体类型集合
    private static final Set<String> STRUCTURE_SON_ENTITY_TYPES = new HashSet<>();

    // 组件实体（管段、焊口、组件等）上级实体类型集合
    private static final Set<String> COMPONENT_ENTITY_TYPES = new HashSet<>();

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    static {
        INSPECTION_PROCESSES.add("FABRICATION/PMI");
        INSPECTION_PROCESSES.add("FABRICATION/PWHT");
        INSPECTION_PROCESSES.add("FABRICATION/NDT");
        INSPECTION_PROCESSES.add("FABRICATION/HD");
        STRUCTURE_PARENT_ENTITY_TYPES.add("WP01");
        STRUCTURE_PARENT_ENTITY_TYPES.add("WP02");
        STRUCTURE_PARENT_ENTITY_TYPES.add("WP03");
        STRUCTURE_PARENT_ENTITY_TYPES.add("WP04");
        STRUCTURE_PARENT_ENTITY_TYPES.add("WP_MISC");
        STRUCTURE_SON_ENTITY_TYPES.add("WP_MISC");
        STRUCTURE_SON_ENTITY_TYPES.add("WP05");
        STRUCTURE_SON_ENTITY_TYPES.add("WP02");
        STRUCTURE_SON_ENTITY_TYPES.add("WP03");
        STRUCTURE_SON_ENTITY_TYPES.add("WP04");
        //add all structure types
        COMPONENT_ENTITY_TYPES.add("ISO");
        COMPONENT_ENTITY_TYPES.add("SPOOL");
    }

    // 材质分析任务名称
    private static final String PMI_TASK_NAME = "PMI";

    // 无损探伤任务名称
    private static final String NDT_TASK_NAME = "NDT";

    // 批处理数据取得件数
    private static final int BATCH_FETCH_SIZE = 50;

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    // 计划业务
    private final PlanBusinessInterface planBusiness;

    // 实体层级关系数据仓库
    private final EntityHierarchyParentRepository entityHierarchyParentRepository;

    // 批处理任务数据仓库
    private final ProcessInterface processService;

    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;

    private final BpmnTaskRelationRepository bpmnTaskRelationRepository;

    private final EntityTypeInterface entityCategoryTypeService;

    private final EntityTypeRelationInterface entityTypeRelationService;

    /**
     * 构造方法。
     */
    @Autowired
    public PlanRelationService(
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository, WBSEntryRepository wbsEntryRepository,
        PlanBusinessInterface planBusinessImpl,
        EntityHierarchyParentRepository entityHierarchyParentRepository,
        StringRedisTemplate stringRedisTemplate,
        ProcessInterface processService,
        EntitySubTypeRuleRepository entityCategoryRuleRepository,
        BpmnTaskRelationRepository bpmnTaskRelationRepository,
        EntityTypeInterface entityCategoryTypeService,
        EntityTypeRelationInterface entityTypeRelationService) {
        super(stringRedisTemplate);
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;

        this.wbsEntryRepository = wbsEntryRepository;
        this.planBusiness = planBusinessImpl;
        this.entityHierarchyParentRepository = entityHierarchyParentRepository;
        this.processService = processService;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.bpmnTaskRelationRepository = bpmnTaskRelationRepository;
        this.entityCategoryTypeService = entityCategoryTypeService;
        this.entityTypeRelationService = entityTypeRelationService;
    }


    /**
     * 生成四级计划前置任务关系。
     *
     * @param operator                操作者信息
     * @param moduleProcessDefinition 模块工作流定义
     * @param successor               实体工序计划条目（四级计划）
     */
    public void generateEntityProcessPredecessorRelations(
        final OperatorDTO operator,
        final ModuleProcessDefinition moduleProcessDefinition,
        final WBSEntry successor
    ) {
        // 取得所有前置任务的【工序阶段/工序/实体类型/实体子类型】信息
        //TODO 项目流程图上 不能有重复的 【工序阶段/工序/实体类型/实体子类型】
//        long startTime = System.currentTimeMillis();
        String category = successor.getStage() + "/" +
            successor.getProcess() + "/" +
            successor.getEntityType() + "/" +
            successor.getEntitySubType();

        Long orgId = successor.getOrgId();
        Long projectId = successor.getProjectId();
        Long moduleProcessDefiniationId = moduleProcessDefinition.getId();

        BpmnTaskRelation bpmnTaskRelation = getBpmnTaskRelationFromRedis(orgId, projectId, moduleProcessDefiniationId, category);

//            bpmnTaskRelationRepository.
//            findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryAndDeletedIsFalse(
//                successor.getOrgId(),
//                successor.getProjectId(),
//                moduleProcessDefinition.getId(),
//                category
//            );
        if(bpmnTaskRelation == null) {
            return;
        }
        Map<String, String> formMap = bpmnTaskRelation.extractFormValueMap(bpmnTaskRelation.getFormValueMap());
        String stageProcessEntityType = formMap.get(STAGE_PROCESS_ENTITY_TYPE);
        Map<String, Long> stageProcessEntityTypeMap = new HashMap<>(); //stage-process ancestor node id
        if(stageProcessEntityType != null) {
            HierarchyNodeRelation stageProcessNode = hierarchyNodeRelationRepository.findFirstByProjectIdAndNodeIdAndAncestorEntityType(projectId, successor.getProjectNodeId(), stageProcessEntityType);
            if(stageProcessNode == null || stageProcessNode.getNodeAncestorId() == null) {
                logger.error("THERE IS NO STAGE ENTITY ID for "+ successor.getEntityId() + " " + stageProcessEntityType);
                return;
            }
            stageProcessEntityTypeMap.put(stageProcessEntityType, stageProcessNode.getNodeAncestorId() );
        }

        List<BpmnSequenceNodeDTO> predecessorNodes = bpmnTaskRelation.getJsonPredecessorNodes();
        //扩展 子实体类型，展开子实体类型 entitySubType1:entitySubType2
//        predecessorNodes = BPMNUtils.expensionNodes(predecessorNodes);
        predecessorNodes = predecessorNodes.stream().filter(predecessorNode ->!predecessorNode.getNodeType().equalsIgnoreCase("STARTEVENT")).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(predecessorNodes)) {
            return;
        }

        String preEntityTypeRelationMapStr = bpmnTaskRelation.getPreEntityTypeRelationMap();
        Map<String, List<String>> preEntityTypeRelationMap = new HashMap<>();
        if(!StringUtils.isEmpty(preEntityTypeRelationMapStr)) {
            preEntityTypeRelationMap = bpmnTaskRelation.extractPreEntityTypeRelationMap(preEntityTypeRelationMapStr);
        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("取得 Redis JSON 设定的时间为：" + (endTime - startTime) + "ms");


        final Long entityId = successor.getEntityId();
        final Set<Long> entityIDs = new HashSet<>();

        entityIDs.add(entityId);

        /*-----------------------------------------
        前置任务 有如下几种情况
        1. 前置任务 的实体ID 与 后置任务 实体ID一致。例如 焊口的 【FITUP】【WELD】。通过 entityId 来查找
        2. 前置任务的实体ID 为 后置任务的父级。例如 试压包 【试验】完成之后的 【保温】。通过 parentEntityId 来查找
        3. 前置任务的实体ID 为 为后置任务的兄弟实体ID。 例如 【FITUP】对应的实体类型为【WELD】，他的前置任务是 【配送】（对应的实体类型 为【PIPEPIECE】。

         --------------------------------------------*/

        int predecessorCount;
        int loopCount = 0;
        //处理完成的 大流程节点
        Set<String> handledNodes = new HashSet<>();

//        startTime = System.currentTimeMillis();

        do {
            // 查找前置任务，生成前置任务关系
            predecessorCount = generateEntityProcessPredecessorRelations(
                operator,
                projectId,
                successor,
                predecessorNodes,
                handledNodes,
                preEntityTypeRelationMap,
                stageProcessEntityTypeMap
            );

            loopCount++;

            // 若生成了前置任务关系则结束
            if (predecessorCount > 0) {
                break;
            }

            if (loopCount > 10) {
                System.out.println(
                    String.format(
                        "[WARN] WBS predecessor relation generating: max loop reached. "
                            + "ID: %s; No.: %s; Process: %s/%s/%s/%s; WBS: %s.",
                        successor.getId(),
                        successor.getNo(),
                        successor.getStage(),
                        successor.getProcess(),
                        successor.getEntityType(),
                        successor.getEntitySubType(),
                        ""
                    )
                );
                break;
            }

            Set<String> categorySet = new HashSet<>();

            predecessorNodes.forEach(predecessorNode -> categorySet.add(predecessorNode.getCategory()));

            predecessorNodes.clear();

            for(String currentCategory : categorySet) {
                if(currentCategory == null) {
                    continue;
                }
                BpmnTaskRelation bpmnTaskR = getBpmnTaskRelationFromRedis(orgId, projectId, moduleProcessDefiniationId, currentCategory);
                predecessorNodes.addAll(bpmnTaskR.getJsonPredecessorNodes());
                predecessorNodes = predecessorNodes.stream().filter(predecessorNode -> !handledNodes.contains(predecessorNode.getCategory())).collect(Collectors.toList());
            }

            // 若不存在前置工序的前置工序则结束
            if (predecessorNodes.size() == 0) {
                break;
            }
            preEntityTypeRelationMap.clear();
        } while (true);
//        endTime = System.currentTimeMillis();
//        System.out.println("取得前置任务总的运行时间：" + (endTime - startTime) + "ms");
    }

    /**
     * 生成四级计划前置任务关系。
     *
     * @param operator              操作者信息
     * @param projectId             项目 ID
     * @param successor             后续任务
     * @param predecessorNodes      前置任务工序及实体类型信息
     * @param handledNodes          处理完成的流程图上的节点
     * @return 生成的前置任务关系的个数
     */
    private int generateEntityProcessPredecessorRelations(
        final OperatorDTO operator,
        final Long projectId,
        final WBSEntry successor,
        final List<BpmnSequenceNodeDTO> predecessorNodes,
        final Set<String> handledNodes,
        final Map<String, List<String>> preEntityTypeRelationMap,
        final Map<String, Long> stageProcessEntityTypeMap

    ) {
        final AtomicInteger predecessorCount = new AtomicInteger();
        final Set<String> handledKeys = new HashSet<>();//stage/process/entityType
        final Map<String, Boolean> optionalMap = new HashMap<>();
        final Long orgId = successor.getOrgId();
        final Long processId = successor.getProcessId();
        final String funcPartStr = successor.getFuncPart();
        String funcPart = null;
        try {
            funcPart =funcPartStr;
        } catch (Exception e) {
            logger.error("FUNC PART is wrong " + funcPartStr);
        }

        if(processId == null || funcPart == null) return 0;

        //整理 predecessors, 根据 /stage/process/entityType 进行汇总
        String successorCategory = successor.getStage() + "/" + successor.getProcess() + "/" + successor.getEntityType() + "/" + successor.getEntitySubType();
        predecessorNodes.forEach(predecessorNode ->{
            String category = predecessorNode.getCategory();
            if(StringUtils.isEmpty(category)) {
                return;
            }
            String[] categoryArr = category.split("/");
            String stage = categoryArr[0];
            String process = categoryArr[1];
            String entityType;

            try {
                entityType = categoryArr[2];

            } catch (Exception e){
                logger.error(e.toString());
                return;
            }
            String entitySubType = categoryArr[3];
            String key = stage + "/" + process + "/" +entityType;
//            String newCategory = stage + "/" + process + "/" + entityType.name() + "/" + entitySubType;

//            if(category.equalsIgnoreCase(successorCategory)){
//                return;
//            }
            List<String> subEntityTypes = preEntityTypeRelationMap.computeIfAbsent(key, k-> new ArrayList<>());
            subEntityTypes.add(entitySubType);
            preEntityTypeRelationMap.put(key, subEntityTypes);
            optionalMap.put(key, predecessorNode.getOptional());
            handledNodes.add(category);


        });

        // 取得前置任务四级计划
        if(MapUtils.isEmpty(preEntityTypeRelationMap)) {
            return 0;
        }

        for(Map.Entry<String, List<String>> entry : preEntityTypeRelationMap.entrySet()){
            if(handledKeys.contains(entry.getKey())){
                continue;
            }
            String partCategory = entry.getKey();
            String[] partCategoryArr = partCategory.split("/");
            String stage = partCategoryArr[0];
            String process = partCategoryArr[1];

            BpmProcess bpmProcess = processService.getBpmProcess(projectId, stage, process);

            if(bpmProcess == null) continue;
            Long preProcessId = bpmProcess.getId();
            String entityTypeStr = partCategoryArr[2];
            BpmEntityType entityType = entityCategoryTypeService.getBpmEntityType(projectId, entityTypeStr);
            if(entityType == null) {
                logger.error("THERE IS NO such EntityType " + entityTypeStr);
                continue;
            }

            handledKeys.add(entry.getKey());
            List<WBSEntry> wbsEntries = new ArrayList<>();

            List<String> subEntityTypes = entry.getValue();
            if(!CollectionUtils.isEmpty(subEntityTypes)) { //子类型集合不为空
                wbsEntries = getWbsEntries(
                    projectId,
                    preProcessId,
                    entityType,
                    subEntityTypes,
                    successor,
                    stageProcessEntityTypeMap
                );
            }

            //TODO 测试 wbsEntries 为 null的时候的反应
            for (WBSEntry predecessor : wbsEntries) {
                String key = predecessor.getStage() + "/" + predecessor.getProcess() + "/" + predecessor.getEntityType();
                Boolean optional = optionalMap.get(key);
                if (optional == null) {
                    optional = false;
                }
                planBusiness.saveWBSEntryRelations(
                    operator,
                    projectId,
                    predecessor.getGuid(),
                    successor.getGuid(),
                    RelationType.FINISH_START,
                    optional
                );
                predecessorCount.getAndIncrement();
            }
        }

        return predecessorCount.get();
    }


    /*
1. 后置任务 的实体类型（PANEL) 是 前置任务 实体类型（SECTION) 的子集类型。
2. 前置任务 的实体类型（PANEL) 是 后置任务 实体类型（SECTION) 的子集类型。
3. 也就是 前置工序 为 兄弟 实体
4. 前置工序 为实体自己
5. RELATION 通过表关联
6. STAGE_PROCESS，阶段性工序
 */
    @SuppressWarnings("unchecked")
    private List<WBSEntry> getWbsEntries(
        final Long projectId,
        final Long processId,
        final BpmEntityType entityType,
        final List<String> subEntityTypes,
        final WBSEntry successor,
        final Map<String, Long> stageProcessEntityTypeMap
    ) {
        if(CollectionUtils.isEmpty(subEntityTypes)) {
            return new ArrayList<>();
        }


        Long parentHierarchyNodeId = null;
        Set<Long> entityIDs = new HashSet<>();
        String relation = "";
        boolean isSibling = false; //是否需要包含兄弟实体ID
        Long orgId = successor.getOrgId();
        BpmEntityType successorEntityType = entityCategoryTypeService.getBpmEntityType(projectId, successor.getEntityType());
//        String successorEntityType = successor.getEntityType();
        String successorEntitySubType = successor.getEntitySubType();
        Long entityId = successor.getEntityId();

        String relatedEntityType = entityType.getNameEn();
        String wbsEntityType = successorEntityType.getNameEn();
        String relationDelegate = null;

        Map<String,String> entityTypeRelationMap = entityTypeRelationService.getEntityTypeRelation(projectId, wbsEntityType);
        if(!MapUtils.isEmpty(entityTypeRelationMap)){
            relationDelegate = entityTypeRelationMap.get(relatedEntityType);

        }

        if(MapUtils.isNotEmpty(stageProcessEntityTypeMap)) {
            relation = "STAGE_PROCESS";
        } else if(!StringUtils.isEmpty(relationDelegate)) {
            relation = "RELATION";
        } else if(!relatedEntityType.equals(wbsEntityType) //两个的实体类型不同
            ) {
            String parentType = null;
            String successorParentType = null;
            EntitySubTypeRule entityCategoryRule = getEntityRuleFromRedis(orgId, projectId, subEntityTypes.get(0));
            if(entityCategoryRule == null) return new ArrayList<>();
            parentType = entityCategoryRule.getParentType();

            EntitySubTypeRule successorEntitySubTypeRule = getEntityRuleFromRedis(orgId, projectId, successorEntitySubType);
            if(successorEntitySubTypeRule == null) return new ArrayList<>();
            successorParentType = successorEntitySubTypeRule.getParentType();

            //读取 wbsEntry 父级的 实体类型定义，successor 父级 的实体类型定义
            List<String> descestorEntityTypes = null;
            List<String> succesorDescestorEntityTypes = null;

            if(parentType != null && parentType.equals(successorParentType)) { // 兄弟节点，包含焊口
                relation = "IS_SIBLING";
            } else {

                descestorEntityTypes = entityType.getListDescestorEntityTypes();
                succesorDescestorEntityTypes = successorEntityType.getListDescestorEntityTypes();


                if (descestorEntityTypes.contains(successorEntityType.getNameEn())) {//后置任务 的实体类型 是 前置任务 实体类型 的 子集类型
                    relation = "IS_DESCESTOR";
                } else if (succesorDescestorEntityTypes.contains(entityType.getNameEn())) {//前置任务 的实体类型 是 后置任务 实体类型 的 子集类型
                    relation = "IS_ANCESTOR";
                }
            }

        } else if (relatedEntityType.equalsIgnoreCase(wbsEntityType) &&
            !INSPECTION_PROCESSES.contains(successor.getStage() + "/" + successor.getProcess())){ //4. 实体类型一致，而且不是阶段性工序
            relation = "IS_SELF";
        } else if (entityType.getListDescestorEntityTypes() != null &&
            entityType.getListDescestorEntityTypes().contains(successor.getEntityType())) { //1. 后置任务 的实体类型（PANEL) 是 前置任务 实体类型（SECTION) 的子集类型。
            relation = "IS_DESCESTOR";


        } else if (successorEntityType.getListDescestorEntityTypes() != null &&
            successorEntityType.getListDescestorEntityTypes().contains(entityType.getNameEn())) { //前置任务 的实体类型（PANEL) 是 后置任务 实体类型（SECTION) 的子集类型。
            relation = "IS_ANCESTOR";

        }

        List<EntityHierarchyParent> entityHierarchyParents;
        switch (relation){
            case "STAGE_PROCESS":
                hierarchyNodeRelationRepository.findByProjectIdAndNodeAncestorIdAndEntityType(
                    projectId, stageProcessEntityTypeMap.values().iterator().next(), entityType.getNameEn()
                ).forEach(hnr -> {
                    entityIDs.add(hnr.getEntityId());
                });
                break;
            case "RELATION":
               List<Long> eIDs = execRelationDelegate(orgId, projectId, relationDelegate, entityId, wbsEntityType, relatedEntityType);
               if(eIDs != null) entityIDs.addAll(eIDs);
                break;
            case "IS_SIBLING":
                isSibling = true;

                break;
            case "IS_DESCESTOR":
                List<BigInteger> parentEntityIds = entityHierarchyParentRepository
                    .findByPathLikeParentPath(successor.getEntityId(), entityType.getNameEn());

                parentEntityIds.forEach(parentEntityId ->
                    entityIDs.add(parentEntityId.longValue()));
                break;
            case "IS_ANCESTOR":
                List<BigInteger> entityBIds = entityHierarchyParentRepository
                    .findByParentPathLikePath(successor.getEntityId(), entityType.getNameEn());

                entityBIds.forEach(entityBId ->
                    entityIDs.add(entityBId.longValue()));
                break;
            case "IS_SELF":
                entityIDs.add(successor.getEntityId());

                break;
            default:
                isSibling = true;
                break;
        }

        if(isSibling) {
            parentHierarchyNodeId = successor.getParentHierarchyNodeId();
            if(LongUtils.isEmpty(parentHierarchyNodeId)) {
                return new ArrayList<>();
            }
            return wbsEntryRepository.findPredecessorsByParentHierarchyNodeId(
                projectId,
                processId,
                entityType.getNameEn(),
                new HashSet(subEntityTypes),
                parentHierarchyNodeId
            );
        }
        if(CollectionUtils.isEmpty(entityIDs)) {
            return new ArrayList<>();
        }



        return wbsEntryRepository.findPredecessors(
            projectId,
            processId,
            entityType.getNameEn(),
            new HashSet(subEntityTypes),
            entityIDs
        );
    }


    /**
     * 取得 REDIS 中的 bpmnTaskRelation
     *@param orgId
     * @param projectId
     * @param moduleProcessDefinitionId
     * @param category
     * @return
     */
    @Override
    public BpmnTaskRelation getBpmnTaskRelationFromRedis(Long orgId,
                                                         Long projectId,
                                                         Long moduleProcessDefinitionId,
                                                         String category) {

        String redisKey = String.format(BPMN_TR_BY_CATEGORY_AT_REDIS_KEY,orgId.toString(),
            projectId.toString(),moduleProcessDefinitionId.toString(),category);
        BpmnTaskRelation bpmnTaskRelation = null;
        String bpmnTaskRelationStr = getRedisKey(redisKey);
        if(bpmnTaskRelationStr == null || bpmnTaskRelationStr.equalsIgnoreCase("null")) {
            if("StartEvent".equalsIgnoreCase(category) || "StopEvent".equalsIgnoreCase(category)) {
                String nodeType = category;
                bpmnTaskRelation = bpmnTaskRelationRepository.
                    findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeTypeAndDeletedIsFalse(
                        orgId,
                        projectId,
                        moduleProcessDefinitionId,
                        nodeType
                    );
            } else {
                bpmnTaskRelation = bpmnTaskRelationRepository.
                    findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryAndDeletedIsFalse(
                        orgId,
                        projectId,
                        moduleProcessDefinitionId,
                        category
                    );
            }

            String bpmnTaskRelationJson = StringUtils.toJSON(bpmnTaskRelation);
            if(StringUtils.isEmpty(bpmnTaskRelationJson)) {
                logger.error("bpmnTaskRelation CONVERT ERROR planRelationService + 753 Line, orgId:" + orgId.toString() + " projectId: " + projectId.toString() +
                    " moduleProcessDefinitionId: " + moduleProcessDefinitionId.toString() + " category:" + category );
            } else {
                setRedisKey(redisKey, StringUtils.toJSON(bpmnTaskRelation));
            }
        } else {
            bpmnTaskRelation = StringUtils.decode(bpmnTaskRelationStr, new TypeReference<BpmnTaskRelation>() {});
        }

        return bpmnTaskRelation;
    }


    /**
     * 取得 REDIS 中的 entitySubType 对应的 规则
     *@param orgId
     * @param projectId
     * @param entitySubType
     * @return
     */
    @Override
    public EntitySubTypeRule getEntityRuleFromRedis(Long orgId,
                                                     Long projectId,
                                                     String entitySubType) {

        String redisKey = String.format(ENTITY_CATEGORY_RULE_AT_REDIS_KEY,orgId.toString(),
            projectId.toString(),entitySubType);
        EntitySubTypeRule entityCategoryRule = null;

        String entityCategoryRuleStr = getRedisKey(redisKey);
        if(entityCategoryRuleStr == null || entityCategoryRuleStr.equalsIgnoreCase("null")) {
            List<EntitySubTypeRule> entityCategoryRules = entityCategoryRuleRepository.
                findByOrgIdAndProjectIdAndEntitySubTypeAndStatusAndDeletedIsFalse(
                    orgId,
                    projectId,
                    entitySubType,
                    EntityStatus.ACTIVE
                );
            if(CollectionUtils.isEmpty(entityCategoryRules)){
                logger.error("There is no entity rule for subType: " + entitySubType);
                return new EntitySubTypeRule();
            }

            entityCategoryRule = entityCategoryRules.get(0);

            setRedisKey(redisKey, StringUtils.toJSON(entityCategoryRule));
        } else {
            entityCategoryRule = StringUtils.decode(entityCategoryRuleStr, new TypeReference<EntitySubTypeRule>() {});
        }

        return entityCategoryRule;

    }

    /**
     * bpmn文件部署后 刷新内存redis中的bpmnTaskRelation
     * @param bpmnTaskRelations 部署的bpmn
     */
    @Override
    public void redeployBpmnInRedis(Long orgId,
                                    Long projectId,
                                    Long moduleProcessDefinitionId,
                                    List<BpmnTaskRelation> bpmnTaskRelations) {
        bpmnTaskRelations.forEach(bpmnTaskRelation -> {
            String category = bpmnTaskRelation.getCategory();
            if(StringUtils.isEmpty(category)) {
                return;
            }
            String redisKey = String.format(BPMN_TR_BY_CATEGORY_AT_REDIS_KEY,orgId.toString(),
                projectId.toString(),moduleProcessDefinitionId.toString(),category);
            String redisStr = getRedisKey(redisKey);
            if(redisStr == null || redisStr.equalsIgnoreCase("null")) {
                setRedisKey(redisKey, StringUtils.toJSON(bpmnTaskRelation));
            }

        });
    }

    /**
     * 执行流程节点上配置的 代理任务
     *
     * @param proxyName 代理
     */

    private List<Long> execRelationDelegate(Long orgId, Long projectId, String proxyName, Long entityId, String entityType, String relatedEntityType) {
        List<Long> entityIds = new ArrayList<>();
        if (!StringUtils.isEmpty(proxyName)) {
                try {
                    Class clazz = Class.forName(proxyName);
                    PlanRelationDelegateInterface delegate = (PlanRelationDelegateInterface) SpringContextUtils.getBean(clazz);
                    entityIds = delegate.getRelatedEntityIds(orgId, projectId, entityId, entityType, relatedEntityType);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
        }
        return entityIds;
    }

}
