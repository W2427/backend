package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ItpCreateDTO;
import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.dto.ItpUpdateDTO;
import com.ose.tasks.entity.Itp;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface ItpAPI {

    /**
     * 创建ITP信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpCreateDTO ITP信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/itps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ItpCreateDTO itpCreateDTO
    );

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/itps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Itp> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ItpCriteriaDTO itpCriteriaDTO
    );

    /**
     * 获取ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     * @return ITP详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Itp> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("itpId") Long itpId
    );


    /**
     * 更新ITP详情。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpId        ITPID
     * @param itpUpdateDTO 更新ITP信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("itpId") Long itpId,
        ItpUpdateDTO itpUpdateDTO
    );


    /**
     * 删除ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     */
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/itps/{itpId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("welderId") Long itpId
    );
}
