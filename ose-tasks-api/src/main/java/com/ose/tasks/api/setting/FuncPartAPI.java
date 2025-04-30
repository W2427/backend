package com.ose.tasks.api.setting;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.funcPart.FuncPartDTO;
import com.ose.tasks.dto.setting.FuncPartCreateDTO;
import com.ose.tasks.dto.setting.FuncPartCriteriaDTO;
import com.ose.tasks.dto.setting.FuncPartUpdateDTO;
import com.ose.tasks.entity.setting.FuncPart;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping(value = "/orgs")
public interface FuncPartAPI {

    /**
     * 创建FUNC PART信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param funcPartCreateDTO ITP信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/func-parts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        FuncPartCreateDTO funcPartCreateDTO
    );

    /**
     * 获取FUNC PART列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param funcPartCriteriaDTO 查询条件
     * @return FUNC PART 列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/func-parts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FuncPart> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        FuncPartCriteriaDTO funcPartCriteriaDTO
    );

    /**
     * 获取FUNC PART详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param funcPartId     FUNC PART ID
     * @return FUNC PART详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/func-parts/{funcPartId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FuncPart> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("funcPartId") Long funcPartId
    );


    /**
     * 更新FUNC PART详情。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param funcPartId        FUNC PART ID
     * @param funcPartUpdateDTO 更新FUNC PART信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/func-parts/{funcPartId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("funcPartId") Long funcPartId,
        FuncPartUpdateDTO funcPartUpdateDTO
    );


    /**
     * 删除FUNCPART详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param funcPartId     FUNC PART ID
     */
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/func-parts/{funcPartId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("funcPartId") Long funcPartId
    );

    /**
     * 取得功能分块列表。
     *
     * @return 功能分块列表
     */
    @RequestMapping(
        method = GET,
        value = "/func-parts",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FuncPartDTO> list(
    );

    /**
     * 获取FUNC PART详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param funcPart     FUNC PART
     * @return FUNC PART详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/func-parts/func-part",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FuncPart> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("funcPart") String funcPart
    );

}
