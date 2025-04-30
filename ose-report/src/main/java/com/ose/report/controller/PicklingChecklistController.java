package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.PicklingChecklistAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.PicklingChecklistDTO;
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

@Tag(name = "生成酸洗磷化检查单")
@RestController
public class PicklingChecklistController extends BaseReportController implements PicklingChecklistAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public PicklingChecklistController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成酸洗磷化检查单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pickling-checklists"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generatePicklingChecklist(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody PicklingChecklistDTO reportDTO
    ) {
        return new JsonObjectResponseBody<>(
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "pickling-checklist.jasper",
                reportDTO
            )
        );
    }

}
