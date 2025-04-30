package com.ose.test.api;

import com.ose.test.dto.FMaterialReturnReceiptDTO;
import com.ose.test.dto.ReceiveReceiptResultDTO;
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
@FeignClient(name = "ose-test")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{projId}")
public interface ReturnReceiptFeignAPI {

    @RequestMapping(
        method = POST,
        value = "save-rti",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> saveRti(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReturnReceiptDTO returnReceiptDTO);

    @RequestMapping(
        method = POST,
        value = "run-rti",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> runRti(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReturnReceiptDTO returnReceiptDTO);

}
