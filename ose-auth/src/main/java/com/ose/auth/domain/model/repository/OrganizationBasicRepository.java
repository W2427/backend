package com.ose.auth.domain.model.repository;

import com.ose.auth.entity.OrganizationBasic;
import com.ose.auth.vo.OrgType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrganizationBasicRepository extends PagingAndSortingRepository<OrganizationBasic, Long> {

    /**
     * 按排序正序获取根部门列表。
     *
     * @return 根部门列表
     */
    List<OrganizationBasic> findByParentIdIsNullAndDeletedIsFalseOrderBySortAsc();

    /**
     * 按正序获取子部门列表。
     *
     * @return 子部门列表
     */
    List<OrganizationBasic> findByParentIdIsNotNullAndDeletedIsFalseOrderBySortAsc();

    /**
     * 获取全部部门信息。
     *
     * @param companyId 公司ID
     */
    List<OrganizationBasic> findByPathLikeAndDeletedIsFalseOrderBySortAsc(Long companyId);

    /**
     * 获取组织列表。
     *
     * @param organizationId 组织ID
     * @param type           组织类型
     */
    List<OrganizationBasic> findByPathLikeAndTypeAndDeletedIsFalseOrderBySortAsc(String organizationId, OrgType type);

    /**
     * 获取组织列表。
     *
     * @param organizationId 组织ID
     * @param type           组织类型
     */
    List<OrganizationBasic> findByPathLikeAndTypeAndDeletedIsFalseOrderByCreatedAtDesc(String organizationId, OrgType type);

    /**
     * 获取默认公司。
     *
     * @return 公司信息
     */
    OrganizationBasic findTopByTypeAndDeletedIsFalse(OrgType type);

    /**
     * 获取组织详情。
     *
     * @param organizationId 组织ID
     * @return 组织信息
     */
    OrganizationBasic findByIdAndDeletedIsFalse(Long organizationId);

    /**
     * 获取组织信息。
     *
     * @param name 组织名称
     * @param type 组织类型
     * @param path 路径信息
     * @return 组织信息
     */
    OrganizationBasic findByNameAndTypeAndPathLikeAndDeletedIsFalse(String name, OrgType type, String path);

    /**
     * 根据ID列表获取组织列表。
     *
     * @param Ids 组织ID列表
     * @return 组织列表
     */
    List<OrganizationBasic> findByIdInAndDeletedIsFalse(List<Long> Ids);

    /**
     * 获取用户的组织。
     *
     * @param organizationId 组织ID
     * @param userId         用户ID
     * @return 组织信息
     */
    @Query(value = "SELECT ob FROM OrganizationBasic ob LEFT JOIN UserOrganization uo ON uo.organizationId = ob.id WHERE ob.id = ?1 AND uo.userId = ?2 AND uo.deleted = FALSE ")
    OrganizationBasic findUserOrgs(Long organizationId, Long userId);

    /**
     * 获取用户当父级部门的子部门列表。
     *
     * @param prentId 父部门ID
     * @param type    部门类型
     * @param userId  用户ID
     * @return 部门列表
     */
    @Query(value = "SELECT ob FROM OrganizationBasic ob LEFT JOIN UserOrganization uo ON uo.organizationId = ob.id WHERE ob.path LIKE ?1 AND ob.type = ?2 AND uo.userId = ?3 AND uo.deleted = FALSE ")
    List<OrganizationBasic> findUserChildrenOrgs(String prentId, OrgType type, Long userId);
}
