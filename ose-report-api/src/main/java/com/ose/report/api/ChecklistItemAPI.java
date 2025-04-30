package com.ose.report.api;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ChecklistItemBatchImportDTO;
import com.ose.report.dto.ChecklistItemDTO;
import com.ose.report.dto.ChecklistItemImportDTO;
import com.ose.report.entity.ChecklistItem;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 检查单报表接口
 */
public interface ChecklistItemAPI {

    /**
     * 查询检查项
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @param page        分页信息
     * @return 检查项列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ChecklistItem> searchItems(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        PageDTO page
    );

    /**
     * 创建检查项
     *
     * @param orgId            组织
     * @param projectId        项目ID
     * @param checklistId      检查单ID
     * @param checklistItemDTO 检查项信息
     * @return 检查项信息
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ChecklistItem> createItem(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @RequestBody ChecklistItemDTO checklistItemDTO
    );

    /**
     * 编辑检查项
     *
     * @param orgId            组织
     * @param projectId        项目ID
     * @param checklistItemId  检查项ID
     * @param checklistItemDTO 检查项内容
     * @return 检查项信息
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items/{checklistItemId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody editItem(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long checklistItemId,
        @RequestBody ChecklistItemDTO checklistItemDTO
    );

    /**
     * 删除检查项
     *
     * @param orgId           组织
     * @param projectId       项目ID
     * @param checklistItemId 检查项ID
     * @return No content
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/items/{checklistItemId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteItem(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @PathVariable Long checklistItemId
    );

    /**
     * 导入检查单的检查项
     *
     * @param orgId          组织
     * @param projectId      项目ID
     * @param batchImportDTO 检查单项目批量导入数据传输对象
     * @return 导入完成的内容
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-checklist-items"
    )
    @ResponseStatus(CREATED)
    JsonListResponseBody<ChecklistItemImportDTO> importItems(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody ChecklistItemBatchImportDTO batchImportDTO
    );

}
