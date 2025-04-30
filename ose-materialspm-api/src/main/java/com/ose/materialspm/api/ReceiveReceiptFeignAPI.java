package com.ose.materialspm.api;

import com.ose.materialspm.dto.FMaterialReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 查询接口
 */
@FeignClient(name = "ose-materialspm", contextId = "receiveReceiptFeign")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{projId}")
public interface ReceiveReceiptFeignAPI {

    @RequestMapping(
        method = POST,
        value = "save-mrr",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> saveMrr(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReceiveReceiptDTO receiveReceiptDTO);

    @RequestMapping(
        method = POST,
        value = "run-mrr",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> runMrr(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReceiveReceiptDTO receiveReceiptDTO);
}
