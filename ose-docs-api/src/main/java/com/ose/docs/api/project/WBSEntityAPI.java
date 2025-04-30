package com.ose.docs.api.project;

import com.ose.docs.entity.project.WBSEntitiesES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * WBS 实体导入文件操作接口。
 */
public interface WBSEntityAPI extends WBSEntityFeignAPI {

    /**
     * 查询 WBS 实体导入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/entity-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntitiesES> list(
        @PathVariable String orgId,
        PageDTO pageDTO
    );

    /**
     * 查询 WBS 实体导入文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-import-files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntitiesES> list(
        @PathVariable String orgId,
        @PathVariable String projectId,
        PageDTO pageDTO
    );

}
