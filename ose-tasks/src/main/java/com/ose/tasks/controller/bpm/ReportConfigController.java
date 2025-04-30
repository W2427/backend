package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ReportConfigAPI;
import com.ose.tasks.domain.model.service.bpm.ReportConfigInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.report.ReportConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "工序接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/reportConfig")
public class ReportConfigController extends BaseController implements ReportConfigAPI {

    /**
     *  报告配置服务
     */
    private final ReportConfigInterface reportConfigService;

    /**
     * 构造方法
     *
     * @param reportConfigService 报告配置服务
     */
    @Autowired
    public ReportConfigController(ReportConfigInterface reportConfigService) {
        this.reportConfigService = reportConfigService;
    }

    /**
     * 获取报告配置列表
     *
     * @return 报告配置列表
     */
    @Override
    @Operation(
        summary = "查询报告配置",
        description = "获取报告配置列表。"
    )
    @RequestMapping(
        method = GET,
        value = "processId/{processId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReportConfigResponseDTO> getList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "processId") Long processId,
        ReportConfigCriteriaDTO criteriaDTO) {

        Page<ReportConfig> reportConfig = reportConfigService.getList(criteriaDTO, projectId, orgId, processId);

        return new JsonListResponseBody(reportConfig);
    }


    @Override
    @Operation(
        summary = "创建报告配置",
        description = "根据报告配置信息，创建报告配置。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ReportConfigResponseDTO> create(
        @RequestBody @Parameter(description = "报告配置信息") ReportConfigDTO reportConfigDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        ReportConfigResponseDTO rcDTO = new ReportConfigResponseDTO(reportConfigService.create(reportConfigDTO, projectId, orgId));

        return new JsonObjectResponseBody(rcDTO);
    }


    @Override
    @Operation(
        summary = "编辑报告配置",
        description = "编辑指定的报告配置"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReportConfigResponseDTO> edit(
        @RequestBody @Parameter(description = "报告配置信息") ReportConfigDTO reportConfigDTO,
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        reportConfigService.modify(id, reportConfigDTO, projectId, orgId);
        return new JsonObjectResponseBody<>();
    }


    @Override
    @Operation(
        summary = "删除报告配置",
        description = "删除指定的报告配置"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteStep(
        @PathVariable @Parameter(description = "报告配置id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        reportConfigService.delete(id, projectId, orgId);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取报告配置详细信息",
        description = "根据ID查询报告配置详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReportConfig> get(
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(getContext(), reportConfigService.get(id, projectId, orgId));
    }
}
