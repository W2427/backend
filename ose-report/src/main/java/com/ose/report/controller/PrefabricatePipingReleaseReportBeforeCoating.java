package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.PrefabricatePipingReleaseReportBeforeCoatingAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.PrefabricatePipingReleaseReportBeforeCoatingDTO;
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

@Tag(name = "生成预制管道放行单(涂装前)")
@RestController
public class PrefabricatePipingReleaseReportBeforeCoating extends BaseReportController implements PrefabricatePipingReleaseReportBeforeCoatingAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public PrefabricatePipingReleaseReportBeforeCoating(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成预制管道放行单(涂装前)")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/prefabricate-piping-release-report-before-coating"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generatePrefabricatePipingReleaseReportBeforeCoating(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody PrefabricatePipingReleaseReportBeforeCoatingDTO reportDTO
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
                "prefabricate-piping-release-report-before-coating.jasper",
                reportDTO
            )
        );
    }

}
