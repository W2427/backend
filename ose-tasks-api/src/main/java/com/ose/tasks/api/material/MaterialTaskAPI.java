package com.ose.tasks.api.material;

import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.MaterialTaskDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 材料工作流管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface MaterialTaskAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE,
        value="/purchase-package/task"
    )
    JsonResponseBody createPurchasePackageTask(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody MaterialTaskDTO materialTaskDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE,
        value="/material-receive/task"
    )
    JsonResponseBody createReceiveTask(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody MaterialTaskDTO materialTaskDTO);

}
