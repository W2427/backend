package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.bpm.BpmActTaskConfigRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmReDeploymentRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.bpm.BpmActTaskConfigCreateDTO;
import com.ose.tasks.dto.bpm.BpmActTaskConfigDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.util.*;


@Component
public class ActTaskConfigService implements ActTaskConfigInterface {

    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmProcessRepository processRepository;

    private final BpmActTaskConfigRepository bpmActTaskConfigRepository;

    private final ProjectInterface projectService;

    @Autowired
    public ActTaskConfigService(BpmReDeploymentRepository bpmReDeploymentRepository,
                                BpmProcessRepository processRepository,
                                BpmActTaskConfigRepository bpmActTaskConfigRepository, ProjectInterface projectService) {
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.processRepository = processRepository;
        this.bpmActTaskConfigRepository = bpmActTaskConfigRepository;
        this.projectService = projectService;
    }

    /**
     * 根据工序id和任务id获取流程节点配置
     */
    @Override
    public List<BpmActTaskConfig> getConfigByProcDefIdAndActTaskId(BpmProcess bpmProcess, BpmRuTask ruTask,
                                                                   BpmActTaskConfigDelegateType delegateType) {
        if (bpmProcess != null) {

            return bpmActTaskConfigRepository.findByOrgIdAndProjectIdAndProcessCategoryAndProcessTypeAndTaskDefKeyAndDelegateTypeAndStatusOrderByOrderNoAsc(
                    bpmProcess.getOrgId(),
                    bpmProcess.getProjectId(),
                    bpmProcess.getProcessCategory().getNameEn(),
                    ProcessType.valueOf(bpmProcess.getProcessType().name()),
                    ruTask.getTaskDefKey(),
                    delegateType,
                    EntityStatus.ACTIVE);
            } else {
                return null;
        }
    }

    /**
     * 根据工序id和任务id获取流程节点配置
     */
    @Override
    public List<BpmActTaskConfig> getConfigByProcDefIdAndActTaskId(BpmProcess bpmProcess, BpmRuTask ruTask,
                                                                   BpmActTaskConfigDelegateStage delegateStage) {
                String processKey = bpmProcess.getProcessStage().getNameEn() + "-" + bpmProcess.getNameEn();

                List<Tuple> proxies = bpmActTaskConfigRepository.findPrepareByTaskDefKey(
                    bpmProcess.getOrgId(),
                    bpmProcess.getProjectId(),
                    ruTask.getTaskDefKey(),
                    ruTask.getTaskType(),
                    processKey,
                    bpmProcess.getProcessType().name(),
                    bpmProcess.getProcessCategory().getNameEn(),
                    EntityStatus.ACTIVE.name()
                );
                List actTaskConfigs = new ArrayList();
                proxies.forEach(proxy -> {
                    BpmActTaskConfig actTaskConfig = new BpmActTaskConfig();
                    actTaskConfig.setProxy((String)proxy.get("proxy"));
                    actTaskConfigs.add(actTaskConfig);
                });
                return actTaskConfigs;
    }

    @Override
    public Map<String, List<String>> getConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey,
                                                           String taskType, String processKey, ProcessType processType,
                                                           String processCategory) {


        String processTypeStr = processType == null ? null : processType.name();

        List<Tuple> actTaskConfigs = bpmActTaskConfigRepository.findAllByTaskDefKey(orgId, projectId, taskDefKey, taskType,
            processKey, processTypeStr, processCategory, EntityStatus.ACTIVE.name());

        Map<String, String> prepareDelegateMap = new HashMap<>();
        Map<String, String> preDelegateMap = new HashMap<>();
        Map<String, String> postDelegateMap = new HashMap<>();
        Map<String, String> completeDelegateMap = new HashMap<>();

        for (Tuple actTaskConfig : actTaskConfigs) {

            String delegateType = (String) actTaskConfig.get("delegate_type");
            String delegateStage = (String) actTaskConfig.get("delegate_stage");
            String proxy = (String) actTaskConfig.get("proxy");
            switch (delegateStage) {
                case "PREPARE":
                    if (!prepareDelegateMap.containsKey(delegateType)) {
                        prepareDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "PRE":
                    if (!preDelegateMap.containsKey(delegateType)) {
                        preDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "POST":
                    if (!postDelegateMap.containsKey(delegateType)) {
                        postDelegateMap.put(delegateType, proxy);
                    }
                    break;

                case "COMPLETE":
                    if (!completeDelegateMap.containsKey(delegateType)) {
                        completeDelegateMap.put(delegateType, proxy);
                    }
                    break;
            }

        }
        Map<String, List<String>> taskDelegateMap = new HashMap<>();
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.PREPARE.name(), new ArrayList<>(prepareDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.PRE.name(), new ArrayList<>(preDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.POST.name(), new ArrayList<>(postDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.COMPLETE.name(), new ArrayList<>(completeDelegateMap.values()));


        return taskDelegateMap;

    }

    @Override
    public Map<String, List<String>> getSuspendConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey,
                                                           String taskType, String processKey, ProcessType processType,
                                                           String processCategory) {


        String processTypeStr = processType == null ? null : processType.name();

        List<Tuple> actTaskConfigs = bpmActTaskConfigRepository.findAllByTaskDefKey(orgId, projectId, taskDefKey, taskType,
            processKey, processTypeStr, processCategory, EntityStatus.ACTIVE.name());

        Map<String, String> postDelegateMap = new HashMap<>();

        for (Tuple actTaskConfig : actTaskConfigs) {

            String delegateType = (String) actTaskConfig.get("delegate_type");
            String delegateStage = (String) actTaskConfig.get("delegate_stage");
            String proxy = (String) actTaskConfig.get("proxy");
            switch (delegateStage) {
                case "SUSPEND":
                    if (!postDelegateMap.containsKey(delegateType)) {
                        postDelegateMap.put(delegateType, proxy);
                    }
                    break;
            }

        }
        Map<String, List<String>> taskDelegateMap = new HashMap<>();

        taskDelegateMap.put(BpmActTaskConfigDelegateStage.SUSPEND.name(), new ArrayList<>(postDelegateMap.values()));

        return taskDelegateMap;

    }

    @Override
    public Map<String, List<String>> getBatchTaskConfigByTaskDefKey(Long orgId, Long projectId, String taskDefKey,
                                                                    String taskType, String processKey, ProcessType processType,
                                                                    String processCategory) {

        String processTypeStr = processType == null ? null : processType.name();
        List<Tuple> actTaskConfigs = bpmActTaskConfigRepository.findAllByTaskDefKey(orgId, projectId, taskDefKey, taskType,
            processKey, processTypeStr, processCategory, EntityStatus.ACTIVE.name());

        Map<String, String> batchTaskPrepareDelegateMap = new HashMap<>();
        Map<String, String> batchTaskPreDelegateMap = new HashMap<>();
        Map<String, String> batchTaskPostDelegateMap = new HashMap<>();
        Map<String, String> batchTaskCompleteDelegateMap = new HashMap<>();
        Map<String, String> batTaskRevocationPreDelegateMap = new HashMap<>();
        Map<String, String> batTaskRevocationPostDelegateMap = new HashMap<>();


        for (Tuple actTaskConfig : actTaskConfigs) {
            String delegateType = (String) actTaskConfig.get("delegate_type");
            String delegateStage = (String) actTaskConfig.get("delegate_stage");
            String proxy = (String) actTaskConfig.get("proxy");
            switch (delegateStage) {
                case "BATCH_PREPARE":
                    if (!batchTaskPrepareDelegateMap.containsKey(delegateType)) {
                        batchTaskPrepareDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "BATCH_PRE":
                    if (!batchTaskPreDelegateMap.containsKey(delegateType)) {
                        batchTaskPreDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "BATCH_POST":
                    if (!batchTaskPostDelegateMap.containsKey(delegateType)) {
                        batchTaskPostDelegateMap.put(delegateType, proxy);
                    }
                    break;

                case "BATCH_COMPLETE":
                    if (!batchTaskCompleteDelegateMap.containsKey(delegateType)) {
                        batchTaskCompleteDelegateMap.put(delegateType, proxy);
                    }
                    break;

                case "PRE_BATCH_REVOCATION":
                    if (!batTaskRevocationPreDelegateMap.containsKey(delegateType)) {
                        batTaskRevocationPreDelegateMap.put(delegateType, proxy);
                    }
                    break;

                case "POST_BATCH_REVOCATION":
                    if (!batTaskRevocationPostDelegateMap.containsKey(delegateType)) {
                        batTaskRevocationPostDelegateMap.put(delegateType, proxy);
                    }
                    break;






            }

        }
        Map<String, List<String>> taskDelegateMap = new HashMap<>();
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.BATCH_PREPARE.name(), new ArrayList<>(batchTaskPrepareDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.BATCH_PRE.name(), new ArrayList<>(batchTaskPreDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.BATCH_POST.name(), new ArrayList<>(batchTaskPostDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.BATCH_COMPLETE.name(), new ArrayList<>(batchTaskCompleteDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.PRE_BATCH_REVOCATION.name(), new ArrayList<>(batTaskRevocationPreDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.POST_BATCH_REVOCATION.name(), new ArrayList<>(batTaskRevocationPostDelegateMap.values()));



        return taskDelegateMap;

    }


    @Override
    public Map<String, List<String>> getConfigForCreate(Long orgId, Long projectId, String processKey, ProcessType processType, String processCategory) {

        String processTypeStr = processType == null ? null : processType.name();

        List<Tuple> actTaskConfigs = bpmActTaskConfigRepository.findAllForCreate(orgId, projectId, processKey, processTypeStr, processCategory, EntityStatus.ACTIVE.name());
        Map<String, String> preCreateDelegateMap = new HashMap<>();
        Map<String, String> createDelegateMap = new HashMap<>();
        Map<String, String> postCreateDelegateMap = new HashMap<>();


        for (Tuple actTaskConfig : actTaskConfigs) {

            String delegateType = (String) actTaskConfig.get("delegate_type");
            String delegateStage = (String) actTaskConfig.get("delegate_stage");
            String proxy = (String) actTaskConfig.get("proxy");

            switch (delegateStage) {
                case "PRE_CREATE":
                    if (!preCreateDelegateMap.containsKey(delegateType)) {
                        preCreateDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "CREATE":
                    if (!createDelegateMap.containsKey(delegateType)) {
                        createDelegateMap.put(delegateType, proxy);
                    }
                    break;
                case "POST_CREATE":
                    if (!postCreateDelegateMap.containsKey(delegateType)) {
                        postCreateDelegateMap.put(delegateType, proxy);
                    }
                    break;
            }


        }
        Map<String, List<String>> taskDelegateMap = new HashMap<>();
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.PRE_CREATE.name(), new ArrayList<>(preCreateDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.CREATE.name(), new ArrayList<>(createDelegateMap.values()));
        taskDelegateMap.put(BpmActTaskConfigDelegateStage.POST_CREATE.name(), new ArrayList<>(postCreateDelegateMap.values()));

        return taskDelegateMap;

    }

    @Override
    public BpmActTaskConfig add(Long orgId, Long projectId, BpmActTaskConfigCreateDTO bpmActTaskConfigCreateDTO, OperatorDTO operator) {
        BpmActTaskConfig bpmActTaskConfig = new BpmActTaskConfig();
        BeanUtils.copyProperties(bpmActTaskConfigCreateDTO, bpmActTaskConfig);
        bpmActTaskConfig.setTaskType(bpmActTaskConfigCreateDTO.getTaskType());

        Project project = projectService.get(orgId, projectId);
        bpmActTaskConfig.setCompanyId(project.getCompanyId());
        bpmActTaskConfig.setCreatedAt();
        bpmActTaskConfig.setProjectId(projectId);
        bpmActTaskConfig.setOrgId(orgId);
        bpmActTaskConfig.setStatus(EntityStatus.ACTIVE);

        return bpmActTaskConfigRepository.save(bpmActTaskConfig);
    }

    @Override
    public Page<BpmActTaskConfig> search(Long orgId, Long projectId, BpmActTaskConfigDTO bpmActTaskConfigDTO, PageDTO pageDTO) {
        return bpmActTaskConfigRepository.search(orgId, projectId, bpmActTaskConfigDTO, pageDTO);
    }

    @Override
    public BpmActTaskConfig edit(Long orgId, Long projectId, Long bpmActTaskConfigId, BpmActTaskConfigDTO bpmActTaskConfigDTO, OperatorDTO operator) {
        Optional<BpmActTaskConfig> bpmActTaskConfigOp = bpmActTaskConfigRepository.findById(bpmActTaskConfigId);
        if (bpmActTaskConfigOp.isPresent()) {
            BpmActTaskConfig bpmActTaskConfig = bpmActTaskConfigOp.get();
            BeanUtils.copyProperties(bpmActTaskConfigDTO, bpmActTaskConfig);
            Project project = projectService.get(orgId, projectId);
            bpmActTaskConfig.setCompanyId(project.getCompanyId());
            bpmActTaskConfig.setCreatedAt();
            bpmActTaskConfig.setProjectId(projectId);
            bpmActTaskConfig.setOrgId(orgId);
            bpmActTaskConfig.setStatus(EntityStatus.ACTIVE);
            return bpmActTaskConfigRepository.save(bpmActTaskConfig);

        } else {
            throw new NotFoundError("There is no this Entity");
        }
    }

    @Override
    public BpmActTaskConfig detail(Long orgId, Long projectId, Long bpmActTaskConfigId) {
        Optional<BpmActTaskConfig> bpmActTaskConfigOp = bpmActTaskConfigRepository.findById(bpmActTaskConfigId);
        if (bpmActTaskConfigOp.isPresent()) {

            return bpmActTaskConfigOp.get();

        } else {
            throw new NotFoundError("There is no this Entity");
        }
    }

    @Override
    public void delete(Long orgId, Long projectId, Long bpmActTaskConfigId, OperatorDTO operator) {
        Optional<BpmActTaskConfig> bpmActTaskConfigOp = bpmActTaskConfigRepository.findById(bpmActTaskConfigId);
        if (bpmActTaskConfigOp.isPresent()) {
            bpmActTaskConfigRepository.deleteById(bpmActTaskConfigId);

        } else {
            throw new NotFoundError("There is no this Entity");
        }

    }


/*
    public static void main(String[] args) {
        getClassName("com.ose.tasks.domain.model.service.delegate", true)
            .forEach(clazz ->{
                if(!clazz.toLowerCase().contains("interface") & !clazz.equalsIgnoreCase("BaseBpmTaskDelegate")) {
                    BpmDelegateClassDTO bpmDelegateClass = new BpmDelegateClassDTO();
                    bpmDelegateClass.setDelegateClassFullName(clazz);
                    bpmDelegateClass.setDelegateClassName(clazz.substring( clazz.lastIndexOf(".") + 1));
                }
            });
        System.out.println("ftj");
    }*/

}
