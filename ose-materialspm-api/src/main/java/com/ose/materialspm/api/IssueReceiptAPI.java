package com.ose.materialspm.api;

import com.ose.materialspm.dto.IssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptListResultsDTO;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 查询接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface IssueReceiptAPI extends IssueReceiptFeignAPI {

    /**
     * 获取合同列表
     *
     * @return 合同列表
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueReceiptListResultsDTO> getIssueReceipt(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        IssueReceiptDTO issueReceiptDTO);
}
