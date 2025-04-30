package com.ose.auth.api;

import com.ose.auth.dto.AddRoleMemberDTO;
import com.ose.auth.dto.QueryRoleMemberDTO;
import com.ose.auth.dto.RoleDTO;
import com.ose.auth.dto.SetUserRoleDTO;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserProfile;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 角色信息接口
 */
@FeignClient(name = "ose-auth", contextId = "roleFeign")
public interface RoleFeignAPI {

    /**
     * 创建角色。
     *
     * @param roleDTO 创建信息
     * @return 角色信息
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Role> create(
        @PathVariable("organizationId") Long organizationId,
        @RequestBody RoleDTO roleDTO
    );

    /**
     * 创建全局角色。
     *
     * @param roleDTO 创建信息
     * @return 角色信息
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/system",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Role> createSystem(
        @PathVariable("organizationId") Long organizationId,
        @RequestBody RoleDTO roleDTO
    );

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
    JsonObjectResponseBody<Role> createRole(
        @RequestBody Role role
    );

    /**
     * 获取角色详情。
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Role> get(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("roleId") Long roleId
    );

    /**
     * 更新角色信息。
     *
     * @param roleId  角色ID
     * @param roleDTO 更新信息
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}",
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("roleId") Long roleId,
        @RequestBody RoleDTO roleDTO
    );

    /**
     * 删除角色。
     *
     * @param roleId 角色ID
     */
    @RequestMapping(
        value = "/roles/{roleId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("roleId") Long roleId
    );

    /**
     * 根据组织Id删除角色。
     *
     * @param orgId 组织 ID
     */
    @RequestMapping(
        value = "/orgs-roles/{orgId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteByOrgId(
        @PathVariable("orgId") Long orgId
    );

    /**
     * 添加成员。
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addMember(
        @PathVariable("roleId") Long roleId,
        @PathVariable("organizationId") Long organizationId,
        @RequestBody AddRoleMemberDTO addRoleMemberDTO
    );

    /**
     * 移除成员。
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members/{memberId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody removeMember(
        @PathVariable("roleId") Long roleId,
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId
    );

    /**
     * 获取成员列表。
     */
    @RequestMapping(
        value = "/orgs/{organizationId}/roles/{roleId}/members",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> members(
        @Parameter(description = "queryRoleMemberDTO") QueryRoleMemberDTO roleMemberDTO,
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("roleId") Long roleId
    );

    /**
     * 更新用户角色信息。
     *
     * @param projectOrgId   项目组织ID
     * @param memberId       用户ID
     * @param setUserRoleDTO 角色列表
     */
    @RequestMapping(
        value = "/orgs/{projectOrgId}/members/{memberId}/set-user-roles",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setUserRole(
        @PathVariable("projectOrgId") Long projectOrgId,
        @PathVariable("memberId") Long memberId,
        SetUserRoleDTO setUserRoleDTO
    );

    /**
     * 获取组织用户下的角色列表。
     *
     * @param organizationId 组织ID
     * @param memberId       用户ID
     * @return 角色列表
     */
    @GetMapping(
        value = "/orgs/{organizationId}/members/{memberId}/roles",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Role> findOrgMemberRoles(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId
    );

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
    JsonListResponseBody<Role> getRoleList(
        @PathVariable("organizationId") Long organizationId
    );
}
