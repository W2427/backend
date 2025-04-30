package com.ose.tasks.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.PrintConfigAPI;
import com.ose.tasks.domain.model.service.PrintConfigInterface;
import com.ose.tasks.dto.PrintConfigDTO;
import com.ose.tasks.entity.PrintConfig;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "打印机管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class PrintConfigController extends BaseController implements PrintConfigAPI {


    private final PrintConfigInterface printConfigService;

    /**
     * 构造方法
     */
    @Autowired
    public PrintConfigController(PrintConfigInterface printConfigService) {
        this.printConfigService = printConfigService;
    }

    @Override
    @Operation(
        summary = "添加打印机配置信息",
        description = "添加打印机配置信息"
    )
    @RequestMapping(
        method = POST,
        value = "/print-configs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<PrintConfig> create(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody PrintConfigDTO configDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            printConfigService.create(orgId, projectId, configDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询打印机配置信息列表",
        description = "查询打印机配置信息列表"
    )
    @RequestMapping(
        method = GET,
        value = "/print-configs"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<PrintConfig> getList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        PageDTO page) {
        return new JsonListResponseBody<>(
            getContext(),
            printConfigService.getList(orgId, projectId, page)
        );
    }

    @Override
    @Operation(
        summary = "查询打印机配置信息",
        description = "查询打印机配置信息"
    )
    @RequestMapping(
        method = GET,
        value = "/print-configs/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<PrintConfig> get(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(),
            printConfigService.get(orgId, projectId, id)
        );
    }

    @Override
    @Operation(
        summary = "删除打印机配置信息",
        description = "删除打印机配置信息"
    )
    @RequestMapping(
        method = DELETE,
        value = "/print-configs/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        printConfigService.delete(orgId, projectId, id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "修改打印机配置信息",
        description = "修改打印机配置信息"
    )
    @RequestMapping(
        method = POST,
        value = "/print-configs/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody PrintConfigDTO configDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            printConfigService.modify(orgId, projectId, id, configDTO)
        );
    }

    @Override
    @Operation(
        summary = "根据type查询打印机配置信息",
        description = "根据type查询打印机配置信息"
    )
    @RequestMapping(
        method = GET,
        value = "/print-configs/type/{type}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<PrintConfig> searchByType(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "type") String type) {
        return new JsonListResponseBody<>(
            printConfigService.searchByType(orgId, projectId, type)
        );
    }

}
