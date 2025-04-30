package com.ose.auth.api;

import com.ose.auth.dto.PythonQueryDTO;
import com.ose.auth.dto.UserCriteriaDTO;
import com.ose.auth.dto.UserProjectDTO;
import com.ose.auth.dto.UserProjectSearchDTO;
import com.ose.auth.entity.UserPosition;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 用户信息接口。
 */
public interface UserAPI extends UserFeignAPI {

    /**
     * 查询用户。
     *
     * @return 用户信息列表
     */
    @RequestMapping(
        method = GET,
        value = "/users",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> search(
        UserCriteriaDTO criteria,
        PageDTO page
    );

    /**
     * 按条件导出用户一股。
     *
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/users/download"
    )
    void downloadUsers(
        UserCriteriaDTO criteriaDTO
    ) throws IOException;

    /**
     * 查询用户所在的项目（即组织根结点）。
     *
     * @return 组织列表
     */
    @RequestMapping(
        method = GET,
        value = "/user/{userId}/projects",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProjectDTO> searchProjects(
        @PathVariable @Parameter(description = "用户 ID") Long userId,
        UserProjectSearchDTO criteria);


    /**
     * 获取所有的职位列表。
     *
     * @return 职位列表
     */
    @GetMapping(
        value = "/user-positions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserPosition> getAllPositions();

    /**
     * 统一统计屏接口
     *
     * @return 统计结果
     */
    @PostMapping(
        value = "/overview",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    Object overview(PythonQueryDTO queryDTO);
}



