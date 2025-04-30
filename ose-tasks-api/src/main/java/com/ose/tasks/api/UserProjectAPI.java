package com.ose.tasks.api;

import com.ose.auth.entity.Organization;
import com.ose.response.JsonListResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通过用户查询项目信息接口。
 */
public interface UserProjectAPI {

    /**
     * 通过用户查询项目信息接口。
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/project-orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getProjectOrgs(
        @PathVariable("userId") Long userId
    );

}
