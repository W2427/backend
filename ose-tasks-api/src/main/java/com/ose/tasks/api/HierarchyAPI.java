package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.ExInspInfoDTO;
import com.ose.tasks.entity.HierarchyNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 项目管理接口。
 */
public interface HierarchyAPI {

    /**
     * 取得层级结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("depth") int depth,
        @RequestParam(name = "viewType", required = false) String viewType,
        @RequestParam(name = "drawingType", required = false) String drawingType,
        @RequestParam(name = "needEntity", required = false, defaultValue = "true") Boolean needEntity
    );

    /**
     * 取得层级结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("rootNodeId") Long rootNodeId,
        @RequestParam("depth") int depth,
        @RequestParam(name = "viewType", required = false) String viewType,
        @RequestParam(name = "drawingType", required = false) String drawingType,
        @RequestParam(name = "needEntity", required = false, defaultValue = "true") Boolean needEntity
    );

    /**
     * 设置层级结构。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyDTO> setHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody HierarchyDTO<HierarchyNodePutDTO> hierarchyDTO
    );

    /**
     * 设置层级结构。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyDTO> setHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("rootNodeId") Long rootNodeId,
        @RequestParam("version") long version,
        @RequestBody HierarchyDTO<HierarchyNodePutDTO> hierarchyDTO
    );

    /**
     * 导入层级结构。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 导入层级结构（指定根节点）。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/import-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("rootNodeId") Long rootNodeId,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 添加节点。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyNode> add(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeModifyDTO nodeDTO
    );

    /**
     * 更新节点。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyNode> update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("nodeId") Long nodeId,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeModifyDTO nodeDTO
    );

    /**
     * 删除节点。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("nodeId") Long nodeId,
        @RequestParam("version") long version
    );

    /**
     * 取得节点列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<HierarchyNode> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("depth") int depth
    );

    /**
     * 取得节点列表（指定根节点）。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{rootNodeId}/nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<HierarchyNode> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("rootNodeId") Long rootNodeId,
        @RequestParam("depth") int depth
    );

    /**
     * 取得节点详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/nodes/{nodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyNode> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("nodeId") Long nodeId
    );

    /**
     * 从层级取得模块的外检报告。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module/{nodeId}/external-inspection-report",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExInspInfoDTO> getExternalInspectionReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("nodeId") Long nodeId
    );

    /**
     * 下载层级取得模块的外检报告。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/module/{nodeId}/download/external-inspection-report",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void downloadExternalInspectionReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("nodeId") Long nodeId
    ) throws IOException;

    /**
     * 更新层级节点上的计划实施进度信息。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/refresh-wbs-progress-on-hierarchy-nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody refreshWBSProgress(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

}
