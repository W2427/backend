package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.QRCodeSearchResultDTO;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface QRCodeSearchAPI {


    @RequestMapping(
        method = GET,
        value = "qrcode-search/{qrcode}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<QRCodeSearchResultDTO> getEntityList(
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        @PathVariable("qrcode") @Parameter(description = "qrcode") String qrcode
    );

}
