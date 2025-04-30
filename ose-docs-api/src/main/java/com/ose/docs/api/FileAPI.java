package com.ose.docs.api;

import com.ose.docs.dto.FileCriteriaDTO;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 文件接口。
 */
public interface FileAPI extends FileFeignAPI {

    /**
     * 查询文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FileBasicViewES> search(
        @PathVariable("orgId") String orgId,
        FileCriteriaDTO criteria,
        PageDTO pageDTO
    );

    /**
     * 查询文件。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/files",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FileBasicViewES> search(
        @PathVariable("orgId") String orgId,
        @PathVariable("projectId") String projectId,
        FileCriteriaDTO criteria,
        PageDTO pageDTO
    );

}
