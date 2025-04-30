package com.ose.tasks.api.material;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.entity.material.FMaterialStructureNestDrawing;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 结构套料零件接口。
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface FMaterialStructureNestDrawingAPI {

    /**
     * 结构套料零件列表。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 套料结构方案id
     * @param pageDTO                  分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "material-structure-nest/{fMaterialStructureNestId}/drawing")
    @Operation(summary = "结构套料零件接口", description = "结构套料零件接口。")
    @WithPrivilege
    @ResponseStatus(OK)
    JsonListResponseBody<FMaterialStructureNestDrawing> search(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                               @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                               @PathVariable @Parameter(description = "套料结构方案id") Long fMaterialStructureNestId,
                                                               PageDTO pageDTO
    );

}
