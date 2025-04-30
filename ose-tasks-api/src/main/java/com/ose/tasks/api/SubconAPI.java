package com.ose.tasks.api;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.SubconDTO;
import com.ose.tasks.entity.Subcon;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface SubconAPI {

    /**
     * 创建分包商信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconDTO 分包商信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/subcons",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        SubconDTO subconDTO
    );

    /**
     * 获取分包商列表
     *
     * @return 分包商列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/subcons",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Subcon> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取分包商详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     * @return 分包商详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/subcons/{subconId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Subcon> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subconId") Long subconId
    );

    /**
     * 获取工作班组
     *
     * @return 工作班组列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/teamName",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BpmActivityInstanceState> searchTeamName(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取工作场地
     *
     * @return 工作场地列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/workSite",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BpmActivityInstanceState> searchWorkSiteName(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );
}
