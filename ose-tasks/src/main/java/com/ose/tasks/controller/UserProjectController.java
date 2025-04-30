package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.entity.Organization;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.UserProjectAPI;
import com.ose.tasks.domain.model.service.UserProjectInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "通过用户查询项目信息接口")
@RestController
public class UserProjectController extends BaseController implements UserProjectAPI {

    private final UserProjectInterface userProjectService;

    /**
     * 构造方法。
     */
    @Autowired
    public UserProjectController(
        UserProjectInterface userProjectService
    ) {
        this.userProjectService = userProjectService;
    }

    @Override
    @Operation(description = "获取用户的顶层项目组织列表")
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/project-orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<Organization> getProjectOrgs(
        @PathVariable @Parameter(description = "所属组织 ID") Long userId
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            userProjectService.getProjectOrgs(
                userId
            )
        );
    }
}
