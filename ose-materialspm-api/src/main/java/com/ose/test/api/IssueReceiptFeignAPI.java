package com.ose.test.api;

import com.ose.test.dto.FMaterialIssueReceiptDTO;
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
public interface IssueReceiptFeignAPI {

    @RequestMapping(
        method = POST,
        value = "save-mir",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> saveMir(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialIssueReceiptDTO issueReceiptDTO);

    @RequestMapping(
        method = POST,
        value = "run-mir",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReceiveReceiptResultDTO> runMir(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialIssueReceiptDTO issueReceiptDTO);

}
