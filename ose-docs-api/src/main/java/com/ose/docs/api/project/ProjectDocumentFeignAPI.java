package com.ose.docs.api.project;

import com.ose.docs.entity.project.ProjectDocumentES;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 项目文档入文件操作接口。
 */
@FeignClient(name = "ose-docs", contextId = "projectDocFeign")
public interface ProjectDocumentFeignAPI {

    /**
     * 取得项目文档入文。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/project-document-files/{fileId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ProjectDocumentES> get(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("fileId") String fileId
    );

}
