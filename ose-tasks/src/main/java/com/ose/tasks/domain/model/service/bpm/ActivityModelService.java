package com.ose.tasks.domain.model.service.bpm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.process.ProcessBpmnRelationRepository;
import com.ose.tasks.dto.bpm.ModelDeployDTO;
import com.ose.tasks.dto.bpm.ModelTaskNode;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.ProcessBpmnRelation;
import com.ose.tasks.util.BPMNUtils;
import com.ose.tasks.util.BpmnCommonUtils;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityCategoryRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityNodePrivilegeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityTaskNodePrivilegeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmReDeploymentRepository;
import com.ose.tasks.dto.bpm.ActivitiModelCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmActivityCategory;
import com.ose.tasks.entity.bpm.BpmActivityNodePrivilege;
import com.ose.tasks.entity.bpm.BpmActivityTaskNodePrivilege;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.vo.SuspensionState;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.auth.vo.UserPrivilege;

/**
 * 模型操作服务。
 */
@Component
public class ActivityModelService implements ActivityModelInterface {

    private final static Logger logger = LoggerFactory.getLogger(ActivityModelService.class);


    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmActivityCategoryRepository bpmActivityCategoryRepository;

    private final BpmProcessRepository bpmProcessRepository;

    private final BpmActivityNodePrivilegeRepository bpmActivityNodePrivilegeRepository;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;


    private final ProcessBpmnRelationRepository processBpmnRelationRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final ProcessInterface processService;



    @Value("${application.files.temporary}")
    private String temporaryDir;


    /**
     * 构造方法
     * @param bpmReDeploymentRepository
     * @param bpmProcessRepository
     * @param bpmActivityNodePrivilegeRepository
     * @param bpmActivityTaskNodePrivilegeRepository
     * @param bpmActivityCategoryRepository
     * @param processBpmnRelationRepository
     * @param uploadFeignAPI
     * @param processService
     */
    @Autowired
    public ActivityModelService(
        BpmReDeploymentRepository bpmReDeploymentRepository,
        BpmProcessRepository bpmProcessRepository,
        BpmActivityNodePrivilegeRepository bpmActivityNodePrivilegeRepository,
        BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
        BpmActivityCategoryRepository bpmActivityCategoryRepository,
        ProcessBpmnRelationRepository processBpmnRelationRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        ProcessInterface processService) {
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmActivityNodePrivilegeRepository = bpmActivityNodePrivilegeRepository;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.bpmActivityCategoryRepository = bpmActivityCategoryRepository;
        this.processBpmnRelationRepository = processBpmnRelationRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.processService = processService;
    }

    /**
     * 保存流程模型信息
     */
    @Override
    public BpmReDeployment createBpmModels(Long orgId, Long projectId, ModelDeployDTO modelDeployDTO, OperatorDTO operatorDTO) {

        Optional<BpmProcess> bpmProcessOpt = bpmProcessRepository.findById(modelDeployDTO.getBpmProcessId());
        if (!bpmProcessOpt.isPresent()) {
            return new BpmReDeployment();
        }

        BpmProcess bpmProcess = bpmProcessOpt.get();
        BpmProcessStage bpmProcessStage = bpmProcess.getProcessStage();
        modelDeployDTO.setCategory(bpmProcess.getProcessCategory().getNameEn());
        modelDeployDTO.setProjectId(projectId);
        modelDeployDTO.setProcessKey(bpmProcess.getId());
        String processName = bpmProcessStage.getNameEn() + "-" + bpmProcess.getNameEn();
        modelDeployDTO.setProcessName(processName);
        String tempFileId = modelDeployDTO.getTemporaryName();
        Long bpmProcessId = modelDeployDTO.getBpmProcessId();
        final File bpmnFile = new File(temporaryDir, tempFileId);
        InputStream bpmnStream;


        try {
            bpmnStream = new FileInputStream(bpmnFile);
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
            throw new NotFoundError();
        }
        BpmReDeployment entity = new BpmReDeployment();
        List<ModelTaskNode> modelTaskNodes = new ArrayList<>();

        BpmnCommonUtils.setProcessDefinitionKey(bpmnFile, "_" + bpmProcessId.toString());

        logger.error("流程模块1 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            modelDeployDTO.getTemporaryName(), new FilePostDTO());
        logger.error("流程模块1 保存docs服务->结束");
        FileES fileEs = fileESResBody.getData();
        String filePath = fileEs.getPath();
        if (filePath == null) {
            return new BpmReDeployment();

        }


        BpmnModel bpmnModel = null;

        bpmnModel = BpmnCommonUtils.readBpmnFile(bpmnStream);

        entity.setOrgId(orgId);
        entity.setProjectId(projectId);
        entity.setProcessName(processName);
        entity.setOperator(operatorDTO.getName());
        entity.setActReDeploymentId("0");
        entity.setFilePath(filePath);
        entity.setFileId(LongUtils.parseLong(fileEs.getId()));
        entity.setFileName(fileEs.getName());

        entity.setDeployTime(new Date());
        entity.setDescription(modelDeployDTO.getDescription());
        entity.setEntitySubType(modelDeployDTO.getCategory());
        entity.setProcessId(bpmProcessId);



        entity.setSuspensionState(SuspensionState.ACTIVE);

        BpmReDeployment maxVersionEntity = bpmReDeploymentRepository.findFirstByProjectIdAndProcessIdOrderByVersionDesc(projectId, bpmProcessId);
        int version = 0;
        if(maxVersionEntity != null) {
            version = maxVersionEntity.getVersion() + 1;
        }
        entity.setVersion(version);
        entity.setProcDefId(bpmProcessId.toString() + ":" + String.valueOf(version));

        entity.setStatus(EntityStatus.ACTIVE);
        entity.setCreatedAt();
        entity.setLastModifiedAt();
        entity.setEntitySubType(bpmProcess.getProcessCategory().getNameEn());



        if (bpmnModel != null) {

            List<BpmnTaskRelation> bpmnTaskRelationBases =
                BPMNUtils.resolveBpmnTaskRelation(bpmnModel, orgId, projectId, entity.getId());

            if (!CollectionUtils.isEmpty(bpmnTaskRelationBases)) {
                List<ProcessBpmnRelation> processBpmnRelations = new ArrayList<>();

                bpmnTaskRelationBases.forEach(bpmnTaskRelationBase -> {
                    if(!(bpmnTaskRelationBase.getNodeType().equalsIgnoreCase("STARTEVENT") ||
                        bpmnTaskRelationBase.getNodeType().equalsIgnoreCase("ENDEVENT"))) {
                        ModelTaskNode modelTaskNode = new ModelTaskNode();
                        modelTaskNode.setCategory(bpmnTaskRelationBase.getCategory());
                        modelTaskNode.setTaskDefKey(bpmnTaskRelationBase.getNodeId());
                        modelTaskNode.setTaskName(bpmnTaskRelationBase.getNodeName());
                        modelTaskNodes.add(modelTaskNode);
                    }
                    ProcessBpmnRelation processBpmnRelation = new ProcessBpmnRelation();
                    BeanUtils.copyProperties(bpmnTaskRelationBase, processBpmnRelation);
                    processBpmnRelation.setOrgId(orgId);
                    processBpmnRelation.setProjectId(projectId);
                    processBpmnRelation.setStatus(EntityStatus.ACTIVE);
                    processBpmnRelation.setDeleted(false);
                    processBpmnRelation.setProcessBpmnDeployId(entity.getId());
                    processBpmnRelation.setProcessId(bpmProcessId);
                    processBpmnRelation.setBpmnVersion(entity.getVersion());
                    processBpmnRelationRepository.save(processBpmnRelation);
                    processBpmnRelations.add(processBpmnRelation);
                });
                processService.redeployBpmnInRedis(projectId, entity.getId(), entity.getVersion(), processBpmnRelations);
            }
        }
        generateBpmActivityNodeInfo(orgId, projectId, bpmProcessId, modelTaskNodes);


        return bpmReDeploymentRepository.save(entity);



    }

    /**
     * 流程模型列表
     */
    @Override
    public Page<BpmReDeployment> list(Long orgId, Long projectId, ActivitiModelCriteriaDTO criteriaDTO,
                                      PageDTO page) {
        return bpmReDeploymentRepository.list(orgId, projectId, criteriaDTO, page);
    }

    /**
     * 获取流程模型信息
     */
    @Override
    public BpmReDeployment findByProcDefId(String procDefId) {
        Optional<BpmReDeployment> op = bpmReDeploymentRepository.findByProcDefId(procDefId);
        if (op.isPresent()) {
            BpmReDeployment result = op.get();
            return result;
        }
        return null;
    }

    /**
     * 修改挂起状态
     */
    @Override
    public boolean updateSuspendState(String procDefId, SuspensionState suspend) {
        Optional<BpmReDeployment> op = bpmReDeploymentRepository.findByProcDefId(procDefId);
        if (op.isPresent()) {
            BpmReDeployment result = op.get();
            result.setSuspensionState(suspend);
            bpmReDeploymentRepository.save(result);
            return true;
        }
        return false;
    }

    /**
     * 获取流程模型分类
     */
    @Override
    public List<BpmActivityCategory> getCategories(Long orgId, Long projectId) {
        return bpmActivityCategoryRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
    }

    /**
     * 生成
     */
    @Override
    public void generateBpmActivityNodeInfo(Long orgId, Long projectId, Long processId,
                                            List<ModelTaskNode> taskNodes) {
        if (taskNodes == null) {
            return;
        }


        List<BpmActivityTaskNodePrivilege> nodePrivileges = bpmActivityTaskNodePrivilegeRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, processId, EntityStatus.ACTIVE);
        for (BpmActivityTaskNodePrivilege nodePrivilege : nodePrivileges) {
            boolean deleteFlg = true;


            for (ModelTaskNode taskNode : taskNodes) {
                String category = taskNode.getCategory();
                String subcategory = null;
                if (category != null
                    && category.contains("/")) {
                    category = category.substring(0, category.indexOf("/"));
                    subcategory = taskNode.getCategory().replace(category, "").substring(1);
                }
                if (StringUtils.trim(nodePrivilege.getTaskDefKey()).equals(StringUtils.trim(taskNode.getTaskDefKey()))
                    && StringUtils.trim(nodePrivilege.getTaskName()).equals(StringUtils.trim(taskNode.getTaskName()))
                    && StringUtils.trim(nodePrivilege.getCategory()).equals(StringUtils.trim(category))
                    && StringUtils.trim(nodePrivilege.getSubCategory()).equals(StringUtils.trim(subcategory))
                    ) {
                    deleteFlg = false;
                    break;
                }
            }
            if (deleteFlg) {
                nodePrivilege.setStatus(EntityStatus.DELETED);
                bpmActivityTaskNodePrivilegeRepository.save(nodePrivilege);
            }
        }

        for (ModelTaskNode taskNode : taskNodes) {
            String category = taskNode.getCategory();
            String subcategory = null;
            if (category != null
                && category.contains("/")) {
                category = category.substring(0, category.indexOf("/"));
                subcategory = taskNode.getCategory().replace(category, "").substring(1);
            }
            boolean insertFlg = true;
            for (BpmActivityTaskNodePrivilege nodePrivilege : nodePrivileges) {
                if (StringUtils.trim(nodePrivilege.getTaskDefKey()).equals(StringUtils.trim(taskNode.getTaskDefKey()))
                    && StringUtils.trim(nodePrivilege.getTaskName()).equals(StringUtils.trim(taskNode.getTaskName()))
                    && StringUtils.trim(nodePrivilege.getCategory()).equals(StringUtils.trim(category))
                    && StringUtils.trim(nodePrivilege.getSubCategory()).equals(StringUtils.trim(subcategory))
                    ) {
                    insertFlg = false;
                    break;
                }
            }
            if (insertFlg) {
                BpmActivityTaskNodePrivilege bpmActivityTaskNode = new BpmActivityTaskNodePrivilege();
                bpmActivityTaskNode.setOrgId(orgId);
                bpmActivityTaskNode.setProjectId(projectId);
                bpmActivityTaskNode.setProcessId(processId);
                bpmActivityTaskNode.setTaskDefKey(taskNode.getTaskDefKey());
                bpmActivityTaskNode.setTaskName(taskNode.getTaskName());
                bpmActivityTaskNode.setCategory(category);
                bpmActivityTaskNode.setSubCategory(subcategory);
                bpmActivityTaskNode.setCreatedAt(new Date());
                bpmActivityTaskNode.setStatus(EntityStatus.ACTIVE);
                bpmActivityTaskNodePrivilegeRepository.save(bpmActivityTaskNode);
            }
        }


        bpmActivityNodePrivilegeRepository.updateByOrgIdAndProjectIdAndProcessId(orgId, projectId, processId);



        Set<String> insertedCategorys = new HashSet<String>();


        for (ModelTaskNode taskNode : taskNodes) {
            if (taskNode.getCategory() == null) {
                continue;
            }

            String category = taskNode.getCategory();
            if (category != null
                && category.contains("/")) {
                category = category.substring(0, category.indexOf("/"));
            }

            UserPrivilege userPrivilege = UserPrivilege.BPM_TASK_CATEGORY_NOT_FOUND;
            try {
                userPrivilege = UserPrivilege.getByName(category);
            } catch (Throwable e) {
                userPrivilege = UserPrivilege.BPM_TASK_CATEGORY_NOT_FOUND;
                System.out.println(category +" value not one of declared Enum");
            }

            if (!insertedCategorys.contains(category)) {
                BpmActivityNodePrivilege bpmActivityNodePrivilege = new BpmActivityNodePrivilege();
                bpmActivityNodePrivilege.setOrgId(orgId);
                bpmActivityNodePrivilege.setProjectId(projectId);
                bpmActivityNodePrivilege.setProcessId(processId);
                bpmActivityNodePrivilege.setPrivilege(userPrivilege);
                insertedCategorys.add(category);
                bpmActivityNodePrivilege.setCreatedAt(new Date());
                bpmActivityNodePrivilege.setStatus(EntityStatus.ACTIVE);
                bpmActivityNodePrivilegeRepository.save(bpmActivityNodePrivilege);
            }

        }
    }

}
