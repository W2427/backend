package com.ose.tasks.api.wbs;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wbs.PTPEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.PressureTestPackageEntryUpdateDTO;
import com.ose.tasks.entity.wbs.entity.PressureTestPackageEntityBase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface PressureTestPackageEntityAPI {

    /**
     * 查询试压包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 试压包实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends PressureTestPackageEntityBase> searchPressureTestPackageEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PTPEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得试压包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 试压包实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<PressureTestPackageEntityBase> getPressureTestPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 更新试压包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updatePressureTestPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody PressureTestPackageEntryUpdateDTO ptpEntryUpdateDTO
    );

    /**
     * 删除试压包实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     * @param version   项目版本号
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deletePressureTestPackageEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 按条件下载试压包实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pressure-test-package-entities/download"
    )
    void downloadPTPEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PTPEntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
