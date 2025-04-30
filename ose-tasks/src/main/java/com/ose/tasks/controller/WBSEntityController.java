package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.response.JsonResponseError;
import com.ose.tasks.api.WBSEntityAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.wbs.entry.WBSEntryExecutionHistory;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_ENTITIES_IMPORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "实体管理接口")
@RestController
public class WBSEntityController extends BaseController implements WBSEntityAPI {

    private final static Logger logger = LoggerFactory.getLogger(WBSEntityController.class);

    private final BatchTaskInterface batchTaskService;


    private final ProjectInterface projectService;


    private final WBSEntityInterface wbsEntityService;


    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public WBSEntityController(
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        WBSEntityInterface wbsEntityService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.wbsEntityService = wbsEntityService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "导入实体",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/wbs-entity-import-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-project-entities.xlsx\">import-project-entities.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-entities/{type}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importEntities(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "类型") String type,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO, project, BatchTaskCode.valueOf(type),
            batchTask -> {

                BatchResultDTO result = wbsEntityService.importEntities(
                    batchTask,
                    operator,
                    project,
                    nodeImportDTO
                );


                logger.info("四级计划1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    nodeImportDTO.getFilename(),
                    new FilePostDTO()
                );
                logger.info("四级计划1 保存docs服务->结束");
                batchTask.setImportFile(
                    LongUtils.parseLong(
                        responseBody.getData().getId()
                    ));

                return result;
            }
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得未挂载到层级结构的实体")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/unmounted-entities",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<ProjectNode> unmountedEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam(name = "entityType", required = false) String entityType,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            wbsEntityService.unmountedEntities(
                projectService.get(orgId, projectId).getId(),
                entityType,
                pageDTO.toPageable()
            )
        );
    }


    /**
     * 将增加的实体插入到 待更新计划表中 wbs_entry_execution_history
     *
     * @param operatorId 操作者
     * @param projectId  项目ID
     * @param entityId   计划实体ID
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-execution-history",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    @Operation(description = "插入新增实体到计划待更新表")
    @ResponseStatus(OK)
    public JsonResponseBody addExecutionHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("operatorId") Long operatorId,
        @RequestParam("entityId") Long entityId
    ) {
        WBSEntryExecutionHistory wbsEeh = wbsEntityService.insertExecutionHistory(operatorId, projectId, entityId);
        if (wbsEeh != null) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new JsonResponseError());
        }
    }


}
