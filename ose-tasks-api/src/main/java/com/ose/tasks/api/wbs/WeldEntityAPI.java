package com.ose.tasks.api.wbs;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.wbs.*;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface WeldEntityAPI {

    /**
     * 查询焊口实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 焊口实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends WeldEntityBase> searchWeldEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得焊口实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 焊口实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends WeldEntityBase> getWeldEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除焊口实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWeldEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    );

    /**
     * 插入焊口实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWeldEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody WeldEntryInsertDTO weldEntryInsertDTO
    );

    /**
     * 更新焊口实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WeldEntityBase> updateWeldEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody WeldEntryUpdateDTO weldEntryUpdateDTO
    );

    /**
     * 更新父级。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveWeld(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    );

    /**
     * 取得焊口WPS信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/already-set-wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getAlreadySetWPSInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得焊口WPS信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/optional-wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getOptionalWPSInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 匹配单个焊口的WPS信息。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param entityId    实体ID
     * @param wpsInfoDTOS WPS信息
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/{entityId}/wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setWPSInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestBody List<WPSInfoDTO> wpsInfoDTOS
    );

    /**
     * 批量匹配焊口的WPS信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param weldMatchDTOS 焊口实体
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/wps-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setWPSInfos(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody List<WeldMatchDTO> weldMatchDTOS
    );

    /**
     * 按条件下载焊口实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/download"
    )
    void downloadWeldEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WeldEntryCriteriaDTO criteriaDTO
    ) throws IOException;

    /**
     * 导入xls 更新焊口WPS
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO
     */
    @RequestMapping(method = POST, value = "/orgs/{orgId}/projects/{projectId}/weld-entities/import-wps",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    JsonObjectResponseBody<WpsImportResultDTO> importUpdateWps(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody WpsImportDTO uploadDTO);

    /**
     * 取得焊口和焊工共同的WPS列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/weld-entities/welder/wps",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getWelderWps(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WeldWelderWPSDTO weldWelderWPSDTO
    );

}
