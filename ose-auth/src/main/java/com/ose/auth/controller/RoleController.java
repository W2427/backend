package com.ose.auth.controller;

import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.RoleAPI;
import com.ose.auth.domain.model.service.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Tag(name = "角色接口")
public class RoleController extends BaseController implements RoleAPI {

    // 角色服务
    private final RoleInterface roleService;
    // 组织服务
    private final OrganizationInterface organizationService;
    private RoleMemberInterface roleMemberService;
    private UserInterface userService;

    /**
     * 构造器。
     *
     * @param roleService 角色服务
     */
    @Autowired
    public RoleController(
        RoleInterface roleService,
        OrganizationInterface organizationService,
        RoleMemberInterface roleMemberService,
        UserInterface userService
    ) {
        this.roleService = roleService;
        this.organizationService = organizationService;
        this.roleMemberService = roleMemberService;
        this.userService = userService;
    }

    /**
     * 创建角色。
     *
     * @param roleDTO 创建信息
     * @return 角色信息
     */
    @Override
    @Operation(
        summary = "创建角色",
        description = "需要有创建角色的权限"
    )
    @RequestMapping(
        value = "/orgs/{organizationId}/roles",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @CheckPrivilege
    public JsonObjectResponseBody<Role> create(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @RequestBody @Parameter(description = "角色创建信息") RoleDTO roleDTO
    ) {
        ContextDTO context = getContext();
        return new JsonObjectResponseBody<>(
            context,
            roleService.create(context.getOperator().getId(), organizationId, roleDTO)
        );
    }

    /**
     * 创建system角色。
     *
     * @param roleDTO 创建信息
     * @return 角色信息
     */
    @Override
    @Operation(
        summary = "创建system角色",
        description = "需要有创建角色的权限"
    )
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/system",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @CheckPrivilege
    public JsonObjectResponseBody<Role> createSystem(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @RequestBody @Parameter(description = "角色创建信息") RoleDTO roleDTO
    ) {
        ContextDTO context = getContext();
        return new JsonObjectResponseBody<>(
            context,
            roleService.createSystem(context.getOperator().getId(), organizationId, roleDTO)
        );
    }

    /**
     * 创建角色。
     *
     * @param role 创建信息
     * @return 角色信息
     */
    @RequestMapping(
        value = "/orgs/flat-role",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<Role> createRole(
        @RequestBody Role role
    ) {
        return new JsonObjectResponseBody<>(
            roleService.createRole(role)
        );
    }

    /**
     * 获取角色列表。
     *
     * @param page 分页参数
     * @return 角色列表
     */
    @Override
    @Operation(
        summary = "获取角色列表",
        description = "需要拥有查看角色权限"
    )
    @RequestMapping(
        value = "/orgs/{organizationId}/roles",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<Role> search(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @Parameter(description = "查询参数") RoleCriteriaDTO roleCriteriaDTO,
        PageDTO page
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            roleService.search(organizationId, roleCriteriaDTO, page)
        ).setIncluded(organizationService);
    }

    /**
     * 获取角色详情。
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    @Override
    @Operation(
        summary = "获取角色详情",
        description = "需要拥有查看角色的权限"
    )
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonObjectResponseBody<Role> get(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @PathVariable @Parameter(description = "角色ID") Long roleId
    ) {
        ContextDTO context = getContext();
        return
            new JsonObjectResponseBody<>(context, roleService.get(roleId))
                .setIncluded(organizationService);
    }

    /**
     * 更新角色信息。
     *
     * @param roleId  角色ID
     * @param roleDTO 更新信息
     */
    @Override
    @Operation(
        summary = "更新角色信息",
        description = "拥有更新角色权限"
    )
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}",
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @PathVariable @Parameter(description = "角色ID") Long roleId,
        @RequestBody RoleDTO roleDTO
    ) {
        ContextDTO context = getContext();
        roleService.update(context.getOperator().getId(), organizationId, roleId, roleDTO);
        return new JsonResponseBody();
    }

    /**
     * 删除角色。
     *
     * @param roleId 角色ID
     */
    @Override
    @Operation(
        summary = "删除角色",
        description = "拥有删除角色权限"
    )
    @RequestMapping(
        value = "/roles/{roleId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "角色ID") Long roleId
    ) {
        ContextDTO context = getContext();
        roleService.delete(context.getOperator().getId(), roleId);
        return new JsonResponseBody();
    }

    /**
     * 根据组织Id删除角色。
     *
     * @param orgId 组织 ID
     */
    @Override
    @Operation(
        summary = "根据组织ID删除角色",
        description = "删除组织ID拥有删除角色权限"
    )
    @RequestMapping(
        value = "/orgs-roles/{orgId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody deleteByOrgId(
        @PathVariable("orgId") @Parameter(description = "组织ID") Long orgId
    ){
        ContextDTO context = getContext();
        roleService.deleteByOrgId(context.getOperator().getId(), orgId);
        return new JsonResponseBody();
    }


    /**
     * 添加成员。
     */
    @Override
    @Operation(
        summary = "添加成员",
        description = "需要拥有操作角色权限"
    )
    @CheckPrivilege
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody addMember(
        @PathVariable @Parameter(description = "角色ID") Long roleId,
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @RequestBody @Parameter(description = "用户ID") AddRoleMemberDTO addRoleMemberDTO
    ) {
        ContextDTO contextDTO = getContext();
        roleService.addMember(contextDTO.getOperator().getId(), organizationId, roleId, addRoleMemberDTO.getMemberId());
        return new JsonResponseBody();
    }

    /**
     * 移除成员。
     */
    @Override
    @Operation(
        summary = "移除角色中的成员",
        description = "需要拥有操作角色的权限"
    )
    @CheckPrivilege
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members/{memberId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody removeMember(
        @PathVariable @Parameter(description = "角色ID") Long roleId,
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @PathVariable @Parameter(description = "用户ID") Long memberId
    ) {
        ContextDTO contextDTO = getContext();
        roleService.removeMember(contextDTO.getOperator().getId(), organizationId, roleId, memberId);
        return new JsonResponseBody();
    }

    /**
     * 获取成员列表。
     */
    @Override
    @Operation(
        summary = "获取成员列表",
        description = "需要拥有查看角色权限"
    )
    @CheckPrivilege
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> members(
        @Parameter(description = "过滤参数") QueryRoleMemberDTO queryRoleMemberDTO,
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @PathVariable @Parameter(description = "角色ID") Long roleId
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            userService.getRoleMembers(roleId, queryRoleMemberDTO)
        );
    }

    /**
     * 更新用户角色信息。
     *
     * @param projectOrgId   项目组织ID
     * @param memberId       用户ID
     * @param setUserRoleDTO 角色列表
     */
    @Override
    @Operation(
        summary = "更新用户角色"
    )
    @RequestMapping(
        value = "/orgs/{projectOrgId}/members/{memberId}/set-user-roles",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody setUserRole(
        @PathVariable @Parameter(description = "项目组织 ID") Long projectOrgId,
        @PathVariable @Parameter(description = "用户 ID") Long memberId,
        @RequestBody @Parameter(description = "角色列表") SetUserRoleDTO setUserRoleDTO
    ) {
        ContextDTO context = getContext();

        roleMemberService.setUserRoles(
            context.getOperator().getId(),
            projectOrgId,
            memberId,
            setUserRoleDTO.getRoleIds()
        );
        return new JsonResponseBody();
    }

    /**
     * 获取组织用户下的角色列表。
     *
     * @param organizationId 组织ID
     * @param memberId       用户ID
     * @return 角色列表
     */
    @Override
    @Operation(
        summary = "获取组织用户下的角色列表"
    )
    @GetMapping(
        value = "/orgs/{organizationId}/members/{memberId}/roles",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<Role> findOrgMemberRoles(
        @PathVariable @Parameter(description = "项目组织ID") Long organizationId,
        @PathVariable @Parameter(description = "用户ID") Long memberId
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            roleService.getOrgMemberRoles(
                organizationId,
                memberId
            )
        );
    }


    /**
     * 获取角色列表。
     *
     * @param organizationId 组织ID
     * @return 角色列表
     */
    @GetMapping(
        value = "/orgs/{organizationId}/role-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<Role> getRoleList(
        @PathVariable("organizationId") Long organizationId
    ){
        return new JsonListResponseBody<>(
            roleService.getRoleList(
                organizationId
            )
        );
    }
}
