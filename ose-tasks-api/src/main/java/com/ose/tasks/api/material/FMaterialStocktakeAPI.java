package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.material.FMaterialStocktakeEntity;
import com.ose.tasks.entity.material.FMaterialStocktakeItemEntity;
import com.ose.tasks.entity.material.FMaterialStocktakeOSDEntity;
import com.ose.tasks.entity.material.FMaterialStocktakeTotalEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 材料盘点接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialStocktakeAPI {

    /**
     * 创建材料盘点清单（开始盘点）。
     *
     * @param projectId
     * @param orgId
     * @param fMaterialStocktakePostDTO
     * @return
     */
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeEntity> postFMST(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialStocktakePostDTO fMaterialStocktakePostDTO);

    /**
     * 获取材料盘点清单列表。
     *
     * @param projectId
     * @param orgId
     * @param fMaterialStocktakeSearchDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeEntity> getFMSTs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FMaterialStocktakeSearchDTO fMaterialStocktakeSearchDTO);

    /**
     * 获取材料盘点清单详情。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeEntity> getFMSTById(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId);

    /**
     * 更新材料盘点清单详情（盘点结果确认）。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fMaterialStocktakePatchDTO
     * @return
     */
    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeEntity> patchFMST(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @RequestBody FMaterialStocktakePatchDTO fMaterialStocktakePatchDTO);

    /**
     * 添加材料盘点明细。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fMaterialStocktakeItemPostDTO
     * @return
     */
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeItemEntity> postFMSTItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @RequestBody FMaterialStocktakeItemPostDTO fMaterialStocktakeItemPostDTO);

    /**
     * 获取材料盘点合计列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeTotalEntity> getFMSTTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        PageDTO pageDTO);

    /**
     * 获取材料盘点合计详情。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstTotalId
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeTotalEntity> getFMSTTotal(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstTotalId") Long fmstTotalId);

    /**
     * 获取材料盘点合计下的材料明细列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstTotalId
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeItemEntity> getFMSTTotalItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstTotalId") Long fmstTotalId,
        PageDTO pageDTO);

    /**
     * 获取材料盘点明细列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeItemEntity> getFMSTItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        PageDTO pageDTO);

    /**
     * 获取材料盘点的无问题材料的树形结构。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeTotalEntity> getFMSTTrees(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId);

    /**
     * 删除材料盘点明细。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstItemId
     * @return
     */
    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody deleteFMSTItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstItemId") Long fmstItemId);

    /**
     * 获取材料盘点OSD列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fMaterialStocktakeOSDSearchCondDTO
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeOSDEntity> getFMSTOSDs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        FMaterialStocktakeOSDSearchCondDTO fMaterialStocktakeOSDSearchCondDTO,
        PageDTO pageDTO);

    /**
     * 添加材料盘点OSD明细。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fMaterialStocktakeItemPostDTO
     * @return
     */
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeItemEntity> postFMSTOSDItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @RequestBody FMaterialStocktakeItemPostDTO fMaterialStocktakeItemPostDTO);

    /**
     * 删除材料盘点OSD明细。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstItemId
     * @return
     */
    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody deleteFMSTOSDItem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstItemId") Long fmstItemId);

    /**
     * 获取材料盘点OSD明细列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeItemEntity> getFMSTOSDItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        PageDTO pageDTO);

    /**
     * 获取材料盘点OSD详情 。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstOsdId
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeOSDEntity> getFMSTOSD(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstOsdId") Long fmstOsdId);

    /**
     * 获取材料盘点OSD下材料明细列表。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @param fmstOsdId
     * @param pageDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeItemEntity> getFMSTOSDItemsByOsdId(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId,
        @PathVariable @Parameter(description = "fmstOsdId") Long fmstOsdId,
        PageDTO pageDTO);

    /**
     * 校验盘点数量与放行量是否一致。
     *
     * @param projectId
     * @param orgId
     * @param fmstId
     * @return
     */
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody check(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmstId") Long fmstId
    );
}
