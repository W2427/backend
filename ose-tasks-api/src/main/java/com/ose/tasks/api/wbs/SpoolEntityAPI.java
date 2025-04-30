package com.ose.tasks.api.wbs;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.SpoolEntryInsertDTO;
import com.ose.tasks.dto.wbs.SpoolEntryUpdateDTO;
import com.ose.tasks.entity.wbs.entity.SpoolEntityBase;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface SpoolEntityAPI {

    /**
     * 查询单管实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 单管实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends SpoolEntityBase> searchSpoolEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得单管实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 单管实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends SpoolEntityBase> getSpoolEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除单管实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteSpoolEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 插入单管实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addSpoolEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody SpoolEntryInsertDTO spoolEntryInsertDTO
    );

    /**
     * 更新单管实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateSpoolEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody SpoolEntryUpdateDTO spoolEntryUpdateDTO
    );

    /**
     * 更新单管父级。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveSpool(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentPutDTO
    );

    /**
     * 取得单管图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getDrawingInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得单管材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getMaterialInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 按条件下载单管实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/download"
    )
    void downloadSpoolEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO
    ) throws IOException;

    /**
     * 取得单管实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param qrcode  实体二维码
     * @return 单管实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-entities/qr-code/{qrcode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends SpoolEntityBase> getSpoolEntityByQrCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("qrcode") String qrcode
    );
}
