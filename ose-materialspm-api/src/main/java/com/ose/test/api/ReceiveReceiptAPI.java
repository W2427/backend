package com.ose.test.api;

import com.ose.test.dto.ReceiveReceiptDTO;
import com.ose.test.dto.ReceiveReceiptListResultsDTO;
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
public interface ReceiveReceiptAPI extends ReceiveReceiptFeignAPI {

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
    JsonListResponseBody<ReceiveReceiptListResultsDTO> getReceiveReceipt(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReceiveReceiptDTO receiveReceiptDTO
    );
}
