package com.ose.auth.domain.model.service;

import com.ose.auth.dto.RoleCriteriaDTO;
import com.ose.auth.dto.RoleDTO;
import com.ose.auth.entity.Role;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleInterface extends EntityInterface {

    /**
     * 创建角色。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleDTO        角色信息
     * @return 角色信息
     */
    Role create(Long operatorId, Long organizationId, RoleDTO roleDTO);

    /**
     * 创建system角色。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleDTO        角色信息
     * @return 角色信息
     */
    Role createSystem(Long operatorId, Long organizationId, RoleDTO roleDTO);

    /**
     * 获取角色列表。
     *
     * @param organizationId  组织ID
     * @param roleCriteriaDTO 角色查询参数
     * @param page            分页参数
     * @return 角色列表
     */
    Page<Role> search(Long organizationId, RoleCriteriaDTO roleCriteriaDTO, PageDTO page);

    /**
     * 更新角色信息。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleId         角色ID
     * @param roleDTO        更新信息
     */
    void update(Long operatorId, Long organizationId, Long roleId, RoleDTO roleDTO);

    /**
     * 获取角色详情。
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    Role get(Long roleId);

    /**
     * 删除角色。
     *
     * @param operatorId 操作人ID
     * @param roleId     角色ID
     */
    void delete(Long operatorId, Long roleId);

    /**
     * 移除成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleId         角色ID
     * @param memberId       成员列表
     */
    void removeMember(Long operatorId, Long organizationId, Long roleId, Long memberId);

    /**
     * 添加成员。
     *
     * @param operatorId     操作人ID
     * @param organizationId 组织ID
     * @param roleId         角色ID
     * @param memberId       成员列表
     */
    void addMember(Long operatorId, Long organizationId, Long roleId, Long memberId);

    /**
     * 获取组织成员的角色列表。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 角色列表
     */
    List<Role> getOrgMemberRoles(Long organizationId, Long userId);

    List<Role> getRoleList(Long organizationId);

    Role createRole(Role role);

    void deleteByOrgId(Long operatorId, Long orgId);
}
