package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.ose.repository.PagingAndSortingWithCrudRepository;

import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 用户资料 CRUD 操作接口。
 */
public interface UserProfileRepository extends PagingAndSortingWithCrudRepository<UserProfile, Long>, UserProfileRepositoryCustom {

    /**
     * 检查登录用户名是否已被注册。
     *
     * @param username 登录用户名
     * @return 登录用户名是否已被注册
     */
    Boolean existsByUsernameAndDeletedIsFalse(String username);

    /**
     * 检查手机号码是否已被注册。
     *
     * @param mobile 手机号码
     * @return 手机号码是否已被注册
     */
    Boolean existsByMobileAndDeletedIsFalse(String mobile);

    /**
     * 检查电子邮箱地址是否已被注册。
     *
     * @param email 电子邮箱地址
     * @return 电子邮箱地址是否已被注册
     */
    Boolean existsByEmailAndDeletedIsFalse(String email);

    /**
     * 通过登录用户名取得用户信息。
     *
     * @param username 登录用户名
     * @return 用户信息
     */
    UserProfile findByUsernameAndDeletedIsFalse(String username);

    /**
     * 通过登录名列表获取用户信息。
     *
     * @param userNames 用户名
     * @return 用户列表
     */
    List<UserProfile> findByUsernameInAndDeletedIsFalse(List<String> userNames);

    /**
     * 通过手机号码取得用户信息。
     *
     * @param mobile 手机号码
     * @return 用户信息
     */
    UserProfile findByMobileAndDeletedIsFalse(String mobile);

    /**
     * 通过电子邮箱地址取得用户信息。
     *
     * @param email 电子邮箱地址
     * @return 用户信息
     */
    UserProfile findByEmailAndDeletedIsFalse(String email);

    /**
     * 取得系统用户信息。
     *
     * @return 用户信息
     */
    @Query("SELECT u FROM UserProfile u WHERE u.username = 'system'")
    UserProfile findSystemUser();

    /**
     * 取得超级用户信息。
     *
     * @return 用户信息
     */
    @Query("SELECT u FROM UserProfile u WHERE u.username = 'super'")
    UserProfile findSuperUser();

    /**
     * 根据ID数组获取用户数量。
     *
     * @param userIds 用户ID列表
     * @return 用户数量
     */
    long countByIdInAndDeletedIsFalse(Long[] userIds);

    /**
     * 根据ID查看用户是否存在。
     *
     * @param id 用户ID
     */
    Boolean existsByIdAndDeletedIsFalse(Long id);

    /**
     * 获取用户信息。
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserProfile findByIdAndDeletedIsFalse(Long userId);

    /**
     * 查看组织下的成员列表。
     *
     * @param organizationId 组织ID
     * @param pageable       分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserOrganization uo ON uo.userId = u.id WHERE uo.organizationId = ?1 AND uo.deleted = FALSE AND u.deleted = FALSE GROUP BY u.id")
    Page<UserProfile> findOrgMembers(Long organizationId, Pageable pageable);

    /**
     * 查看多个组织下的成员列表。
     *
     * @param organizationId 组织ID
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserOrganization uo ON uo.userId = u.id WHERE uo.organizationId IN ?1 AND uo.deleted = FALSE AND u.deleted = FALSE GROUP BY u.id")
    List<UserProfile> findMultipleOrgMembers(List<Long> organizationId);

    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserOrganization uo ON uo.userId = u.id INNER JOIN UserRole ur ON ur.userId = u.id INNER JOIN Role r ON r.id = ur.roleId WHERE uo.organizationId = ?1 AND r.organizationId = ?1  AND uo.deleted = FALSE AND u.deleted = FALSE AND"+"("+"r.privileges LIKE '%cosign%' or "+"r.privileges LIKE '%all%')"+ "GROUP BY u.id")
    Page<UserProfile> findOrgCosignMembers(Long organizationId, Pageable pageable);



    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserOrganization uo ON uo.userId = u.id INNER JOIN UserRole ur ON ur.userId = u.id INNER JOIN Role r ON r.id = ur.roleId WHERE uo.organizationId = ?1 AND r.organizationId = ?1 AND (u.name LIKE ?2 OR u.username LIKE ?2 OR u.mobile LIKE ?2 OR u.email LIKE ?2)   AND uo.deleted = FALSE AND u.deleted = FALSE AND"+"("+"r.privileges LIKE '%cosign%' or "+"r.privileges LIKE '%all%')"+ "GROUP BY u.id")
    Page<UserProfile> findOrgCosignMembersByKeyword(Long organizationId, String keyword, Pageable pageable);

    /**
     * 查看组织下的成员列表。
     *
     * @param organizationId 组织ID
     * @param keyword        关键字检索
     * @param pageable       分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserOrganization uo  ON u.id = uo.userId WHERE uo.organizationId = ?1 AND (u.name LIKE ?2 OR u.username LIKE ?2 OR u.mobile LIKE ?2 OR u.email LIKE ?2) AND uo.deleted = FALSE AND u.deleted = FALSE GROUP BY u.id")
    Page<UserProfile> findOrgMembersByKeyword(Long organizationId, String keyword, Pageable pageable);

    /**
     * 查看角色下的成员列表。
     *
     * @param roleId   角色ID
     * @param pageable 分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserRole ur ON ur.userId = u.id WHERE ur.roleId = ?1 AND ur.deleted = FALSE AND u.deleted = FALSE GROUP BY u.id")
    Page<UserProfile> findRoleMembers(Long roleId, Pageable pageable);

    /**
     * 查看角色下的成员列表。
     *
     * @param roleId   角色ID
     * @param keyword  关键字检索
     * @param pageable 分页参数
     * @return 成员列表
     */
    @Query(value = "SELECT u FROM UserProfile u INNER JOIN UserRole ur  ON u.id = ur.userId WHERE ur.roleId = ?1 AND (u.name LIKE ?2 OR u.username LIKE ?2 OR u.mobile LIKE ?2 OR u.email LIKE ?2) AND ur.deleted = FALSE AND u.deleted = FALSE GROUP BY u.id")
    Page<UserProfile> findRoleMembersByKeyword(Long roleId, String keyword, Pageable pageable);


    @Query("SELECT u FROM UserProfile u JOIN UserOrganization uo ON u.id = uo.userId " +
        "WHERE uo.organizationId = :orgId AND u.deleted = FALSE AND u.username LIKE :username")
//    @Query("SELECT u FROM UserProfile u " +
//        "WHERE u.id IN (SELECT uo.id FROM UserOrganization uo WHERE uo.organizationId = :orgId) AND u.deleted = FALSE AND u.username LIKE :username")
    List<UserProfile> findByOrgIdAndUsername(@Param("orgId") Long orgId,
                                             @Param("username") String username);
    @Query("SELECT u FROM UserProfile u JOIN UserOrganization uo ON u.id = uo.userId " +
        "WHERE uo.organizationId = :orgId AND u.deleted = FALSE AND u.name LIKE :name")
    List<UserProfile> findByOrgIdAndName(@Param("orgId") Long orgId, @Param("name") String name);

    List<UserProfile> findByDeletedIsFalse();


    @Query("SELECT u FROM UserProfile u JOIN UserOrganization uo ON u.id = uo.userId " +
        "WHERE uo.organizationId = :orgId AND u.deleted = FALSE")
    List<UserProfile> findByOrgId(@Param("orgId") Long orgId);
}
