package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.worksite.WorkSiteMoveDTO;
import com.ose.tasks.dto.worksite.WorkSitePatchDTO;
import com.ose.tasks.dto.worksite.WorkSitePostDTO;
import com.ose.tasks.entity.worksite.WorkSite;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 工作场地管理接口。
 */
public interface WorkSiteAPI {

    /**
     * 查询工作场地信息。
     */
    @RequestMapping(
        method = GET,
        value = "/companies/{companyId}/work-sites",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WorkSite> search(
        @PathVariable("companyId") Long companyId,
        @RequestParam(name = "projectId", required = false) Long projectId,
        PageDTO pageDTO
    );

    /**
     * 查询工作场地信息。
     */
    @RequestMapping(
        method = GET,
        value = "/companies/{companyId}/work-sites/{parentId}/work-sites",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WorkSite> search(
        @PathVariable("companyId") Long companyId,
        @PathVariable("parentId") Long parentId,
        @RequestParam(name = "projectId", required = false) Long projectId,
        PageDTO pageDTO
    );

    /**
     * 取得工作场地信息。
     */
    @RequestMapping(
        method = GET,
        value = "/companies/{companyId}/work-sites/{workSiteId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> get(
        @PathVariable("companyId") Long companyId,
        @PathVariable("workSiteId") Long workSiteId
    );

    /**
     * 创建工作场地信息。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/work-sites",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> create(
        @PathVariable("companyId") Long companyId,
        @RequestBody WorkSitePostDTO workSiteDTO
    );

    /**
     * 创建工作场地信息。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/work-sites/{parentId}/work-sites",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> create(
        @PathVariable("companyId") Long companyId,
        @PathVariable("parentId") Long parentId,
        @RequestBody WorkSitePostDTO workSiteDTO
    );

    /**
     * 创建工作场地信息。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/projects/{projectId}/work-sites",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> createForProject(
        @PathVariable("companyId") Long companyId,
        @PathVariable("projectId") Long projectId,
        @RequestBody WorkSitePostDTO workSiteDTO
    );

    /**
     * 创建工作场地信息。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/projects/{projectId}/work-sites/{parentId}/work-sites",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> createForProject(
        @PathVariable("companyId") Long companyId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("parentId") Long parentId,
        @RequestBody WorkSitePostDTO workSiteDTO
    );

    /**
     * 更新工作场地信息。
     */
    @RequestMapping(
        method = PATCH,
        value = "/companies/{companyId}/work-sites/{workSiteId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> update(
        @PathVariable("companyId") Long companyId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version,
        @RequestBody WorkSitePatchDTO workSiteDTO
    );

    /**
     * 更新工作场地信息。
     */
    @RequestMapping(
        method = PATCH,
        value = "/companies/{companyId}/projects/{projectId}/work-sites/{workSiteId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> update(
        @PathVariable("companyId") Long companyId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version,
        @RequestBody WorkSitePatchDTO workSiteDTO
    );

    /**
     * 移动工作场地。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/work-sites/{workSiteId}/move",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> move(
        @PathVariable("companyId") Long companyId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version,
        @RequestBody WorkSiteMoveDTO workSiteDTO
    );

    /**
     * 移动工作场地。
     */
    @RequestMapping(
        method = POST,
        value = "/companies/{companyId}/projects/{projectId}/work-sites/{workSiteId}/move",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WorkSite> move(
        @PathVariable("companyId") Long companyId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version,
        @RequestBody WorkSiteMoveDTO workSiteDTO
    );

    /**
     * 删除工作场地信息。
     */
    @RequestMapping(
        method = DELETE,
        value = "/companies/{companyId}/work-sites/{workSiteId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("companyId") Long companyId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version
    );

    /**
     * 删除工作场地信息。
     */
    @RequestMapping(
        method = DELETE,
        value = "/companies/{companyId}/projects/{projectId}/work-sites/{workSiteId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("companyId") Long companyId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workSiteId") Long workSiteId,
        @RequestParam("version") long version
    );

}
