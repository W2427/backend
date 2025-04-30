package com.ose.report.controller;

import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.report.api.ModuleProcessStaticsAPI;
import com.ose.report.domain.service.ModuleProcessStaticsInterface;
import com.ose.report.dto.ModuleRelationDTO;
import com.ose.report.dto.ModuleRelationSearchDTO;
import com.ose.report.dto.ModuleShipmentDetailDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessStaticDTO;
import com.ose.report.entity.moduleProcess.ModuleProcessStatics;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "模块完成进度统计")
@RestController
public class ModuleProcessStaticsController extends BaseController implements ModuleProcessStaticsAPI {

    private final ModuleProcessStaticsInterface moduleProcessStaticsInterface;

    /**
     * 构造方法。
     */
    @Autowired
    public ModuleProcessStaticsController(ModuleProcessStaticsInterface moduleProcessStaticsInterface) {
        this.moduleProcessStaticsInterface = moduleProcessStaticsInterface;
    }

    @Override
    @Operation(description = "创建模块完成进度统计")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-statics"
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @PathVariable(value = "orgId") @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目ID") Long projectId,
        @RequestBody @Parameter(description = "模块完成进度统计DTO") ModuleProcessStaticDTO moduleProcessStaticDTO) {
        moduleProcessStaticsInterface.create(
            orgId,
            projectId,
            moduleProcessStaticDTO
        );
        return new JsonResponseBody();
    }

    /**
     * 查询模块完成进度统计
     *
     * @param
     * @param
     * @return 查询模块完成进度统计
     */
    @Override
    @Operation(
        summary = "查询模块完成进度统计",
        description = "查询模块完成进度统计。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-statics")
    @ResponseStatus(OK)
    public JsonListResponseBody<ModuleProcessStatics> search(
        @PathVariable(value = "orgId") @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目ID") Long projectId,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            moduleProcessStaticsInterface.search(orgId, projectId, page)
        );
    }

    /**
     * 查询发运批次
     *
     * @param
     * @param
     * @return 查询发运批次
     */
    @Override
    @Operation(
        summary = "查询发运批次",
        description = "查询发运批次。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no")
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ModuleRelationDTO> searchModuleRelationNos(
        @PathVariable(value = "orgId") @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目ID") Long projectId,
        ModuleRelationSearchDTO moduleRelationSearchDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            moduleProcessStaticsInterface.searchModuleRelationNos(orgId, projectId, moduleRelationSearchDTO.getDiscipline())
        );
    }

    /**
     * 查询发运批次下的模块
     *
     * @param
     * @param
     * @return 查询发运批次
     */
    @Override
    @Operation(
        summary = "查询发运批次",
        description = "查询发运批次。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no/{shipmentNo}/module")
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ModuleRelationDTO> searchModuleNos(
        @PathVariable(value = "orgId") @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目ID") Long projectId,
        @PathVariable(value = "shipmentNo") @Parameter(description = "发运批次号") String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            moduleProcessStaticsInterface.searchModuleNos(orgId, projectId, shipmentNo, moduleRelationSearchDTO.getDiscipline())
        );
    }

    /**
     * 查询发运批次下的模块信息
     *
     * @param
     * @param
     * @return 查询发运批次下的模块信息
     */
    @Override
    @Operation(
        summary = "查询发运批次下的模块信息",
        description = "查询发运批次下的模块信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no/{shipmentNo}/module-detail")
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ModuleShipmentDetailDTO> searchModuleDetail(
        @PathVariable(value = "orgId") @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目ID") Long projectId,
        @PathVariable(value = "shipmentNo") @Parameter(description = "发运批次号") String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            moduleProcessStaticsInterface.searchModuleDetail(orgId, projectId, shipmentNo, moduleRelationSearchDTO)
        );
    }
}
