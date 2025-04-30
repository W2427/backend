package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "ose-material", contextId = "mmReleaseReceiveFeign")
public interface MmReleaseReceiveFeignAPI {

    @Operation(
        summary = "创建入库单",
        description = "创建入库单。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/release-receive",
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "创建入库单信息") MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO);

    /**
     * 获取入库单列表
     *
     * @return 入库单列表
     */
    @Operation(
        summary = "查询入库单列表",
        description = "获取入库单列表。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/release-receive",
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO);

    @Operation(
        summary = "编辑入库单",
        description = "编辑入库单"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody @Parameter(description = "入库单信息") MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO

    );

    @Operation(
        summary = "开始盘点",
        description = "开始盘点"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/running-status"
    )
    @ResponseStatus(OK)
    JsonResponseBody updateRunningStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody @Parameter(description = "入库单信息") MmReleaseReceiveUpdateRunningStatusDTO mmReleaseReceiveUpdateRunningStatusDTO

    );

    @Operation(
        summary = "删除入库单",
        description = "删除入库单"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId);

    @Operation(
        summary = "获取入库单详细信息",
        description = "获取入库单详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmReleaseReceiveEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId);

    /**
     * 入库单信息导入
     *
     * @return 入库单信息导入
     */
    @Operation(
        summary = "入库单信息导入",
        description = "入库单信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail-import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "materialReceiveNoteId") Long materialReceiveNoteId,
        @RequestBody MmImportBatchTaskImportDTO importDTO);

    /**
     * 查询入库单明细
     *
     * @return 查询入库单明细
     */
    @Operation(
        summary = "查询入库单明细",
        description = "查询入库单明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveDetailSearchDetailDTO> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO
    );

    /**
     * 查询入库单是否完成盘库
     *
     * @return 查询入库单是否完成盘库
     */
    @Operation(
        summary = "查询入库单是否完成盘库",
        description = "查询入库单是否完成盘库。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/inventory-status"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmReleaseReceiveInventoryStatusDTO> searchInventoryStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId
    );

    @Operation(
        summary = "删除入库单明细",
        description = "删除入库单明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/details/{materialReceiveNoteDetailId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteDetailId);

    @Operation(
        summary = "删除入库单明细二维码",
        description = "删除入库单明细二维码"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/details/{materialReceiveNoteDetailId}/qrcode/{qrcodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteDetailId,
        @PathVariable @Parameter(description = "二维码id") Long qrcodeId);

    /**
     * 查询入库单明细二维码
     *
     * @return 查询入库单明细二维码
     */
    @Operation(
        summary = "查询入库单明细二维码",
        description = "查询入库单明细二维码。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailEntityDetailId}/qrcode"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveDetailQrCodeEntity> searchDetailQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单明细id") Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeSearchDTO mmReleaseReceiveQrCodeSearchDTO);

    /**
     * 生成入库单明细二维码
     *
     * @return 生成入库单明细二维码
     */
    @Operation(
        summary = "生成入库单明细二维码",
        description = "生成入库单明细二维码。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailEntityDetailId}/qrcode"
    )
    @ResponseStatus(OK)
    JsonResponseBody createDetailQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单明细id") Long materialReceiveNoteDetailEntityDetailId,
        @RequestBody MmReleaseReceiveQrCodeCreateDTO mmReleaseReceiveQrCodeCreateDTO);

    /**
     * 入库单明细二维码盘点。
     *
     * @return 入库单明细二维码盘点
     */
    @Operation(
        summary = "入库单明细二维码盘点。",
        description = "入库单明细二维码盘点。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}/qrcode-inventory"
    )
    @ResponseStatus(OK)
    JsonResponseBody inventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
//        @PathVariable @Parameter(description = "入库单详情二维码id") Long materialReceiveNoteDetailQrCodeId,
        @RequestBody MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO);

    /**
     * 入库单明细二维码取消盘点。
     *
     * @return 入库单明细二维码取消盘点
     */
    @Operation(
        summary = "入库单明细二维码取消盘点。",
        description = "入库单明细二维码取消盘点。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/qrcode-inventory-cancel"
    )
    @ResponseStatus(OK)
    JsonResponseBody cancelInventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO);

    /**
     * 查找入库二维码信息。
     *
     * @return 查找入库二维码信息
     */
    @Operation(
        summary = "查找入库二维码信息。",
        description = "查找入库二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/qrcode/{qrCode}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmReleaseReceiveQrCodeResultDTO> qrCodeSearch(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "二维码") String qrCode
    );

    /**
     * 入库
     *
     * @return 入库
     */
    @Operation(
        summary = "入库",
        description = "入库。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/receive"
    )
    @ResponseStatus(OK)
    JsonResponseBody receive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO);

    /**
     * 单项材料入库
     *
     * @return 单项材料入库
     */
    @Operation(
        summary = "单项材料入库",
        description = "单项材料入库。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}/receive"
    )
    @ResponseStatus(OK)
    JsonResponseBody detailReceive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
        @RequestBody MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO);

    /**
     * 修改入库单放行数量。
     *
     * @return 修改入库单放行数量
     */
    @Operation(
        summary = "修改入库单放行数量。",
        description = "修改入库单放行数量。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody updateDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库详情ID") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
        @RequestBody MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO);

    /**
     * 获取材料编码下生成的二维码的信息。
     *
     * @return 获取材料编码下生成的二维码的信息
     */
    @Operation(
        summary = "获取材料编码下生成的二维码的信息。",
        description = "获取材料编码下生成的二维码的信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/items/{materialReceiveNoteDetailId}/qrcodes"
    )
    @ResponseStatus(OK)
    JsonResponseBody getReleaseNoteItemQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库详情ID") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId);


    /**
     * 批量修改入库单放行数量。
     *
     * @return 批量修改入库单放行数量
     */
    @Operation(
        summary = "批量修改入库单放行数量。",
        description = "批量修改入库单放行数量。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/batch-update"
    )
    @ResponseStatus(OK)
    JsonResponseBody batchUpdateDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库详情ID") Long materialReceiveNoteId,
        @RequestBody MmReleaseReceiveBatchUpdateDTO mmReleaseReceiveBatchUpdateDTO);


    /**
     * 按条件下载入库单详情。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param mmReleaseReceiveSearchDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{materialReceiveNoteId}/download"
    )
    void download(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("materialReceiveNoteId") Long materialReceiveNoteId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO
    ) throws IOException;

    /**
     * 获取入库单文件记录
     *
     * @return 入库单文件记录
     */
    @Operation(
        summary = "查询入库单文件记录",
        description = "查询入库单文件记录。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{releaseReceiveId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveFileEntity> searchFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long releaseReceiveId,
        MmReleaseReceiveFileSearchDTO mmReleaseReceiveFileSearchDTO);

    @Operation(
        summary = "创建入库单文件",
        description = "创建入库单文件。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/{releaseReceiveId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody createFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long releaseReceiveId,
        @RequestBody @Parameter(description = "采购包信息") MmReleaseReceiveFileCreateDTO mmReleaseReceiveFileCreateDTO);

    /**
     * 获取手机端入库单待办列表
     *
     * @return 入库单列表
     */
    @Operation(
        summary = "获取手机端入库单待办列表",
        description = "获取手机端入库单待办列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/release-receive/mobile"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveEntity> searchByAssignee(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO);
}
