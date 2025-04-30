package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmIssueDetailEntity;
import com.ose.material.entity.MmIssueDetailQrCodeEntity;
import com.ose.material.entity.MmIssueEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-issue")
public interface MmIssueAPI {

    @Operation(
        summary = "创建出库单",
        description = "创建出库单。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "出库单信息") MmIssueCreateDTO mmIssueCreateDTO);

    /**
     * 获取出库单列表
     *
     * @return 出库单列表
     */
    @Operation(
        summary = "查询出库单列表",
        description = "查询出库单列表。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmIssueEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmIssueSearchDTO mmIssueSearchDTO);

    @Operation(
        summary = "编辑出库单",
        description = "编辑出库单"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody @Parameter(description = "出库单信息") MmIssueCreateDTO mmIssueCreateDTO

    );

    @Operation(
        summary = "删除出库单",
        description = "删除出库单"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId);

    @Operation(
        summary = "获取出库单详细信息",
        description = "获取出库单详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmIssueEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId);

    /**
     * 出库单明细信息导入
     *
     * @return 出库单明细信息导入
     */
    @Operation(
        summary = "出库单明细信息导入",
        description = "出库单明细信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody MmImportBatchTaskImportDTO importDTO);

    /**
     * 获取物料明细列表
     *
     * @return 获取物料明细列表
     */
    @Operation(
        summary = "获取物料明细列表",
        description = "获取物料明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}/details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmIssueDetailEntity> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单 id") Long materialIssueEntityId,
        MmIssueSearchDTO mmIssueSearchDTO);

    /**
     * 获取物料明细二维码列表
     *
     * @return 获取物料明细二维码列表
     */
    @Operation(
        summary = "获取物料明细二维码列表",
        description = "获取物料明细二维码列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}/details/{mmIssueDetailEntityId}/qrcode"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmIssueDetailQrCodeEntity> searchQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单 id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单明细 id") Long mmIssueDetailEntityId,
        MmIssueSearchDTO mmIssueSearchDTO);

    /**
     * 出库盘点。
     *
     * @return 出库盘点
     */
    @Operation(
        summary = "出库盘点。",
        description = "出库盘点。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/qrcode-inventory"
    )
    @ResponseStatus(OK)
    JsonResponseBody inventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody MmIssueInventoryQrCodeDTO mmIssueInventoryQrCodeDTO);

    /**
     * 出库确认。
     *
     * @return 出库确认
     */
    @Operation(
        summary = "出库确认。",
        description = "出库确认。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/confirm"
    )
    @ResponseStatus(OK)
    JsonResponseBody confirm(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId);

    /**
     * 添加出库详情。
     *
     * @return 添加出库详情
     */
    @Operation(
        summary = "添加出库详情。",
        description = "添加出库详情。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/detail"
    )
    @ResponseStatus(OK)
    JsonResponseBody addDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody MmIssueDetailCreateDTO mmIssueDetailCreateDTO);

    @Operation(
        summary = "删除出库单详情",
        description = "删除出库单详情"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}/detail/{mmIssueDetailEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单详情id") Long mmIssueDetailEntityId
    );

    @Operation(
        summary = "删除出库单详情二维码",
        description = "删除出库单详情二维码"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}/detail/{mmIssueDetailEntityId}/qrcode/{mmIssueDetailQrCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单详情id") Long mmIssueDetailEntityId,
        @PathVariable @Parameter(description = "出库单详情二维码ID") Long mmIssueDetailQrCodeId
    );
}
