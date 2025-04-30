package com.ose.tasks.api.wbs;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.SubSystemEntryCriteriaDTO;
import com.ose.tasks.dto.SubSystemEntryUpdateDTO;
import com.ose.tasks.entity.wbs.entity.SubSystemEntityBase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface SubSystemEntityAPI {

    /**
     * 查询子系统实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 子系统实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends SubSystemEntityBase> searchSubSystemEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        SubSystemEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得子系统实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 子系统详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<SubSystemEntityBase> getSubSystemEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 更新子系统实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateSubSystemEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody SubSystemEntryUpdateDTO ssEntryUpdateDTO
    );

    /**
     * 删除子系统实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteSubSystemEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 按条件下载子系统实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/sub-system-entities/download"
    )
    void downloadSubSystemEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        SubSystemEntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
