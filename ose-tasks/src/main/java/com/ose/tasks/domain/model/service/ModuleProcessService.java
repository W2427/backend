package com.ose.tasks.domain.model.service;

import com.ose.util.*;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.process.BpmnTaskRelationRepository;
import com.ose.tasks.domain.model.repository.process.ModuleProcessDefinitionBasicRepository;
import com.ose.tasks.domain.model.repository.process.ModuleProcessDefinitionRepository;
import com.ose.tasks.domain.model.service.plan.PlanRelationInterface;
import com.ose.tasks.dto.process.EntityProcessRelationsDTO;
import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import com.ose.tasks.entity.process.ModuleProcessDefinitionBasic;
import com.ose.tasks.util.BPMNUtils;
import com.ose.tasks.util.BpmnCommonUtils;
import com.ose.tasks.util.wbs.WorkflowResolver;
import com.ose.tasks.util.wbs.WorkflowValidator;
import com.ose.vo.EntityStatus;
import org.activiti.bpmn.model.BpmnModel;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

/**
 * 模块工作流服务。
 */
@Component
public class ModuleProcessService implements ModuleProcessInterface {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;

    private final static Logger logger = LoggerFactory.getLogger(ModuleProcessService.class);



    private final ModuleProcessDefinitionBasicRepository moduleProcessDefinitionBasicRepository;


    private final ModuleProcessDefinitionRepository moduleProcessDefinitionRepository;


    private final BpmnTaskRelationRepository bpmnTaskRelationRepository;


    private final UploadFeignAPI uploadFeignAPI;

    private final PlanRelationInterface planRelationService;


    /**
     * 构造方法。
     */
    @Autowired
    public ModuleProcessService(
        ModuleProcessDefinitionBasicRepository moduleProcessDefinitionBasicRepository,
        ModuleProcessDefinitionRepository moduleProcessDefinitionRepository,
        BpmnTaskRelationRepository bpmnTaskRelationRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        PlanRelationInterface planRelationService) {
        this.moduleProcessDefinitionBasicRepository = moduleProcessDefinitionBasicRepository;
        this.moduleProcessDefinitionRepository = moduleProcessDefinitionRepository;
        this.bpmnTaskRelationRepository = bpmnTaskRelationRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.planRelationService = planRelationService;
    }

    /**
     * 解除部署。
     *
     * @param operator                  操作者信息
     * @param deployedProcessDefinition 已部署的工作流信息
     */
    private void undeploy(OperatorDTO operator, ModuleProcessDefinition deployedProcessDefinition) {





        bpmnTaskRelationRepository.deleteByModuleProcessDefinitionId(
            deployedProcessDefinition.getOrgId(),
            deployedProcessDefinition.getProjectId(),
            deployedProcessDefinition.getId()
        );

        deployedProcessDefinition.setDeletedAt();
        deployedProcessDefinition.setDeletedBy(operator.getId());


        moduleProcessDefinitionRepository.save(deployedProcessDefinition);
    }

    /**
     * 取得工作流部署信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param funcPart   功能块
     * @param version    工作流更新版本号
     * @return 工作流部署信息
     */
    private ModuleProcessDefinition getDeployment(Long orgId, Long projectId, String funcPart, long version) {


        ModuleProcessDefinition deployedProcessDefinition = moduleProcessDefinitionRepository
            .findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(orgId, projectId, funcPart);


        if ((deployedProcessDefinition == null && version != 0)
            || (deployedProcessDefinition != null && deployedProcessDefinition.getVersion() != version)) {
            throw new ConflictError();
        }

        return deployedProcessDefinition;
    }


    /**
     * 部署项目模块工作流。
     *
     * @param operator                  操作者信息
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param version                   模块工作流部署更新版本号
     * @param bpmnFilename              工作流定义文件文件名
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 模块工作流定义
     */
    @Override
    @Transactional
    public ModuleProcessDefinition deploy(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final long version,
        final String bpmnFilename,
        final EntityProcessRelationsDTO entityProcessRelationsDTO,
        final String bpmnName
    ) {
//        String funcPart = entityProcessRelationsDTO.getFuncPart();
        return deploy(
            operator,
            orgId,
            projectId,
            version,
            bpmnFilename,
            entityProcessRelationsDTO,
            bpmnName
        );

    }


    /**
     * 部署项目模块工作流。
     *
     * @param operator                  操作者信息
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param version                   模块工作流部署更新版本号
     * @param bpmnFilename              工作流定义文件文件名
     * @param funcPart                  功能块
     * @param entityProcessRelationsDTO 工序-实体类型映射表
     * @return 模块工作流定义
     */
    @Override
    @Transactional
    public ModuleProcessDefinition deploy(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final long version,
        final String bpmnFilename,
        final String funcPart,
        final EntityProcessRelationsDTO entityProcessRelationsDTO,
        final String bpmnName
    ) {


        final String processDefinitionKey = WorkflowResolver.PROCESS_DEFINITION_KEY + "." + funcPart;

        final File bpmnFile = new File(temporaryDir, bpmnFilename);


        BpmnCommonUtils.setProcessDefinitionKey(bpmnFile, processDefinitionKey);

        InputStream bpmnStream;


        try {
            bpmnStream = new FileInputStream(bpmnFile);
        } catch (FileNotFoundException e) {
            logger.error(e.toString());
            throw new NotFoundError();
        }



        ModuleProcessDefinition deployedProcessDefinition = getDeployment(orgId, projectId, funcPart, version);


        if (deployedProcessDefinition != null) {
            undeploy(operator, deployedProcessDefinition);
        }

        ModuleProcessDefinition processDefinitionEntity = new ModuleProcessDefinition();
        processDefinitionEntity.setOrgId(orgId);
        processDefinitionEntity.setProjectId(projectId);
        processDefinitionEntity.setBpmnName(bpmnName);

        InputStream diagramStream = null;
        BpmnModel bpmnModel = null;

        try {


            BpmnCommonUtils.validateConditionExpressions(bpmnFile, funcPart);


            bpmnModel = BpmnCommonUtils.readBpmnFile(bpmnStream);



            WorkflowValidator.validate(entityProcessRelationsDTO, bpmnModel);


            diagramStream = BpmnCommonUtils.generateProcessDiagram(bpmnModel);

            String diagramFilePath = multipartFormDataDir + CryptoUtils.uniqueId() + ".jpg";

            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem(
                "file", IMAGE_JPEG_VALUE, true, diagramFilePath
            );

            try {
                IOUtils.copy(diagramStream, fileItem.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
            logger.error("模块1 上传docs服务->开始");
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody =
                uploadFeignAPI.uploadProjectDocumentFile(
                    orgId.toString(),
                    fileItem1
                );
            logger.error("模块1 上传docs服务->结束");

            logger.error("模块1 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(
                orgId.toString(),
                projectId.toString(),
                tempFileResBody.getData().getName(),
                new FilePostDTO()
            );
            logger.error("模块1 保存docs服务->结束");
            processDefinitionEntity.setDiagramFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
            FileUtils.remove(diagramFilePath);

        } catch (Exception e) {
            logger.error(e.toString());
            try {
                throw e;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } finally {

            if (diagramStream != null) {
                try {
                    diagramStream.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                    e.printStackTrace(System.out);
                }
            }

            try {
                bpmnStream.close();
            } catch (IOException e) {
                logger.error(e.toString());
                e.printStackTrace(System.out);
            }

        }


        processDefinitionEntity.setDeploymentId("0");
        processDefinitionEntity.setCreatedAt();
        processDefinitionEntity.setCreatedBy(operator.getId());
        processDefinitionEntity.setLastModifiedAt();
        processDefinitionEntity.setLastModifiedBy(operator.getId());
        processDefinitionEntity.setStatus(EntityStatus.ACTIVE);
        processDefinitionEntity.setFuncPart(funcPart);



        logger.error("模块2 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
            orgId.toString(),
            projectId.toString(),
            bpmnFilename,
            new FilePostDTO()
        );
        logger.error("模块2 保存docs服务->结束");

        processDefinitionEntity.setFileId(LongUtils.parseLong(responseBody.getData().getId()));



        if (bpmnModel != null) {

            List<BpmnTaskRelation> bpmnTaskRelationBases =
                BPMNUtils.resolveBpmnTaskRelation(bpmnModel, orgId, projectId, processDefinitionEntity.getId());

            if (!CollectionUtils.isEmpty(bpmnTaskRelationBases)) {

                List<BpmnTaskRelation> bpmnTaskRelations = new ArrayList<>();

                bpmnTaskRelationBases.forEach(bpmnTaskRelationBase -> {
                    BpmnTaskRelation bpmnTaskRelation = new BpmnTaskRelation(processDefinitionEntity.getId());
                    BeanUtils.copyProperties(bpmnTaskRelationBase, bpmnTaskRelation);
                    bpmnTaskRelation.setOrgId(orgId);
                    bpmnTaskRelation.setProjectId(projectId);
                    bpmnTaskRelation.setStatus(EntityStatus.ACTIVE);
                    bpmnTaskRelation.setDeleted(false);
                    bpmnTaskRelations.add(bpmnTaskRelation);
                });

                bpmnTaskRelationRepository.saveAll(bpmnTaskRelations);
                planRelationService.redeployBpmnInRedis(orgId, projectId, processDefinitionEntity.getId(), bpmnTaskRelations);
            }
        }

        return save(processDefinitionEntity);
    }



    /**
     * 保存项目模块工作流部署信息。
     *
     * @param moduleProcessDefinition 项目模块工作流部署信息
     * @return 项目模块工作流部署信息
     */
    @Override
    public ModuleProcessDefinition save(ModuleProcessDefinition moduleProcessDefinition) {
        return moduleProcessDefinitionRepository.save(moduleProcessDefinition);
    }

    /**
     * 取得项目的模块工作流部署信息列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 项目的模块工作流部署信息列表
     */
    @Override
    public List<ModuleProcessDefinitionBasic> list(Long orgId, Long projectId) {
        return moduleProcessDefinitionBasicRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalseOrderByIdDesc(orgId, projectId);
    }

    /**
     * 取得项目的指定类型模块的工作流定义部署的历史列表。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @return 项目的指定类型模块的工作流定义部署的历史列表
     */
    @Override
    public List<ModuleProcessDefinitionBasic> history(Long orgId, Long projectId, String moduleType) {
        return moduleProcessDefinitionBasicRepository
            .findByOrgIdAndProjectIdAndFuncPartAndDeletedIsTrueOrderByIdDesc(orgId, projectId, moduleType);
    }

    /**
     * 删除模块工作流部署。
     *
     * @param operator   操作者信息
     * @param orgId      组织信息
     * @param projectId  项目 ID
     * @param version    工作流部署信息更新版本号
     */
    @Override
    public void delete(OperatorDTO operator, Long orgId, Long projectId, long version) {

        String funcPart = "PIPING";
        delete(operator, orgId, projectId, funcPart, version);
    }

    /**
     * 删除模块工作流部署。
     *
     * @param operator   操作者信息
     * @param orgId      组织信息
     * @param projectId  项目 ID
     * @param funcPart   功能块
     * @param version    工作流部署信息更新版本号
     */
    @Override
    public void delete(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        String funcPart,
        long version) {

        ModuleProcessDefinition deployedProcessDefinition = moduleProcessDefinitionRepository
            .findByOrgIdAndProjectIdAndFuncPartAndDeletedIsFalse(orgId, projectId, funcPart);

        if (deployedProcessDefinition == null) {
            throw new NotFoundError();
        }

        if (deployedProcessDefinition.getVersion() != version) {
            throw new ConflictError();
        }

        undeploy(operator, deployedProcessDefinition);
    }
}
