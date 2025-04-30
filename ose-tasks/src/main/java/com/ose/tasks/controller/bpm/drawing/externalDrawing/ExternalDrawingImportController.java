package com.ose.tasks.controller.bpm.drawing.externalDrawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.externalDrawing.ExternalDrawingListImportAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.externalDrawing.ExternalDrawingImportInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_LIST_IMPORT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "生产设计图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/external-drawing")
public class ExternalDrawingImportController extends BaseController implements ExternalDrawingListImportAPI {


    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;
    // 批处理任务管理服务接口
    private BatchTaskInterface batchTaskService;

    // 生产设计图纸清单服务接口
    private ExternalDrawingImportInterface externalDrawingService;

    // 文件上传服务接口
    private UploadFeignAPI uploadFeignAPI;

    // 项目管理服务接口
    private ProjectInterface projectService;


    /**
     * 构造方法
     */
    @Autowired
    public ExternalDrawingImportController(
        ProjectInterface projectService,
        BatchTaskInterface batchTaskService,
        ExternalDrawingImportInterface externalDrawingService,
        UploadFeignAPI uploadFeignAPI) {
        this.projectService = projectService;
        this.batchTaskService = batchTaskService;
        this.externalDrawingService = externalDrawingService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "导入管线生产设计图纸清单",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-document-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/external-import-drawing-list.xlsx\">import-drawing-list.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "{discipline}/import",
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

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());

        batchTaskService.run(
            contextDTO, project, DRAWING_LIST_IMPORT,
            batchTask -> {

                BatchResultDTO result = externalDrawingService.importDrawingList(orgId, projectId, operator, batchTask, importDTO, discipline);

                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    importDTO.getFileName(),
                    new FilePostDTO()
                );

                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));

                externalDrawingService.updateFileId(batchTask.getId(), LongUtils.parseLong(
                    responseBody.getData().getId()
                ));

                return result;
            }
        );

        return new JsonResponseBody();
    }
}
