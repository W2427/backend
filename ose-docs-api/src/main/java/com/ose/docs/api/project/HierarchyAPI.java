package com.ose.docs.api.project;

import com.ose.docs.entity.project.HierarchyES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 项目层级结构导入文件操作接口。
 */
public interface HierarchyAPI extends HierarchyFeignAPI {

    /**
     * 查询项目层级结构导入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/hierarchy-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<HierarchyES> list(
        @PathVariable String orgId,
        PageDTO pageDTO
    );

    /**
     * 查询项目层级结构导入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<HierarchyES> list(
        @PathVariable String orgId,
        @PathVariable String projectId,
        PageDTO pageDTO
    );

}
