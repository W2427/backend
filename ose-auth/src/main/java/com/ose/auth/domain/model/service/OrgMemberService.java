package com.ose.auth.domain.model.service;

import com.ose.auth.domain.model.repository.*;
import com.ose.auth.dto.QueryOrgMemberDTO;
import com.ose.auth.entity.*;
import com.ose.dto.BaseDTO;
import com.ose.exception.NotFoundError;
import com.ose.auth.vo.OrgType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.*;

@Component
public class OrgMemberService implements OrgMemberInterface {

    private UserOrganizationRepository userOrganizationRepository;
    private UserProfileRepository userProfileRepository;
    private OrganizationBasicRepository organizationBasicRepository;
    private UserRoleRepository userRoleRepository;
    private OrganizationRepository organizationRepository;

    /**
     * 构造器。
     *
     * @param userOrganizationRepository 用户组织数据仓库
     */
    public OrgMemberService(
        UserOrganizationRepository userOrganizationRepository,
        UserProfileRepository userProfileRepository,
        OrganizationBasicRepository organizationBasicRepository,
        UserRoleRepository userRoleRepository,
        OrganizationRepository organizationRepository
    ) {
        this.userOrganizationRepository = userOrganizationRepository;
        this.userProfileRepository = userProfileRepository;
        this.organizationBasicRepository = organizationBasicRepository;
        this.userRoleRepository = userRoleRepository;
        this.organizationRepository = organizationRepository;
    }

    /**
     * 获取组织成员列表。
     *
     * @param organizationId    组织ID
     * @param queryOrgMemberDTO 查询参数
     * @return 组织成员列表
     */
    @Override
    public Page<UserOrganization> members(Long organizationId, QueryOrgMemberDTO queryOrgMemberDTO) {

        Organization organization = organizationRepository.findByIdAndDeletedIsFalse(organizationId);
        if (organization == null) {
            throw new NotFoundError("organization is not found");
        }

        // 根据组织ID列表 获取组织成员列表
        Page<UserOrganization> members;
        if (queryOrgMemberDTO.getKeyword() == null) {
            members = userOrganizationRepository
                .findMembers(organizationId, queryOrgMemberDTO.toPageable());
        } else {
            members = userOrganizationRepository
                .findMembersByKeyword(organizationId, queryOrgMemberDTO.getKeyword(), queryOrgMemberDTO.toPageable());
        }

        if (members.getContent().size() == 0) {
            return members;
        }

        // 获取用户ID列表
        List<Long> userIds = new ArrayList<>();
        for (UserOrganization member : members.getContent()) {
            if (!userIds.contains(member.getUserId())) {
                userIds.add(member.getUserId());
            }
        }

        if (organization.getType().equals(OrgType.COMPANY)) {
            return members;
        }

        // 获取用户角色信息
        List<UserRole> userRoles = userRoleRepository.findOrgMembersRoles(organizationId, userIds);

        Map<Long, List<Long>> userRoleMap = new HashMap<>();
        for (UserRole userRole : userRoles) {
            if (userRoleMap.get(userRole.getUserId()) == null) {
                List<Long> roleIds = new ArrayList<>();
                roleIds.add(userRole.getRoleId());
                userRoleMap.put(userRole.getUserId(), roleIds);
            } else {
                userRoleMap.get(userRole.getUserId()).add(userRole.getRoleId());
            }
        }

        for (UserOrganization member : members.getContent()) {
            if (userRoleMap.get(member.getUserId()) != null) {
                member.setRoleIds(userRoleMap.get(member.getUserId()));
            } else {
                member.setRoleIds(new ArrayList<>());
            }
        }

        return members;
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(Map<Long, Object> included, List<T> entities) {

        if (entities.size() == 0) {
            return included;
        }

        UserRole userRole;
        UserOrganization userOrganization;
        List<Long> userIds = new ArrayList<>();
        List<Long> orgIds = new ArrayList<>();

        for (T entity : entities) {

            if (entity instanceof UserRole) {
                userRole = (UserRole) entity;
                userIds.add(userRole.getUserId());
                if (userRole.getOrgIds() != null && userRole.getOrgIds().size() > 0) {
                    for (Long orgId : userRole.getOrgIds()) {
                        if (!orgIds.contains(orgId)) {
                            orgIds.add(orgId);
                        }
                    }
                }
            } else if (entity instanceof UserOrganization) {

                userOrganization = (UserOrganization) entity;
                if (userOrganization.getOrganizationId() != null
                    && !orgIds.contains(userOrganization.getOrganizationId())) {
                    orgIds.add(userOrganization.getOrganizationId());
                }
            }
        }

        // 获取用户信息
        List<UserProfile> users = new ArrayList<>();
        if (userIds.size() != 0) {
            users = (List<UserProfile>) userProfileRepository.findAllById(userIds);
        }

        for (UserProfile user : users) {
            included.put(user.getId(), user);
        }

        // 获取组织信息
        List<OrganizationBasic> organizations = new ArrayList<>();
        if (orgIds.size() != 0) {
            organizations = organizationBasicRepository.findByIdInAndDeletedIsFalse(orgIds);
        }

        for (OrganizationBasic org : organizations) {
            included.put(org.getId(), org);
        }

        return included;
    }

    /**
     * 获取项目组织用户的全部部门。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 部门列表
     */
    @Override
    public List<UserOrganization> getDepartments(Long organizationId, Long userId, OrgType type) {

        return userOrganizationRepository.findMemberOrgs("%" + organizationId + "%", userId, type);
    }

    /**
     * 用户切换项目组织。
     *
     * @param userId         用户ID
     * @param organizationId 项目组织ID
     */
    @Transactional
    public void switchProjectOrg(Long userId, Long organizationId) {

        // 取消用户默认项目组织
        userOrganizationRepository.cancelDefaultUserProjectOrg(userId);
        // 设置用户默认项目组织
        userOrganizationRepository.switchUserProjectOrg(userId, organizationId);
    }

    /**
     * 更新用户的组织。
     * @param operatorId 操作人ID
     * @param organizationId 项目组织ID
     * @param userId 用户ID
     * @param orgs 组织ID列表
     */
//    // TODO 退出组织1 -> 加入组织2 可以拆分开 在controller中组合
//    @Override
//    @Transactional
//    public void setUserOrgs(String operatorId, String organizationId, String userId, List<String> orgs) {

//        // 获取用户项目组织下的组织列表
//        List<UserOrganization> userOrganizations = userOrganizationRepository
//            .findByOrgPathLikeAndUserIdAndDeletedIsFalse("%" + organizationId + "%", userId);
//
//        // 检测组织参数的有效性
//        List<Organization> organizations = organizationRepository
//            .findByProjectOrgIdAndIdInAndDeletedIsFalse(organizationId, orgs);
//
//        List<String> orgIds = new ArrayList<>();
//
//        for (Organization organization : organizations) {
//            orgIds.add(organization.getId());
//        }
//
//        // 已加入的组织列表
//        List<String> existsOrgs = new ArrayList<>();
//        for (UserOrganization userOrganization : userOrganizations) {
//            existsOrgs.add(userOrganization.getId());
//        }
//
//        // 待加入的组织列表
//        List<String> removedOrgs = new ArrayList<>();
//        // 待移除的组织列表
//        List<String> addedOrgs = new ArrayList<>();
//
//        // 获取待加入的组织列表
//        for (String org : orgIds) {
//            if (!existsOrgs.contains(org) && !addedOrgs.contains(org)) {
//                addedOrgs.add(org);
//            }
//        }
//
//        // 获取待移除的组织列表
//        for (UserOrganization userOrganization : userOrganizations) {
//            if (!orgs.contains(userOrganization.getOrganizationId())
//                && !removedOrgs.contains(userOrganization.getProjectOrgId())) {
//                removedOrgs.add(userOrganization.getId());
//            }
//        }
//
//        // 移除用户组织
//        if (removedOrgs.size() != 0) {
//            userOrganizationRepository.removeUserOrgs(operatorId, removedOrgs);
//        }
//
//        // 添加用户组织
//        if (addedOrgs.size() != 0) {
//
//            List<UserOrganization> userOrgs = new ArrayList<>();
//
//            for (String addedOrg : addedOrgs) {
//
//                Date now = new Date();
//                UserOrganization userOrganization = new UserOrganization();
//
//                userOrganization.setProjectOrgId(projectOrgId);
//                userOrganization.setUserId(userId);
//                userOrganization.setOrganizationId(addedOrg);
//                userOrganization.setOrganizationType(OrgType.PROJECT_DEPARTMENT);
//
//                userOrganization.setCreatedBy(operatorId);
//                userOrganization.setCreatedAt(now);
//                userOrganization.setLastModifiedBy(operatorId);
//                userOrganization.setLastModifiedAt(now);
//                userOrganization.setStatus(EntityStatus.ACTIVE);
//
//                userOrgs.add(userOrganization);
//            }
//
//            userOrganizationRepository.saveAll(userOrgs);
//        }
//    }
}
