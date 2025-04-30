package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import com.ose.tasks.vo.setting.CategoryTypeTag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.EntityTypeDTO;
import com.ose.tasks.entity.bpm.BpmEntityType;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/entity-types/{type}")
public interface EntityTypeAPI {

    /**
     * 创建实体类型分类
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmEntityType> create(
        @RequestBody EntityTypeDTO typeDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

    /**
     * 获取实体类型分类列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityType> getList(
        PageDTO page,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

    /**
     * 获取实体类型分类列表
     */
    @RequestMapping(
        method = GET,
        value = "/fixed-level"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityType> getFixedLevelList(
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type,
        EntityTypeDTO entityTypeDTO
    );

    /**
     * 获取实体类型分类详细信息
     *
     * @return 工序分类
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmEntityType> detail(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

    /**
     * 通过名称获取实体类型分类
     *
     * @return 实体类型分类信息
     */
    @RequestMapping(
        method = GET,
        value = "/name/{name}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmEntityType> getCategoryTypeByName(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type,
        @PathVariable @Parameter(description = "name") String name
    );

    /**
     * 删除实体类型分类
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

    /**
     * 编辑实体类型分类
     */
    @RequestMapping(
        method = POST,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modify(
        @PathVariable("id") Long id,
        @RequestBody EntityTypeDTO typeDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

}
