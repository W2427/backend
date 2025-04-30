package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.api.HeatBatchNoAPI;
import com.ose.material.domain.model.service.HeatBatchNoInterface;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmHeatBatchNoEntity;
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

@Tag(name = "炉批号接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/heat-batch-no")
public class HeatBatchNoController extends BaseController implements HeatBatchNoAPI {

    /**
     * 炉批号接口服务
     */
    private final HeatBatchNoInterface heatBatchNoService;

    // 批处理任务管理服务接口
    private MmImportBatchTaskInterface materialbatchTaskService;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     *
     * @param materialbatchTaskService 炉批号接口服务
     */
    @Autowired
    public HeatBatchNoController(HeatBatchNoInterface heatBatchNoService,
                                 MmImportBatchTaskInterface materialbatchTaskService,
                                 UploadFeignAPI uploadFeignAPI) {
        this.heatBatchNoService = heatBatchNoService;
        this.materialbatchTaskService = materialbatchTaskService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "创建炉批号",
        description = "创建炉批号。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "炉批号信息") HeatBatchNoCreateDTO heatBatchNoCreateDTO) {
        heatBatchNoService.create(orgId, projectId, heatBatchNoCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 炉批号信息列表
     *
     * @return 炉批号列表
     */
    @Override
    @Operation(
        summary = "炉批号列表",
        description = "炉批号列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmHeatBatchNoEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        HeatBatchNoSearchDTO heatBatchNoSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            heatBatchNoService.search(
                orgId,
                projectId,
                heatBatchNoSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑炉批号",
        description = "编辑炉批号"
    )
    @RequestMapping(
        method = POST,
        value = "/{heatBatchId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId,
        @RequestBody @Parameter(description = "炉批号信息") HeatBatchNoCreateDTO heatBatchNoCreateDTO

    ) {
        heatBatchNoService.update(orgId, projectId, heatBatchId, heatBatchNoCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除炉批号",
        description = "删除炉批号"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{heatBatchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId) {
        heatBatchNoService.delete(orgId, projectId, heatBatchId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取炉批号详细信息",
        description = "获取炉批号详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{heatBatchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmHeatBatchNoEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId) {
        return new JsonObjectResponseBody<>(getContext(), heatBatchNoService.detail(orgId, projectId, heatBatchId));
    }

    /**
     * 炉批号信息导入
     *
     * @return 炉批号信息导入
     */
    @Override
    @Operation(
        summary = "炉批号信息导入",
        description = "炉批号信息导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/import"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody importHeatBatch(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmImportBatchTaskImportDTO importDTO) {
        final OperatorDTO operator = getContext().getOperator();

        ContextDTO contextDTO = getContext();
        contextDTO.setRequestMethod(contextDTO.getRequest().getMethod());
        materialbatchTaskService.run(
            orgId,
            projectId,
            0L,
            "",
            contextDTO,
            MaterialImportType.RECEIVE_RELEASE,
            importDTO.getDiscipline(),
            batchTask -> {

                MmImportBatchResultDTO result = heatBatchNoService.importHeatBatch(
                    orgId,
                    projectId,
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
