package com.ose.tasks.controller.performance;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.MmImportBatchResultDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.performance.PerformanceAPI;
import com.ose.tasks.controller.bpm.drawing.DrawingImportController;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.performance.PerformanceAppraisalListInterface;
import com.ose.tasks.domain.model.service.performance.impl.PerformanceAppraisalListService;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.PerformanceBatchResultDTO;
import com.ose.tasks.dto.performance.PerformanceAppraisalListImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import static com.ose.tasks.vo.setting.BatchTaskCode.PERFORMANCE_LIST_IMPORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class PerformanceController extends BaseController implements PerformanceAPI {

    private final static Logger logger = LoggerFactory.getLogger(PerformanceController.class);

    private PerformanceAppraisalListInterface performanceAppraisalListService;

    private BatchTaskInterface batchTaskService;

    private UploadFeignAPI uploadFeignAPI;
    // 项目管理服务接口
    private ProjectInterface projectService;


    public PerformanceController(
        PerformanceAppraisalListInterface performanceAppraisalListService,
        ProjectInterface projectService,
        BatchTaskInterface batchTaskService) {
        this.performanceAppraisalListService = performanceAppraisalListService;
        this.projectService = projectService;
        this.batchTaskService = batchTaskService;
    }

    @Override
    @Operation(
        summary = "绩效考核清单",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-document-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/samples/import-drawing-list.xlsx\">import-drawing-list.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/performance-appraisal-list/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importPerformanceAppraisalList(
        @RequestBody PerformanceAppraisalListImportDTO importDTO) {

        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(1533021345379520082L);
        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());

        batchTaskService.run(
            contextDTO, project, PERFORMANCE_LIST_IMPORT,
            batchTask -> {
                BatchResultDTO result = performanceAppraisalListService.importDetailList( operator, batchTask, importDTO);
                logger.info("图纸导入controller1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                        null,
                    null,
                    importDTO.getFileName(),
                    new FilePostDTO()
                );
                logger.info("图纸导入controller1 保存docs服务->结束");
                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));
                return result;
            }
        );
        return new JsonResponseBody();
    }
}
