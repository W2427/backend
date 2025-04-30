package com.ose.auth.domain.model.service;

import com.ose.auth.domain.model.repository.*;
import com.ose.auth.entity.*;
import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.*;

@Component
public class RoleMemberService implements RoleMemberInterface {

    private UserRoleRepository userRoleRepository;

    private UserProfileRepository userProfileRepository;

    private RoleRepository roleRepository;

    /**
     * 角色成员构造器。
     */
    public RoleMemberService(
        UserRoleRepository userRoleRepository,
        UserProfileRepository userProfileRepository,
        RoleRepository roleRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userProfileRepository = userProfileRepository;
        this.roleRepository = roleRepository;
    }

//    /**
//     * 获取成员列表。
//     * @param organizationId 组织ID
//     * @param roleId 角色ID
//     * @param queryRoleMemberDTO 过滤参数
//     * @return 成员列表
//     */
//    @Override
//    public Page<RoleMember> members(String organizationId, String roleId, QueryRoleMemberDTO queryRoleMemberDTO) {
//
//        if (!roleRepository.existsByIdAndOrganizationIdAndDeletedIsFalse(roleId, organizationId)) {
//            throw new NotFoundError("role is not found");
//        }
//
//        Page<RoleMember> members;
//        if (queryRoleMemberDTO.getKeyword() != null) {
//            members = roleMemberRepository
//                .findMemberByKeyword(
//                    roleId,
//                    "%" + queryRoleMemberDTO.getKeyword() + "%",
//                    queryRoleMemberDTO.toPageable()
//                );
//        } else {
//            members = roleMemberRepository.findByRoleId(roleId, queryRoleMemberDTO.toPageable());
//        }
//
//        if (members.getContent().size() == 0) {
//            return members;
//        }
//
//        // 获取用户ID列表
//        List<String> users = new ArrayList<>();
//        for (RoleMember member : members.getContent()) {
//            if (!users.contains(member.getUserId())) {
//                users.add(member.getUserId());
//            }
//        }
//        if (users.size() == 0) {
//            return members;
//        }
//
//        Map<String, List<String>> userMap = new HashMap<>();
//        List<UserOrganization> userOrganizations = userOrganizationRepository
//            .findByOrganizationIdAndUserIdInAndDeletedIsFalse(organizationId, users);
//
//        for (UserOrganization userOrganization : userOrganizations) {
//            if (userMap.get(userOrganization.getUserId()) == null) {
//                List<String> organizations = new ArrayList<>();
//                organizations.add(userOrganization.getOrganizationId());
//                userMap.put(userOrganization.getUserId(), organizations);
//            } else {
//                userMap.get(userOrganization.getUserId()).add(userOrganization.getOrganizationId());
//            }
//        }
//
//        for (RoleMember member : members.getContent()) {
//            if (userMap.containsKey(member.getUserId())) {
//                member.setOrganizations(userMap.get(member.getUserId()));
//            }
//        }
//
//        return members;
//    }

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

        UserOrganization userOrganization;
        UserRole userRole;
        List<Long> userIds = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();

        for (T entity : entities) {
            // 实体类型为用户组织关系
            if (entity instanceof UserOrganization) {
                userOrganization = (UserOrganization) entity;
                userIds.add(userOrganization.getUserId());
                if (userOrganization.getRoleIds() != null && userOrganization.getRoleIds().size() > 0) {
                    for (Long roleId : userOrganization.getRoleIds()) {
                        if (!roleIds.contains(roleId)) {
                            roleIds.add(roleId);
                        }
                    }
                }
                // 实体类型为用户角色关系
            } else if (entity instanceof UserRole) {
                userRole = (UserRole) entity;
                roleIds.add(userRole.getRoleId());
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
        List<Role> roles = new ArrayList<>();
        if (roleIds.size() != 0) {
            roles = roleRepository.findByIdInAndDeletedIsFalse(roleIds);
        }

        for (Role role : roles) {
            included.put(role.getId(), role);
        }

        return included;
    }

    /**
     * 更新用户角色。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @param roleIds        角色ID列表
     */
    @Override
    @Transactional
    public void setUserRoles(Long operatorId, Long organizationId, Long userId, List<Long> roleIds) {

        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        // 获取用户角色信息
        List<UserRole> userRoles = userRoleRepository.findOrgMembersRoles(organizationId, userIds);

        // 获取用户的角色ID列表
        List<Long> existsUserRoleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            existsUserRoleIds.add(userRole.getRoleId());
        }

        List<Long> removeRoleIds = new ArrayList<>();
        List<Long> addRoleIds = new ArrayList<>();

        // 获取待移除的角色
        for (UserRole userRole : userRoles) {
            if (!roleIds.contains(userRole.getRoleId())) {
                removeRoleIds.add(userRole.getId());
            }
        }

        // 获取待添加的角色列表
        for (Long roleId : roleIds) {
            if (!existsUserRoleIds.contains(roleId)) {
                addRoleIds.add(roleId);
            }
        }

        // 移除角色
        if (removeRoleIds.size() != 0) {
            userRoleRepository.removeUserRoles(operatorId, removeRoleIds);
        }

        // 添加角色
        // 获取已添加的角色信息
        List<Role> addedRoles = roleRepository.findByIdInAndDeletedIsFalse(addRoleIds);

        if (addedRoles.size() != 0) {

            // 待添加的用户角色列表
            List<UserRole> addUserRoles = new ArrayList<>();
            for (Role role : addedRoles) {
                UserRole userRole = new UserRole();
                Date now = new Date();
                userRole.setUserId(userId);
                userRole.setRoleId(role.getId());

                userRole.setCreatedAt(now);
                userRole.setCreatedBy(operatorId);
                userRole.setLastModifiedAt(now);
                userRole.setLastModifiedBy(operatorId);
                userRole.setStatus(EntityStatus.ACTIVE);

                addUserRoles.add(userRole);
            }

            userRoleRepository.saveAll(addUserRoles);
        }

    }
}
