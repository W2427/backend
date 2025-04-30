package com.ose.tasks.api.bpm;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


/**
 * 任务代理设置 API
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface BpmActTaskConfigAPI {

    /**
     * 创建任务代理设置。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param bpmActTaskConfigCreateDTO 传输对象
     * @return
     */
    @RequestMapping(method = POST, value = "task-delegate-config")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmActTaskConfig> add(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BpmActTaskConfigCreateDTO bpmActTaskConfigCreateDTO
    );

    /**
     * 查询任务代理设置列表。
     *
     * @param orgId               组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigDTO 查询参数
     * @param pageDTO             分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "task-delegate-config")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActTaskConfig> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BpmActTaskConfigDTO bpmActTaskConfigDTO,
        PageDTO pageDTO
    );


    /**
     * 编辑任务代理设置。
     *
     * @param orgId               组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigId  任务代理设置ID
     * @param bpmActTaskConfigDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = PUT,
        value = "task-delegate-config/{bpmActTaskConfigId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmActTaskConfig> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmActTaskConfigId") Long bpmActTaskConfigId,
        @RequestBody BpmActTaskConfigDTO bpmActTaskConfigDTO
    );

    /**
     * 任务代理设置详情。
     *
     * @param orgId              组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "task-delegate-config/{bpmActTaskConfigId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmActTaskConfig> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmActTaskConfigId") Long bpmActTaskConfigId
    );

    /**
     * 删除任务代理设置。
     *
     * @param orgId              组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "task-delegate-config/{bpmActTaskConfigId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmActTaskConfigId") Long bpmActTaskConfigId
    );


}
