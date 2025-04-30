package com.ose.tasks.api.repairData;

import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.rapairData.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 修改项目数据接口。
 */
public interface DataRepairAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/repair-upload-path",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody repairPath(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody UploadFileDTO uploadFileDTO
    ) throws IOException;



    /**
     * 批量撤回流程。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/batch-revocation-node",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody batchRevocationNode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody RepairBatchRevocationNodesDTO repairBatchRevocationNodesDTO
    );



    /**
     * 批量修复drawing plan hour数据
     */
    @RequestMapping(
        method = POST,
        value = "/batch-repair-drawing-plan-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody batchRepairDrawingPlanHour();
}
