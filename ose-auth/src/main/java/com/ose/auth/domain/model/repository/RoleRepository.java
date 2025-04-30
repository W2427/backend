package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.Role;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色数据仓库。
 */
public interface RoleRepository extends PagingAndSortingWithCrudRepository<Role, Long>, RoleRepositoryCustom {

    /**
     * 获取角色详情。
     *
     * @param roleId 角色ID
     * @return 角色详情
     */
    Role findByIdAndDeletedIsFalse(Long roleId);

    /**
     * 按Sort正序获取角色信息。
     *
     * @return 角色详情
     */
    Role findTopByDeletedIsFalseOrderBySortAsc();

    /**
     * 查看是否有重名的角色信息。
     *
     * @param name           角色名称
     * @param organizationId 组织ID
     * @return 是否存在
     */
    Boolean existsByNameAndDeletedIsFalseAndOrganizationId(String name, Long organizationId);

    /**
     * 查看是否有重复编号的角色信息。
     *
     * @param no 编号
     * @return 是否存在
     */
    Boolean existsByNoAndDeletedIsFalse(String no);

    /**
     * 获取最大排序值的角色信息。
     *
     * @return 角色信息
     */
    Role findTopByDeletedIsFalseOrderBySortDesc();

    /**
     * 根绝角色ID和部门ID获取角色信息。
     *
     * @param roleId         角色ID
     * @param organizationId 部门ID
     * @return 角色信息
     */
    Role findByIdAndOrganizationIdAndDeletedIsFalse(Long roleId, Long organizationId);

    /**
     * 根据角色ID列表获取角色信息列表。
     *
     * @param roleIds 角色ID列表
     * @return 角色信息列表
     */
    List<Role> findByIdInAndDeletedIsFalse(List<Long> roleIds);

    /**
     * 获取用户在当前组织下的角色列表。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 角色列表
     */
    @Query("SELECT role FROM Role role INNER JOIN RoleMember rm ON rm.roleId = role.id WHERE rm.userId = ?2 AND rm.deleted = false AND role.organizationId = ?1 AND role.deleted = false")
    List<Role> findOrgMemberRoles(Long organizationId, Long userId);

    List<Role> findByOrganizationIdAndDeletedIsFalse(Long organizationId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM roles WHERE organization_id = :orgId", nativeQuery = true)
    void deleteRoleByOrgId(@Param("orgId") Long orgId);}
