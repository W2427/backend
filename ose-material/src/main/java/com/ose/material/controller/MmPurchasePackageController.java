package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.api.MmPurchasePackageAPI;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.domain.model.service.MmPurchasePackageFileInterface;
import com.ose.material.domain.model.service.MmPurchasePackageInterface;
import com.ose.material.domain.model.service.MmPurchasePackageItemInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.material.entity.MmPurchasePackageFileEntity;
import com.ose.material.entity.MmPurchasePackageItemEntity;
import com.ose.material.vo.MaterialImportType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "采购包接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/purchase-package")
public class MmPurchasePackageController extends BaseController implements MmPurchasePackageAPI {

    /**
     * 采购包接口服务
     */
    private MmPurchasePackageInterface mmPurchasePackageService;

    private MmPurchasePackageItemInterface mmPurchasePackageItemService;

    private MmPurchasePackageFileInterface mmPurchasePackageFileService;

    private MmImportBatchTaskInterface materialBatchTaskService;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     */
    @Autowired
    public MmPurchasePackageController(
        MmPurchasePackageInterface mmPurchasePackageService,
        MmPurchasePackageItemInterface mmPurchasePackageItemService,
        MmPurchasePackageFileInterface mmPurchasePackageFileService,
        MmImportBatchTaskInterface materialBatchTaskService,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.mmPurchasePackageService = mmPurchasePackageService;
        this.mmPurchasePackageItemService = mmPurchasePackageItemService;
        this.mmPurchasePackageFileService = mmPurchasePackageFileService;
        this.materialBatchTaskService = materialBatchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "创建采购包",
        description = "创建采购包。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO) {
        mmPurchasePackageService.create(orgId, projectId, mmPurchasePackageCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取采购包信息
     *
     * @return 采购包列表
     */
    @Override
    @Operation(
        summary = "查询采购包",
        description = "查询采购包。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmPurchasePackageEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmPurchasePackageService.search(
                orgId,
                projectId,
                mmPurchasePackageSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑采购包",
        description = "编辑采购包"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmPurchasePackageEntity> edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO

    ) {
        return new JsonObjectResponseBody<>(
            getContext(), mmPurchasePackageService.update(orgId, projectId, purchasePackageId, mmPurchasePackageCreateDTO, getContext())
        );
    }

    @Override
    @Operation(
        summary = "删除采购包",
        description = "删除采购包"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{purchasePackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId) {
        mmPurchasePackageService.delete(orgId, projectId, purchasePackageId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取采购包信息",
        description = "获取采购包信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{purchasePackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmPurchasePackageEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmPurchasePackageService.detail(orgId, projectId, purchasePackageId)
        );
    }


    @Override
    @Operation(
        summary = "创建采购包明细",
        description = "创建采购包明细。"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/item",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody createItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包明细信息") MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO) {
        mmPurchasePackageItemService.create(orgId, projectId, purchasePackageId, mmPurchasePackageItemCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取采购包明细信息
     *
     * @return 采购包明细列表
     */
    @Override
    @Operation(
        summary = "查询采购包明细",
        description = "查询采购包明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/{purchasePackageId}/item",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmPurchasePackageItemEntity> searchItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageItemSearchDTO mmPurchasePackageItemSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmPurchasePackageItemService.search(
                orgId,
                projectId,
                purchasePackageId,
                mmPurchasePackageItemSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑采购包明细",
        description = "编辑采购包明细"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody editItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId,
        @RequestBody @Parameter(description = "采购包明细信息") MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO

    ) {
        mmPurchasePackageItemService.update(orgId, projectId, purchasePackageId, purchasePackageItemId, mmPurchasePackageItemCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除采购包明细",
        description = "删除采购包明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId
    ) {
        mmPurchasePackageItemService.delete(orgId, projectId, purchasePackageId, purchasePackageItemId, getContext());
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "获取采购包明细详情信息",
        description = "获取采购包明细详情信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{purchasePackageId}/item/{purchasePackageItemId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmPurchasePackageItemEntity> itemDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包明细id") Long purchasePackageItemId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(), mmPurchasePackageItemService.detail(orgId, projectId, purchasePackageId, purchasePackageItemId)
        );
    }

    /**
     * 采购包明细信息导入
     *
     * @return 采购包明细信息导入
     */
    @Override
    @Operation(
        summary = "采购包明细信息导入",
        description = "采购包明细信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/import"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody MmImportBatchTaskImportDTO importDTO) {
        final OperatorDTO operator = getContext().getOperator();

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        materialBatchTaskService.run(
            orgId,
            projectId,
            purchasePackageId,
            "",
            contextDTO,
            MaterialImportType.PURCHASE_PACKAGE_ITEM,
            importDTO.getDiscipline(),
            batchTask -> {

                MmImportBatchResultDTO result = mmPurchasePackageItemService.importPurchasePackageItemBatch(
                    orgId,
                    projectId,
                    purchasePackageId,
                    operator,
                    batchTask,
                    importDTO);

                JsonObjectResponseBody<FileES> responseBody
                    = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    importDTO.getFileName(),
                    new FilePostDTO()
                );

                batchTask.setImportFile(
                    LongUtils.parseLong(responseBody.getData().getId()
                    ));

                return result;
            }
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "采购包里添加供货商",
        description = "采购包里添加供货商。"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/vendor",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody addVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @RequestBody MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO
    ) {
        mmPurchasePackageService.addVendor(orgId, projectId, purchasePackageId, mmPurchasePackageVendorAddDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 采购包里查询供货商
     *
     * @return 采购包里查询供货商
     */
    @Override
    @Operation(
        summary = "采购包里查询供货商",
        description = "采购包里查询供货商。"
    )
    @RequestMapping(
        method = GET,
        value = "/{purchasePackageId}/vendor",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmPurchasePackageVendorReturnDTO> searchVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO
    ) {

        return new JsonListResponseBody<>(
            getContext(),
            mmPurchasePackageService.searchVendor(
                orgId,
                projectId,
                purchasePackageId,
                mmPurchasePackageVendorSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑采购包中的供货商",
        description = "编辑采购包中的供货商"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/vendor/{purchasePackageVendorRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody updateVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包与供货商关系id") Long purchasePackageVendorRelationId,
            @RequestBody MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO
    ) {
        mmPurchasePackageService.updateVendor(orgId, projectId, purchasePackageId, purchasePackageVendorRelationId, mmPurchasePackageVendorAddDTO, getContext());
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "删除采购包中的供货商",
        description = "删除采购包中的供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{purchasePackageId}/vendor/{purchasePackageVendorRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteVendor(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包与供货商关系id") Long purchasePackageVendorRelationId
    ) {
        mmPurchasePackageService.deleteVendor(orgId, projectId, purchasePackageId, purchasePackageVendorRelationId, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取采购包相关的实体子类
     *
     * @return 采购包相关的实体子类
     */
    @Override
    @Operation(
        summary = "查询实体子类型",
        description = "查询实体子类型。"
    )
    @RequestMapping(
        method = GET,
        value = "/search-entity-sub-type/{entityType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmPurchasePackageEntitySubTypeDTO> searchEntitySubType(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "实体大类") String entityType) {

        return new JsonListResponseBody<>(
            getContext(),
            mmPurchasePackageService.searchEntitySubType(
                orgId,
                projectId,
                entityType
            )
        );
    }

    /**
     * 获取采购包文件记录
     *
     * @return 采购包文件记录
     */
    @Override
    @Operation(
        summary = "查询采购包文件记录",
        description = "查询采购包文件记录。"
    )
    @RequestMapping(
        method = GET,
        value = "/{purchasePackageId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmPurchasePackageFileEntity> searchFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        MmPurchasePackageFileSearchDTO mmPurchasePackageFileSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmPurchasePackageFileService.search(
                orgId,
                projectId,
                purchasePackageId,
                mmPurchasePackageFileSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "创建采购包文件",
        description = "创建采购包文件。"
    )
    @RequestMapping(
        method = POST,
        value = "/{purchasePackageId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody createFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long purchasePackageId,
        @RequestBody @Parameter(description = "采购包信息") MmPurchasePackageFileCreateDTO mmPurchasePackageFileCreateDTO) {
        mmPurchasePackageFileService.create(orgId, projectId, purchasePackageId, mmPurchasePackageFileCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除采购包文件",
        description = "删除采购包文件"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{purchasePackageId}/file/{fileId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long purchasePackageId,
        @PathVariable @Parameter(description = "采购包文件id") Long fileId) {
        mmPurchasePackageFileService.delete(orgId, projectId, purchasePackageId, fileId, getContext());
        return new JsonResponseBody();
    }

}
