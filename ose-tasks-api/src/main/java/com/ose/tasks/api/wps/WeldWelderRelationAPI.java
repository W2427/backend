package com.ose.tasks.api.wps;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wps.WeldWelderRelationCreateDTO;
import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface WeldWelderRelationAPI {

    /**
     * 创建焊口焊工关系
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param weldWelderRelationCreateDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/weld-welder-relation",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WeldWelderRelationCreateDTO weldWelderRelationCreateDTO
    );

    /**
     * 获取wps列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param weldWelderRelationSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/weld-welder-relation",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WeldWelderRelation> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    );

}
