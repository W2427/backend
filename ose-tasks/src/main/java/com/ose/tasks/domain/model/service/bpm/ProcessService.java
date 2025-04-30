package com.ose.tasks.domain.model.service.bpm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.exception.BusinessError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.process.ProcessBpmnRelationRepository;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.RelationReturnEnum;
import com.ose.tasks.vo.drawing.RuleType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProcessService extends StringRedisService implements ProcessInterface {

    private final static Logger logger = LoggerFactory.getLogger(ProcessService.class);
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
    private final BpmEntitySubTypeRepository entityCategoryRepository;
    /**
     * 工序阶段 操作仓库
     */
    private final BpmProcessStageRepository processStageRepository;

    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmProcessCategoryRepository processCategoryRepository;

    private final BpmProcessCheckListRepository processCheckListRepository;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final ProcessBpmnRelationRepository processBpmnRelationRepository;

    private final UserFeignAPI userFeignAPI;

    private final BpmProcessVersionRuleRepository versionRuleRepository;

    /**
     * 构造方法
     *
     * @param processRepository
     * @param relationRepository
     * @param entityCategoryRepository
     * @param processStageRepository
     * @param bpmReDeploymentRepository
     * @param processCheckListRepository
     * @param processCategoryRepository
     * @param bpmActivityTaskNodePrivilegeRepository
     * @param uploadFeignAPI
     * @param processBpmnRelationRepository
     */
    @Autowired
    public ProcessService(BpmProcessRepository processRepository,
                          EntitySubTypeProcessRelationRepository relationRepository,
                          BpmEntitySubTypeRepository entityCategoryRepository,
                          BpmProcessStageRepository processStageRepository,
                          BpmReDeploymentRepository bpmReDeploymentRepository,
                          BpmProcessCheckListRepository processCheckListRepository,
                          StringRedisTemplate stringRedisTemplate,
                          BpmProcessCategoryRepository processCategoryRepository,
                          BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
                          @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
                          ProcessBpmnRelationRepository processBpmnRelationRepository,
                          BpmProcessVersionRuleRepository versionRuleRepository,
                          UserFeignAPI userFeignAPI) {
        super(stringRedisTemplate);
        this.processRepository = processRepository;
        this.relationRepository = relationRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.processStageRepository = processStageRepository;
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.processCategoryRepository = processCategoryRepository;
        this.processCheckListRepository = processCheckListRepository;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.processBpmnRelationRepository = processBpmnRelationRepository;
        this.userFeignAPI = userFeignAPI;
        this.versionRuleRepository = versionRuleRepository;
    }

    /**
     * 获取工序列表。
     *
     * @param pagination 分页信息
     * @return 工序列表
     */
    @Override
    public Page<BpmProcess> getList(ProcessCriteriaDTO pagination, Long projectId, Long orgId) {
        return processRepository.search(pagination, projectId, orgId);

    }

    /**
     * 创建工序。
     *
     * @param processDTO 工序信息
     * @param projectId  工序分类id
     * @return 工序
     */
    @Override
    public BpmProcess create(ProcessDTO processDTO, Long projectId, Long orgId) {

        long count = processRepository.count();

        BpmProcess process = BeanUtils.copyProperties(processDTO, new BpmProcess());
        Optional<BpmProcessStage> stageOptional = processStageRepository.findById(processDTO.getProcessStageId());
        if (stageOptional.isPresent()) {
            process.setProcessStage(stageOptional.get());
        }
        Optional<BpmProcessCategory> categoryOptional = processCategoryRepository.findById(processDTO.getProcessCategoryId());
        if (categoryOptional.isPresent()) {
            process.setProcessCategory(categoryOptional.get());
        }
        process.setProjectId(projectId);
        process.setOrgId(orgId);
        process.setCreatedAt();
        process.setStatus(EntityStatus.ACTIVE);
        process.setOrderNo((int) (count + 1));

        processRepository.save(process);

        if (processDTO.getCheckListFileName() != null
            && !processDTO.getCheckListFileName().isEmpty()) {
            for (String tempName : processDTO.getCheckListFileName()) {
                logger.error("工序1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), tempName,
                    new FilePostDTO());
                logger.error("工序1 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {

                    BpmProcessCheckList checkList = new BpmProcessCheckList();
                    checkList.setCreatedAt();
                    checkList.setStatus(EntityStatus.ACTIVE);
                    checkList.setFileId(LongUtils.parseLong(fileEs.getId()));
                    checkList.setFileName(fileEs.getName());
                    checkList.setProcessId(process.getId());
                    processCheckListRepository.save(checkList);
                }
            }
        }


        return process;
    }

    /**
     * 编辑工序。
     *
     * @param processDTO 工序信息
     * @param projectId  工序id
     * @return 工序
     */
    @Override
    public BpmProcess modify(Long id, ProcessDTO processDTO, Long projectId, Long orgId) {
        Optional<BpmProcess> result = processRepository.findById(id);
        if (result.isPresent()) {
            BpmProcess process = result.get();
            process.setNameCn(processDTO.getNameCn());
            process.setNameEn(processDTO.getNameEn());
            process.setOrderNo(processDTO.getOrderNo());
            process.setMemo(processDTO.getMemo());
            process.setConstructionLogClass(processDTO.getConstructionLogClass());
            process.setProcessType(processDTO.getProcessType());
            process.setFuncPart(processDTO.getFuncPart());
            process.setOneOffReport(processDTO.getOneOffReport());
            String funcPart = process.getFuncPart() == null ? null : process.getFuncPart();
            String stage = null;
            String processName = process.getNameEn();
            Optional<BpmProcessStage> stageOptional = processStageRepository.findById(processDTO.getProcessStageId());
            if (stageOptional.isPresent()) {
                process.setProcessStage(stageOptional.get());
                stage = process.getProcessStage().getNameEn();
            }
            Optional<BpmProcessCategory> categoryOptional = processCategoryRepository.findById(processDTO.getProcessCategoryId());
            if (categoryOptional.isPresent()) {
                process.setProcessCategory(categoryOptional.get());
            }
            process.setLastModifiedAt();

            List<BpmProcessCheckList> listDB = processCheckListRepository.findByProcessId(process.getId());

            if (processDTO.getCheckListFileName() != null
                && !processDTO.getCheckListFileName().isEmpty()) {

                for (BpmProcessCheckList cl : listDB) {
                    String fileIdStr = cl.getFileId() == null ? "" : cl.getFileId().toString();
                    if (!processDTO.getCheckListFileName().contains(fileIdStr)) {
                        processCheckListRepository.delete(cl);
                    } else {
                        processDTO.getCheckListFileName().remove(fileIdStr);
                    }
                }
                for (String tempName : processDTO.getCheckListFileName()) {
                    logger.error("工序2 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), tempName,
                        new FilePostDTO());
                    logger.error("工序2 保存docs服务->结束");
                    FileES fileEs = fileESResBody.getData();
                    if (fileEs != null && fileEs.getId() != null) {

                        BpmProcessCheckList checkList = new BpmProcessCheckList();
                        checkList.setCreatedAt();
                        checkList.setStatus(EntityStatus.ACTIVE);
                        checkList.setFileId(LongUtils.parseLong(fileEs.getId()));
                        checkList.setFileName(fileEs.getName());
                        checkList.setProcessId(process.getId());
                        processCheckListRepository.save(checkList);
                    }
                }
            } else {
                for (BpmProcessCheckList cl : listDB) {
                    processCheckListRepository.delete(cl);
                }
            }

            setBpmProcess(process);
            setBpmProcess(funcPart, stage, processName, process);
            return processRepository.save(process);
        }
        return null;
    }

    /**
     * 添加实体类型。
     *
     * @param processId       工序id
     * @param entitySubTypeId 实体分类id
     * @return 工序
     */
    @Override
    public RelationReturnEnum addEntitySubType(Long processId, Long entitySubTypeId, Long projectId, Long orgId) {
        Optional<BpmEntityTypeProcessRelation> result = relationRepository.findByProcessIdAndEntitySubTypeIdAndStatus(processId, entitySubTypeId, EntityStatus.ACTIVE);
        if (!result.isPresent()) {
            BpmEntityTypeProcessRelation map = new BpmEntityTypeProcessRelation();
            map.setProjectId(projectId);
            map.setOrgId(orgId);
            map.setCreatedAt();
            map.setStatus(EntityStatus.ACTIVE);

            Optional<BpmEntitySubType> optionalEntity = entityCategoryRepository.findById(entitySubTypeId);
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
     * 删除实体类型。
     *
     * @param processId       工序id
     * @param entitySubTypeId 实体id
     * @return 工序
     */
    @Override
    public RelationReturnEnum deleteEntitySubType(Long processId, Long entitySubTypeId, Long projectId, Long orgId) {
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
     * 查询工序详细信息。
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    @Override
    public BpmProcess get(Long id, Long projectId, Long orgId) {
        Optional<BpmProcess> process = processRepository.findById(id);
        if (process.isPresent()) {
            BpmProcess result = process.get();
            List<BpmProcessCheckList> checkList = processCheckListRepository.findByProcessId(result.getId());
            result.setCheckList(checkList);
            return result;
        }
        return null;
    }

    /**
     * 取得工序详细信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @return 工序详细信息
     */
    @Override
    public BpmProcess get(
        final Long orgId,
        final Long projectId,
        final String stageName,
        final String processName
    ) {
        return processRepository
            .findByStageAndProcessCode(orgId, projectId, stageName, processName);
    }

    /**
     * 获取全部实体类型。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     */
    @Override
    public List<BpmEntitySubType> getEntitySubTypeList(Long projectId, Long orgId) {
        return entityCategoryRepository.findByProjectIdAndStatus(projectId, EntityStatus.ACTIVE);
    }

    /**
     * 批量排序。
     *
     * @param projectId 项目ID
     */
    @Override
    public boolean sort(List<SortDTO> sortDTOs, Long projectId, Long orgId) {
        for (SortDTO dto : sortDTOs) {
            Optional<BpmProcess> optionalProcess = processRepository.findById(dto.getId());
            if (optionalProcess.isPresent()) {
                BpmProcess process = optionalProcess.get();
                process.setOrderNo((short) dto.getOrderNo());
                process.setLastModifiedAt();
                processRepository.save(process);
            }
        }
        return true;
    }

    /**
     * 删除工序。
     */
    @Override
    public boolean delete(Long id, Long projectId, Long orgId) {
        Optional<BpmProcess> processOptional = processRepository.findById(id);
        if (processOptional.isPresent()) {
            BpmProcess process = processOptional.get();
            process.setStatus(EntityStatus.DELETED);
            process.setLastModifiedAt();
            processRepository.save(process);
            return true;
        }
        return false;
    }

    /**
     * 取得工序对应的实体类型列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public List<BpmEntitySubType> getEntitySubTypeByProcessId(Long id, Long projectId, Long orgId) {
        return relationRepository.findEntitySubTypeByProcessIdAndProjectIdAndOrgId(id, projectId, orgId);
    }

    /**
     * 批量添加实体类型。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @param dto
     * @return
     */
    @Override
    public boolean addEntityBatch(Long orgId, Long projectId, Long id, BatchAddRelationDTO dto) {
        for (Long entitySubTypeId : dto.getIds()) {
            Optional<BpmEntityTypeProcessRelation> result = relationRepository.findByProcessIdAndEntitySubTypeIdAndStatus(id, entitySubTypeId, EntityStatus.ACTIVE);
            if (!result.isPresent()) {
                BpmEntityTypeProcessRelation map = new BpmEntityTypeProcessRelation();
                map.setProjectId(projectId);
                map.setOrgId(orgId);
                map.setCreatedAt();
                map.setStatus(EntityStatus.ACTIVE);

                Optional<BpmEntitySubType> optionalEntity = entityCategoryRepository.findById(entitySubTypeId);
                if (optionalEntity.isPresent()) {
                    map.setEntitySubType(optionalEntity.get());
                }
                Optional<BpmProcess> optionalProcess = processRepository.findById(id);
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
     * 查询工序的实体对应关系。
     *
     * @param id
     * @return
     */
    @Override
    public List<BpmEntityTypeProcessRelation> getRelationByEntitySubTypeId(Long id) {
        List<BpmEntityTypeProcessRelation> l = relationRepository.findByProcessIdAndStatus(id, EntityStatus.ACTIVE);
        if (l != null && l.size() > 0) {
            return l;
        }
        return null;
    }

    /**
     * 获取工序对应的实体类型列表。
     */
    @Override
    public Page<BpmEntitySubType> getEntitySubTypeList(Long id, Long projectId, Long orgId, ProcessEntitySubTypeCriteriaDTO criteriaDTO) {
        if (LongUtils.isEmpty(criteriaDTO.getEntityTypeId())) {
            return relationRepository.findEntitySubTypeByProcessIdAndProjectIdAndOrgIdasPage(id, projectId, orgId, criteriaDTO.toPageable());
        }

        return relationRepository.findEntitySubTypeByProcessIdAndProjectIdAndOrgIdAndTypeId(id, projectId, orgId,
            criteriaDTO.getEntityTypeId(), criteriaDTO.toPageable());
    }

    /**
     * 获取全部工序对应的工序阶段列表。
     */
    @Override
    public List<BpmProcessStage> getProcessStageList(Long projectId, Long orgId) {
        return processRepository.findProcessStageByProjectIdAndOrgId(projectId, orgId);
    }

    /**
     * 获取工序对应的实体类型分类列表。
     */
    @Override
    public List<BpmEntityType> getEntityTypeList(Long id, Long projectId, Long orgId) {
        return relationRepository.findEntityTypesByProcessIdAndProjectIdAndOrgId(id, projectId, orgId);
    }

    /**
     * 获取工序对应的工作流模型。
     */
    @Override
    public BpmReDeployment findActivityModel(Long orgId, Long projectId, Long processId) {
        return bpmReDeploymentRepository.findLastVerisonByProcessId(orgId, projectId, processId);
    }

    /**
     * 获取全部工序对应的工序分类列表。
     */
    @Override
    public List<BpmProcessCategory> getProcessCategoryList(Long projectId, Long orgId) {
        return processRepository.findProcessCategoryByProjectIdAndOrgId(projectId, orgId);
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

        List<Long> processIDs = new ArrayList<>();

        for (T entity : entities) {

            if (entity instanceof RatedTime && ((RatedTime) entity).getProcessId() != null) {
                processIDs.add(((RatedTime) entity).getProcessId());
            }

            if (entity instanceof RatedTimeCriterion && ((RatedTimeCriterion) entity).getProcessId() != null) {
                processIDs.add(((RatedTimeCriterion) entity).getProcessId());
            }
        }

        List<BpmProcess> processes = processRepository.findByIdIn(processIDs);

        for (BpmProcess process : processes) {
            included.put(process.getId(), process);
        }

        return included;
    }

    /**
     * 根据英文名查询工序。
     */
    @Override
    public List<BpmProcess> findByOrgIdAndProjectIdAndNameEn(Long orgId, Long projectId, String nameEn) {

        return processRepository.findByOrgIdAndProjectIdAndNameEnAndStatus(orgId, projectId, nameEn, EntityStatus.ACTIVE);
    }

    /**
     * 根据阶段和工序CODE获取工序信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param stage       阶段CODE
     * @param processCode 工序CODE
     * @return 工序信息
     */
    @Override
    public BpmProcess findByStageAndProcessCode(Long orgId, Long projectId, String stage, String processCode) {
        return processRepository.findByStageAndProcessCode(orgId, projectId, stage, processCode);
    }

    /**
     * 查询工序权限列表
     */
    @Override
    public List<TaskPrivilegeDTO> getProcessPrivileges(Long orgId, Long projectId, Long id) {
        List<BpmActivityTaskNodePrivilege> list = bpmActivityTaskNodePrivilegeRepository
            .findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, id, EntityStatus.ACTIVE);
        Map<String, Long> categoryMap = new HashMap<>();
        for (BpmActivityTaskNodePrivilege p : list) {
            if (p.getCategory() != null) {
                categoryMap.put(p.getCategory(), p.getAssignee());
            }
        }
        List<TaskPrivilegeDTO> result = new ArrayList<>();
        Iterator<String> iterator = categoryMap.keySet().iterator();
        while (iterator.hasNext()) {
            String entitySubType = iterator.next();
            String displayName = UserPrivilege.getByName(entitySubType).getDisplayName();
            String category = UserPrivilege.getByName(entitySubType).getCode();
            if (displayName.endsWith("执行")) {
                displayName = displayName.substring(0, displayName.length() - 2);
            }
            result.add(new TaskPrivilegeDTO(category, entitySubType, displayName, categoryMap.get(entitySubType)));
        }

        if (!result.isEmpty()) {
            for (TaskPrivilegeDTO taskPrivilegeDTO : result) {
                if (null != taskPrivilegeDTO.getAssignee()) {
                    UserProfile userProfile = userFeignAPI.get(taskPrivilegeDTO.getAssignee()).getData();
                    if (null != userProfile) {
                        TaskPrivilegeUserDTO user = new TaskPrivilegeUserDTO();
                        user.setId(userProfile.getId());
                        user.setName(userProfile.getName());

                        taskPrivilegeDTO.setUser(user);
                    }
                }

            }
        }


        return result;
    }

    @Override
    public boolean setProcessPrivileges(Long orgId, Long projectId, Long id, TaskPrivilegeDTO dTO) {
        List<BpmActivityTaskNodePrivilege> list = bpmActivityTaskNodePrivilegeRepository
            .findByOrgIdAndProjectIdAndProcessIdAndCategoryAndStatus(orgId, projectId, id, dTO.getEntitySubType(), EntityStatus.ACTIVE);
        for (BpmActivityTaskNodePrivilege p : list) {
            p.setAssignee(dTO.getAssignee());
            bpmActivityTaskNodePrivilegeRepository.save(p);
            System.out.println("工序分配人员： " + "用户id：" + dTO.getAssignee());
        }
        return true;
    }

    @Override
    public List<ProcessKeyDTO> getProcessKeys(Long orgId, Long projectId) {
        List<String> list = processRepository.getProcessKeys(orgId, projectId, "ACTIVE");
        List<ProcessKeyDTO> resultList = new ArrayList<ProcessKeyDTO>();

        if (list != null && list.size() > 0) {
            for (String item : list) {
                ProcessKeyDTO processKeyDTO = new ProcessKeyDTO();
                processKeyDTO.setProcessKey(item);
                resultList.add(processKeyDTO);
            }
        }
        return resultList;
    }

    @Override
    public BpmProcess getBpmProcess(Long projectId, String stage, String process) {
        stage = stage == null ? "" : stage;
//        String funcPartStr = funcPart == null?"":funcPart;
        String processNameKey = String.format(RedisKey.PROCESS_NAME.getDisplayName(), projectId.toString(), stage, process);
        String processStr = getRedisKey(processNameKey);

        if (StringUtils.isEmpty(processStr)) {
            BpmProcess bpmProcess = null;
            bpmProcess = processRepository.findByProjectIdAndStageNameAndName(projectId, stage, process).orElse(null);
            if (bpmProcess != null) {
                processStr = StringUtils.toJSON(bpmProcess);
                setRedisKey(processNameKey, processStr);
            }
            return bpmProcess;

        } else {
            return StringUtils.decode(processStr, new TypeReference<BpmProcess>() {
            });
        }

    }

    @Override
    public BpmProcess getBpmProcess(Long processId) {
        String processIdKey = String.format(RedisKey.PROCESS_ID.getDisplayName(), processId);
        String processStr = getRedisKey(processIdKey);

        if (StringUtils.isEmpty(processStr)) {
            BpmProcess bpmProcess = null;
            bpmProcess = processRepository.findById(processId).orElse(null);
            if (bpmProcess != null) {
                processStr = StringUtils.toJSON(bpmProcess);
                setRedisKey(processIdKey, processStr);
            }
            return bpmProcess;
        } else {
            return StringUtils.decode(processStr, new TypeReference<BpmProcess>() {
            });
        }

    }


    @Override
    public void setBpmProcess(String funcPart, String stage, String process, BpmProcess bpmProcess) {
        stage = stage == null ? "" : stage;
        funcPart = funcPart == null ? "" : funcPart;
        Long projectId = bpmProcess.getProjectId();
        String processNameKey = String.format(RedisKey.PROCESS_NAME.getDisplayName(), projectId.toString(), funcPart, stage, process);

        String processStr = StringUtils.toJSON(bpmProcess);

        setRedisKey(processNameKey, processStr);
    }

    @Override
    public void setBpmProcess(BpmProcess bpmProcess) {
        Long processId = bpmProcess.getId();
        String processIdKey = String.format(RedisKey.PROCESS_ID.getDisplayName(), processId.toString());

        String processStr = StringUtils.toJSON(bpmProcess);

        setRedisKey(processIdKey, processStr);
    }

    /**
     * bpmn文件部署后 刷新内存redis中的bpmnTaskRelation
     *
     * @param processBpmnRelations 部署的bpmn
     */
    @Override
    public void redeployBpmnInRedis(Long projectId,
                                    Long processId,
                                    int version,
                                    List<ProcessBpmnRelation> processBpmnRelations) {
        processBpmnRelations.forEach(processBpmnRelation -> {
            String taskDefKey = processBpmnRelation.getNodeId();
            if (StringUtils.isEmpty(taskDefKey)) {
                return;
            }
            String redisKey = String.format(RedisKey.PROCESS_NODE_BPMN.getDisplayName(), processId.toString(), String.valueOf(version), taskDefKey);
            String redisStr = getRedisKey(redisKey);
            if (redisStr == null || redisStr.equalsIgnoreCase("null")) {
                setRedisKey(redisKey, StringUtils.toJSON(processBpmnRelation));
            }

        });
    }

    /**
     * bpmn文件部署后 取得内存redis中的bpmnTaskRelation
     *
     * @param taskDefKey 部署的bpmn PROCESS_BPMN:ProcessId:TaskDefKey
     */
    @Override
    public ProcessBpmnRelation getBpmnRelation(Long projectId,
                                               Long processId,
                                               int bpmnVersion,
                                               String taskDefKey) {

        if (StringUtils.isEmpty(taskDefKey) || LongUtils.isEmpty(processId)) {
            System.out.println("taskDefKey is NUll or processId is null");
            return null;
        }
        ProcessBpmnRelation processBpmnRelation = new ProcessBpmnRelation();
        String redisKey = String.format(RedisKey.PROCESS_NODE_BPMN.getDisplayName(), processId.toString(), String.valueOf(bpmnVersion), taskDefKey);
        String redisStr = getRedisKey(redisKey);
        if (redisStr == null || redisStr.equalsIgnoreCase("null")) {
            processBpmnRelation = processBpmnRelationRepository.findByProjectIdAndProcessIdAndBpmnVersionAndNodeId(
                projectId, processId, bpmnVersion, taskDefKey
            );
            if (processBpmnRelation != null) {
                setRedisKey(redisKey, StringUtils.toJSON(processBpmnRelation));
            }

        } else {
            processBpmnRelation = StringUtils.decode(redisStr, new TypeReference<ProcessBpmnRelation>() {
            });
            System.out.println("DE-JSON");
        }
        return processBpmnRelation;
    }

    @Override
    public boolean setProcessVersionRule(Long orgId, Long projectId, Long processId, BpmProcessVersionRuleDTO dto) {

        BpmProcessVersionRule versionRule = versionRuleRepository.findByOrgIdAndProjectIdAndProcessId(orgId, projectId, processId);

        if (versionRule != null) {
            BeanUtils.copyProperties(dto, versionRule);
            versionRule.setOrgId(orgId);
            versionRule.setProjectId(projectId);
            versionRule.setProcessId(processId);
            versionRule.setCreatedAt();
            versionRule.setStatus(EntityStatus.ACTIVE);
            if (dto.getRuleType() != null) {
                versionRule.setRuleType(RuleType.valueOf(dto.getRuleType()));
            }

            versionRuleRepository.save(versionRule);
        } else {
            BpmProcessVersionRule result = new BpmProcessVersionRule();
            BeanUtils.copyProperties(dto, result);
            if (dto.getRuleType() != null) {
                result.setRuleType(RuleType.valueOf(dto.getRuleType()));
            }
            result.setOrgId(orgId);
            result.setProjectId(projectId);
            result.setProcessId(processId);
            result.setStatus(EntityStatus.ACTIVE);
            result.setLastModifiedAt();
            versionRuleRepository.save(result);
        }

        return true;
    }

    @Override
    public BpmProcessVersionRule getVersionRule(Long orgId, Long projectId, Long processId) {
        return versionRuleRepository.findByOrgIdAndProjectIdAndProcessId(orgId, projectId, processId);
    }

    @Override
    public List<ProcessHierarchyDTO> getHierarchy(Long orgId, Long projectId) {
        List<ProcessHierarchyDTO> resultList = new ArrayList<>();

        List<BpmProcessStage> bpmProcessStages = processStageRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
        if (bpmProcessStages.size() > 0) {
            for (BpmProcessStage bpmProcessStage : bpmProcessStages) {
                ProcessHierarchyDTO stageDTO = new ProcessHierarchyDTO();
                stageDTO.setLabel(bpmProcessStage.getNameEn());
                stageDTO.setValue(bpmProcessStage.getId());
                List<ProcessHierarchyDTO> dto = new ArrayList<>();
                List<BpmProcess> bpmProcesses = processRepository.findProcessesByStageId(bpmProcessStage.getId());
                if (bpmProcesses.size() > 0) {
                    for (BpmProcess bpmProcess : bpmProcesses) {
                        ProcessHierarchyDTO processDTO = new ProcessHierarchyDTO();
                        processDTO.setLabel(bpmProcess.getNameEn());
                        processDTO.setValue(bpmProcess.getId());
                        dto.add(processDTO);
                    }
                    stageDTO.setChildren(dto);
                }
                resultList.add(stageDTO);
            }
        }
        return resultList;
    }
}
