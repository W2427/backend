package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.entity.qc.*;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 实体质量检测
 */
public interface QualityControlAPI {


    /**
     * 获取 工序 质量检测结果。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param processId 查询条件
     * @return 结果列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/{processId}/{entityId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends BaseConstructionLog> getProcessTestResult(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long processId,
        @PathVariable Long entityId,
        PageDTO pageDTO
    );



}
