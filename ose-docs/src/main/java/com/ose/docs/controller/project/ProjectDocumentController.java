package com.ose.docs.controller.project;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.project.ProjectDocumentAPI;
import com.ose.docs.domain.model.service.project.DocumentInterface;
import com.ose.docs.entity.project.ProjectDocumentES;
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

@Tag(name = "项目文档文件接口")
@RestController
public class ProjectDocumentController extends BaseController implements ProjectDocumentAPI {

    // 项目层级结构导入文件查询服务
    private final DocumentInterface<ProjectDocumentES> projectDocumentService;

    /**
     * 构造方法。
     *
     * @param projectDocumentService 项目文档文件查询服务
     */
    @Autowired
    public ProjectDocumentController(
        DocumentInterface<ProjectDocumentES> projectDocumentService
    ) {
        this.projectDocumentService = projectDocumentService;
    }

    @Override
    @Operation(description = "取得项目文档文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/project-document-files/{fileId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    public JsonObjectResponseBody<ProjectDocumentES> get(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @PathVariable @Parameter(description = "文件 ID") String fileId
    ) {
        return new JsonObjectResponseBody<>(
            projectDocumentService.get(orgId, projectId, fileId)
        );
    }

    @Override
    @Operation(description = "查询项目文档文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/project-document-files",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonListResponseBody<ProjectDocumentES> list(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return list(orgId, null, pageDTO);
    }

    @Override
    @Operation(description = "查询项目文档文件")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/project-document-files",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonListResponseBody<ProjectDocumentES> list(
        @PathVariable @Parameter(description = "所属组织 ID") String orgId,
        @PathVariable @Parameter(description = "项目 ID") String projectId,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            projectDocumentService.list(orgId, projectId, pageDTO.toPageable())
        );
    }

}
