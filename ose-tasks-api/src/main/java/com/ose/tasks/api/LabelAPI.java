package com.ose.tasks.api;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Label;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/label")
public interface LabelAPI {

    /**
     * 获取列表
     *
     * @return 列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<Label> getList(
        LabelCriteriaDTO criteriaDTO
    );

    @Operation(
        summary = "查询标签信息",
        description = "查询标签信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Label> get(
        @PathVariable @Parameter(description = "id") Long id
    );

    /**
     * 创建标签
     *
     * @param labelDTO 标签信息
     * @return 标签信息
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Label> create(
        @RequestBody LabelDTO labelDTO
    );

    /**
     * 编辑标签
     *
     * @param labelDTO 标签信息
     * @param id         标签id
     * @return 标签信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    JsonObjectResponseBody<Label> edit(
        @RequestBody LabelDTO labelDTO,
        @PathVariable("id") Long id
    );

    /**
     * 删除标签
     *
     * @param id 工序id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("id") Long id
    );
}
