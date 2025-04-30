package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.ApplicationForInspectionExternalAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.ApplicationForInspectionExternalDTO;
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

@Tag(name = "生成外检确认单")
@RestController
public class ApplicationForInspectionExternalController extends BaseReportController implements ApplicationForInspectionExternalAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public ApplicationForInspectionExternalController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成外检确认单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/application-for-inspection-external"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generateApplicationForInspectionExternal(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody ApplicationForInspectionExternalDTO reportDTO
    ) {
        return new JsonObjectResponseBody<>(
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "application-for-inspection-external.jasper",
                reportDTO
            )
        );
    }

}
