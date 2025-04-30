package com.ose.tasks.api.material;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.material.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/processes")
public interface FItemAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<QrCodeFItemVo> getQrCodeFItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "qrCode") String qrCode);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FItemTotalStatisticsDTO> getFItemStatistics(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "ident") String ident);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExportFileDTO> exportSummaryInventory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FItemDetailEntityDTO> repairQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FItemQrCodeCriteriaDTO fItemQrCodeCriteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "repair-qrcode/{fItemDetailId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody repairQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fItemDetailId") Long fItemDetailId,
        FItemQrCodeCriteriaDTO criteria
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FItemQtyInformationDTO> getFItemQtyInformation(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FItemQtyInfoCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExportFileDTO> exportFItemQtyInformation(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        FItemQtyInfoCriteriaDTO criteriaDTO
    );
}
