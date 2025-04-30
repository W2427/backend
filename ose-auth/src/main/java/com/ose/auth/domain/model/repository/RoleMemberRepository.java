package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.RoleMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface RoleMemberRepository extends CrudRepository<RoleMember, Long> {

    /**
     * 获取角色成员列表。
     *
     * @param roleId   角色ID
     * @param pageable 分页参数
     * @return 成员列表
     */
    Page<RoleMember> findByRoleId(Long roleId, Pageable pageable);

    /**
     * 根据关键字查询成员列表。
     *
     * @param roleId   角色ID
     * @param keyword  关键字
     * @param pageable 分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT rm FROM RoleMember rm WHERE rm.roleId = ?1 AND (rm.name LIKE ?2 OR rm.username LIKE ?2 OR rm.mobile LIKE ?2 OR rm.email LIKE ?2)")
    Page<RoleMember> findMemberByKeyword(Long roleId, String keyword, Pageable pageable);

    /**
     * 根据角色ID列表获取用户角色关系列表。
     *
     * @param roleIds 角色ID列表
     * @return 用户角色关系列表
     */
    List<RoleMember> findByRoleIdInAndDeletedIsFalse(Collection<Long> roleIds);

    List<RoleMember> findByRoleIdInAndDeletedIsFalseAndUsernameLike(Collection<Long> roleIds,String username);
}
