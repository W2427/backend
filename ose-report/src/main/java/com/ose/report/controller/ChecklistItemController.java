package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.report.api.ChecklistItemAPI;
import com.ose.report.domain.service.ChecklistInterface;
import com.ose.report.dto.ChecklistItemBatchImportDTO;
import com.ose.report.dto.ChecklistItemDTO;
import com.ose.report.dto.ChecklistItemImportDTO;
import com.ose.report.entity.ChecklistItem;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "检查单项目接口")
@RestController
public class ChecklistItemController extends BaseReportController implements ChecklistItemAPI {

    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    // 检查单服务
    private final ChecklistInterface checklistService;

    /**
     * 构造方法。
     */
    @Autowired
    public ChecklistItemController(
        ChecklistInterface checklistService
    ) {
        super(null, null);
        this.checklistService = checklistService;
    }

    /**
     * 查询检查项
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @param page        分页信息
     * @return 检查项列表
     */
    @Override
    @Operation(
        summary = "查询检查项列表",
        description = "根据检查单ID，查询检查项列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ChecklistItem> searchItems(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        PageDTO page
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            checklistService.searchItem(checklistId, page)
        );
    }

    /**
     * 创建检查项
     *
     * @param orgId            组织
     * @param projectId        项目ID
     * @param checklistId      检查单ID
     * @param checklistItemDTO 检查项信息
     * @return 检查项信息
     */
    @Override
    @Operation(
        summary = "创建检查项",
        description = "根据检查单ID，创建检查单下的检查项。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ChecklistItem> createItem(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @RequestBody @Parameter(description = "检查项信息") ChecklistItemDTO checklistItemDTO
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistService.createItem(checklistId, checklistItemDTO)
        );
    }

    /**
     * 编辑检查项
     *
     * @param orgId            组织
     * @param projectId        项目ID
     * @param checklistItemId  检查项ID
     * @param checklistItemDTO 检查项内容
     * @return 检查项信息
     */
    @Override
    @Operation(
        summary = "编辑检查项",
        description = "根据检查项ID，更新检查单下的检查项。"
    )
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items/{checklistItemId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ChecklistItem> editItem(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "检查项ID") Long checklistItemId,
        @RequestBody @Parameter(description = "检查项内容") ChecklistItemDTO checklistItemDTO
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            checklistService.updateItem(checklistItemId, checklistItemDTO)
        );
    }

    /**
     * 删除检查项
     *
     * @param orgId           组织
     * @param projectId       项目ID
     * @param checklistItemId 检查项ID
     * @return No content
     */
    @Override
    @Operation(
        summary = "删除检查项",
        description = "根据检查项ID，删除检查单下的检查项。"
    )
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items/{checklistItemId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteItem(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "检查单ID") Long checklistId,
        @PathVariable @Parameter(description = "检查项ID") Long checklistItemId
    ) {
        checklistService.deleteItem(checklistItemId);
        return new JsonResponseBody();
    }

    /**
     * 导入检查单的检查项
     *
     * @param orgId          组织
     * @param projectId      项目ID
     * @param batchImportDTO 检查单项目批量导入数据传输对象
     * @return 导入完成的内容
     */
    @Override
    @Operation(
        summary = "导入检查项",
        description = "导入检查单的检查项。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-checklist-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonListResponseBody<ChecklistItemImportDTO> importItems(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody @Parameter(description = "检查单检查项数组")
            ChecklistItemBatchImportDTO batchImportDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            checklistService.saveImportItem(
                orgId,
                projectId,
                batchImportDTO.getChecklistItems()
            )
        );
    }

}
