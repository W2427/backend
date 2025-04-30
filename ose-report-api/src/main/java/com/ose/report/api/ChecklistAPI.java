package com.ose.report.api;

import com.ose.dto.PageDTO;
import com.ose.report.dto.ChecklistDTO;
import com.ose.report.entity.Checklist;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 检查单报表接口
 */
public interface ChecklistAPI {

    /**
     * 查询检查单列表
     *
     * @param orgId           组织
     * @param projectId       项目ID
     * @param searchCondition 查询条件（名称 / 编号）
     * @param page            分页信息
     * @return 检查单列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<Checklist> search(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestParam(name = "search", required = false) String searchCondition,
        PageDTO page
    );

    /**
     * 查询单个检查单
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return 检查单信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}"
    )
    JsonObjectResponseBody<Checklist> search(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId
    );

    /**
     * 创建检查单
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param checklistDTO 检查项信息
     * @return 检查项信息
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Checklist> create(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @RequestBody ChecklistDTO checklistDTO
    );

    /**
     * 编辑检查单
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param checklistId  检查单ID
     * @param checklistDTO 检查单内容
     * @return No content
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Checklist> edit(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId,
        @RequestBody ChecklistDTO checklistDTO
    );

    /**
     * 删除检查单
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return No content
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId
    );

    /**
     * 预览检查单制作
     *
     * @param orgId       组织
     * @param projectId   项目ID
     * @param checklistId 检查单ID
     * @return 检查单实体
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/checklists/{checklistId}/generate"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Checklist> previewGenerate(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long checklistId
    );

}
