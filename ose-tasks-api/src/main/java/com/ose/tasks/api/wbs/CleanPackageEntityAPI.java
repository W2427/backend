package com.ose.tasks.api.wbs;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.CLPEntryCriteriaDTO;
import com.ose.tasks.dto.CleanPackageEntryUpdateDTO;
import com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface CleanPackageEntityAPI {

    /**
     * 查询清洁包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 清洁包实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends CleanPackageEntityBase> searchCleanPackageEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        CLPEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得清洁包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 清洁包详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<CleanPackageEntityBase> getCleanPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 更新清洁包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateCleanPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody CleanPackageEntryUpdateDTO clpEntryUpdateDTO
    );

    /**
     * 删除清洁包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteCleanPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 按条件下载清洁包实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/clean-package-entities/download"
    )
    void downloadCLPEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        CLPEntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
