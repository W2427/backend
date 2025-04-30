package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserOrganization;
import com.ose.auth.vo.OrgType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserOrganizationRepository extends PagingAndSortingWithCrudRepository<UserOrganization, Long> {

    /**
     * 根据成员ID获取关系列表。
     *
     * @param memberId 成员ID
     * @return 组织与成员的关系列表
     */
    List<UserOrganization> findByUserIdAndDeletedIsFalse(Long memberId);


    /**
     * 根据成员ID获取关系列表。
     *
     * @param userId 成员ID
     * @return 组织与成员的关系列表
     */
    List<UserOrganization> findByUserIdAndOrganizationTypeAndApplyRoleIsTrueAndDeletedIsFalse(Long userId, OrgType orgType);

    /**
     * 用户是否已经加入组织。
     *
     * @param userId         用户ID
     * @param organizationId 组织ID
     */
    Boolean existsByUserIdAndOrganizationIdAndDeletedIsFalse(Long userId, Long organizationId);

    /**
     * 根据用户ID和组织ID获取成员。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 用户与组织关系
     */
    UserOrganization findTopByOrganizationIdAndUserIdAndDeletedIsFalse(Long organizationId, Long userId);

    /**
     * 获取用户组织关系列表。
     *
     * @param userIds 用户ID列表
     * @return 用户组织关系列表
     */
    List<UserOrganization> findByUserIdInAndDeletedIsFalse(List<Long> userIds);

    /**
     * 查看组织下的成员列表。
     *
     * @param organizationId 组织ID
     * @param pageable       分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserOrganization u WHERE u.organizationId = ?1 AND u.deleted = FALSE GROUP BY u.userId")
    Page<UserOrganization> findMembers(Long organizationId, Pageable pageable);

    /**
     * 查看组织下的成员列表。
     *
     * @param organizationId 组织ID
     * @param keyword        关键字检索
     * @param pageable       分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT uo FROM UserOrganization uo LEFT JOIN User u ON u.id = uo.userId WHERE uo.organizationId = ?1 AND (u.name LIKE %?2% OR u.username LIKE %?2% OR u.mobile LIKE %?2% OR u.email LIKE %?2%) AND uo.deleted = FALSE AND u.deleted = FALSE GROUP BY uo.userId")
    Page<UserOrganization> findMembersByKeyword(Long organizationId, String keyword, Pageable pageable);

    /**
     * 是否存在默认组织。
     *
     * @param memberId 用户ID
     * @return 是否存在默认组织
     */
    boolean existsByUserIdAndIsDefaultTrueAndDeletedIsFalse(Long memberId);

    /**
     * 获取用户的根项目组织。
     *
     * @param organizationId 排除的组织ID
     * @param userId         用户ID
     * @return 用户组织列表
     */
    @Query(value = "SELECT uo FROM UserOrganization uo INNER JOIN Organization o ON o.id = uo.organizationId WHERE o.depth = 1 AND o.deleted = false AND o.type = 'project' AND uo.deleted = false")
    List<UserOrganization> findRootProjectOrgs(Long organizationId, Long userId);

    /**
     * 获取用户组织。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @param type           组织类型
     * @return 部门列表
     */
    @Query("SELECT uo FROM UserOrganization uo INNER JOIN Organization o ON o.id = uo.organizationId WHERE o.path LIKE ?1 AND uo.userId = ?2 AND uo.deleted = FALSE AND o.deleted = FALSE AND o.type = ?3")
    List<UserOrganization> findMemberOrgs(String organizationId, Long userId, OrgType type);

    /**
     * 组织下是否存在成员。
     *
     * @param organizationId 组织ID
     */
    Boolean existsByOrganizationIdAndDeletedIsFalse(Long organizationId);

    /**
     * 切换用户项目组织。
     *
     * @param userId         用户ID
     * @param organizationId 组织ID
     */
    @Modifying
    @Query("UPDATE UserOrganization uo SET uo.isDefault = TRUE WHERE uo.userId = ?1 AND uo.organizationId = ?2")
    void switchUserProjectOrg(Long userId, Long organizationId);

    /**
     * 取消用户的默认项目组织。
     *
     * @param userId 用户ID
     */
    @Modifying
    @Query("UPDATE UserOrganization uo SET uo.isDefault = FALSE WHERE uo.userId = ?1")
    void cancelDefaultUserProjectOrg(Long userId);

    /**
     * 获取用户组织下的部门列表。
     *
     * @param organizationId 项目组织ID
     * @param userIds        用户ID
     */
    List<UserOrganization> findByOrganizationIdAndUserIdInAndDeletedIsFalse(Long organizationId, List<Long> userIds);

    /**
     * 用户退出组织。
     *
     * @param userId 用户ID
     */
    @Modifying
    @Query(value = "UPDATE UserOrganization uo SET uo.status = 'DELETED', uo.deleted = TRUE, uo.deletedAt = current_timestamp, uo.deletedBy = ?1 WHERE uo.userId = ?2 AND uo.deleted = FALSE ")
    void quitOrgs(Long operatorId, Long userId);

    /**
     * 获取子部门成员。
     *
     * @param orgId  组织ID
     * @param userId 用户ID
     * @return 关系列表
     */
    @Query(value = "SELECT uo FROM UserOrganization uo INNER JOIN Organization o ON uo.organizationId = o.id WHERE o.path LIKE ?1 AND uo.userId = ?2 AND uo.deleted = FALSE AND o.deleted = FALSE ")
    List<UserOrganization> findChildrenOrgMembers(String orgId, Long userId);

    /**
     * 获取已加入的上级组织。
     *
     * @param organizationIds 组织ID
     * @param userId          用户ID
     * @return 组织列表
     */
    @Query(value = "SELECT o FROM UserOrganization uo LEFT JOIN Organization o On uo.organizationId = o.id WHERE uo.organizationId IN ?1 AND uo.userId = ?2 AND uo.deleted = FALSE AND o.deleted = FALSE ")
    List<Organization> findJoinedParentOrgs(List<Long> organizationIds, Long userId);

    /**
     * 移除用户组织。
     *
     * @param operatorId 操作人ID
     * @param userOrgIds 用户组织ID列表
     */
    @Modifying
    @Query(value = "UPDATE UserOrganization uo SET uo.deleted = TRUE, uo.deletedBy = ?1, uo.deletedAt = current_timestamp WHERE uo.id IN ?2 AND uo.deleted = FALSE")
    void removeUserOrgs(Long operatorId, List<Long> userOrgIds);

    /**
     * 获取用户默认项目组织。
     *
     * @param userId 用户ID
     * @param type   组织类型
     * @return 加入组织信息
     */
    UserOrganization findTopByUserIdAndIsDefaultIsTrueAndOrganizationTypeAndDeletedIsFalse(Long userId, OrgType type);

    UserOrganization findByOrganizationIdAndUserIdAndDeletedIsFalse(Long orgId, Long userId);

    UserOrganization findByOrganizationIdAndApplyRoleIsTrueAndDeletedIsFalse(Long orgId);

    List<UserOrganization> findByOrganizationIdAndIdcIsTrueAndDeletedIsFalse(Long orgId);

    UserOrganization findByOrganizationIdAndOrganizationTypeAndDeletedIsFalse(Long orgId, OrgType type);
}
