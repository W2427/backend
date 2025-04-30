package com.ose.tasks.controller;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.WeldMaterialAPI;
import com.ose.tasks.domain.model.service.WeldMaterialInterface;
import com.ose.tasks.dto.WeldMaterialDTO;
import com.ose.tasks.dto.WpsWeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(description = "焊材接口")
@RequestMapping(value = "/orgs")
public class WeldMaterialController extends BaseController implements WeldMaterialAPI {

    private final WeldMaterialInterface weldMaterialService;

    public WeldMaterialController(WeldMaterialInterface weldMaterialService) {
        this.weldMaterialService = weldMaterialService;
    }

    /**
     * 创建焊材信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param weldMaterialDTO 焊材信息
     */
    @Operation(
        summary = "创建焊材信息",
        description = "创建焊材信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/weld-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WeldMaterialDTO weldMaterialDTO
    ) {

        ContextDTO context = getContext();

        weldMaterialService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            weldMaterialDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取焊材分页列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 焊材列表
     */
    @Operation(
        summary = "获取焊材列表（分页）",
        description = "获取焊材列表（分页）"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<WeldMaterial> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WeldMaterialDTO weldMaterialDTO,
        PageDTO pageDTO
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            weldMaterialService.search(
                orgId,
                projectId,
                weldMaterialDTO.getBatchNo(),
                pageDTO
            )
        );
    }

    /**
     * 删除焊材信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  批次号
     */
    @Operation(
        summary = "删除焊材信息",
        description = "删除焊材信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/weld-material/{batchNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody detele(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "批次号") String batchNo
    ) {

        ContextDTO context = getContext();

        weldMaterialService.delete(
            context.getOperator().getId(),
            orgId,
            projectId,
            batchNo
        );
        return new JsonResponseBody();
    }

    /**
     * 获取焊材详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  批次号
     * @return 焊材详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-material/{batchNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<WeldMaterial> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "批次号") String batchNo
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            weldMaterialService.get(
                orgId,
                projectId,
                batchNo
            )
        );
    }

    /**
     * 获取焊材详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊材详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-material-batch",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<WeldMaterial> getWeldMaterialDetail(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WpsWeldMaterialDTO wpsWeldMaterialDTO
    ) {

        return new JsonListResponseBody<>(
            weldMaterialService.getDetail(
                orgId,
                projectId,
                wpsWeldMaterialDTO
            )
        );
    }

    /**
     * 根据焊材类型筛选
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 焊材详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-material-weldMaterialType",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<WeldMaterial> getWeldMaterialList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WeldMaterialDTO weldMaterialDTO
    ) {

        return new JsonListResponseBody<>(
            weldMaterialService.getList(
                orgId,
                projectId,
                weldMaterialDTO
            )
        );
    }


    /**
     * 更新焊材信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param batchNo  分包商ID
     * @param weldMaterialDTO 更新信息
     */
    @Operation(
        summary = "更新分包商信息",
        description = "更新分包商信息"
    )
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/weld-material/{batchNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "分包商ID") String batchNo,
        @RequestBody WeldMaterialDTO weldMaterialDTO
    ) {

        ContextDTO context = getContext();

        weldMaterialService.update(
            context.getOperator().getId(),
            orgId,
            projectId,
            batchNo,
            weldMaterialDTO
        );
        return new JsonResponseBody();
    }
}
