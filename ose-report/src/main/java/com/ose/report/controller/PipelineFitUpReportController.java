package com.ose.report.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.PipelineFitUpReportAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.InspectionReportPostDTO;
import com.ose.report.dto.InspectionApplicationPostDTO;
import com.ose.report.dto.InspectionReportApplicationFormPostDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "管线组对检验报告单接口")
@RestController
public class PipelineFitUpReportController extends BaseReportController implements PipelineFitUpReportAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public PipelineFitUpReportController(ReportHistoryInterface reportHistoryService,
                                         @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Override
    @Operation(description = "生成管道组对检查报告")
    @RequestMapping(method = POST, value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-reports", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> postPipelineFitUpInspectionReport(
        @PathVariable @Parameter(description = "组织ID") Long orgId, @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody @Parameter(description = "检查项内容") InspectionReportPostDTO reportPostDTO) {
        ContextDTO contextDTO = null;
        if (reportPostDTO.getContextDTO() != null) {
            contextDTO = reportPostDTO.getContextDTO();
        } else {
            contextDTO = getContext();
        }
        return new JsonObjectResponseBody<>(contextDTO,
            generateReportFile(
                contextDTO.getOperator(),
                orgId,
//            projectId, "structure-material-inspection-record.jasper", reportPostDTO));
                projectId,
                "pipeline-fit-up-inspection/report.jasper",
                reportPostDTO));
    }

    @Override
    @Operation(description = "生成管道组对检验申请单")
    @RequestMapping(method = POST, value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-applications", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> postApplicationForPipelineFitUpInspection(
        @PathVariable @Parameter(description = "组织 ID") Long orgId, @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "报表数据") InspectionApplicationPostDTO reportPostDTO) {
        return new JsonObjectResponseBody<>(getContext(), generateReportFile(getContext().getOperator(), orgId,
            projectId, "pipeline-fit-up-inspection/application.jasper", reportPostDTO));
    }

    @Override
    @Operation(description = "生成管道组对检验申请单目录封面")
    @RequestMapping(method = POST, value = "/orgs/{orgId}/projects/{projectId}/pipeline-fit-up-inspection-application-forms", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> postApplicationFormForPipelineFitUpInspection(
        @PathVariable @Parameter(description = "组织 ID") Long orgId, @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "报表数据") InspectionReportApplicationFormPostDTO reportPostDTO) {
        // TODO Auto-generated method stub
        ContextDTO contextDTO = null;
        if (reportPostDTO.getContextDTO() != null) {
            contextDTO = reportPostDTO.getContextDTO();
        } else {
            contextDTO = getContext();
        }
        return new JsonObjectResponseBody<>(
            contextDTO,
            generateReportFile(
                contextDTO.getOperator(),
                orgId,
                projectId,
                "pipeline-fit-up-inspection/report-applicaton-form.jasper",
                reportPostDTO));
    }

}
