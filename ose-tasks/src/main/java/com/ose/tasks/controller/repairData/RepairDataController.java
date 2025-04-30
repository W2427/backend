package com.ose.tasks.controller.repairData;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.repairData.DataRepairAPI;
import com.ose.tasks.domain.model.service.repairData.*;
import com.ose.tasks.dto.rapairData.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "修改项目数据接口")
@RestController
public class RepairDataController extends BaseController implements DataRepairAPI {


    private final RepairDataInterface repairDataService;



    /**
     * 构造方法。
     */
    @Autowired
    public RepairDataController(
        RepairDataInterface repairDataService) {
        this.repairDataService = repairDataService;
    }

    @Override
    @WithPrivilege
    public JsonResponseBody repairPath(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody UploadFileDTO uploadFileDTO
    ) throws IOException {
        repairDataService.repairPath(orgId, projectId, uploadFileDTO);
        return new JsonResponseBody();
    }


    /**
     * 批量撤回流程。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/batch-revocation-node",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody batchRevocationNode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody RepairBatchRevocationNodesDTO repairBatchRevocationNodesDTO
    ) {
        repairDataService.batchRevocationNode(orgId, projectId, repairBatchRevocationNodesDTO, getContext());

        return new JsonResponseBody();
    }


    @RequestMapping(
        method = POST,
        value = "/batch-repair-drawing-plan-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody batchRepairDrawingPlanHour() {
        repairDataService.batchRepairDrawingPlanHour();

        return new JsonResponseBody();
    }

}
