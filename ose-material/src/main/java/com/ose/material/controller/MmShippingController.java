package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.material.api.MmShippingAPI;
import com.ose.material.domain.model.repository.MmShippingRepository;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.domain.model.service.MmShippingInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import com.ose.material.vo.MaterialImportType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "发货单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/shipping")
public class MmShippingController extends BaseController implements MmShippingAPI {

    /**
     * 采购合同接口服务
     */
    private final MmShippingInterface shippingService;
    private final MmShippingRepository mmShippingRepository;
    private final MmImportBatchTaskInterface materialBatchTaskService;
    private final UploadFeignAPI uploadFeignAPI;
    /**
     * 构造方法
     *
     * @param shippingService 发货单服务
     */
    @Autowired
    public MmShippingController(MmShippingInterface shippingService,
                                MmShippingRepository mmShippingRepository,
                                MmImportBatchTaskInterface materialBatchTaskService,
                                UploadFeignAPI uploadFeignAPI
    ) {
        this.shippingService = shippingService;
        this.mmShippingRepository = mmShippingRepository;
        this.materialBatchTaskService = materialBatchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "创建发货单",
        description = "创建发货单。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "采购合同信息") MmShippingCreateDTO mmShippingCreateDTO) {
        shippingService.create(orgId, projectId, mmShippingCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 查询采购合同列表
     *
     * @return 查询采购合同列表
     */
    @Override
    @Operation(
        summary = "查询发货单列表",
        description = "查询发货单列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmShippingEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmShippingSearchDTO mmShippingSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            shippingService.search(
                orgId,
                projectId,
                mmShippingSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "删除发货单",
        description = "删除发货单"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId) {
        shippingService.delete(orgId, projectId, shippingId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "编辑发货单",
        description = "编辑发货单"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody @Parameter(description = "发货单信息") MmShippingCreateDTO mmShippingCreateDTO

    ) {
        shippingService.update(orgId, projectId, shippingId, mmShippingCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取发货单信息",
        description = "获取发货单信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmShippingEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId) {
        return new JsonObjectResponseBody<>(getContext(), shippingService.detail(orgId, projectId, shippingId));
    }

    /**
     * 获取当前项目的请购明细列表
     *
     * @return 获取当前项目的请购明细列表
     */
    @Override
    @Operation(
        summary = "获取当前项目的请购明细列表",
        description = "获取当前项目的请购明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "{shippingId}/requisition-details"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmShippingDetailsDTO> searchRequisitionDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            shippingService.searchRequisitionDetails(
                orgId,
                projectId,
                shippingId,
                mmShippingSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "添加发货单明细",
        description = "添加发货单明细。"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody addDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody @Parameter(description = "发货详情添加信息") MmShippingDetailAddDTO mmShippingDetailAddDTO) {
        shippingService.addDetails(orgId, projectId, shippingId, mmShippingDetailAddDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取采购明细列表
     *
     * @return 获取发货单明细列表
     */
    @Override
    @Operation(
        summary = "获取发货单明细列表",
        description = "获取发货单明细列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}/details"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmShippingDetailSearchDetailDTO> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            shippingService.searchDetails(
                orgId,
                projectId,
                shippingId,
                mmShippingSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "删除发货单明细",
        description = "删除发货单明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}/details/{shippingDetailId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId) {
        shippingService.deleteItem(orgId, projectId, shippingId, shippingDetailId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除发货单明细的关系信息",
        description = "删除发货单明细的关系信息"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{shippingId}/details/{shippingDetailId}/relations/{shippingDetailRelationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItemRelation(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId,
        @PathVariable @Parameter(description = "发货单详情关系id") Long shippingDetailRelationId) {
        shippingService.deleteItemRelation(orgId, projectId, shippingId, shippingDetailId, shippingDetailRelationId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "发货单定版出库",
        description = "发货单定版出库"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/update-status"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody updateStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody @Parameter(description = "发货单信息") MmShippingUpdateStatusDTO mmShippingUpdateStatusDTO

    ) {
        shippingService.updateStatus(orgId, projectId, shippingId, mmShippingUpdateStatusDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 查询发货单中关联的入库单
     *
     * @return 查询发货单中关联的入库单
     */
    @Override
    @Operation(
        summary = "查询发货单中关联的入库单",
        description = "查询发货单中关联的入库单。"
    )
    @RequestMapping(
        method = GET,
        value = "/{shippingId}/release-receive"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveEntity> searchReleaseReceive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            shippingService.searchReleaseReceive(
                orgId,
                projectId,
                shippingId,
                mmShippingSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "修改发货量",
        description = "修改发货量"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details/{shippingDetailId}/relations/{shippingRelationId}/update-qty"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody updateQty(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId,
        @PathVariable @Parameter(description = "发货单详情关系id") Long shippingRelationId,
        @RequestBody @Parameter(description = "修改量dto") MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO

    ) {
        shippingService.updateQty(orgId, projectId, shippingId, shippingDetailId, shippingRelationId, mmShippingDetailUpdateQtyDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "批量修改发货量",
        description = "批量修改发货量"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/details/{shippingDetailId}/update-qty"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody batchUpdateQty(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @PathVariable @Parameter(description = "发货单详情id") Long shippingDetailId,
        @RequestBody @Parameter(description = "修改量dto") MmShippingDetailUpdateQtyDTO mmShippingDetailUpdateQtyDTO

    ) {
        shippingService.batchUpdateQty(orgId, projectId, shippingId, shippingDetailId, mmShippingDetailUpdateQtyDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 发货详情信息导入
     *
     * @return 发货详情信息导入
     */
    @Override
    @Operation(
        summary = "发货详情信息导入",
        description = "发货详情信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{shippingId}/import"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "发货单id") Long shippingId,
        @RequestBody MmImportBatchTaskImportDTO importDTO) {
        final OperatorDTO operator = getContext().getOperator();

        MmShippingEntity shippingEntityFind = mmShippingRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            shippingId,
            EntityStatus.ACTIVE
        );
        if (shippingEntityFind == null) {
            throw new BusinessError("发货单不存在");
        }
        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        materialBatchTaskService.run(
            orgId,
            projectId,
            shippingEntityFind.getId(),
            shippingEntityFind.getNo(),
            contextDTO,
            MaterialImportType.SHIPPING_DETAIL,
            importDTO.getDiscipline(),
            batchTask -> {
                MmImportBatchResultDTO result = shippingService.importDetailItem(
                    orgId,
                    projectId,
                    shippingId,
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
}
