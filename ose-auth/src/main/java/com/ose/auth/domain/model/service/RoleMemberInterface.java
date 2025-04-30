package com.ose.auth.domain.model.service;

import com.ose.service.EntityInterface;

import java.util.List;

public interface RoleMemberInterface extends EntityInterface {

//    /**
//     * 获取角色下的成员列表。
//     * @param organizationId 组织ID
//     * @param roleId 角色ID
//     * @param queryRoleMemberDTO 过滤参数
//     * @return 成员列表
//     */
//    Page<RoleMember> members(String organizationId, String roleId, QueryRoleMemberDTO queryRoleMemberDTO);

    /**
     * 设置用户角色。
     *
     * @param operatorId   操作人ID
     * @param projectOrgId 项目组织ID
     * @param userId       用户ID
     * @param roleIds      角色ID列表
     */
    void setUserRoles(Long operatorId, Long projectOrgId, Long userId, List<Long> roleIds);
}
