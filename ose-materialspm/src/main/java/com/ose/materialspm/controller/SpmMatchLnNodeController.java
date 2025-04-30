package com.ose.materialspm.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.SpmMatchLnNodeAPI;
import com.ose.materialspm.domain.model.service.SpmMatchLnNodeInterface;
import com.ose.materialspm.dto.SpmMatchLnCriteriaDTO;
import com.ose.materialspm.dto.SpmMatchLnNodeDTO;
import com.ose.materialspm.entity.SpmListPosDTO;
import com.ose.materialspm.entity.SpmMatchLnNode;
import com.ose.materialspm.entity.SpmMatchLns;
import com.ose.response.JsonListResponseBody;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "SPM 请购单(REQ) 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class SpmMatchLnNodeController extends BaseController implements SpmMatchLnNodeAPI {

    /**
     * SPM BOM NODE MATCH PERCENT 查询服务
     */
    private final SpmMatchLnNodeInterface spmMatchLnNodeService;

    /**
     * 构造方法
     *
     * @param spmMatchLnNodeService 材料匹配节点查询服务
     */
    @Autowired
    public SpmMatchLnNodeController(SpmMatchLnNodeInterface spmMatchLnNodeService) {
        this.spmMatchLnNodeService = spmMatchLnNodeService;
    }


//
//	@RequestMapping(//ftjftj  test
//			method = GET,
//			value = "ftj",
//			consumes = ALL_VALUE,
//			produces = APPLICATION_JSON_VALUE
//	)
//	@WithPrivilege
//	@ResponseStatus(OK)
//	@Override
//	public int test() {
//		return 0;
//	}

    /**
     * 获取 SPM BOM 节点 M_LIST_NODES 总数
     *
     * @return 节点个数
     */
    @RequestMapping(
        method = GET,
        value = "spm-match-ln-node-count",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public Long matchLnNodeCount(@PathVariable Long projectId,
                                 @PathVariable Long orgId,
                                 @RequestBody SpmMatchLnNodeDTO spmMatchLnNodeDTO) {
        return spmMatchLnNodeService.getMatchLnNodeCount(spmMatchLnNodeDTO);
    }

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
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<SpmMatchLnNode> matchLnNodes(
        @PathVariable Long projectId,
        @PathVariable Long orgId,
        @RequestBody SpmMatchLnNodeDTO spmMatchLnNodeDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            spmMatchLnNodeService.getList(spmMatchLnNodeDTO)
        );
    }

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
    @WithPrivilege
    @Override
    public JsonListResponseBody<SpmMatchLns> matchLns(@PathVariable("projectId") Long projectId,
                                                      @PathVariable("orgId") Long orgId,
                                                      @RequestBody SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO) {
        Page<SpmMatchLns> spmMatchLns = spmMatchLnNodeService.getMatchList(spmMatchLnCriteriaDTO);

        return new JsonListResponseBody<>(
            getContext(),
            spmMatchLns
        );
    }

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
    @WithPrivilege
    @Override
    public JsonListResponseBody<SpmListPosDTO> getListPos(@PathVariable("projectId") @Parameter(description = "项目ID") Long projectId,
                                                          @PathVariable("orgId") @Parameter(description = "组织ID") Long orgId,
                                                          @RequestBody SpmMatchLnCriteriaDTO spmMatchLnCriteriaDTO) {
        List<BigDecimal> lnIds = StringUtils.decode(spmMatchLnCriteriaDTO.getLnIdsString(), new TypeReference<List<BigDecimal>>() {});
//            JSONObject.parseArray(spmMatchLnCriteriaDTO.getLnIdsString(), BigDecimal.class);
        if (StringUtils.isEmpty(spmMatchLnCriteriaDTO.getSpmProjId()) || CollectionUtils.isEmpty(lnIds)) {
            return new JsonListResponseBody<>(
                getContext(),
                new ArrayList<>()
            );
        }
        return new JsonListResponseBody<>(
            getContext(),
            spmMatchLnNodeService.getListPos(spmMatchLnCriteriaDTO.getSpmProjId(), lnIds)
        );
    }
}
