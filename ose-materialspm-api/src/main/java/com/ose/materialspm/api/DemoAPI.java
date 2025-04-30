package com.ose.materialspm.api;

import static org.springframework.http.HttpStatus.OK;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.materialspm.dto.DemoCriteriaDTO;
import com.ose.response.JsonResponseBody;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/processes")
public interface DemoAPI {

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonResponseBody getList(
        HttpServletRequest request,
        HttpServletResponse response,
        DemoCriteriaDTO criteriaDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );


}
