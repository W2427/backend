package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.material.ReleaseNoteEntity;
import com.ose.tasks.entity.material.ReleaseNoteItemDetailEntity;
import com.ose.tasks.entity.material.ReleaseNoteItemDetailQrCodeEntity;
import com.ose.tasks.entity.material.ReleaseNoteItemEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 放行单 接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/processes")
public interface ReleaseNoteAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postReleaseNote(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody ReleaseNotePostDTO releaseNotePostDTO);

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteEntity> getReleaseNotes(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchReleaseNote(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody ReleaseNotePatchDTO releaseNotePatchDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteReleaseNote(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemEntity> getReleaseNoteItemAll(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        ReleaseNoteItemSearchDTO releaseNoteItemSearchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemEntity> getReleaseNoteItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemDetailEntity> getReleaseNoteItemHeat(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemDetailQrCodeEntity> getReleaseNoteItemQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "relnItemDetailId") Long relnItemDetailId,
        PageDTO pageDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteItemDetailEntity> saveItemDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @RequestBody ReleaseNoteItemDetailDTO releaseNoteItemDetailDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteItemDetailEntity> patchItemDetail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "relnItemDetailId") Long relnItemDetailId,
        @RequestBody ReleaseNoteItemDetailPatchDTO releaseNoteItemDetailPatchDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemDetailQrCodeEntity> createQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "relnItemDetailId") Long relnItemDetailId,
        @RequestBody ReleaseNoteItemDetailQrCodeDto releaseNoteItemHeatNoQrCodeDto);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<QrCodeReleaseNoteItemVo> getQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        String qrCode
    );

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteInfoVo> getByRelnId(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteInfoVo> getReleaseNoteItemById(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchReleaseNoteItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody ReleaseNoteItemPatchForRelnQtyDTO releaseNoteItemPatchForRelnQtyDTO
    );

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchReleaseNoteItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @RequestBody ReleaseNoteItemPatchDTO releaseNoteItemPatchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteInfoVo> getReleaseNoteItemDetailById(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "relnItemDetailId") Long relnItemDetailId);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteReleaseNoteItemDetailById(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "relnItemDetailId") Long relnItemDetailId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteEntity> getRelnActivityInfo(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNotePrintDTO> getReleaseNoteItemQrCode(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modifyPrintCount(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modifyPrintCount(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "relnItemId") Long relnItemId,
        @PathVariable @Parameter(description = "qrcode") String qrcode,
        @PathVariable @Parameter(description = "cnt") int cnt);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MaterialImportFileResultDTO> importRelnItemDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("relnId") Long relnId,
        @RequestBody MaterialImportFileDTO uploadDTO
    );
}
