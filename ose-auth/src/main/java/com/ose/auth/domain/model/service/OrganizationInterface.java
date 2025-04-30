package com.ose.auth.domain.model.service;

import com.ose.auth.dto.*;
import com.ose.auth.entity.DCalendar;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.OrganizationBasic;
import com.ose.auth.entity.UserOrganization;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface OrganizationInterface extends EntityInterface {

    /**
     * 创建部门信息。
     *
     * @param operatorId      操作者 ID
     * @param organizationDTO 部门数据
     */
    Organization create(Long operatorId, OrganizationDTO organizationDTO);

    /**
     * 更新部门信息。
     *
     * @param operatorId      操作者ID
     * @param organizationId  部门ID
     * @param organizationDTO 部门数据
     */
    void update(Long operatorId, Long organizationId, OrganizationDTO organizationDTO);

    /**
     * 获取部门的层级结构。
     *
     * @param organizationId  组织ID
     * @param orgHierarchyDTO 查询参数
     * @return 层级列表
     */
    OrganizationBasic hierarchy(Long organizationId, OrgHierarchyDTO orgHierarchyDTO);

    /**
     * 获取工作组下的组织成员层级。
     *
     * @param orgId         组织ID
     * @param teamId        工作组ID
     * @param privilegesDTO 权限集合
     * @return 组织成员层级
     */
    List<Organization> getOrgsMembers(Long orgId, Long teamId, PrivilegesDTO privilegesDTO);

    /**
     * 获取组织详情。
     *
     * @param orgId    组织 ID
     * @param parentId 上级组织 ID
     * @return 组织详情
     */
    Organization details(Long orgId, Long parentId);

    /**
     * 删除部门。
     *
     * @param operatorId     操作人ID
     * @param organizationId 部门ID
     */
    void delete(Long operatorId, Long organizationId);

    /**
     * 添加成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param memberId       成员ID
     */
    void addMember(Long operatorId, Long organizationId, Long memberId);

    /**
     * 设置成员为主管。
     *
     * @param organizationId 组织ID
     * @param memberId       成员ID
     * @param organizationMemberSetVpDTO
     */
    void setVp(Long organizationId, Long memberId, OrganizationMemberSetVpDTO organizationMemberSetVpDTO);

    /**
     * 设置成员为IDC负责人。
     *
     * @param organizationId 组织ID
     * @param memberId       成员ID
     * @param organizationMemberSetIdcDTO
     */
    void setIdc(Long organizationId, Long memberId, OrganizationMemberSetIdcDTO organizationMemberSetIdcDTO);

    /**
     * 移除成员。
     *
     * @param operatorId     操作ID
     * @param organizationId 部门ID
     * @param memberId       成员ID
     */
    void removeMember(Long operatorId, Long organizationId, Long memberId);

    /**
     * 查询组织列表。
     *
     * @param type    组织类型
     * @param pageDTO 分页参数
     * @return 组织列表
     */
    Page<Organization> search(String type, PageDTO pageDTO);

    /**
     * 获取用户的顶层项目组织列表。
     *
     * @param userId 用户ID
     * @return 用户项目组织列表
     */
    List<Organization> getTopProjectOrgs(Long userId);

    /**
     * 根据组织 ID 集合取得组织信息列表。
     *
     * @param orgIDs 组织 ID 集合
     * @return 组织信息列表
     */
    List<OrganizationBasicDTO> getByEntityIDs(Set<Long> orgIDs);

    List<Organization> getOrgList(Long organizationId, String orgType);

    List<Organization> getRootOrgListByUserId(Long organizationId, String orgType);

    Organization save(Organization organization);

    Organization get(Long organizationId);

    UserOrganization get(Long orgId, Long userId);

    void deleteOrgByOrgPath(Long operatorId, String path);

    OrganizationBasicDTO getTeamByOrg(Long orgId, Long teamId);

    List<OrganizationBasicDTO> getDepartments(Long organizationId, String name);

    DCalendar getProjectHour(Long projectId);

    void updateProjectHour(Long projectId, UpdateProjectHourDTO updateProjectHourDTO);

    List<DCalendar> getProjectHourList();

    UserOrgChartDTO getOrgChartData();

    UserOrgChartDTO getProfessionalOrgChartData();

    List<OrganizationIDCDetailDTO> getHierarchyInfo(
        Long orgId
    );
}
