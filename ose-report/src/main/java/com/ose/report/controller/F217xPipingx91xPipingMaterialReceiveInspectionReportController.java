package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.F217xPipingx91xPipingMaterialReceiveInspectionReportAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.PipingMaterialReceiveReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "生成报告")
@RestController
public class F217xPipingx91xPipingMaterialReceiveInspectionReportController extends BaseReportController implements F217xPipingx91xPipingMaterialReceiveInspectionReportAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public F217xPipingx91xPipingMaterialReceiveInspectionReportController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "管系材料出库报告")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/piping-material-receive-reports"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generatePipingMaterialReceiveReport(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody PipingMaterialReceiveReportDTO reportDTO
    ) {
        ContextDTO contextDTO = null;
        if (reportDTO.getContextDTO() != null) {
            contextDTO = reportDTO.getContextDTO();
        } else {
            contextDTO = getContext();
        }

        return new JsonObjectResponseBody<>(
            generateReportFile(
                contextDTO.getOperator(),
                orgId,
                projectId,
                "F217xPipingx91xPipingMaterialReceiveInspectionReport.jasper",
                reportDTO
            )
        );
    }
}
