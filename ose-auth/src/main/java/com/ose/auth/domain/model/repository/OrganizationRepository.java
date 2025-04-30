package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.OrganizationBasicDTO;
import com.ose.auth.entity.Organization;
import com.ose.auth.vo.OrgType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门增删改查接口
 */
public interface OrganizationRepository extends PagingAndSortingWithCrudRepository<Organization, Long> {

    /**
     * 检查部门名称是否已经存在。
     *
     * @param name 部门名称
     * @return 部门名称是否已经存在
     */
    Boolean existsByNameAndDeletedIsFalse(String name);

    /**
     * 根据ID查看部门是否存在。
     */
    Boolean existsByIdAndDeletedIsFalse(Long organizationId);

    /**
     * 根据部门ID获取部门信息。
     *
     * @param organizationId 部门ID
     * @return 部门信息
     */
    Organization findByIdAndDeletedIsFalse(Long organizationId);

    /**
     * 查询所有组织id。
     *
     * @return 部门信息
     */
    List<Organization> findByDeletedIsFalse();

    /**
     * 取得组织信息。
     *
     * @param parentId 上级 ID
     * @param id       组织 ID
     * @return 组织信息
     */
    Organization findByPathLikeAndIdAndDeletedIsFalse(String parentId, Long id);

    /**
     * 根据上级部门获取全部子部门。
     *
     * @param parentId 上级部门ID
     * @return 子部门列表
     */
    List<Organization> findByPathLikeAndDeletedIsFalse(Long parentId);

    List<Organization> findByPathLikeAndTypeAndDeletedIsFalse(String parentId, OrgType type);

    /**
     * 根据上级部门获取全部子部门。
     *
     * @param parentId 上级部门ID
     * @return 子部门列表
     */
    Organization findTopByPathLikeAndDeletedIsFalseOrderBySortDesc(String parentId);

    /**
     * 根据上级部门获取全部子部门字符串。
     *
     * @param parentId 上级部门ID
     * @return 子部门列表
     */
    @Query("SELECT o.children FROM Organization o WHERE o.id =:parentId AND o.deleted = FALSE ")
    String findByIdAndDeletedIsFalseChild(
        @Param("parentId") Long parentId
    );

    /**
     * 获取根部门列表。
     *
     * @return 部门列表
     */
    Organization findTopByParentIdIsNullAndDeletedIsFalseOrderBySortAsc();

    /**
     * 获取部门下最大排序部门。
     *
     * @param parentId 上级部门
     * @return 部门信息
     */
    Organization findTopByParentIdAndDeletedIsFalseOrderBySortDesc(Long parentId);

    /**
     * 最大排序部门。
     */
    Organization findTopByDeletedIsFalseOrderBySortDesc();

    /**
     * 更新大于目标部门的排序。
     *
     * @param companyId 公司 ID
     * @param sortFrom  起始排序
     */
    @Modifying
    @Query("UPDATE Organization o SET o.sort = o.sort + 1 WHERE o.companyId = :companyId AND o.sort > :sortFrom")
    void increaseSort(
        @Param("companyId") Long companyId,
        @Param("sortFrom") int sortFrom
    );

    /**
     * 获取父级部门下的第一个部门。
     *
     * @param parentId 上级ID
     * @return 部门信息
     */
    Organization findTopByParentIdAndDeletedIsFalseOrderBySortAsc(Long parentId);

    /**
     * 设置排序（置顶）。
     *
     * @param target  目标顺序
     * @param orgSort 当前部门顺序
     * @param max     当前部门及其子部门最大顺序
     */
    @Modifying
    @Query("UPDATE Organization o SET o.sort = o.sort + ?3 WHERE (o.sort >= ?1 AND o.sort < ?2) OR o.sort > ?3 ")
    void setSortBySortGreaterThanEqualTargetAndMax(int target, int orgSort, int max);

    /**
     * 设置排序（向上排序）。
     *
     * @param target  目标顺序
     * @param orgSort 当前部门顺序
     * @param max     当前部门及其子部门最大顺序
     */
    @Modifying
    @Query("UPDATE Organization o SET o.sort = o.sort + ?3 WHERE (o.sort > ?1 AND o.sort < ?2) OR o.sort > ?3")
    void setSortBySortGreaterThanTarget(int target, int orgSort, int max);

    /**
     * 设置排序（向下排序）。
     *
     * @param target  目标顺序
     * @param orgSort 当前部门顺序
     * @param max     当前部门及其子部门最大顺序
     */
    @Modifying
    @Query("UPDATE Organization o SET o.sort = o.sort + ?1 WHERE (o.sort >= ?2 AND o.sort <= ?3) OR o.sort > ?1")
    void setSortBySortGreaterThanEqualOrgSort(int target, int orgSort, int max);

    /**
     * 获取子部门的数量。
     *
     * @param organizationId 部门ID
     * @return 子部门数量
     */
    long countByParentIdAndDeletedIsFalse(Long organizationId);

    /**
     * 删除部门。
     *
     * @param organizationId 部门ID
     */
    @Modifying
    @Query("UPDATE Organization o SET o.deleted = TRUE WHERE id = ?1")
    void deleteById(Long organizationId);

    /**
     * 获取组织列表。
     *
     * @param type     组织类型
     * @param pageable 分页参数
     * @return 组织列表
     */
    Page<Organization> findByTypeAndDeletedIsFalse(OrgType type, Pageable pageable);

    /**
     * 获取组织列表。
     *
     * @param organizationIds 组织ID数组
     * @return 组织列表
     */
    List<Organization> findByIdInAndDeletedIsFalse(Collection<Long> organizationIds);

    /**
     * 获取用户顶层项目组织。
     *
     * @param userId 用户ID
     * @return 项目组织列表
     */
//    @Query(value = "SELECT o FROM Organization o INNER JOIN UserOrganization uo ON o.id = uo.organizationId WHERE o.deleted = FALSE AND o.depth = 1 AND o.type = 'PROJECT' AND uo.deleted = FALSE AND uo.userId = ?1 ORDER BY o.name")
//    List<Organization> findUserTopProjectOrgs(Long userId);
    @Query(nativeQuery = true,
    value = "SELECT " +
        "o.*,p.id as project_id " +
        "FROM " +
        "organizations o " +
        "INNER JOIN user_organization_relations uo ON o.id = uo.organization_id " +
        "INNER JOIN saint_whale_tasks.project p ON p.org_id = o.id " +
        "WHERE " +
        "o.deleted = FALSE  " +
        "AND p.`status` = 'ACTIVE' " +
        "AND o.depth = 1  " +
        "AND o.type = 'PROJECT'  " +
        "AND uo.deleted = FALSE  " +
        "AND uo.user_id = :userId  " +
        "ORDER BY " +
        "o.NAME")
    List<Map<String, Object>>  findUserTopProjectOrgs(
        @Param("userId") Long userId
    );

    @Query(value = "SELECT " +
        "  o.*  " +
        "FROM " +
        "  user_organization_relations uor " +
        "  LEFT JOIN organizations o ON FIND_IN_SET(uor.organization_id, o.children) > 0  " +
        "WHERE " +
        "  o.deleted IS FALSE " +
        "  AND uor.deleted IS FALSE " +
        "  AND uor.user_id = :userId " +
        "  AND uor.apply_role IS TRUE " +
        "  AND uor.organization_type = :orgType", nativeQuery = true)
    List<Organization> findByUserIdAndIsApployRole(@Param("userId") Long userId,
                                                   @Param("orgType") String orgType);

    /**
     * 取得组织信息列表。
     *
     * @param entityIDs 组织数据实体 ID 列表
     * @return 组织信息列表
     */
    @Query("SELECT new com.ose.auth.dto.OrganizationBasicDTO(o.id, o.type, o.name) FROM Organization o WHERE o.id IN :entityIDs")
    List<OrganizationBasicDTO> findByIdIn(@Param("entityIDs") Set<Long> entityIDs);

    @Query(value = "SELECT * FROM organizations WHERE path LIKE :parentPath AND `type` = :type AND status = 'ACTIVE'", nativeQuery = true)
    List<Organization> findByParentIdAndType(@Param("parentPath") String parentPath,
                                             @Param("type") String type);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM organizations WHERE path LIKE :path", nativeQuery = true)
    void deleteOrgByOrgPath(@Param("path") String path);

    /**
     * 根据任务包设定的信息，定义四级计划权限。
     *
     */
    @Query(
        value = "  SELECT"
            + "    new com.ose.auth.dto.OrganizationBasicDTO(t.id, t.type, t.name)"
            + "    FROM"
            + "     Organization AS o"
            + "     LEFT OUTER JOIN Organization AS t"
            + "       ON (t.id = o.id OR t.path LIKE CONCAT(o.path, o.id, '/%'))"
            + "   WHERE"
            + "     o.id = :orgId AND t.id = :teamId ")
    OrganizationBasicDTO findTeamByOrg(@Param("orgId") Long orgId, @Param("teamId") Long teamId);

    @Query(
        value = "  SELECT"
            + "    new com.ose.auth.dto.OrganizationBasicDTO(t.id, t.type, t.name)"
            + "    FROM"
            + "     Organization AS t"
            + "   WHERE"
            + "     t.path LIKE :orgId AND t.name = :name ")
    List<OrganizationBasicDTO> findDeparmentByOrgIdAndName(@Param("orgId") String orgId, @Param("name") String name);

    @Query(
        value = "SELECT"
            + "   u.id, "
            + "   u.name "
            + " FROM"
            + "   user_organization_relations AS uo "
            + " LEFT JOIN users AS u ON uo.user_id = u.id "
            + " WHERE"
            + "   uo.organization_id = :orgId "
            + " AND  uo.deleted is false ",
        nativeQuery = true
    )
    Map<String, Object> findUserInfoByOrgId(
        @Param("orgId") Long orgId
    );
}

