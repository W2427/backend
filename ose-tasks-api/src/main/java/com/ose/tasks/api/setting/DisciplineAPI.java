package com.ose.tasks.api.setting;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.discipline.DisciplineCodeDTO;
import com.ose.tasks.dto.setting.DiscCreateDTO;
import com.ose.tasks.dto.setting.DiscCriteriaDTO;
import com.ose.tasks.dto.setting.DiscUpdateDTO;
import com.ose.tasks.entity.setting.Discipline;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping(value = "/orgs")
public interface DisciplineAPI {

    /**
     * 创建DISCIPLINE信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param discCreateDTO ITP信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/disciplines",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DiscCreateDTO discCreateDTO
    );

    /**
     * 获取DISCIPLINE列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param discCriteriaDTO 查询条件
     * @return DISCIPLINE列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/disciplines",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Discipline> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DiscCriteriaDTO discCriteriaDTO
    );

    /**
     * 获取DISCIPLINE详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param disciplineId     DISCIPLINEID
     * @return DISCIPLINE详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/disciplines/{disciplineId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Discipline> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("disciplineId") Long disciplineId
    );


    /**
     * 更新DISCIPLINE详情。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param disciplineId        DISCIPLINEID
     * @param itpUpdateDTO 更新DISCIPLINE信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/disciplines/{disciplineId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("disciplineId") Long disciplineId,
        DiscUpdateDTO itpUpdateDTO
    );


    /**
     * 删除DISCIPLINE详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param disciplineId     DISCIPLINEID
     */
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/disciplines/{disciplineId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("welderId") Long disciplineId
    );

    /**
     * 取得专业代码列表。
     *
     * @return 专业代码列表
     */
    @RequestMapping(
        method = GET,
        value = "/discipline-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DisciplineCodeDTO> list(
    );

    @RequestMapping(
        method = GET,
        value = "/bean-list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DisciplineCodeDTO> beanList();
}
