package com.ose.auth.controller;

import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.PrivilegeAPI;
import com.ose.auth.domain.model.service.PrivilegeInterface;
import com.ose.auth.dto.AuthCheckDTO;
import com.ose.auth.dto.PrivilegeCheckDTO;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.auth.vo.UserPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.NoPrivilegeError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户权限接口")
@RestController
public class PrivilegeController extends BaseController implements PrivilegeAPI {

    // 权限服务
    private PrivilegeInterface privilegeService;

    /**
     * 构造方法。
     */
    @Autowired
    public PrivilegeController(
        PrivilegeInterface privilegeService
    ) {
        this.privilegeService = privilegeService;
    }

    /**
     * 检查用户权限。
     */
    @Override
    @Operation(
        summary = "检查用户权限",
        description = "检查访问令牌持有者用户是否拥有对指定资源执行指定操作的权限。"
    )
    @CheckPrivilege(false)
    public JsonObjectResponseBody<OperatorDTO> check(
        @RequestBody PrivilegeCheckDTO privilegeCheckDTO
    ) {

        ContextDTO context = getContext();
        OperatorDTO operator = context.getOperator();

        if (!privilegeService.hasPrivilege(operator, privilegeCheckDTO)) {
            throw new NoPrivilegeError(UserPrivilege.toNameSet(privilegeCheckDTO.getPrivileges()));
        }

        return new JsonObjectResponseBody<>(context, operator);
    }

    /**
     * 检查用户权限。
     */
    @Override
    @Operation(
        summary = "检查用户权限",
        description = "检查访问令牌持有者用户是否拥有对指定资源执行指定操作的权限。"
    )
    public JsonObjectResponseBody<AuthCheckDTO> checkAuth(
        @RequestBody AuthCheckDTO authCheckDTO
    ) {

        if (!privilegeService.hasAuth(authCheckDTO)) {
            throw new NoPrivilegeError();
        }

        authCheckDTO.setAuth(true);
        return new JsonObjectResponseBody<>(authCheckDTO);
    }

    @Override
    @Operation(description = "取得登录用户在指定组织的权限，并根据作用域分组")
    @WithPrivilege
    public JsonListResponseBody<UserPrivilegeDTO> getUserPrivileges(
        @PathVariable @Parameter(description = "组织 ID") Long orgId
    ) {
        return new JsonListResponseBody<>(
            privilegeService.getUserPrivileges(getContext().getOperator(), orgId)
        );
    }

    @Override
    @Operation(description = "取得当前用户在指定组织及其所有下级组织所用有的所有权限")
    @WithPrivilege
    public JsonObjectResponseBody<UserPrivilegeDTO> getUserAvailablePrivileges(
        @PathVariable @Parameter(description = "组织 ID") Long orgId
    ) {
        return new JsonObjectResponseBody<>(privilegeService.getUserAvailablePrivileges(getContext().getOperator(), orgId));
    }

}
