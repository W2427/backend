package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.material.entity.MmPurchasePackageFileEntity;
import com.ose.material.entity.MmPurchasePackageItemEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "ose-material", contextId = "mmPurchasePkgFeign")
public interface MmPurchasePackageFeignAPI {
    @Operation(
        summary = "创建采购包",
        description = "创建采购包。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package",
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO);

    /**
     * 获取采购包信息
     *
     * @return 采购包列表
     */
    @Operation(
        summary = "查询采购包",
        description = "查询采购包。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package",
        method = GET,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmPurchasePackageEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO);

    @Operation(
        summary = "编辑采购包",
        description = "编辑采购包"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmPurchasePackageEntity> edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO
    );

    @Operation(
        summary = "删除采购包",
        description = "删除采购包"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId);

    @Operation(
        summary = "获取采购包信息",
        description = "获取采购包信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmPurchasePackageEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId);

    @Operation(
        summary = "创建采购包明细",
        description = "创建采购包明细。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/item",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody createItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包明细信息") MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO);

    /**
     * 获取采购包明细信息
     *
     * @return 采购包明细列表
     */
    @Operation(
        summary = "查询采购包明细列表",
        description = "查询采购包明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/item",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmPurchasePackageItemEntity> searchItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageItemSearchDTO mmPurchasePackageItemSearchDTO);

    @Operation(
        summary = "编辑采购包明细",
        description = "编辑采购包明细"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody editItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId,
        @RequestBody @Parameter(description = "采购包明细信息") MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO
    );

    @Operation(
        summary = "删除采购包明细",
        description = "删除采购包明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId
    );

    @Operation(
        summary = "获取采购包明细信息",
        description = "获取采购包明细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmPurchasePackageItemEntity> itemDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId
    );

    /**
     * 采购包明细导入
     *
     * @return 采购包明细导入
     */
    @Operation(
        summary = "采购包明细导入",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/project-document-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/templates/import-purchase-package.xlsx\">import-purchase-package.xlsx</a>。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody MmImportBatchTaskImportDTO importDTO);

    @Operation(
        summary = "采购包里添加供货商",
        description = "采购包里添加供货商"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/vendor",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody addVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO
    );

    @Operation(
        summary = "采购包里查询供货商",
        description = "采购包里查询供货商"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/vendor",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmPurchasePackageVendorReturnDTO> searchVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO
    );

    @Operation(
        summary = "编辑采购包中的供货商",
        description = "编辑采购包中的供货商"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/vendor/{purchasePackageVendorRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody updateVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包与供货商关系id") Long purchasePackageVendorRelationId,
        @RequestBody MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO
    );

    @Operation(
        summary = "删除采购包中的供货商",
        description = "删除采购包中的供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/vendor/{purchasePackageVendorRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包与供货商关系id") Long purchasePackageVendorRelationId
    );

    /**
     * 获取采购包相关的实体子类型
     *
     * @return 采购包列表
     */
    @Operation(
        summary = "查询实体子类型",
        description = "查询实体子类型。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/search-entity-sub-type/{entityType}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmPurchasePackageEntitySubTypeDTO> searchEntitySubType(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "实体大类") String entityType);

    /**
     * 获取采购包文件记录
     *
     * @return 采购包文件记录
     */
    @Operation(
        summary = "查询采购包文件记录",
        description = "查询采购包文件记录。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmPurchasePackageFileEntity> searchFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageFileSearchDTO mmPurchasePackageFileSearchDTO);

    @Operation(
        summary = "创建采购包文件",
        description = "创建采购包文件。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody createFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageFileCreateDTO mmPurchasePackageFileCreateDTO);

    @Operation(
        summary = "删除采购包文件",
        description = "删除采购包文件"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/purchase-package/{purchasePackageId}/file/{fileId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包文件id") Long fileId);

}
