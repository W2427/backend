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
import com.ose.material.api.MmIssueAPI;
import com.ose.material.domain.model.repository.MmIssueRepository;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.domain.model.service.MmIssueInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmIssueDetailEntity;
import com.ose.material.entity.MmIssueDetailQrCodeEntity;
import com.ose.material.entity.MmIssueEntity;
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

@Tag(name = "出库单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-issue")
public class MmIssueController extends BaseController implements MmIssueAPI {

    /**
     * 出库单接口服务
     */
    private final MmIssueInterface materialIssueService;

    // 批处理任务管理服务接口
    private MmImportBatchTaskInterface materialbatchTaskService;

    private UploadFeignAPI uploadFeignAPI;

    private final MmIssueRepository mmIssueRepository;

    /**
     * 构造方法
     *
     * @param materialbatchTaskService 出库单服务
     */
    @Autowired
    public MmIssueController(MmIssueInterface materialIssueService,
                             MmImportBatchTaskInterface materialbatchTaskService,
                             UploadFeignAPI uploadFeignAPI,
                             MmIssueRepository mmIssueRepository) {
        this.materialIssueService = materialIssueService;
        this.materialbatchTaskService = materialbatchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.mmIssueRepository = mmIssueRepository;
    }

    @Override
    @Operation(
        summary = "创建出库单",
        description = "创建出库单。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "出库单信息") MmIssueCreateDTO mmIssueCreateDTO) {
        materialIssueService.create(orgId, projectId, mmIssueCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取出库单列表
     *
     * @return 出库列表
     */
    @Override
    @Operation(
        summary = "查询出库单列表",
        description = "获取出库单列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmIssueEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmIssueSearchDTO mmIssueSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialIssueService.search(
                orgId,
                projectId,
                mmIssueSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑出库单",
        description = "编辑出库单"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody @Parameter(description = "出库单信息") MmIssueCreateDTO mmIssueCreateDTO

    ) {
        materialIssueService.update(orgId, projectId, materialIssueEntityId, mmIssueCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除物料",
        description = "删除物料"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId) {
        materialIssueService.delete(orgId, projectId, materialIssueEntityId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取出库单详细信息",
        description = "获取出库单详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmIssueEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId) {
        return new JsonObjectResponseBody<>(getContext(), materialIssueService.detail(orgId, projectId, materialIssueEntityId));
    }

    /**
     * 出库单详情信息导入
     *
     * @return 出库单详情信息导入
     */
    @Override
    @Operation(
        summary = "出库单详情信息导入",
        description = "出库单详情信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/import"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody MmImportBatchTaskImportDTO importDTO) {
        final OperatorDTO operator = getContext().getOperator();

        MmIssueEntity mmIssueEntity = mmIssueRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialIssueEntityId,
            EntityStatus.ACTIVE
        );
        if (mmIssueEntity == null) {
            throw new BusinessError("设备物资单不存在");
        }
        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        materialbatchTaskService.run(
            orgId,
            projectId,
            materialIssueEntityId,
            mmIssueEntity.getNo(),
            contextDTO,
            MaterialImportType.MATERIAL_ISSUE,
            importDTO.getDiscipline(),
            batchTask -> {

                MmImportBatchResultDTO result = materialIssueService.importDetail(
                    orgId,
                    projectId,
                    materialIssueEntityId,
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

    /**
     * 获取出库单详情明细
     *
     * @return 获取出库单详情明细
     */
    @Override
    @Operation(
        summary = "获取出库单详情明细",
        description = "获取出库单详情明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}/details"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmIssueDetailEntity> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        MmIssueSearchDTO mmIssueSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialIssueService.searchDetails(
                orgId,
                projectId,
                materialIssueEntityId,
                mmIssueSearchDTO
            )
        );
    }

    /**
     * 获取物料明细二维码列表
     *
     * @return 获取物料明细二维码列表
     */
    @Override
    @Operation(
        summary = "获取物料明细二维码列表",
        description = "获取物料明细二维码列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialIssueEntityId}/details/{mmIssueDetailEntityId}/qrcode"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmIssueDetailQrCodeEntity> searchQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单明细 id") Long mmIssueDetailEntityId,
        MmIssueSearchDTO mmIssueSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialIssueService.searchQrCode(
                orgId,
                projectId,
                materialIssueEntityId,
                mmIssueDetailEntityId,
                mmIssueSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "出库盘点",
        description = "出库盘点"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/qrcode-inventory"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody inventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody @Parameter(description = "出库单信息") MmIssueInventoryQrCodeDTO mmIssueInventoryQrCodeDTO

    ) {
        materialIssueService.inventoryQrCodes(orgId, projectId, materialIssueEntityId, mmIssueInventoryQrCodeDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "出库确认",
        description = "出库确认"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/confirm"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody confirm(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId
    ) {
        materialIssueService.confirm(orgId, projectId, materialIssueEntityId, getContext());
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "添加出库详情",
        description = "添加出库详情"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialIssueEntityId}/detail"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody addDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @RequestBody MmIssueDetailCreateDTO mmIssueDetailCreateDTO
    ) {
        materialIssueService.addDetail(orgId, projectId, materialIssueEntityId, mmIssueDetailCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除出库单详情",
        description = "删除物料"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}/detail/{mmIssueDetailEntityId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单详情id") Long mmIssueDetailEntityId
    ) {
        materialIssueService.deleteItem(orgId, projectId, materialIssueEntityId, mmIssueDetailEntityId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除出库单详情",
        description = "删除物料"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialIssueEntityId}/detail/{mmIssueDetailEntityId}/qrcode/{mmIssueDetailQrCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "出库单id") Long materialIssueEntityId,
        @PathVariable @Parameter(description = "出库单详情id") Long mmIssueDetailEntityId,
        @PathVariable @Parameter(description = "出库单详情二维码ID") Long mmIssueDetailQrCodeId
    ) {
        materialIssueService.deleteQrCode(
            orgId,
            projectId,
            materialIssueEntityId,
            mmIssueDetailEntityId,
            mmIssueDetailQrCodeId,
            getContext());
        return new JsonResponseBody();
    }

}
