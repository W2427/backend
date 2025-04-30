package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.tasks.dto.bpm.ModelDeployDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ActivitiModelCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmReDeployment;

/**
 * 流程模型接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ActivityModelAPI {

    @RequestMapping(method = POST, value = "models")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmReDeployment> deploy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ModelDeployDTO modelDeployDTO
    );

    @RequestMapping(method = GET, value = "models")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmReDeployment> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ActivitiModelCriteriaDTO criteriaDTO,
        PageDTO page
    );

    @RequestMapping(method = GET, value = "/models/{procDefId}")
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmReDeployment> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("procDefId") String procDefId
    );

    @RequestMapping(method = POST, value = "models/{procDefId}/suspend")
    @ResponseStatus(OK)
    public JsonResponseBody updateStateSuspend(
        @PathVariable("procDefId") String procDefId
    );

    @RequestMapping(method = POST, value = "models/{procDefId}/active")
    @ResponseStatus(OK)
    public JsonResponseBody updateStateActive(
        @PathVariable("procDefId") String procDefId
    );

//    @RequestMapping(method = GET, value="/model-categories")
//    @ResponseStatus(OK)
//    JsonListResponseBody<BpmActivityCategory> getCategories(
//            @PathVariable("orgId") Long orgId,
//            @PathVariable("projectId") Long projectId
//    );
}
