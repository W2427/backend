package com.ose.tasks.api.drawing;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.*;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-project-dept-idc-relation")
public interface DrawingProjectDeptIDCRelationAPI {

    /**
     * 创建
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingProjectDeptIDCRelation> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingProjectDeptIDCRelationDTO dto
    );

    /**
     * 修改
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    JsonObjectResponseBody<DrawingProjectDeptIDCRelation> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingProjectDeptIDCRelationDTO dto
    );

    /**
     * 获取列表
     *
     * @return 列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingProjectDeptIDCRelation> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DrawingProjectDeptIDCRelationSearchDTO dto
    );

    /**
     * 获取列表
     *
     * @return 列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingProjectDeptIDCRelationDTO> getListByDrawingId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId
    );

//    /**
//     * 获取单个信息
//     */
//    @RequestMapping(
//        method = GET,
//        value = "/{id}"
//    )
//    JsonObjectResponseBody<DrawingProjectDeptIDCRelation> detail(
//        @PathVariable("orgId") Long orgId,
//        @PathVariable("projectId") Long projectId,
//        @PathVariable("id") Long id
//    );


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );
}
