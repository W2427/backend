package com.ose.tasks.api.material;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialStructureNestDTO;
import com.ose.tasks.dto.material.FMaterialStructureNestImportDTO;
import com.ose.tasks.entity.material.FMaterialRequisitionEntity;
import com.ose.tasks.entity.material.FMaterialStructureNest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 结构套料方案接口。
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface FMaterialStructureNestAPI {

    /**
     * 创建结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @RequestMapping(method = POST, value = "material-structure-nest")
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody FMaterialStructureNestDTO fMaterialStructureNestDTO
    );

    /**
     * 结构套料方案列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest")
    @Operation(summary = "结构套料方案列表", description = "结构套料方案列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    JsonListResponseBody<FMaterialStructureNest> search(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                        @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                        PageDTO pageDTO
    );

    /**
     * 结构套料方案详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "material-structure-nest/{fMaterialStructureNestId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<FMaterialStructureNest> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId
    );

    /**
     * 更新结构套料方案。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId,
        @RequestBody FMaterialStructureNestDTO fMaterialStructureNestDTO
    );

    /**
     * 删除结构套料方案。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "material-structure-nest/{fMaterialStructureNestId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId
    );

    /**
     * 导入结构套料。
     *
     * @param orgId                           组织id
     * @param projectId                       项目id
     * @param fMaterialStructureNestId        结构套料方案id
     * @param version                         项目版本
     * @param fMaterialStructureNestImportDTO 导入文件
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody importStructureNest(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId,
        @RequestParam("version") long version,
        @RequestBody FMaterialStructureNestImportDTO fMaterialStructureNestImportDTO
    );

    /**
     * 创建结构套料方案流程。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/activity"
    )
    @ResponseStatus(OK)
    JsonResponseBody createActivity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId
    );

    /**
     * 更新结构套料方案流程状态。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/activity/modify"
    )
    @ResponseStatus(OK)
    JsonResponseBody changeActivityStatus(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId,
        @RequestParam("version") long version
    );

    /**
     * 关联领料单。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/material-requisitions"
    )
    @ResponseStatus(OK)
    JsonResponseBody saveMaterialRequisitions(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId,
        @RequestBody FMaterialStructureNestDTO fMaterialStructureNestDTO
    );

    /**
     * 查找领料单详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "material-structure-nest/{fMaterialStructureNestId}/material-requisitions"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<FMaterialRequisitionEntity> findMaterialRequisitionsByEntityId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId
    );

    /**
     * 设置分包商。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param fMaterialStructureNestId  结构套料方案id
     * @param fMaterialStructureNestDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "material-structure-nest/{fMaterialStructureNestId}/subcontractor"
    )
    @ResponseStatus(OK)
    JsonResponseBody setSubcontractor(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("fMaterialStructureNestId") Long fMaterialStructureNestId,
        @RequestBody FMaterialStructureNestDTO fMaterialStructureNestDTO
    );

}
