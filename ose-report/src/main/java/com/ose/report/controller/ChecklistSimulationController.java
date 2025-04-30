package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.report.api.ChecklistSimulationAPI;
import com.ose.report.domain.service.ChecklistInterface;
import com.ose.report.domain.service.ChecklistSimulationInterface;
import com.ose.report.domain.service.ReportGeneratorInterface;
import com.ose.report.dto.ChecklistSimulationDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.ChecklistSimulation;
import com.ose.report.exception.ReportGeneratingException;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "模拟检查单接口")
@RestController
public class ChecklistSimulationController extends BaseReportController implements ChecklistSimulationAPI {

    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    // 检查单报表生成服务
    private final ReportGeneratorInterface<ChecklistReportDTO> checklistGeneratorService;

    // 检查单服务
    private final ChecklistInterface checklistService;

    // 模拟检查单服务
    private final ChecklistSimulationInterface checklistSimulationService;

    // 文件上传接口
    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public ChecklistSimulationController(
        ReportGeneratorInterface<ChecklistReportDTO> checklistGeneratorService,
        ChecklistInterface checklistService,
        ChecklistSimulationInterface checklistSimulationService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(null, uploadFeignAPI);
        this.checklistGeneratorService = checklistGeneratorService;
        this.checklistService = checklistService;
        this.checklistSimulationService = checklistSimulationService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 查询模拟检查单
     *
     * @param orgId     组织
     * @param projectId 项目ID
     * @param page      分页信息
     * @return 模拟检查单列表
     */
    @Override
    @Operation(
        summary = "查询模拟检查单",
        description = "查询模拟检查单列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ChecklistSimulation> searchSimulations(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        PageDTO page
    ) {
        Page<ChecklistSimulation> simulations =
            checklistSimulationService.searchSimulation(orgId, projectId, page);
        return (new JsonListResponseBody<>(getContext(), simulations)).setIncluded(checklistService);
    }

    /**
     * 查询单个模拟检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 模拟检查单信息
     */
    @Override
    @Operation(
        summary = "查询单个模拟检查单",
        description = "模拟检查单ID进行查询。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}"
    )
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ChecklistSimulation> searchSimulation(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "检查单ID") Long simulationId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistSimulationService.searchSimulationInfo(orgId, projectId, simulationId)
        );
    }

    /**
     * 创建模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param checklistSimulationDTO 模拟检查单信息
     * @return 模拟检查单信息
     */
    @Override
    @Operation(
        summary = "创建模拟检查单",
        description = "根据模拟检查单信息，创建模拟检查单。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ChecklistSimulation> createSimulation(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @RequestBody @Parameter(description = "模拟检查单信息") ChecklistSimulationDTO checklistSimulationDTO
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistSimulationService.createSimulation(orgId, projectId, checklistSimulationDTO)
        );
    }

    /**
     * 编辑模拟检查单
     *
     * @param orgId                  组织ID
     * @param projectId              项目ID
     * @param simulationId           模拟检查单ID
     * @param checklistSimulationDTO 模拟检查单内容
     * @return 模拟检查单内容
     */
    @Override
    @Operation(
        summary = "编辑模拟检查单",
        description = "根据模拟检查单信息，编辑模拟检查单。"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody editSimulation(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "模拟检查单ID") Long simulationId,
        @RequestBody @Parameter(description = "模拟检查单内容") ChecklistSimulationDTO checklistSimulationDTO
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistSimulationService.updateSimulation(orgId, projectId, simulationId, checklistSimulationDTO)
        );
    }

    /**
     * 删除模拟检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return No content
     */
    @Override
    @Operation(
        summary = "删除模拟检查单",
        description = "根据模拟检查单ID，删除模拟检查单。"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteSimulation(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "模拟检查单ID") Long simulationId
    ) {
        checklistSimulationService.deleteSimulation(simulationId);
        return new JsonResponseBody();
    }

    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 制作完成的检查单
     */
    @Override
    @Operation(
        summary = "生成模拟检查单报表",
        description = "根据模拟检查单ID，制作检查单报表，并存入文件系统。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/simulations/{simulationId}/generate"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ChecklistSimulation> simulationGenerate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "模拟检查单ID") Long simulationId
    ) {

        // 制作报表数据
        ChecklistReportDTO checklistReportDTO = checklistSimulationService
            .assembleSimulationReportData(simulationId);

        if (checklistReportDTO == null) {
            throw new NotFoundError();
        }

        checklistReportDTO.setOutType("pdf");

        ChecklistSimulation checklistSimulation;

        try {
            checklistSimulation = checklistSimulationService.updateGeneratedFile(
                simulationId,
                LongUtils.parseLong(
                    uploadPDFFile(
                        orgId,
                        projectId,
                        checklistGeneratorService.generateReport(checklistReportDTO),
                        uploadFeignAPI
                    ).getId())
            );
        } catch (ReportGeneratingException e) {
            throw new BusinessError(e.getMessage());
        }

        // 返回模拟检查单实体
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistSimulation
        );
    }
}
