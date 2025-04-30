package com.ose.materialspm.api;

import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLnNode;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * SPM 放行单 接口
 */
@FeignClient(name = "ose-materialspm", contextId = "mmLnNodeFeign")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface SpmMatchLnNodeFeignAPI {

    @RequestMapping(//ftjftj
        method = GET,
        value = "spm-match-ln-node-count",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    Long matchLnNodeCount(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @RequestBody SpmMatchLnNodeDTO spmMatchLnNodeDTO
    );


    /**
     * 获取 SPM BOM 节点 M_LIST_NODES
     *
     * @return SpmMatchLnNode
     */
    @RequestMapping(
        method = GET,
        value = "spm-match-ln-nodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SpmMatchLnNode> matchLnNodes(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @RequestBody SpmMatchLnNodeDTO spmMatchLnNodeDTO
    );

    /**
     * 获取 SPM BOM 节点 M_LIST_NODES 的材料汇总
     *
     * @return List <MaterialMatchDTO>
     */
    @RequestMapping(
        method = GET,
        value = "spm-match-lns",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SpmMatchLns> matchLns(
        @PathVariable("projectId") @Parameter(description = "项目ID") Long projectId,
        @PathVariable("orgId") @Parameter(description = "组织ID") Long orgId,
        @RequestBody SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO
    );


    /**
     * 获取 SPM BOM 节点集 LN_IDs  的材料汇总
     *
     * @return List <SpmListPos>
     */
    @RequestMapping(
        method = GET,
        value = "spm-list-pos",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SpmListPosDTO> getListPos(@PathVariable("projectId") @Parameter(description = "项目ID") Long projectId,
                                                   @PathVariable("orgId") @Parameter(description = "组织ID") Long orgId,
                                                   @RequestBody SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO);
}
