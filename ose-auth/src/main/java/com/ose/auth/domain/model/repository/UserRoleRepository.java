package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.OrganizationUserPosition;
import com.ose.auth.entity.UserRole;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends PagingAndSortingWithCrudRepository<UserRole, Long> {

    /**
     * 查看用户是否拥有色。
     *
     * @param roleId   角色ID
     * @param memberId 用户ID
     */
    boolean existsByRoleIdAndUserIdAndDeletedIsFalse(Long roleId, Long memberId);

    /**
     * 获取用户角色关系。
     *
     * @param roleId   角色ID
     * @param memberId 用户ID
     * @return 用户角色关系
     */
    UserRole findByRoleIdAndUserIdAndDeletedIsFalse(Long roleId, Long memberId);

    /**
     * 查询某个用户的所有角色
     * @param memberId
     * @return
     */
    List<UserRole> findByUserIdAndDeletedIsFalse(Long memberId);

    /**
     * 获取组织成员的角色信息。
     *
     * @param organizationId 组织ID
     * @param userIds        用户ID列表
     * @return 用户的角色信息
     */
    @Query("SELECT ur FROM Role r INNER JOIN UserRole ur ON r.id = ur.roleId WHERE ur.userId IN ?2 AND ur.deleted = FALSE AND r.deleted = FALSE AND r.organizationId = ?1")
    List<UserRole> findOrgMembersRoles(Long organizationId, List<Long> userIds);

    /**
     * 获取组织成员的角色信息。
     *
     * @param organizationId 组织ID
     * @param userIds        用户ID列表
     * @return 用户的角色信息
     */
    @Query("SELECT ur FROM Role r INNER JOIN UserRole ur ON r.id = ur.roleId WHERE ur.userId IN ?2 AND ur.deleted = FALSE AND r.deleted = FALSE AND r.organizationId = ?1")
    List<OrganizationUserPosition> findOrgMembersPositions(Long organizationId, List<Long> userIds);

    /**
     * 获取所有组织成员的角色信息。
     *
     * @param userIds        用户ID列表
     * @return 用户的角色信息
     */
    @Query("SELECT ur FROM Role r INNER JOIN UserRole ur ON r.id = ur.roleId WHERE r.organizationId IN ?1 and ur.userId IN ?2 AND ur.deleted = FALSE AND r.deleted = FALSE")
    List<UserRole> findOrgMembersRoles(List<Long> organizationIds, List<Long> userIds);

    /**
     * 删除用户角色关系。
     *
     * @param operatorId 操作人ID
     * @param userId     用户ID
     */
    @Modifying
    @Query(value = "UPDATE UserRole ur SET ur.status = 'DELETED', ur.deleted = TRUE, ur.deletedBy = ?1, ur.deletedAt = current_timestamp WHERE ur.userId = ?2 AND ur.deleted = FALSE ")
    void deleteUserRoles(Long operatorId, Long userId);

    /**
     * 删除用户角色。
     *
     * @param operatorId 操作人ID
     * @param userRoles  用户角色关系ID列表
     */
    @Modifying
    @Query(value = "UPDATE UserRole ur SET ur.status = 'DELETED', ur.deleted = TRUE, ur.deletedBy = ?1, ur.deletedAt = current_timestamp WHERE ur.id IN ?2 ")
    void removeUserRoles(Long operatorId, List<Long> userRoles);


    /**
     * 取得指定用户在指定组织的权限信息。
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 权限数据
     */
    @Query(
        value = "SELECT CONCAT(co.path, co.id), GROUP_CONCAT(r.privileges SEPARATOR '')"
            + " FROM organizations AS o"
            + " INNER JOIN organizations AS po ON po.depth = 0 AND po.deleted = 0 AND (po.id = o.id OR o.path LIKE CONCAT('/', po.id, '/%'))"
            + " INNER JOIN organizations AS co ON (co.id = po.id OR co.path LIKE CONCAT(IFNULL(po.path, '/'), po.id, '/%')) AND co.deleted = 0"
            + " INNER JOIN roles AS r ON r.organization_id = co.id AND r.deleted = 0"
            + " INNER JOIN user_role_relations AS ur ON ur.role_id = r.id AND ur.user_id = :userId AND ur.deleted = 0"
            + " WHERE o.id = :orgId AND o.deleted = 0"
            + " GROUP BY co.id"
            + " ORDER BY co.depth ASC",
        nativeQuery = true
    )
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    List<Object[]> findByUserIdAndOrgId(
        @Param("userId") Long userId,
        @Param("orgId") Long orgId
    );

    /**
     * 取得当前用户在指定组织及其所有下级组织所用有的所有权限。
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 权限代码列表
     */
    @Query(
        value = "SELECT GROUP_CONCAT(r.privileges ORDER BY LENGTH(r.privileges) ASC SEPARATOR '')"
            + " FROM organizations AS o"
            + " INNER JOIN organizations AS co ON (co.id = o.id OR co.path LIKE CONCAT(IFNULL(o.path, '/'), o.id, '/%')) AND co.deleted = 0"
            + " INNER JOIN roles AS r ON r.organization_id = co.id AND r.deleted = 0"
            + " INNER JOIN user_role_relations AS ur ON ur.role_id = r.id AND ur.user_id = :userId AND ur.deleted = 0"
            + " WHERE o.id = :orgId AND o.deleted = 0"
            + " GROUP BY o.id, ur.user_id",
        nativeQuery = true
    )
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    String findAvailableByUserIdAndOrgId(
        @Param("userId") Long userId,
        @Param("orgId") Long orgId
    );

    /**
     * 取得当前用户在全局页面的所有权限。
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 权限代码列表
     */
    @Query(
        value = "SELECT GROUP_CONCAT(r.privileges ORDER BY LENGTH(r.privileges) ASC SEPARATOR '')"
            + " FROM organizations AS o"
            + " INNER JOIN roles AS r ON r.organization_id = o.id AND r.deleted = 0"
            + " INNER JOIN user_role_relations AS ur ON ur.role_id = r.id AND ur.user_id = :userId AND ur.deleted = 0"
            + " WHERE o.id = :orgId AND o.deleted = 0"
            + " GROUP BY o.id, ur.user_id",
        nativeQuery = true
    )
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    String findGlobalAvailableByUserIdAndOrgId(
        @Param("userId") Long userId,
        @Param("orgId") Long orgId
    );

}
