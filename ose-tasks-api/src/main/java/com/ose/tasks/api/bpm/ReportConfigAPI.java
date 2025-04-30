package com.ose.tasks.api.bpm;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ReportConfigCriteriaDTO;
import com.ose.tasks.dto.bpm.ReportConfigDTO;
import com.ose.tasks.dto.bpm.ReportConfigResponseDTO;
import com.ose.tasks.entity.report.ReportConfig;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 报告配置管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/reportConfig")
public interface ReportConfigAPI {

    /**
     * 获取报告配置列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "processId/{processId}"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ReportConfigResponseDTO> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("processId") Long processId,
        ReportConfigCriteriaDTO criteriaDTO
    );

    /**
     * 获取报告配置详细信息
     *
     * @return 报告配置
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ReportConfig> get(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 删除报告配置
     *
     * @param id 报告配置id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteStep(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 创建报告配置
     *
     * @param reportConfigDTO 报告配置信息
     * @return 报告配置信息
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportConfigResponseDTO> create(
        @RequestBody ReportConfigDTO reportConfigDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 编辑报告配置
     *
     * @param reportConfigDTO 报告配置信息
     * @param id         报告配置id
     * @return 报告配置信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    JsonObjectResponseBody<ReportConfigResponseDTO> edit(
        @RequestBody ReportConfigDTO reportConfigDTO,
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

}
