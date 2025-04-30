package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.ItpAPI;
import com.ose.tasks.domain.model.service.ItpInterface;
import com.ose.tasks.dto.ItpCreateDTO;
import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.dto.ItpUpdateDTO;
import com.ose.tasks.entity.Itp;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "质量检验计划接口")
@RestController
@RequestMapping("/orgs")
public class ItpController extends BaseController implements ItpAPI {

    private ItpInterface itpService;

    @Autowired
    public ItpController(
        ItpInterface itpService
    ) {
        this.itpService = itpService;
    }

    /**
     * 创建质量检验计划。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpCreateDTO ITP信息
     */
    @Operation(
        summary = "创建ITP信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/itps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody ItpCreateDTO itpCreateDTO
    ) {

        ContextDTO context = getContext();

        itpService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            itpCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    @Operation(summary = "获取ITP列表")
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/itps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<Itp> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        ItpCriteriaDTO itpCriteriaDTO
    ) {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            itpService.search(orgId, projectId, itpCriteriaDTO)
        );
    }

    /**
     * 获取ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     * @return ITP详情
     */
    @Operation(
        summary = "获取ITP详情"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<Itp> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "ITPID") Long itpId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            itpService.get(
                orgId,
                projectId,
                itpId
            )
        );

    }

    /**
     * 更新ITP详情。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpId        ITPID
     * @param itpUpdateDTO 更新ITP信息
     */
    @Operation(
        summary = "更新ITP信息"
    )
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "ITPID") Long itpId,
        @RequestBody ItpUpdateDTO itpUpdateDTO
    ) {

        ContextDTO context = getContext();

        itpService.update(
            context.getOperator().getId(),
            orgId,
            projectId,
            itpId,
            itpUpdateDTO
        );
        return new JsonResponseBody();
    }

    /**
     * 删除ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     */
    @Operation(
        summary = "删除ITP信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "ITPID") Long itpId
    ) {

        ContextDTO context = getContext();

        itpService.delete(
            context.getOperator().getId(),
            orgId,
            projectId,
            itpId
        );
        return new JsonResponseBody();
    }
}
