package com.ose.test.api;

import com.ose.test.dto.BomNodeDTO;
import com.ose.test.dto.BomNodeResultsDTO;
import com.ose.test.dto.ExportFileDTO;
import com.ose.test.entity.ViewMxjPosEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface BomAPI {

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "bomnodes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BomNodeResultsDTO> bomNodes(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        String projId
    );

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "boms"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ViewMxjPosEntity> boms(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        BomNodeDTO bomNodeDTO
    );

    /**
     * 导出BOM List
     *
     * @param projectId  项目ID
     * @param orgId      组织ID
     * @param bomNodeDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "export-boms"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportBoms(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        BomNodeDTO bomNodeDTO
    );
}
