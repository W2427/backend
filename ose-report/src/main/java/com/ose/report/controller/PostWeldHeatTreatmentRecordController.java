package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.PostWeldHeatTreatmentRecordAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.PostWeldHeatTreatmentRecordDTO;
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

@Tag(name = "生成焊后热处理报告")
@RestController
public class PostWeldHeatTreatmentRecordController extends BaseReportController implements PostWeldHeatTreatmentRecordAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public PostWeldHeatTreatmentRecordController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成焊后热处理报告")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/post-weld-heat-treatment-records"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generatePostWeldHeatTreatmentRecord(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody PostWeldHeatTreatmentRecordDTO reportDTO
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
                "post-weld-heat-treatment-record.jasper",
                reportDTO
            )
        );
    }

}
