package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ProcessStageDTO;
import com.ose.tasks.entity.bpm.BpmProcessStage;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ProcessStageAPI {

    /**
     * 创建工序分类
     *
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/process-stages"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmProcessStage> create(
        @RequestBody ProcessStageDTO stageDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序分类列表
     *
     * @return 工序分类列表
     */
    @RequestMapping(
        method = GET,
        value = "/process-stages"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmProcessStage> getList(
        PageDTO page,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序分类详细信息
     *
     * @return 工序分类
     */
    @RequestMapping(
        method = GET,
        value = "/process-stages/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmProcessStage> getStage(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 删除工序分类
     *
     * @param id 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = DELETE,
        value = "/process-stages/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 编辑工序分类
     *
     * @param id 工序分类id
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/process-stages/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modify(
        @PathVariable("id") Long id,
        @RequestBody ProcessStageDTO stageDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

}
