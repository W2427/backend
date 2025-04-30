package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.materialspm.api.SpmReleaseNoteAPI;
import com.ose.materialspm.domain.model.service.ReleaseNoteInterface;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "SPM 放行单 接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{spmProjectId}")
public class SpmReleaseNoteController extends BaseController implements SpmReleaseNoteAPI {

    /**
     * 查询服务
     */
    private final ReleaseNoteInterface releaseNoteServeice;

    /**
     * 构造方法
     */
    @Autowired
    public SpmReleaseNoteController(ReleaseNoteInterface releaseNoteServeice) {
        this.releaseNoteServeice = releaseNoteServeice;
    }

    @Override
    @Operation(
        summary = "获取放行单列表",
        description = "获取放行单列表。"
    )
    @RequestMapping(
        method = GET,
        value = "spm-release-notes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReleaseNoteHead> getReleaseNoteHeadList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "SPM项目id") String spmProjectId,
        ReleaseNoteListDTO releaseNoteListDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            releaseNoteServeice.getReleaseNoteHeadList(spmProjectId, releaseNoteListDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取放行单详情",
        description = "获取放行单详情。"
    )
    @RequestMapping(
        method = GET,
        value = "spm-release-notes/{relnNumber}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReleaseNoteHead> getReleaseNote(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable @Parameter(description = "relnNumber") String relnNumber) {

        return new JsonObjectResponseBody<>(
            getContext(),
            releaseNoteServeice.getReleaseNote(spmProjectId, relnNumber)
        );
    }

    @Override
    @Operation(
        summary = "获取放行单明细",
        description = "获取放行单明细。"
    )
    @RequestMapping(
        method = POST,
        value = "spm-release-notes/{relnNumber}/items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReleaseNote> getReleaseNoteItemsByPage(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable @Parameter(description = "relnNumber") String relnNumber,
        @RequestBody PageDTO pageDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            releaseNoteServeice.getReleaseNoteItemsByPage(spmProjectId, relnNumber, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取放行单明细（无分页）",
        description = "获取放行单明细（无分页）。"
    )
    @RequestMapping(
        method = POST,
        value = "spm-release-notes/{relnNumber}/no-page-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReleaseNote> getReleaseNoteItems(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable @Parameter(description = "relnNumber") String relnNumber) {

        return new JsonListResponseBody<>(
            getContext(),
            releaseNoteServeice.getReleaseNoteItems(spmProjectId, relnNumber)
        );
    }
}
