package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.HierarchyNodeRelationAPI;
import com.ose.tasks.domain.model.service.HierarchyNodeRelationInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "层级关系接口")
@RestController
@RequestMapping("/orgs")
public class HierarchyNodeRelationController extends BaseController implements HierarchyNodeRelationAPI {

    private HierarchyNodeRelationInterface hierarchyNodeRelationService;

    @Autowired
    public HierarchyNodeRelationController(
        HierarchyNodeRelationInterface hierarchyNodeRelationService) {
        this.hierarchyNodeRelationService = hierarchyNodeRelationService;
    }

    /**
     * 创建层级关系数据。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     */
    @Operation(
        summary = "重新创建Hierarchy Node Relation 信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/re-generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) {

        ContextDTO context = getContext();

        hierarchyNodeRelationService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            context
        );

        return new JsonResponseBody();
    }


}
