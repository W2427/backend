package com.ose.tasks.controller.wps;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wps.WeldWelderRelationAPI;
import com.ose.tasks.domain.model.service.wps.WeldWelderRelationInterface;
import com.ose.tasks.dto.wps.WeldWelderRelationCreateDTO;
import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(description = "焊口焊工关系接口")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/weld-welder-relation")
public class WeldWelderRelationController extends BaseController implements WeldWelderRelationAPI {

    private WeldWelderRelationInterface weldWelderRelationService;

    @Autowired
    public WeldWelderRelationController(
        WeldWelderRelationInterface weldWelderRelationService
    ) {
        this.weldWelderRelationService = weldWelderRelationService;
    }

    /**
     * 创建焊口焊工关系
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param weldWelderRelationCreateDTO 创建信息
     */
    @Operation(
        summary = "创建Wps",
        description = "创建Wps"
    )
    @PostMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WeldWelderRelationCreateDTO weldWelderRelationCreateDTO
    ) {
        ContextDTO context = getContext();
        weldWelderRelationService.create(
            orgId,
            projectId,
            context,
            weldWelderRelationCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 查询焊口焊工关系列表
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param weldWelderRelationSearchDTO 查询参数
     * @return 查询焊口焊工关系列表
     */
    @Operation(
        summary = "获取Wps列表",
        description = "获取Wps列表"
    )
    @GetMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<WeldWelderRelation> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            weldWelderRelationService.search(
                orgId,
                projectId,
                weldWelderRelationSearchDTO
            )
        );
    }

}
