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
import com.ose.exception.NotFoundError;
import com.ose.material.api.MmReleaseReceiveAPI;
import com.ose.material.domain.model.repository.MmReleaseReceiveRepository;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.domain.model.service.MmReleaseReceiveFileInterface;
import com.ose.material.domain.model.service.MmReleaseReceiveInterface;
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
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "入库单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/release-receive")
public class MmReleaseReceiveController extends BaseController implements MmReleaseReceiveAPI {

    /**
     * 入库单接口服务
     */
    private final MmReleaseReceiveInterface materialReceiveNoteService;

    private final MmReleaseReceiveFileInterface mmReleaseReceiveFileService;

    // 批处理任务管理服务接口
    private MmImportBatchTaskInterface materialbatchTaskService;

    private UploadFeignAPI uploadFeignAPI;
    private final MmReleaseReceiveRepository mmReleaseReceiveRepository;

    /**
     * 构造方法
     *
     * @param materialbatchTaskService 入库单服务
     */
    @Autowired
    public MmReleaseReceiveController(MmReleaseReceiveInterface materialReceiveNoteService,
                                      MmReleaseReceiveFileInterface mmReleaseReceiveFileService,
                                      MmImportBatchTaskInterface materialbatchTaskService,
                                      UploadFeignAPI uploadFeignAPI,
                                      MmReleaseReceiveRepository mmReleaseReceiveRepository) {
        this.materialReceiveNoteService = materialReceiveNoteService;
        this.mmReleaseReceiveFileService = mmReleaseReceiveFileService;
        this.materialbatchTaskService = materialbatchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.mmReleaseReceiveRepository = mmReleaseReceiveRepository;
    }

    @Override
    @Operation(
        summary = "创建入库单",
        description = "创建入库单。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "入库单信息") MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO) {
        materialReceiveNoteService.create(orgId, projectId, mmReleaseReceiveCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取入库单列表
     *
     * @return 入库单列表
     */
    @Override
    @Operation(
        summary = "查询入库单列表",
        description = "获取入库单列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialReceiveNoteService.search(
                orgId,
                projectId,
                mmReleaseReceiveSearchDTO
            )
        );
    }

    /**
     * 获取手机端入库单待办列表
     *
     * @return 入库单列表
     */
    @Override
    @Operation(
        summary = "获取手机端入库单待办列表",
        description = "获取手机端入库单待办列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/mobile"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveEntity> searchByAssignee(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialReceiveNoteService.searchByAssignee(
                orgId,
                projectId,
                mmReleaseReceiveSearchDTO,
                getContext().getOperator()
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑入库单",
        description = "编辑入库单"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody @Parameter(description = "入库单信息") MmReleaseReceiveCreateDTO mmReleaseReceiveCreateDTO

    ) {
        materialReceiveNoteService.update(orgId, projectId, materialReceiveNoteId, mmReleaseReceiveCreateDTO, getContext());
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "开始盘点",
        description = "开始盘点"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/running-status"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody updateRunningStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody @Parameter(description = "入库单信息") MmReleaseReceiveUpdateRunningStatusDTO mmReleaseReceiveUpdateRunningStatusDTO

    ) {
        materialReceiveNoteService.updateRunningStatus(orgId, projectId, materialReceiveNoteId, mmReleaseReceiveUpdateRunningStatusDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除入库单",
        description = "删除入库单"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialReceiveNoteId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId) {
        materialReceiveNoteService.delete(orgId, projectId, materialReceiveNoteId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取入库单详细信息",
        description = "获取入库单详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmReleaseReceiveEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "放行单id") Long materialReceiveNoteId) {
        return new JsonObjectResponseBody<>(getContext(), materialReceiveNoteService.detail(orgId, projectId, materialReceiveNoteId));
    }

    /**
     * 入库单详情信息导入
     *
     * @return 入库单详情信息导入
     */
    @Override
    @Operation(
        summary = "入库单详情信息导入",
        description = "入库单详情信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/detail-import"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "materialReceiveNoteId") Long materialReceiveNoteId,
        @RequestBody MmImportBatchTaskImportDTO importDTO) {
        final OperatorDTO operator = getContext().getOperator();

        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            materialReceiveNoteId,
            EntityStatus.ACTIVE
        );

        if (mmReleaseReceiveEntity == null) {
            throw new BusinessError("入库单不存在");
        }

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());

        materialbatchTaskService.run(
            orgId,
            projectId,
            mmReleaseReceiveEntity.getId(),
            mmReleaseReceiveEntity.getNo(),
            contextDTO,
            MaterialImportType.RECEIVE_RELEASE,
            importDTO.getDiscipline(),
            batchTask -> {

                MmImportBatchResultDTO result = materialReceiveNoteService.importDetail(
                    orgId,
                    projectId,
                    materialReceiveNoteId,
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
     * 查询入库单明细
     *
     * @return 查询入库单明细
     */
    @Override
    @Operation(
        summary = "查询入库单明细",
        description = "查询入库单明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/details"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveDetailSearchDetailDTO> searchDetails(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
         MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            materialReceiveNoteService.searchDetails(
                orgId,
                projectId,
                materialReceiveNoteId,
                mmReleaseReceiveDetailSearchDTO
            )
        );
    }

    /**
     * 查询入库单是否完成盘库
     *
     * @return 查询入库单是否完成盘库
     */
    @Override
    @Operation(
        summary = "查询入库单是否完成盘库",
        description = "查询入库单是否完成盘库。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/inventory-status"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmReleaseReceiveInventoryStatusDTO> searchInventoryStatus(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            materialReceiveNoteService.searchInventoryStatus(
                orgId,
                projectId,
                materialReceiveNoteId
            )
        );
    }

    @Override
    @Operation(
        summary = "删除入库单明细",
        description = "删除入库单明细"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialReceiveNoteId}/details/{materialReceiveNoteDetailId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteDetailId) {
        materialReceiveNoteService.deleteItem(orgId, projectId, materialReceiveNoteDetailId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除入库单明细二维码",
        description = "删除入库单明细二维码"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialReceiveNoteId}/details/{materialReceiveNoteDetailId}/qrcode/{qrcodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteDetailId,
        @PathVariable @Parameter(description = "二维码id") Long qrcodeId) {
        materialReceiveNoteService.deleteQrCode(orgId, projectId, materialReceiveNoteDetailId, qrcodeId, getContext());
        return new JsonResponseBody();
    }

    /**
     * 查询入库单明细二维码
     *
     * @return 查询入库单明细二维码
     */
    @Override
    @Operation(
        summary = "查询入库单明细二维码",
        description = "查询入库单明细二维码。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailEntityDetailId}/qrcode"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveDetailQrCodeEntity> searchDetailQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单明细id") Long materialReceiveNoteDetailEntityDetailId,
        MmReleaseReceiveQrCodeSearchDTO mmReleaseReceiveQrCodeSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialReceiveNoteService.searchDetailQrCodes(
                orgId,
                projectId,
                materialReceiveNoteId,
                materialReceiveNoteDetailEntityDetailId,
                mmReleaseReceiveQrCodeSearchDTO
            )
        );
    }


    /**
     * 生成入库单明细二维码
     *
     * @return 生成入库单明细二维码
     */
    @Override
    @Operation(
        summary = "生成入库单明细二维码",
        description = "生成入库单明细二维码。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailEntityDetailId}/qrcode"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody createDetailQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单明细id") Long materialReceiveNoteDetailEntityDetailId,
        @RequestBody MmReleaseReceiveQrCodeCreateDTO mmReleaseReceiveQrCodeCreateDTO) {

        materialReceiveNoteService.createDetailQrCodes(
            orgId,
            projectId,
            materialReceiveNoteId,
            materialReceiveNoteDetailEntityDetailId,
            mmReleaseReceiveQrCodeCreateDTO,
            getContext()
        );

        return new JsonResponseBody();
    }


    /**
     * 入库单明细二维码盘点
     *
     * @return 入库单明细二维码盘点
     */
    @Override
    @Operation(
        summary = "入库单明细二维码盘点",
        description = "入库单明细二维码盘点。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}/qrcode-inventory"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody inventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
//        @PathVariable @Parameter(description = "入库单详情二维码id") Long materialReceiveNoteDetailQrCodeId,
        @RequestBody MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO) {
        materialReceiveNoteService.inventoryQrCodes(
            orgId, projectId, materialReceiveNoteId, materialReceiveNoteDetailId, mmReleaseReceiveInventoryQrCodeDTO, getContext()
        );
        return new JsonResponseBody();
    }

    /**
     * 入库单明细二维码取消盘点
     *
     * @return 入库单明细二维码取消盘点
     */
    @Override
    @Operation(
        summary = "入库单明细二维码取消盘点",
        description = "入库单明细二维码取消盘点。"
    )
    @RequestMapping(
        method = POST,
        value = "/qrcode-inventory-cancel"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody cancelInventoryQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO) {
        materialReceiveNoteService.cancelInventoryQrCodes(
            orgId, projectId, materialReceiveNoteId, mmReleaseReceiveInventoryQrCodeDTO, getContext()
        );
        return new JsonResponseBody();
    }

    /**
     * 查找入库二维码信息
     *
     * @return 查找入库二维码信息
     */
    @Override
    @Operation(
        summary = "查找入库二维码信息",
        description = "查找入库二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/qrcode/{qrCode}"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmReleaseReceiveQrCodeResultDTO> qrCodeSearch(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "二维码") String qrCode) {

        return new JsonObjectResponseBody<>(
            materialReceiveNoteService.qrCodeSearch(
                orgId, projectId, materialReceiveNoteId, qrCode
            )
        );
    }


    /**
     * 入库
     *
     * @return 入库
     */
    @Override
    @Operation(
        summary = "入库",
        description = "入库。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/receive"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody receive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @RequestBody MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO) {
        if (mmReleaseReceiveReceiveDTO.getReceived() != null && mmReleaseReceiveReceiveDTO.getReceived()) {
            materialReceiveNoteService.startReceive(
                orgId,
                projectId,
                materialReceiveNoteId,
                "TT",
                mmReleaseReceiveReceiveDTO,
                getContext(),
                null
            );
        }

        return new JsonResponseBody();
    }

    /**
     * 单项材料入库
     *
     * @return 单项材料入库
     */
    @Override
    @Operation(
        summary = "单项材料入库",
        description = "单项材料入库。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}/receive"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody detailReceive(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
        @RequestBody MmReleaseReceiveReceiveDTO mmReleaseReceiveReceiveDTO) {
        if (mmReleaseReceiveReceiveDTO.getReceived() != null && mmReleaseReceiveReceiveDTO.getReceived()) {
            materialReceiveNoteService.detailReceive(
                orgId,
                projectId,
                materialReceiveNoteId,
                materialReceiveNoteDetailId,
                mmReleaseReceiveReceiveDTO,
                getContext(),
                mmReleaseReceiveReceiveDTO.getInExternalQuality()
            );
        }

        return new JsonResponseBody();
    }

    /**
     * 修改入库单放行数量
     *
     * @return 修改入库单放行数量
     */
    @Override
    @Operation(
        summary = "修改入库单放行数量",
        description = "修改入库单放行数量。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/detail/{materialReceiveNoteDetailId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody updateDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId,
        @RequestBody MmReleaseReceiveInventoryQrCodeDTO mmReleaseReceiveInventoryQrCodeDTO) {
        materialReceiveNoteService.updateDetail(
            orgId,
            projectId,
            materialReceiveNoteId,
            materialReceiveNoteDetailId,
            mmReleaseReceiveInventoryQrCodeDTO,
            getContext()
        );

        return new JsonResponseBody();
    }

    /**
     * 获取材料编码下生成的二维码的信息
     *
     * @return 获取材料编码下生成的二维码的信息
     */
    @Override
    @Operation(
        summary = "获取材料编码下生成的二维码的信息",
        description = "获取材料编码下生成的二维码的信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/items/{materialReceiveNoteDetailId}/qrcodes"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseNotePrintDTO> getReleaseNoteItemQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库单id") Long materialReceiveNoteId,
        @PathVariable @Parameter(description = "入库单详情id") Long materialReceiveNoteDetailId) {
        return new JsonListResponseBody<>(
            getContext(),
            materialReceiveNoteService.getQrCodeByRelnItemId(
                orgId,
                projectId,
                materialReceiveNoteId,
                materialReceiveNoteDetailId
            )
        );
    }


    /**
     * 修改入库单放行数量
     *
     * @return 修改入库单放行数量
     */
    @Override
    @Operation(
        summary = "修改入库单放行数量",
        description = "修改入库单放行数量。"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialReceiveNoteId}/batch-update"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody batchUpdateDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "入库详情ID") Long materialReceiveNoteId,
        @RequestBody MmReleaseReceiveBatchUpdateDTO mmReleaseReceiveBatchUpdateDTO) {
        materialReceiveNoteService.batchUpdateDetail(
            orgId,
            projectId,
            materialReceiveNoteId,
            mmReleaseReceiveBatchUpdateDTO,
            getContext()
        );

        return new JsonResponseBody();
    }

    /**
     * 按条件下载管线实体列表。
     *
     * @param orgId                     组织 ID
     * @param projectId                 项目 ID
     * @param mmReleaseReceiveSearchDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/{materialReceiveNoteId}/download"
    )
    @Operation(description = "按条件下载管线实体列表")
    @WithPrivilege
    @Override
    public synchronized void download(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("materialReceiveNoteId") Long materialReceiveNoteId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO
    ) throws IOException {

        final ContextDTO context = getContext();
        final OperatorDTO operator = context.getOperator();
        File excel = materialReceiveNoteService.download(orgId, projectId, materialReceiveNoteId, mmReleaseReceiveSearchDTO, operator.getId());

        HttpServletResponse response = context.getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-material-release.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

    /**
     * 查询入库单文件记录
     *
     * @return 入库单文件记录
     */
    @Override
    @Operation(
        summary = "查询入库单文件记录",
        description = "查询入库单文件记录。"
    )
    @RequestMapping(
        method = GET,
        value = "/{releaseReceiveId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveFileEntity> searchFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "采购包id") Long releaseReceiveId,
        MmReleaseReceiveFileSearchDTO mmPurchasePackageFileSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmReleaseReceiveFileService.search(
                orgId,
                projectId,
                releaseReceiveId,
                mmPurchasePackageFileSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "创建入库单文件",
        description = "创建入库单文件。"
    )
    @RequestMapping(
        method = POST,
        value = "/{releaseReceiveId}/file",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody createFile(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "项目id") Long releaseReceiveId,
        @RequestBody @Parameter(description = "采购包信息") MmReleaseReceiveFileCreateDTO mmReleaseReceiveFileCreateDTO) {
        mmReleaseReceiveFileService.create(orgId, projectId, releaseReceiveId, mmReleaseReceiveFileCreateDTO, getContext());
        return new JsonResponseBody();
    }

}
