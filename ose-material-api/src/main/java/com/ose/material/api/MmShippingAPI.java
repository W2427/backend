package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.*;
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

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/shipping")
public interface MmShippingAPI {

    @Operation(
        summary = "创建发货单",
        description = "创建发货单。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "采购合同信息") MmShippingCreateDTO mmShippingCreateDTO);

    /**
     * 获取发货单列表
     *
     * @return 采购合同列表
     */
    @Operation(
        summary = "查询发货单列表",
        description = "查询发货单列表。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmShippingEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmShippingSearchDTO mmShippingSearchDTO);

    @Operation(
        summary = "编辑发货单",
        description = "编辑发货单"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody @Parameter(description = "发货单信息") MmShippingCreateDTO mmShippingCreateDTO
    );

    @Operation(
        summary = "发货单定版出库",
        description = "发货单定版出库"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/update-status"
    )
    @ResponseStatus(OK)
    JsonResponseBody updateStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购合同id") Long shippingId,
        @RequestBody @Parameter(description = "采购合同信息") MmShippingUpdateStatusDTO mmShippingUpdateStatusDTO
    );

    @Operation(
        summary = "删除发货单",
        description = "删除发货单"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购合同id") Long shippingId);

    @Operation(
        summary = "删除发货单明细",
        description = "删除发货单明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}/details/{shippingDetailId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货明细id") Long shippingDetailId
    );

    @Operation(
        summary = "删除发货单明细的关系信息",
        description = "删除发货单明细的关系信息"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}/details/{shippingDetailId}/relations/{shippingDetailRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItemRelation(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货明细id") Long shippingDetailId,
        @PathVariable @Parameter(description = "发货单详情关系id") Long shippingDetailRelationId
    );

    @Operation(
        summary = "获取发货单的详细信息",
        description = "获取发货单的详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmShippingEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId);

    /**
     * 获取发货单明细列表
     *
     * @return 获取发货单明细列表
     */
    @Operation(
        summary = "获取发货单明细列表",
        description = "获取发货单明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}/details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmShippingDetailSearchDetailDTO> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO);



    /**
     * 获取当前项目的请购明细列表
     *
     * @return 获取当前项目的请购明细列表
     */
    @Operation(
        summary = "获取当前项目的请购明细列表",
        description = "获取当前项目的请购明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/requisition-details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmShippingDetailsDTO> searchRequisitionDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO);


    @Operation(
        summary = "添加发货单明细",
        description = "添加发货单明细。"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details"
    )
    @ResponseStatus(CREATED)
    JsonResponseBody addDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody @Parameter(description = "发货单详情添加信息") MmShippingDetailAddDTO mmShippingDetailAddDTO);

    /**
     * 查询发货单中关联的入库单
     *
     * @return 查询发货单中关联的入库单
     */
    @Operation(
        summary = "查询发货单中关联的入库单",
        description = "查询发货单中关联的入库单。"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}/release-receive"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveEntity> searchReleaseReceive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO);

    @Operation(
        summary = "修改发货量",
        description = "修改发货量"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details/{shippingDetailId}/relations/{shippingRelationId}/update-qty"
    )
    @ResponseStatus(OK)
    JsonResponseBody updateQty(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId,
        @PathVariable @Parameter(description = "发货单详情关系id") Long shippingRelationId,
        @RequestBody @Parameter(description = "修改量dto") MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO
    );

    @Operation(
        summary = "批量修改发货量",
        description = "批量修改发货量"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details/{shippingDetailId}/update-qty"
    )
    @ResponseStatus(OK)
    JsonResponseBody batchUpdateQty(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId,
        @RequestBody @Parameter(description = "修改量dto") MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO
    );

    /**
     * 发货单信息导入
     *
     * @return 发货单信息导入
     */
    @Operation(
        summary = "发货单信息导入",
        description = "发货单信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody MmImportBatchTaskImportDTO importDTO);
}
