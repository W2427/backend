package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.PeLiningInspectionChecklistAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.PeLiningInspectionChecklistDTO;
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

@Tag(name = "生成镀塑检验单")
@RestController
public class PeLiningInspectionChecklistController extends BaseReportController implements PeLiningInspectionChecklistAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public PeLiningInspectionChecklistController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成镀塑检验单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pe-lining-inspection-checklists"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generatePeLiningInspectionChecklist(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody PeLiningInspectionChecklistDTO reportDTO
    ) {
        return new JsonObjectResponseBody<>(
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "pe-lining-inspection-checklist.jasper",
                reportDTO
            )
        );
    }

}
