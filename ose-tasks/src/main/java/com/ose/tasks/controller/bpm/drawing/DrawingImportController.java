package com.ose.tasks.controller.bpm.drawing;

import static com.ose.tasks.vo.setting.BatchTaskCode.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingListImportAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.DetailDesignDrawingInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingImportInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTaskBasic;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "生产设计图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class DrawingImportController extends BaseController implements DrawingListImportAPI {

    private final static Logger logger = LoggerFactory.getLogger(DrawingImportController.class);

    private BatchTaskInterface batchTaskService;


    private DrawingImportInterface drawingService;

    private DetailDesignDrawingInterface detailDesignListService;


    private UploadFeignAPI uploadFeignAPI;


    private ProjectInterface projectService;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingImportController(
        ProjectInterface projectService,
        BatchTaskInterface batchTaskService,
        DrawingImportInterface drawingService,
        DetailDesignDrawingInterface detailDesignListService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI) {
        this.projectService = projectService;
        this.batchTaskService = batchTaskService;
        this.drawingService = drawingService;
        this.detailDesignListService = detailDesignListService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "导入管线生产设计图纸清单",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-document-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-drawing-list.xlsx\">import-drawing-list.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "drawing-list/{discipline}/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importDrawingList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "专业") String discipline,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody DrawingImportDTO importDTO) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        BatchTaskCode batchTaskCode;
        if (importDTO.getDrawingType() == null || importDTO.getDrawingType().equals("DED")) {
            batchTaskCode = DRAWING_LIST_IMPORT;
        } else {
            batchTaskCode = PED_DRAWING_LIST_IMPORT;
        }
        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());

        batchTaskService.run(
            contextDTO, project, batchTaskCode,
            batchTask -> {

                BatchResultDTO result = drawingService.importDrawingList(orgId, projectId, operator, batchTask, importDTO, discipline);
                logger.info("图纸导入controller1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    importDTO.getFileName(),
                    new FilePostDTO()
                );
                logger.info("图纸导入controller1 保存docs服务->结束");
                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));

                drawingService.updateFileId(batchTask.getId(), LongUtils.parseLong(
                    responseBody.getData().getId()
                ));

                return result;
            }
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "查询导入记录",
        description = "生产设计图纸清单导入记录。"
    )
    @RequestMapping(
        method = GET,
        value = "drawing-list/import-records",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonListResponseBody<BatchTaskBasic> importRecords(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO page) {
        return new JsonListResponseBody<>(drawingService.search(orgId, projectId, batchTaskCriteriaDTO, page));
    }

    @Override
    @Operation(
        summary = "导入详细设计图纸清单",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-document-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-detail-design-list.xlsx\">import-detail-design-list.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "detail-design/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody importDetailDesign(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody DrawingImportDTO importDTO) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId, version);

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        batchTaskService.run(
            contextDTO, project, DETAIL_DESIGN_IMPORT,
            batchTask -> {

                BatchResultDTO result = detailDesignListService.importDetailDesign(orgId, projectId, operator, batchTask, importDTO);
                logger.error("图纸导入controller2 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    importDTO.getFileName(),
                    new FilePostDTO()
                );
                logger.error("图纸导入controller2 保存docs服务->结束");
                batchTask.setImportFile(
                    LongUtils.parseLong(
                        responseBody.getData().getId()
                    ));

                detailDesignListService.updateFileId(batchTask.getId(), LongUtils.parseLong(responseBody.getData().getId()));

                return result;
            }
        );

        return new JsonResponseBody();
    }
}
