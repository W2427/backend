package com.ose.tasks.api.bpm;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
//import com.ose.tasks.dto.bpm.ActivitiyInstanceDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseCreateDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

/**
 * 任务处理case
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface BpmExecuteCaseAPI {

    /**
     * 创建任务处理case。
     *
     * @param orgId                   组织id
     * @param projectId               项目id
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @return
     */
    @RequestMapping(method = POST, value = "bpm-execute-case")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmExecuteCase> add(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO
    );

    /**
     * 查询任务处理case列表。
     *
     * @param orgId             组织id
     * @param projectId         项目id
     * @param bpmExecuteCaseDTO 查询参数
     * @param pageDTO           分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "bpm-execute-case")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExecuteCase> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BpmExecuteCaseDTO bpmExecuteCaseDTO,
        PageDTO pageDTO
    );

    /**
     * 查询特殊任务处理case。
     *
     * @param orgId             组织id
     * @param projectId         项目id
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    @RequestMapping(method = GET, value = "bpm-execute-case/item-case")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmExecuteCase> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BpmExecuteCaseDTO bpmExecuteCaseDTO
    );

    /**
     * 编辑任务处理case。
     *
     * @param orgId                   组织id
     * @param projectId               项目id
     * @param bpmExecuteCaseId        任务去处理caseid
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "bpm-execute-case/{bpmExecuteCaseId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmExecuteCase> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmExecuteCaseId") Long bpmExecuteCaseId,
        @RequestBody BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO
    );

    /**
     * 任务处理case详情。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "bpm-execute-case/{bpmExecuteCaseId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmExecuteCase> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmExecuteCaseId") Long bpmExecuteCaseId
    );

    /**
     * 删除任务处理case。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "bpm-execute-case/{bpmExecuteCaseId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmExecuteCaseId") Long bpmExecuteCaseId
    );
}
