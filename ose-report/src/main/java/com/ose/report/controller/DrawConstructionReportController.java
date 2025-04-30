package com.ose.report.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.dto.ContextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.DrawConstructionReportAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.DrawSubPipeDTO;
import com.ose.report.dto.DrawSubPipeSupportDTO;
import com.ose.report.dto.DrawSubPipeSupportOverallDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "管线组对检验报告单接口")

@RestController
@RequestMapping(value = "/")
public class DrawConstructionReportController extends BaseReportController implements DrawConstructionReportAPI {

    // 检查单报表生成服务
    private final ReportHistoryInterface reportHistoryInterface;

    // 文件上传接口
    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawConstructionReportController(
        ReportHistoryInterface reportHistoryInterface,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI) {
        super(reportHistoryInterface, uploadFeignAPI);
        this.reportHistoryInterface = reportHistoryInterface;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 生产图纸目录清单
     *
     * @param orgId     组织ID
     *                  组织
     * @param projectId 项目ID
     *                  项目ID
     * @return 制作完成的检查单
     */
    @Override
    @Operation(summary = "生产图纸目录清单", description = "生产图纸目录清单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> generateDrawConstructionreport(@PathVariable @Parameter(description = "组织ID") Long orgId,
                                                                                @PathVariable @Parameter(description = "项目ID") Long projectId,
                                                                                @RequestBody @Parameter(description = "检查项内容") DrawSubPipeDTO drawSubPipeDTO
    ) {

        ContextDTO contextDTO = null;
        if (drawSubPipeDTO.getContextDTO() != null) {
            contextDTO = drawSubPipeDTO.getContextDTO();
        } else {
            contextDTO = getContext();
        }
        return new JsonObjectResponseBody<>(
            contextDTO,
            generateReportFile(
                contextDTO.getOperator(),
                orgId,
                projectId,
                "drawing-pipeline-construction/input.jasper",
                drawSubPipeDTO
            )
        );
    }

    /**
     * 生产图纸管支架统计目录清单
     *
     * @param orgId     组织ID
     *                  组织
     * @param projectId 项目ID
     *                  项目ID
     * @return 制作完成的检查单
     */
    @Override
    @Operation(summary = "生产图纸目录清单", description = "生产图纸目录清单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-support-overall-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> generateDrawConstructionSupportOverallReport(@PathVariable @Parameter(description = "组织ID") Long orgId,
                                                                                              @PathVariable @Parameter(description = "项目ID") Long projectId,
                                                                                              @RequestBody @Parameter(description = "检查项内容") DrawSubPipeSupportOverallDTO drawSubPipeSupportOverallDTO
    ) {

        return new JsonObjectResponseBody<>(
            getContext(),
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "drawing-pipeline-construction/support_overall.jasper",
                drawSubPipeSupportOverallDTO
            )
        );
    }

    /**
     * 生产图纸管支架目录清单
     *
     * @param orgId     组织ID
     *                  组织
     * @param projectId 项目ID
     *                  项目ID
     * @return 制作完成的检查单
     */
    @Override
    @Operation(summary = "生产图纸目录清单", description = "生产图纸目录清单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-support-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> generateDrawConstructionSupportReport(@PathVariable @Parameter(description = "组织ID") Long orgId,
                                                                                       @PathVariable @Parameter(description = "项目ID") Long projectId,
                                                                                       @RequestBody @Parameter(description = "检查项内容") DrawSubPipeSupportDTO drawSubPipeSupportDTO
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "drawing-pipeline-construction/support_detail.jasper",
                drawSubPipeSupportDTO
            )
        );
    }

}
