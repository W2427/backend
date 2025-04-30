package com.ose.report.api;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ModuleRelationDTO;
import com.ose.report.dto.ModuleRelationSearchDTO;
import com.ose.report.dto.ModuleShipmentDetailDTO;
import com.ose.report.dto.moduleProcess.ModuleProcessStaticDTO;
import com.ose.report.entity.moduleProcess.ModuleProcessStatics;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 模块完成进度统计
 */
@FeignClient(name = "ose-report", contextId = "moduleProcessStaticsFeign")
public interface ModuleProcessStaticsAPI {

    /**
     * 创建模块完成进度统计。
     *
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param moduleProcessStaticDTO 报告数据
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-statics"
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ModuleProcessStaticDTO moduleProcessStaticDTO
    );

    /**
     * 查询模块完成进度统计
     *
     * @param
     * @param
     * @return 查询模块完成进度统计
     */
    @Operation(
        summary = "查询模块完成进度统计",
        description = "查询模块完成进度统计。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-process-statics")
    @ResponseStatus(OK)
    JsonListResponseBody<ModuleProcessStatics> search(
        @PathVariable(value="orgId")  @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value="projectId")  @Parameter(description = "项目ID") Long projectId,
        PageDTO page);

    /**
     * 查询发运批次
     *
     * @param
     * @param
     * @return 查询发运批次
     */
    @Operation(
        summary = "查询发运批次",
        description = "查询发运批次。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ModuleRelationDTO> searchModuleRelationNos(
        @PathVariable(value="orgId")  @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value="projectId")  @Parameter(description = "项目ID") Long projectId,
        ModuleRelationSearchDTO moduleRelationSearchDTO
    );

    /**
     * 查询发运批次下的模块
     *
     * @param
     * @param
     * @return 查询发运批次下的模块
     */
    @Operation(
        summary = "查询发运批次下的模块",
        description = "查询发运批次下的模块。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no/{shipmentNo}/module")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ModuleRelationDTO> searchModuleNos(
        @PathVariable(value="orgId")  @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value="projectId")  @Parameter(description = "项目ID") Long projectId,
        @PathVariable(value="shipmentNo")  @Parameter(description = "发运批次号") String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO
    );

    /**
     * 查询发运批次下的模块信息
     *
     * @param
     * @param
     * @return 查询发运批次下的模块信息
     */
    @Operation(
        summary = "查询发运批次下的模块信息",
        description = "查询发运批次下的模块信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module-relations-no/{shipmentNo}/module-detail")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ModuleShipmentDetailDTO> searchModuleDetail(
        @PathVariable(value="orgId")  @Parameter(description = "组织ID") Long orgId,
        @PathVariable(value="projectId")  @Parameter(description = "项目ID") Long projectId,
        @PathVariable(value="shipmentNo")  @Parameter(description = "发运批次号") String shipmentNo,
        ModuleRelationSearchDTO moduleRelationSearchDTO
    );
}
