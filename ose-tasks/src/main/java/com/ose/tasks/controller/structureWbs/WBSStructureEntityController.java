package com.ose.tasks.controller.structureWbs;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wbsStructure.WBSStructureEntityAPI;
import com.ose.tasks.domain.model.service.*;
import com.ose.tasks.domain.model.service.wbs.WBSEntityInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.util.LongUtils;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.PROJECT_ENTITIES_IMPORT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(description = "结构实体管理接口")
@RestController
public class WBSStructureEntityController extends BaseController implements WBSStructureEntityAPI {
    private final static Logger logger = LoggerFactory.getLogger(WBSStructureEntityController.class);

    private final BatchTaskInterface batchTaskService;


    private final ProjectInterface projectService;


    private final WBSEntityInterface wbsEntityService;


    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public WBSStructureEntityController(
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
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/wbs-entity-import-structure-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-project-structure-entities.xlsx\">import-project-structure-entities.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-structure-entities",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importEntities(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    ) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        batchTaskService.run(
            getContext(), project, PROJECT_ENTITIES_IMPORT,
            batchTask -> {

                BatchResultDTO result = wbsEntityService.importEntities(
                    batchTask,
                    operator,
                    project,
                    nodeImportDTO
                );



                logger.info("结构四级计划1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    nodeImportDTO.getFilename(),
                    new FilePostDTO()
                );
                logger.info("结构四级计划1 保存docs服务->结束");

                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));

                return result;
            }
        );

        return new JsonResponseBody();
    }


}
