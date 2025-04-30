package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.report.api.DesignChangeReviewFormAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.report.dto.DesignChangeReviewFormDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "生成设计变更申请单")
@RestController
public class DesignChangeReviewFormController extends BaseReportController implements DesignChangeReviewFormAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public DesignChangeReviewFormController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    /**
     * 生成设计变更单。
     *
     * @param orgId
     * @param projectId
     * @param reportDTO
     * @return
     */
    @Operation(description = "生成设计变更申请单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/design-change-review-forms"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generateDesignChangeReviewForm(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody DesignChangeReviewFormDTO reportDTO
    ) {
        return new JsonObjectResponseBody<>(
            generateReportFile(
                getContext().getOperator(),
                orgId,
                projectId,
                "design-change-review-form.jasper",
                reportDTO
            )
        );
    }

}
