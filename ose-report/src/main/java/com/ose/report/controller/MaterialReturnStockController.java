package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.report.api.MaterialReturnStockAPI;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.MaterialReturnStockDTO;
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

@Tag(name = "生成退库单")
@RestController
public class MaterialReturnStockController extends BaseReportController implements MaterialReturnStockAPI {

    /**
     * 构造方法。
     */
    @Autowired
    public MaterialReturnStockController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(reportHistoryService, uploadFeignAPI);
    }

    @Operation(description = "生成退库单")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/material-return-stock-lists"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<ReportHistory> generateMaterialReturnStock(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody MaterialReturnStockDTO reportDTO
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
                "material-return-stock.jasper",
                reportDTO
            )
        );
    }

}
