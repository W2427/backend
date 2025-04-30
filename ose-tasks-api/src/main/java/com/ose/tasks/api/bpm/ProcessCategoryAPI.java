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
import com.ose.tasks.dto.bpm.ProcessCategoryDTO;
import com.ose.tasks.entity.bpm.BpmProcessCategory;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ProcessCategoryAPI {

    /**
     * 创建工序分类
     *
     * @param stageDTO 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/process-categories"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmProcessCategory> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ProcessCategoryDTO stageDTO
    );

    /**
     * 获取工序分类列表
     *
     * @return 工序分类列表
     */
    @RequestMapping(
        method = GET,
        value = "/process-categories"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmProcessCategory> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page
    );

    /**
     * 获取工序分类详细信息
     *
     * @return 工序分类
     */
    @RequestMapping(
        method = GET,
        value = "/process-categories/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmProcessCategory> getEntitySubType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 删除工序分类
     *
     * @param id 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = DELETE,
        value = "/process-categories/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 编辑工序分类
     *
     * @param id       工序分类id
     * @param stageDTO 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/process-categories/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modify(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody ProcessCategoryDTO stageDTO
    );

}
