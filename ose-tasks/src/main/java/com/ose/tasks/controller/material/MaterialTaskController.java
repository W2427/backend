package com.ose.tasks.controller.material;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.material.MaterialTaskAPI;
import com.ose.tasks.domain.model.service.material.MaterialTaskInterface;
import com.ose.tasks.dto.material.MaterialTaskDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "材料采购包工作流管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class MaterialTaskController extends BaseController implements MaterialTaskAPI {

    private MaterialTaskInterface materialTaskService;

    /**
     * 构造方法。
     */
    @Autowired
    public MaterialTaskController(MaterialTaskInterface materialTaskService) {
        this.materialTaskService = materialTaskService;
    }

    @Override
    @Operation(description = "创建采购包任务")
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE,
        value="/purchase-package/task"
    )
    @WithPrivilege
    public JsonResponseBody createPurchasePackageTask(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody MaterialTaskDTO materialTaskDTO
    ) {
        materialTaskService.createPurchasePackageTask(
            orgId,
            projectId,
            getContext(),
            materialTaskDTO
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "创建入库单任务")
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE,
        value="/material-receive/task"
    )
    @WithPrivilege
    public JsonResponseBody createReceiveTask(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody MaterialTaskDTO materialTaskDTO
    ) {
        materialTaskService.createReceiveTask(
            orgId,
            projectId,
            getContext(),
            materialTaskDTO
        );
        return new JsonResponseBody();
    }

}
