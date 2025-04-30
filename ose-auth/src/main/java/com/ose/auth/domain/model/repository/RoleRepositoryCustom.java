package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.RoleCriteriaDTO;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleRepositoryCustom {

    Page<Role> search(Long organizationId, RoleCriteriaDTO roleCriteriaDTO, Pageable pageable);

    /**
     * 根据权限取得用户信息。
     *
     * @param orgId          组织 ID
     * @param teamPrivileges 组织-权限映射表
     * @return 用户信息列表
     */
    List<UserBasic> findUsersByPrivilege(Long orgId, Map<Long, Set<String>> teamPrivileges);

    /**
     * 根据组织和权限集合获取角色列表。
     *
     * @param orgId      组织ID
     * @param privileges 权限集合
     * @return 角色列表
     */
    List<Role> findByOrgAndPrivileges(Long orgId, Set<String> privileges);
}
