package com.ose.docs.api.project;

import com.ose.docs.entity.project.WBSEntitiesES;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * WBS 实体导入文件操作接口。
 */
@FeignClient(name = "ose-docs", contextId = "wbsEntityFeign")
public interface WBSEntityFeignAPI {

    /**
     * 取得 WBS 实体导入文件。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-import-files/{fileId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntitiesES> get(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        @PathVariable("fileId") String fileId
    );

}
