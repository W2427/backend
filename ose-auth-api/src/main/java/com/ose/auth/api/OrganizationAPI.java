package com.ose.auth.api;

import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface OrganizationAPI extends OrganizationFeignAPI {

    /**
     * 获取部门树形结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/hierarchy",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<OrganizationBasic> hierarchy(
        @PathVariable("organizationId") Long organizationId,
        OrgHierarchyDTO orgHierarchyDTO
    );

    /**
     * 获取组织列表。
     *
     * @param type    组织类型
     * @param pageDTO 分页参数
     */
    @RequestMapping(
        method = GET,
        value = "/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Organization> search(
        @RequestParam("type") String type,
        PageDTO pageDTO
    );

    /**
     * 获取所有组织下成员列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/members",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> members(
        @PathVariable("organizationId") Long organizationId,
        QueryOrgMemberDTO queryOrgMemberDTO,
        PageDTO pageDTO
    );

    /**
     * 获取单个组织下成员列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/members/single",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserProfile> singleMembers(
        @PathVariable("organizationId") Long organizationId,
        QueryOrgMemberDTO queryOrgMemberDTO,
        PageDTO pageDTO
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



    @RequestMapping(
        method = GET,
        value = "/orgs/{projectId}/project-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<DCalendar> getProjectHour(
        @PathVariable("projectId") Long orgId
    );



    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/project-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateProjectHour(
        @PathVariable("orgId") Long orgId,
        @RequestBody UpdateProjectHourDTO updateProjectHourDTO
    );



    @RequestMapping(
        method = GET,
        value = "/orgs/project-hour-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DCalendar> getProjectHourList(
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/org-chart-data",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserOrgChartDTO> getOrgChartData(
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/org-chart-data/professional",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<UserOrgChartDTO> getProfessionalOrgChartData(
    );

    /**
     * 获取IDC部门树形结构及其人员。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/idc-hierarchy",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<OrganizationIDCDTO> idcHierarchy(
        @PathVariable("organizationId") Long organizationId
    );

}
