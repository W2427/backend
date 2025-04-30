package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.HierarchyAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.EntityDocInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.ExInspDocDTO;
import com.ose.tasks.dto.bpm.ExInspInfoDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_LIST_IMPORT;
import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_HIERARCHY_IMPORT;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "项目层级结构管理接口")
@RestController
public class HierarchyController extends BaseController implements HierarchyAPI {

    private final static Logger logger = LoggerFactory.getLogger(HierarchyController.class);


    private final BatchTaskInterface batchTaskService;


    private final ProjectInterface projectService;


    private final HierarchyInterface hierarchyService;


    private final EntityDocInterface entityDocService;


    private final UploadFeignAPI uploadFeignAPI;

    private static final String HYPHEN = "-";

    private final BpmProcessRepository bpmProcessRepository;

    private final PlanInterface planService;

    /**
     * 构造方法。
     */
    @Autowired
    public HierarchyController(
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        HierarchyInterface hierarchyService,
        EntityDocInterface entityDocService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI,
        BpmProcessRepository bpmProcessRepository, PlanInterface planService) {
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.hierarchyService = hierarchyService;
        this.entityDocService = entityDocService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.bpmProcessRepository = bpmProcessRepository;
        this.planService = planService;
    }

    @Override
    @Operation(description = "取得层级结构")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(defaultValue = "0") @Parameter(description = "展开深度") int depth,
        @RequestParam(required = false) @Parameter(description = "视图类型") String viewType,
        @RequestParam(required = false) @Parameter(description = "视图类型") String drawingType,
        @RequestParam(required = false, defaultValue = "true") @Parameter(description = "是否需要返回实体节点") Boolean needEntity
    ) {
        return getHierarchy(orgId, projectId, projectId, depth, viewType, drawingType, needEntity);
    }

    @Override
    @Operation(description = "取得层级结构")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "根节点 ID") Long rootNodeId,
        @RequestParam(defaultValue = "0") @Parameter(description = "展开深度") int depth,
        @RequestParam(required = false) @Parameter(description = "视图类型") String viewType,
        @RequestParam(required = false) @Parameter(description = "视图类型") String drawingType,
        @RequestParam(required = false, defaultValue = "true") @Parameter(description = "是否需要返回实体节点") Boolean needEntity
    ) {
        Project project = projectService.get(orgId, projectId);




        HierarchyDTO<HierarchyNodeDTO> responseData = new HierarchyDTO<>();

        responseData.setVersion(project.getVersion());

        String hierarchyType;

        try {
            if (viewType.equals("ALL")) {
                hierarchyType = null;
            } else {
                hierarchyType = viewType;
            }
        } catch (Exception e) {
            hierarchyType = "NOT_VALID";
        }

        responseData.setChildren(
            hierarchyService.getHierarchy(
                orgId, projectId, rootNodeId, depth, hierarchyType, drawingType, needEntity, true
            )
        );

        return new JsonObjectResponseBody<>(responseData);
    }

    @Override
    @Operation(description = "设置层级结构")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyDTO> setHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyDTO<HierarchyNodePutDTO> hierarchyDTO
    ) {
        return setHierarchy(orgId, projectId, null, version, hierarchyDTO);
    }

    @Override
    @Operation(description = "设置层级结构")
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyDTO> setHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "根节点 ID") Long rootNodeId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyDTO<HierarchyNodePutDTO> hierarchyDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);

        JsonObjectResponseBody<HierarchyDTO> responseBody
            = new JsonObjectResponseBody<>();




        if (hierarchyService.saveHierarchy(
            getContext().getOperator(),
            project,
            rootNodeId,
            hierarchyDTO.getChildren(),
            hierarchyDTO.getHierarchyType(),
            false
        )) {
            responseBody = new JsonObjectResponseBody<>(hierarchyDTO);
            responseBody.setError(new ValidationError());
        }



        getResponse().setHeader(DATA_REVISION, "" + project.getVersion());

        return responseBody;
    }

    @Override
    @Operation(
        summary = "导入层级结构",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-hierarchy-import-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-project-hierarchy.xlsx\">import-project-hierarchy.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    ) {
        return importHierarchy(orgId, projectId, null, version, nodeImportDTO);
    }

    @Override
    @Operation(
        summary = "导入层级结构（指定根节点）",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-hierarchy-import-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-project-hierarchy.xlsx\">import-project-hierarchy.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/import-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importHierarchy(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "根节点 ID") Long rootNodeId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        BatchTaskCode batchTaskCode;
        if("Engineering".equalsIgnoreCase(nodeImportDTO.getHierarchyType())){
            batchTaskCode = PROJECT_HIERARCHY_IMPORT;
        } else {
            batchTaskCode = PROJECT_HIERARCHY_IMPORT;
        }

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO, project, batchTaskCode,
            batchTask -> {

                BatchResultDTO result = hierarchyService.importHierarchy(
                    batchTask,
                    operator,
                    project,
                    rootNodeId,
                    nodeImportDTO
                );


                logger.error("层级1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    nodeImportDTO.getFilename(),
                    new FilePostDTO()
                );
                logger.error("层级1 保存docs服务->结束");
                batchTask.setImportFile(LongUtils.parseLong(responseBody.getData().getId()));

                return result;
            }
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "添加节点")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyNode> add(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeModifyDTO nodeDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);

        HierarchyNode node = hierarchyService
            .add(getContext().getOperator(), project, nodeDTO);



        getResponse().setHeader(DATA_REVISION, "" + project.getVersion());

        return new JsonObjectResponseBody<>(node);
    }

    @Override
    @Operation(description = "更新节点")
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<HierarchyNode> update(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "节点 ID") Long nodeId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeModifyDTO nodeDTO
    ) {
        Project project = projectService.get(orgId, projectId, version);
        HierarchyNode node = hierarchyService
            .update(getContext().getOperator(), project, nodeId, nodeDTO);

        getResponse().setHeader(DATA_REVISION, "" + project.getVersion());
        return new JsonObjectResponseBody<>(node);
    }

    @Override
    @Operation(description = "删除节点")
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "节点 ID") Long nodeId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    ) {
        Project project = projectService.get(orgId, projectId, version);
        OperatorDTO operatorDTO = getContext().getOperator();

        ProjectNode pn = hierarchyService.delete(operatorDTO, project, nodeId);

        if(pn != null && pn.getDeletable() != null) {
            planService.updateStatusOfWBSOfDeletedEntity(project.getId(), pn.getEntityType(), pn.getEntityId(), operatorDTO.getId(), pn.getDeletable());
        }
        getResponse().setHeader(DATA_REVISION, "" + project.getVersion());
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得节点列表")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<HierarchyNode> list(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @RequestParam(required = false, defaultValue = "0")
        @Parameter(description = "最大层级深度")
            int depth
    ) {
        return list(orgId, projectId, null, depth);
    }

    @Override
    @Operation(description = "取得节点列表（指定根节点）")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<HierarchyNode> list(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "所属项目 ID") Long projectId,
        @PathVariable @Parameter(description = "根节点 ID") Long rootNodeId,
        @RequestParam(required = false, defaultValue = "0")
        @Parameter(description = "最大层级深度")
            int depth
    ) {
        return (new JsonListResponseBody<>(
            hierarchyService.getFlatList(orgId, projectId, rootNodeId, depth)
        )).setIncluded(hierarchyService);
    }

    @Override
    @Operation(description = "取得节点详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<HierarchyNode> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "节点 ID") Long nodeId
    ) {
        return new JsonObjectResponseBody<>(
            hierarchyService.get(orgId, projectId, nodeId)
        );
    }

    /**
     * 从层级取得模块的外检报告。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param nodeId    模块层级ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module/{nodeId}/external-inspection-report",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    @SetUserInfo
    @Operation(description = "从层级取得模块的外检报告")
    public JsonObjectResponseBody<ExInspInfoDTO> getExternalInspectionReport(
        @PathVariable("orgId") @Parameter(description = "组织 ID") Long orgId,
        @PathVariable("projectId") @Parameter(description = "项目 ID") Long projectId,
        @PathVariable("nodeId") @Parameter(description = "模块层级ID") Long nodeId) {

        List<HierarchyNodeDTO> hierarchyNodeDTOS = hierarchyService.getHierarchy(
            orgId,
            projectId,
            nodeId,
            0,
            null,
            null,
            true,
            false);

        List<Long> childEntityIds = new ArrayList<>();
        if (hierarchyNodeDTOS != null && hierarchyNodeDTOS.size() != 0) {
            for (HierarchyNodeDTO hierarchyDTO : hierarchyNodeDTOS) {
                childEntityIds.add(hierarchyDTO.getNode().getEntityId());
            }
        }

        if (childEntityIds == null || childEntityIds.size() == 0) {
            return new JsonObjectResponseBody<>();
        }

        List<BpmEntityDocsMaterials> bpmEntityDocsMaterials = entityDocService.getBpmEntityDocsList(
            childEntityIds,
            ActInstDocType.EXTERNAL_INSPECTION);


        List<ExInspDocDTO> exInspDocDTOS = new ArrayList<>();

        List<String> processNameStageNameList = new ArrayList<>();
        if (bpmEntityDocsMaterials != null && bpmEntityDocsMaterials.size() != 0) {
            for (BpmEntityDocsMaterials docEntity : bpmEntityDocsMaterials) {
                ExInspDocDTO dto = new ExInspDocDTO();
                List<ActReportDTO> actReportDTOS = docEntity.getJsonDocsReadOnly();
                if (actReportDTOS != null && actReportDTOS.size() != 0) {
                    for (ActReportDTO actReportDto : actReportDTOS) {

                        dto.setReportQrCode(actReportDto.getReportQrCode());

                        dto.setFileId(actReportDto.getFileId());

                        dto.setReportNo(actReportDto.getReportNo());

                        dto.setFilePath(actReportDto.getFilePath());

                        dto.setType(docEntity.getType());

                        dto.setModuleId(nodeId);

                        HierarchyNode hierarchyNode = hierarchyService.get(orgId, projectId, nodeId);
                        dto.setModuleNo(hierarchyNode.getNo());

                        dto.setProcessId(docEntity.getProcessId());

                        Long processId = docEntity.getProcessId();
                        if(processId != null) {
                            BpmProcess bpmProcess = bpmProcessRepository.findById(processId).orElse(null);
                            if(bpmProcess != null && bpmProcess.getStatus() == EntityStatus.ACTIVE) {
                                dto.setProcessNameCn(bpmProcess.getNameCn());
                                processNameStageNameList.add(bpmProcess.getNameCn()
                                    + HYPHEN + bpmProcess.getProcessStage().getNameCn());
                                dto.setProcessNameEn(bpmProcess.getNameEn());
                                dto.setProcessStageId(bpmProcess.getProcessStage().getId());
                                dto.setProcessStageNameCn(bpmProcess.getProcessStage().getNameCn());
                                dto.setProcessStageNameEn(bpmProcess.getProcessStage().getNameEn());
                            }
                        }
                        exInspDocDTOS.add(dto);
                    }
                }
            }
        }
        ExInspInfoDTO externalInspectionInfo = new ExInspInfoDTO();
        externalInspectionInfo.setExternalInspectionDocList(exInspDocDTOS);
        externalInspectionInfo.setProcessNameStageNamePairList(processNameStageNameList);

        return new JsonObjectResponseBody<>(externalInspectionInfo);
    }

    /**
     * 从层级下载模块的外检报告。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param nodeId    模块层级ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module/{nodeId}/download/external-inspection-report"
    )
    @Override
    @WithPrivilege
    @Operation(description = "从层级下载模块的外检报告")
    public void downloadExternalInspectionReport(
        @PathVariable("orgId") @Parameter(description = "组织 ID") Long orgId,
        @PathVariable("projectId") @Parameter(description = "项目 ID") Long projectId,
        @PathVariable("nodeId") @Parameter(description = "模块层级ID") Long nodeId) throws IOException {

        List<HierarchyNodeDTO> hierarchyNodeDTOS = hierarchyService.getHierarchy(
            orgId,
            projectId,
            nodeId,
            0,
            null,
            null,
            true,
            false);

        List<Long> childEntityIds = new ArrayList<>();
        if (hierarchyNodeDTOS != null && hierarchyNodeDTOS.size() != 0) {
            for (HierarchyNodeDTO hierarchyDTO : hierarchyNodeDTOS) {
                childEntityIds.add(hierarchyDTO.getNode().getEntityId());
            }
        }

        if (childEntityIds == null || childEntityIds.size() == 0) {
            return;
        }

        List<BpmEntityDocsMaterials> bpmEntityDocsMaterials = entityDocService.getBpmEntityDocsList(
            childEntityIds,
            ActInstDocType.EXTERNAL_INSPECTION);


        List<ExInspDocDTO> exInspDocDTOS = new ArrayList<>();
        if (bpmEntityDocsMaterials != null && bpmEntityDocsMaterials.size() != 0) {
            for (BpmEntityDocsMaterials docEntity : bpmEntityDocsMaterials) {
                ExInspDocDTO dto = new ExInspDocDTO();
                List<ActReportDTO> actReportDTOS = docEntity.getJsonDocsReadOnly();
                if (actReportDTOS != null && actReportDTOS.size() != 0) {
                    for (ActReportDTO actReportDto : actReportDTOS) {

                        dto.setReportQrCode(actReportDto.getReportQrCode());

                        dto.setFileId(actReportDto.getFileId());

                        dto.setReportNo(actReportDto.getReportNo());

                        dto.setFilePath(actReportDto.getFilePath());

                        dto.setType(docEntity.getType());

                        dto.setModuleId(nodeId);

                        HierarchyNode hierarchyNode = hierarchyService.get(orgId, projectId, nodeId);
                        dto.setModuleNo(hierarchyNode.getNo());

                        dto.setProcessId(docEntity.getProcessId());

                        Long processId = docEntity.getProcessId();
                        if(processId != null) {
                            BpmProcess bpmProcess = bpmProcessRepository.findById(processId).orElse(null);
                            if(bpmProcess != null && bpmProcess.getStatus() == EntityStatus.ACTIVE) {
                                dto.setProcessNameCn(bpmProcess.getNameCn());
                                dto.setProcessNameEn(bpmProcess.getNameEn());
                                dto.setProcessStageId(bpmProcess.getProcessStage().getId());
                                dto.setProcessStageNameCn(bpmProcess.getProcessStage().getNameCn());
                                dto.setProcessStageNameEn(bpmProcess.getProcessStage().getNameEn());
                            }
                        }
                        exInspDocDTOS.add(dto);
                    }
                }
            }
        }

        File zipFile = entityDocService.createDownloadZipFile(orgId, projectId, exInspDocDTOS);

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"" + zipFile.getName() + "\""
            );

            IOUtils.copy(
                new FileInputStream(zipFile), response.getOutputStream()
            );


        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        } finally {

            if (zipFile.exists()) {
                try {
                    zipFile.delete();
                } catch (Exception e) {
                    logger.error("Can not delete file");
                }
            }
        }

        response.flushBuffer();
    }

    @Override
    @Operation(description = "更新层级节点上的计划实施进度信息")
    @WithPrivilege
    public JsonResponseBody refreshWBSProgress(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    ) {
        hierarchyService.refreshWBSProgress(
            getContext().getOperator(),
            orgId,
            projectId
        );
        return new JsonResponseBody();
    }

}
