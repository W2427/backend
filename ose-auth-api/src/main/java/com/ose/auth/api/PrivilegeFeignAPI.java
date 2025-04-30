package com.ose.auth.api;

import com.ose.auth.dto.AuthCheckDTO;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.dto.OperatorDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 权限接口。
 */
@FeignClient(name = "ose-auth", contextId = "privilegeFeign")
public interface PrivilegeFeignAPI {

    @RequestMapping(
        method = POST,
        value = "/check-privilege",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<OperatorDTO> check(
        @RequestBody PrivilegeCheckDTO privilegeCheckDTO
    );

    @RequestMapping(
        method = GET,
        value = "/current-user/orgs/{orgId}/privileges",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserPrivilegeDTO> getUserPrivileges(
        @PathVariable("orgId") Long orgId
    );

    @RequestMapping(
        method = GET,
        value = "/current-user/orgs/{orgId}/available-privileges",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserPrivilegeDTO> getUserAvailablePrivileges(
        @PathVariable("orgId") Long orgId
    );

    /**
     * 检查用户权限。
     */
    @Operation(
        summary = "检查用户权限",
        description = "检查访问令牌持有者用户是否拥有对指定资源执行指定操作的权限。"
    )
    @RequestMapping(
        method = POST,
        value = "/check-offline-auth",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<AuthCheckDTO> checkAuth(
        @RequestBody AuthCheckDTO authCheckDTO
    );

}
