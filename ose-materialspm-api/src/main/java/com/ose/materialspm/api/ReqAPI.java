package com.ose.materialspm.api;

import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ReqDetail;
import com.ose.materialspm.entity.ViewMxjReqs;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 请购单查询接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ReqAPI {

    /**
     * 获取请购单列表
     *
     * @return 请购单列表
     */
    @RequestMapping(
        method = GET,
        value = "reqs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ViewMxjReqs> getList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqListCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "req",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ViewMxjReqs> getReq(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO);

    @RequestMapping(
        method = GET,
        value = "export-req",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportReq(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO);

    /**
     * 获取请购单详情
     *
     * @return 请购单详情
     */
    @RequestMapping(
        method = GET,
        value = "req-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ReqDetail> getDetail(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO
    );

}
