package com.ose.auth.api;

import com.ose.auth.dto.*;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserOrganization;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 部门信息接口。
 */
@FeignClient(name = "ose-auth", contextId = "organizationFeign")
public interface OrganizationFeignAPI {

    /**
     * 创建部门信息。
     *
     * @param organizationDTO 部门数据
     * @return 部门信息
     */
    @RequestMapping(
        method = POST,
        value = "/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Organization> create(
        @RequestBody OrganizationDTO organizationDTO
    );

    /**
     * 获取部门树形结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/flat-list",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getOrgList(
        @PathVariable("organizationId") Long organizationId,
        @RequestParam("orgType") String orgType
    );

    /**
     * 更新部门信息。
     *
     * @param organizationId  部门ID
     * @param organizationDTO 部门数据
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{organizationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("organizationId") Long organizationId,
        @RequestBody OrganizationDTO organizationDTO
    );

    /**
     * 获取部门详情。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Organization> details(
        @PathVariable("orgId") Long orgId,
        @RequestParam("parentId") Long parentId
    );

    /**
     * 删除部门。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{organizationId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("organizationId") Long organizationId
    );


    /**
     * 根据组织 PATH 删除部门。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs-path/{path}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteOrgByOrgPath(
        @PathVariable("path") String path
    );

    /**
     * 添加成员。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addMembers(
        @PathVariable("organizationId") Long organizationId,
        @RequestBody AddOrganizationMemberDTO addOrganizationMemberDTO
    );

    /**
     * 设置成员为主管。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members/{memberId}/set-vp",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setVp(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId,
        @RequestBody OrganizationMemberSetVpDTO organizationMemberSetVpDTO
    );

    /**
     * 设置成员为IDC负责人。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members/{memberId}/set-idc",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setIdc(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId,
        @RequestBody OrganizationMemberSetIdcDTO organizationMemberSetIdcDTO
    );

    /**
     * 移除成员。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{organizationId}/members/{memberId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody removeMembers(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId
    );

//    /**
//     * 获取成员列表。
//     */
//    @RequestMapping(
//        value = "/{organizationId}/members",
//        method = GET,
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
//    )
//    JsonListResponseBody<UserOrganization> members(
//        QueryOrgMemberDTO queryOrgMemberDTO,
//        @PathVariable("organizationId") String organizationId
//    );

    /**
     * 获取项目组织下的用户的组织列表。
     *
     * @param organizationId 项目组织ID
     * @param memberId       用户ID
     * @param type           组织类型
     * @return 部门列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/members/{memberId}/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserOrganization> getDepartments(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("memberId") Long memberId,
        @RequestParam("type") String type
    );

    /**
     * 获取项目组织下的用户的组织列表。
     *
     * @param organizationId 项目组织ID
     * @param name       部门名称
     * @return 部门列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/department-name/{name}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<OrganizationBasicDTO> getDepartments(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("name") String name
    );

    /**
     * 批量获取组织信息。
     *
     * @return 组织列表
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/batch-get",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<OrganizationBasicDTO> batchGet(
        @RequestBody BatchGetDTO batchGetDTO
    );

    /**
     * 根据工作组和权限获取组织成员信息列表。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 带成员的组织列表
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/teams/{teamId}/orgs-members",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getOrgMembersByPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("teamId") Long teamId,
        @RequestBody PrivilegesDTO privilegesDTO
    );

    /**
     * 根据工作组和权限获取组织成员信息列表。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 带成员的组织列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/teams/{teamId}/orgs-members-batch",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> getBatchOrgMembersByPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("teamId") Long teamId,
        PrivilegesDTO privilegesDTO
    );


    /**
     * 创建部门信息,扁平信息。
     *
     * @param organization 部门数据
     * @return 部门信息
     */
    @RequestMapping(
        method = POST,
        value = "/flat-org",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Organization> createOrg(
        @RequestBody Organization organization
    );

    /**
     * 获得 组织信息。
     *
     * @param organizationId 部门数据Id
     * @return 部门信息
     */
    @RequestMapping(
        method = GET,
        value = "/flat-org/{organizationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Organization> get(
        @PathVariable("organizationId") Long organizationId
    );

    /**
     * 用户是否存在于组织中
     * @param orgId
     * @param userId
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/user/{userId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserOrganization> getOrgListByMember(
        @PathVariable("orgId") Long orgId,
        @PathVariable("userId") Long userId);

    /**
     * 取得组织下的工作组Id
     * @param orgId
     * @param teamId
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/team/{teamId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<OrganizationBasicDTO> getTeamByOrgId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("teamId") Long teamId);


}
