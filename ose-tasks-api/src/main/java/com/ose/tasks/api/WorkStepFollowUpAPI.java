package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.WeightsUpdateDTO;
import com.ose.tasks.dto.WorkCodeCreateDTO;
import com.ose.tasks.entity.WorkCode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface WorkStepFollowUpAPI {

    /**
     * 创建工作项代码
     * @param orgId 组织Id
     * @param projectId 项目Id
     * @param workCodeCreateDTO 工作项代码数据传输数据
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/createWorkCode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WorkCodeCreateDTO workCodeCreateDTO
    );

    /**
     * 查询工作项代码列表
     *
     * @param orgId 组织Id
     * @param projectId 项目Id
     * @param pageDTO 分页传输数据
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/searchWorkCode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WorkCode> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    );

    /**
     * 更新权重
     *
     * @param orgId 组织Id
     * @param projectId 项目Id
     * @param workCodeId 工作项代码Id
     * @param weightsUpdateDTO 更新权重传输对象
     * @return
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/updateWeights/{workCodeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateWeights(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workCodeId") Long workCodeId,
        WeightsUpdateDTO weightsUpdateDTO
    );

    /**
     * 导出进度跟踪表
     *
     * @param orgId 组织Id
     * @param projectId 项目Id
     * @param workCodeId 工作项代码Id
     * @param workCode 工作项代码
     */
    @RequestMapping(
        method =POST,
        value = "/orgs/{orgId}/projects/{projectId}/export-workStep-followUp",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportWorkStepFollowUp(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("workCodeId") Long workCodeId,
        @RequestParam("workCode") String workCode
    ) throws IOException;
}
