package com.ose.docs.api.project;

import com.ose.docs.entity.project.HierarchyES;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 项目层级结构导入文件操作接口。
 */
@FeignClient(name = "ose-docs", contextId = "hierarchyFeign")
public interface HierarchyFeignAPI {

    /**
     * 取得项目层级结构导入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy-import-files/{fileId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyES> get(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("fileId") String fileId
    );

}
