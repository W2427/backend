package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.InspectionReleaseFormAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.InspectionReleaseFormDTO;
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

@Tag(name = "生成管道检验放行单")
@RestController
public class InspectionReleaseFormController extends BaseReportController implements InspectionReleaseFormAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public InspectionReleaseFormController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成管道检验放行单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/inspection-release-forms"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generateInspectionReleaseForm(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody InspectionReleaseFormDTO reportDTO
    ) {
        return new JsonObjectResponseBody<>(
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "inspection-release-form.jasper",
                reportDTO
            )
        );
    }

}
