package com.ose.report.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.F253xStructureFabriationFitupInspectionReportAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.F253StructureFabricationFitupInspectionReportDTO;
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

@Tag(name = "结构装配接口")
@RestController
public class F253xStructureFabricationFitupInspectionReportController extends BaseReportController implements F253xStructureFabriationFitupInspectionReportAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public F253xStructureFabricationFitupInspectionReportController(ReportHistoryInterface reportHistoryService,
                                                                    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Override
    @Operation(description = "生成结构装配报告")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/f253-structure-fit-up-inspection-reports",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportHistory> postF253StructureFitUpInspectionReport(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody @Parameter(description = "检查项内容") F253StructureFabricationFitupInspectionReportDTO reportPostDTO) {
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
                "F253xStructureFabricationFitupInspectionReport.jasper",
                reportPostDTO));
    }
}
