package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.report.api.ChecklistAPI;
import com.ose.report.domain.service.ChecklistInterface;
import com.ose.report.domain.service.ReportGeneratorInterface;
import com.ose.report.domain.service.ReportTemplateInterface;
import com.ose.report.dto.ChecklistDTO;
import com.ose.report.dto.report.ChecklistReportDTO;
import com.ose.report.entity.Checklist;
import com.ose.report.exception.ReportGeneratingException;
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
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "检查单接口")
@RestController
public class ChecklistController extends BaseReportController implements ChecklistAPI {

    // 检查单报表生成服务
    private final ReportGeneratorInterface<ChecklistReportDTO> checklistGeneratorService;

    // 检查单服务
    private final ChecklistInterface checklistService;

    // 报表模板服务
    private final ReportTemplateInterface reportTemplateService;

    // 文件上传接口
    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public ChecklistController(
        ReportGeneratorInterface<ChecklistReportDTO> checklistGeneratorService,
        ChecklistInterface checklistService,
        ReportTemplateInterface reportTemplateService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {
        super(null, uploadFeignAPI);
        this.checklistGeneratorService = checklistGeneratorService;
        this.checklistService = checklistService;
        this.reportTemplateService = reportTemplateService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 查询检查单列表
     *
     * @param orgId           组织
     * @param projectId       项目ID
     * @param searchCondition 查询条件（名称 / 编号）
     * @param page            分页信息
     * @return 检查单列表
     */
    @Override
    @Operation(
        summary = "查询检查单列表",
        description = "查询检查单列表，或使用过滤条件（名称/编号）进行查询。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Checklist> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestParam(name = "search", required = false) @Parameter(description = "过滤条件（名称/编号）") String searchCondition,
        PageDTO page) {

        return (new JsonListResponseBody<>(
            getContext(),
            checklistService.search(orgId, projectId, searchCondition, page)
        )).setIncluded(reportTemplateService);
    }

    /**
     * 查询单个检查单
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return 检查单信息
     */
    @Override
    @Operation(
        summary = "查询单个检查单",
        description = "检查单ID进行查询。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Checklist> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId) {

        return (new JsonObjectResponseBody<>(
            getContext(),
            checklistService.search(checklistId)
        )).setIncluded(reportTemplateService);
    }

    /**
     * 创建检查单
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param checklistDTO 检查单基本信息
     * @return 检查单基本信息
     */
    @Override
    @Operation(
        summary = "创建检查单",
        description = "根据检查单基本信息，创建检查单。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Checklist> create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody @Parameter(description = "检查单基本信息") ChecklistDTO checklistDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            checklistService.create(orgId, projectId, checklistDTO)
        );
    }

    /**
     * 编辑更新检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param checklistId  检查单ID
     * @param checklistDTO 检查单基本信息
     * @return 修改后的检查单基本信息
     */
    @Override
    @Operation(
        summary = "编辑更新检查单",
        description = "根据检查单ID，使用变更的检查单基本信息，更新检查单。"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Checklist> edit(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @RequestBody @Parameter(description = "检查单基本信息") ChecklistDTO checklistDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            checklistService.update(orgId, projectId, checklistId, checklistDTO)
        );
    }

    /**
     * 删除检查单
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return No Content
     */
    @Override
    @Operation(
        summary = "删除检查单",
        description = "根据检查单ID删除检查单。"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId) {

        checklistService.delete(checklistId);

        return new JsonResponseBody();
    }

    /**
     * 预览检查单制作
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return 检查单实体
     */
    @Override
    @Operation(
        summary = "生成预览检查单报表",
        description = "根据检查单ID，制作空白检查单报表，并存入文件系统。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/generate"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Checklist> previewGenerate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId) {

        // 制作报表数据
        ChecklistReportDTO checklistReportDTO = checklistService
            .assemblePreviewReportData(checklistId);

        if (checklistReportDTO == null) {
            throw new NotFoundError();
        }

        checklistReportDTO.setOutType("pdf");

        Checklist checklist;

        try {
            checklist = checklistService.updatePreviewFile(
                checklistId,
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

        // 返回检查单实体
        return new JsonObjectResponseBody<>(getContext(), checklist);
    }

}
