package com.ose.docs.controller.project;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.project.HierarchyAPI;
import com.ose.docs.domain.model.service.project.DocumentInterface;
import com.ose.docs.entity.project.HierarchyES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "项目层级结构导入文件接口")
@RestController
public class HierarchyController extends BaseController implements HierarchyAPI {

    // 项目层级结构导入文件查询服务
    private final DocumentInterface<HierarchyES> hierarchyService;

    /**
     * 构造方法。
     *
     * @param hierarchyService 项目层级结构导入文件查询服务
     */
    @Autowired
    public HierarchyController(
        DocumentInterface<HierarchyES> hierarchyService
    ) {
        this.hierarchyService = hierarchyService;
    }

    @Override
    @Operation(description = "取得项目层级结构导入文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy-import-files/{fileId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    public JsonObjectResponseBody<HierarchyES> get(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) {
        return new JsonObjectResponseBody<>(
            hierarchyService.get(orgId, projectId, fileId)
        );
    }

    @Override
    @Operation(description = "查询项目层级结构导入文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/hierarchy-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonListResponseBody<HierarchyES> list(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return list(orgId, null, pageDTO);
    }

    @Override
    @Operation(description = "查询项目层级结构导入文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonListResponseBody<HierarchyES> list(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            hierarchyService.list(orgId, projectId, pageDTO.toPageable())
        );
    }

}
