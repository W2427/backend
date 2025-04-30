package com.ose.auth.controller;

import com.ose.auth.annotation.CheckPrivilege;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.OrganizationAPI;
import com.ose.auth.domain.model.service.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.auth.vo.OrgType;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.CollectionUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Tag(name = "部门接口")
public class OrganizationController extends BaseController implements OrganizationAPI {

    // 用户操作服务
    private final UserInterface userService;

    // 部门业务服务
    private final OrganizationInterface organizationService;
    private final OrgMemberInterface orgMemberService;
    private final RoleInterface roleService;
    private final UserPositionInterface userPositionService;

    /**
     * 构造器。
     *
     * @param userService         用户操作服务
     * @param organizationService 部门业务服务
     */
    @Autowired
    public OrganizationController(
        UserInterface userService,
        OrganizationInterface organizationService,
        OrgMemberInterface orgMemberService,
        RoleInterface roleService, UserPositionInterface userPositionService
    ) {
        this.userService = userService;
        this.organizationService = organizationService;
        this.orgMemberService = orgMemberService;
        this.roleService = roleService;
        this.userPositionService = userPositionService;
    }

    /**
     * 创建组织。
     * @param organizationDTO 组织数据
     * @return 组织信息
     */
    @Override
    @Operation(
        summary = "创建部门",
        description = "需要职员用有创建部门的权限"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    @CheckPrivilege
    public JsonObjectResponseBody<Organization> create(
        @RequestBody @Parameter(description = "部门信息") OrganizationDTO organizationDTO
    ) {
        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            organizationService.create(
                context.getOperator().getId(),
                organizationDTO
            )
        );
    }

    /**
     * 更新部门信息。
     *
     * @param organizationId  部门ID
     * @param organizationDTO 部门数据
     */
    @Override
    @Operation(
        summary = "更新部门",
        description = "需要拥有更新部门的权限"
    )
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{organizationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "部门ID") Long organizationId,
        @RequestBody @Parameter(description = "部门待更新数据") OrganizationDTO organizationDTO
    ) {
        ContextDTO context = getContext();

        organizationService.update(
            context.getOperator().getId(),
            organizationId,
            organizationDTO
        );
        return new JsonResponseBody();
    }

    /**
     * 获取部门树形结构。
     */
    @Override
    @Operation(
        summary = "获取部门的层级列表",
        description = "需要拥有查看部门权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = GET,
        produces = APPLICATION_JSON_VALUE,
        value = "/orgs/{organizationId}/hierarchy"
    )
    public JsonObjectResponseBody<OrganizationBasic> hierarchy(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        OrgHierarchyDTO orgHierarchyDTO
    ) {

        ContextDTO context = getContext();
        return new JsonObjectResponseBody<>(
            context,
            organizationService
                .hierarchy(
                    organizationId,
                    orgHierarchyDTO
                )
        );
    }

    /**
     * 根据工作组和权限获取组织成员信息列表。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 带成员的组织列表
     */
    @Override
    @Operation(summary = "获取工作组下的组织成员列表")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/teams/{teamId}/orgs-members",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<Organization> getOrgMembersByPrivileges(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工作组ID") Long teamId,
        @RequestBody PrivilegesDTO privilegesDTO
    ) {
        ContextDTO context = getContext();
        if(!CollectionUtils.isEmpty(privilegesDTO.getPrivileges()) && privilegesDTO.getPrivileges().contains("NDT_EXECUTE")) {
            return new JsonListResponseBody<>(
                context,
                organizationService.getOrgsMembers(
                    orgId,
                    orgId,
                    privilegesDTO
                )
            );
        }


        return new JsonListResponseBody<>(
            context,
            organizationService.getOrgsMembers(
                orgId,
                teamId,
                privilegesDTO
            )
        );
    }

    /**
     * 根据工作组和权限获取组织成员信息列表。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 带成员的组织列表
     */
    @Override
    @Operation(summary = "获取工作组下的组织成员列表")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/teams/{teamId}/orgs-members/batch",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<Organization> getBatchOrgMembersByPrivileges(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工作组ID") Long teamId,
        PrivilegesDTO privilegesDTO
    ) {
        ContextDTO context = getContext();
        if(!CollectionUtils.isEmpty(privilegesDTO.getPrivileges()) && privilegesDTO.getPrivileges().contains("NDT_EXECUTE")) {
            return new JsonListResponseBody<>(
                context,
                organizationService.getOrgsMembers(
                    orgId,
                    orgId,
                    privilegesDTO
                )
            );
        }


        return new JsonListResponseBody<>(
            context,
            organizationService.getOrgsMembers(
                orgId,
                teamId,
                privilegesDTO
            )
        );
    }

    /**
     * 获取部门详情。
     */
    @Override
    @Operation(
        summary = "获取组织详情",
        description = "需要拥有查看部门权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<Organization> details(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @RequestParam(required = false) @Parameter(description = "上级组织 ID") Long parentId
    ) {
        ContextDTO context = getContext();
        return new JsonObjectResponseBody<>(
            context,
            organizationService.details(orgId, parentId)
        ).setIncluded(userService);
    }

    /**
     * 删除部门。
     */
    @Override
    @Operation(
        summary = "删除部门",
        description = "需要拥有删除部门权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{organizationId}",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "部门ID") Long organizationId
    ) {
        ContextDTO context = getContext();

        organizationService.delete(
            context.getOperator().getId(),
            organizationId
        );

        return new JsonResponseBody();
    }

    /**
     * 根据组织 PATH 删除部门。
     */
    @Override
    @Operation(
        summary = "根据组织 PATH 删除部门",
        description = "需要拥有删除部门权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/orgs-path/{path}",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody deleteOrgByOrgPath(
        @PathVariable("path") String path
    ){
        ContextDTO context = getContext();

        organizationService.deleteOrgByOrgPath(
            context.getOperator().getId(),
            path
        );

        return new JsonResponseBody();
    }

    /**
     * 添加成员。
     */
    @Override
    @Operation(
        summary = "部门添加成员",
        description = "需要有操作部门的权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody addMembers(
        @PathVariable @Parameter(description = "部门ID") Long organizationId,
        @RequestBody @Parameter(description = "成员ID列表") AddOrganizationMemberDTO addOrganizationMemberDTO
    ) {
        ContextDTO contextDTO = getContext();
        organizationService.addMember(
            contextDTO.getOperator().getId(),
            organizationId,
            addOrganizationMemberDTO.getMemberId()
        );

        return new JsonResponseBody();
    }

    /**
     * 添加成员。
     */
    @Override
    @Operation(
        summary = "部门设置成员为主管",
        description = "需要有操作部门的权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members/{memberId}/set-vp",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody setVp(
        @PathVariable @Parameter(description = "部门ID") Long organizationId,
        @PathVariable("memberId") Long memberId,
        @RequestBody @Parameter(description = "成员ID列表") OrganizationMemberSetVpDTO organizationMemberSetVpDTO
    ) {
        organizationService.setVp(
            organizationId,
            memberId,
            organizationMemberSetVpDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 添加成员。
     */
    @Override
    @Operation(
        summary = "部门设置成员为IDC负责人",
        description = "需要有操作部门的权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = POST,
        value = "/orgs/{organizationId}/members/{memberId}/set-idc",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody setIdc(
        @PathVariable @Parameter(description = "部门ID") Long organizationId,
        @PathVariable("memberId") Long memberId,
        @RequestBody @Parameter(description = "成员ID列表") OrganizationMemberSetIdcDTO organizationMemberSetIdcDTO
    ) {
        organizationService.setIdc(
            organizationId,
            memberId,
            organizationMemberSetIdcDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 移除成员。
     */
    @Override
    @Operation(
        summary = "部门移除成员",
        description = "需要有操作部门的权限"
    )
    @CheckPrivilege
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{organizationId}/members/{memberId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody removeMembers(
        @PathVariable @Parameter(description = "部门ID") Long organizationId,
        @PathVariable @Parameter(description = "成员ID") Long memberId
    ) {
        ContextDTO contextDTO = getContext();
        organizationService.removeMember(
            contextDTO.getOperator().getId(),
            organizationId,
            memberId
        );

        return new JsonResponseBody();
    }

//    /**
//     * 获取成员列表。
//     */
//    @Override
//    @Operation(
//        summary = "获取成员列表",
//        description = "需要拥有查看组织权限"
//    )
//    @CheckPrivilege
//    @RequestMapping(
//        value = "/{organizationId}/members",
//        method = GET,
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
//    )
//    public JsonListResponseBody<UserOrganization> members(
//        @Parameter(description = "查询参数") QueryOrgMemberDTO queryOrgMemberDTO,
//        @PathVariable @Parameter(description = "组织ID") String organizationId
//    ) {
//        ContextDTO context = getContext();
//        return new JsonListResponseBody<>(
//            context,
//            orgMemberService.members(organizationId, queryOrgMemberDTO)
//        )
//            .setIncluded(roleMemberService)
//            .setIncluded(orgMemberService);
//    }

    /**
     * 获取用户的组织部门。
     * @param organizationId 组织ID
     * @param memberId       用户ID
     */
    @Override
    @Operation(
        summary = "获取用户组织部门"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/members/{memberId}/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<UserOrganization> getDepartments(
        @PathVariable @Parameter(description = "组织 ID") Long organizationId,
        @PathVariable @Parameter(description = "用户 ID") Long memberId,
        @RequestParam @Parameter(description = "组织类型") String type
    ) {
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            orgMemberService.getDepartments(organizationId, memberId, OrgType.valueOf(type))
        ).setIncluded(organizationService);
    }

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
    public JsonListResponseBody<OrganizationBasicDTO> getDepartments(
        @PathVariable("organizationId") Long organizationId,
        @PathVariable("name") String name
    ){
        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            organizationService.getDepartments(organizationId, name)
        );
    }

    /**
     * 设置用户组织。
     * @param projectOrgId 项目组织ID
     * @param memberId 用户ID
     * @param setUserOrgsDTO 组织列表
     */
//    @Override
//    @Operation(
//        value = "设置用户组织"
//    )
//    @RequestMapping(
//        method = POST,
//        value = "/{projectOrgId}/members/{memberId}/set-user-orgs",
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
//    )
//    @CheckPrivilege
//    public JsonResponseBody setUserOrgs(
//        @PathVariable @Parameter(description = "项目组织ID") String projectOrgId,
//        @PathVariable @Parameter(description = "用户ID") String memberId,
//        @RequestBody @Parameter(description = "组织列表") SetUserOrgsDTO setUserOrgsDTO
//    ) {
//        ContextDTO context = getContext();
//
//        orgMemberService.setUserOrgs(
//            context.getOperator().getId(),
//            projectOrgId,
//            memberId,
//            setUserOrgsDTO.getOrgs()
//        );
//        return new JsonResponseBody();
//    }

    /**
     * 获取组织列表。
     *
     * @param type    组织类型
     * @param pageDTO 分页参数
     */
    @Override
    @Operation(
        summary = "获取组织列表"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @CheckPrivilege
    public JsonListResponseBody<Organization> search(
        @RequestParam @Parameter(description = "组织类型") String type,
        PageDTO pageDTO
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            organizationService.search(type, pageDTO)
        );
    }

    /**
     * 获取所有组织下的成员列表。
     */
    @Override
    @Operation(
        summary = "获取所有组织下的成员列表",
        description = "需要拥有查看组织权限"
    )
    @CheckPrivilege
    @RequestMapping(
        value = "/orgs/{organizationId}/members",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> members(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @Parameter(description = "查询参数") QueryOrgMemberDTO queryOrgMemberDTO,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            userService.getOrgMembers(organizationId, queryOrgMemberDTO, pageDTO)
        ).setIncluded(roleService);
    }

    /**
     * 获取某个组织下成员列表。
     */
    @Override
    @Operation(
        summary = "获取某个组织下成员列表",
        description = "需要拥有查看组织权限"
    )
    @CheckPrivilege
    @RequestMapping(
        value = "/orgs/{organizationId}/members/single",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<UserProfile> singleMembers(
        @PathVariable @Parameter(description = "组织ID") Long organizationId,
        @Parameter(description = "查询参数") QueryOrgMemberDTO queryOrgMemberDTO,
        @Parameter(description = "分页参数") PageDTO pageDTO
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            userService.getSingleOrgMembers(organizationId, queryOrgMemberDTO, pageDTO)
        ).setIncluded(roleService).setIncluded(userPositionService);
    }

    @Override
    @Operation(description = "批量取得组织信息（仅供其他服务调用）")
    @RequestMapping(
        method = POST,
        value = "/orgs/batch-get",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<OrganizationBasicDTO> batchGet(
        @RequestBody BatchGetDTO batchGetDTO
    ) {
        return new JsonListResponseBody<>(
            organizationService.getByEntityIDs(batchGetDTO.getEntityIDs())
        );
    }

    /**
     * 获取部门 扁平 结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{organizationId}/flat-list",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<Organization> getOrgList(
        @PathVariable("organizationId") Long organizationId,
        @RequestParam("orgType") String orgType
    ) {
        return new JsonListResponseBody<>(
            organizationService.getOrgList(organizationId, orgType)
        );
    }

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
    public JsonObjectResponseBody<Organization> createOrg(
        @RequestBody Organization organization
    ){
        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            organizationService.save(
                organization
            )
        );

    }

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
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<Organization> get(
        @PathVariable("organizationId") Long organizationId
    ){
        return new JsonObjectResponseBody<>(
            organizationService.get(
                organizationId
            )
        );
    }

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
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<UserOrganization> getOrgListByMember(
        @PathVariable("orgId") Long orgId,
        @PathVariable("userId") Long userId){
        return new JsonObjectResponseBody<>(
            organizationService.get(
                orgId, userId
            )
        );
    }



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
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<OrganizationBasicDTO> getTeamByOrgId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("teamId") Long teamId){
        return new JsonObjectResponseBody<>(
            organizationService.getTeamByOrg(
                orgId, teamId
            )
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{projectId}/project-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<DCalendar> getProjectHour(
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonObjectResponseBody<>(
            organizationService.getProjectHour(projectId)
        );
    }

    @RequestMapping(
        method = PATCH,
        value = "/orgs/{projectId}/project-hour",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonResponseBody updateProjectHour(
        @PathVariable("projectId") Long projectId,
        @RequestBody UpdateProjectHourDTO updateProjectHourDTO
    ) {
        ContextDTO context = getContext();

        organizationService.updateProjectHour(projectId,updateProjectHourDTO);

        return new JsonResponseBody();
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/project-hour-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonListResponseBody<DCalendar> getProjectHourList(
    ) {
        return new JsonListResponseBody<>(getContext(),
            organizationService.getProjectHourList()
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/org-chart-data",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<UserOrgChartDTO> getOrgChartData(
    ) {
        return new JsonObjectResponseBody<>(
            organizationService.getOrgChartData()
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/org-chart-data/professional",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    public JsonObjectResponseBody<UserOrgChartDTO> getProfessionalOrgChartData(
    ) {
        return new JsonObjectResponseBody<>(
            organizationService.getProfessionalOrgChartData()
        );
    }

    /**
     * 获取部门树形结构。
     */
    @Override
    @Operation(
        summary = "获取IDC部门树形结构及其人员",
        description = "获取IDC部门树形结构及其人员"
    )
    @CheckPrivilege
    @RequestMapping(
        method = GET,
        produces = APPLICATION_JSON_VALUE,
        value = "/orgs/{organizationId}/idc-hierarchy"
    )
    public JsonObjectResponseBody<OrganizationIDCDTO> idcHierarchy(
        @PathVariable @Parameter(description = "组织ID") Long organizationId
    ) {

        OrganizationIDCDTO responseData = new OrganizationIDCDTO();

        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = contextDTO.getOperator();

        List<OrganizationIDCDetailDTO> entries;
        entries = organizationService
            .getHierarchyInfo(organizationId);

        responseData.setChildren(entries);
        JsonObjectResponseBody<OrganizationIDCDTO> result = new JsonObjectResponseBody<>(responseData);

        return result;
    }

}
