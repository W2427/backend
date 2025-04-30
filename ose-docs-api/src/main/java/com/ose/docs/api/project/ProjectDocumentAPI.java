package com.ose.docs.api.project;

import com.ose.docs.entity.project.ProjectDocumentES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 项目文档入文件操作接口。
 */
public interface ProjectDocumentAPI extends ProjectDocumentFeignAPI {

    /**
     * 查询项目文档入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/project-document-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectDocumentES> list(
        @PathVariable String orgId,
        PageDTO pageDTO
    );

    /**
     * 查询项目文档入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/project-document-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectDocumentES> list(
        @PathVariable String orgId,
        @PathVariable String projectId,
        PageDTO pageDTO
    );

}
