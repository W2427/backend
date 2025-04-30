package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.bizcode.BizCodeTypeDTO;
import com.ose.tasks.entity.BizCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 业务代码数据仓库。
 */
public interface BizCodeRepository extends PagingAndSortingWithCrudRepository<BizCode, Long> {

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @param pageable  分页参数
     * @return 业务代码分页数据
     */
    Page<BizCode> findByOrgIdAndProjectIdAndTypeAndDeletedIsFalseOrderBySortAsc(
        Long orgId,
        Long projectId,
        String type,
        Pageable pageable
    );

    /**
     * 取得业务代码列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码类型
     * @param bizCode   业务代码
     * @return 业务代码数据实体
     */
    Optional<BizCode> findByOrgIdAndProjectIdAndTypeAndCodeAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String type,
        String bizCode
    );

    /**
     * 取得业务代码大类型列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 业务代码大类型列表
     */
    @Query("SELECT distinct new com.ose.tasks.dto.bizcode.BizCodeTypeDTO(companyId, orgId, projectId, type, typeName) \n"
        + " FROM BizCode \n"
        + " WHERE projectId = :projectId \n"
        + " AND orgId = :orgId \n"
        + " AND deleted = FALSE \n")
    List<BizCodeTypeDTO> findTypeByOrgIdAndProjectIdAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId
    );

    /**
     * 按大类型名称取得业务代码的个数。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      业务代码大类型
     * @return 业务代码个数
     */
    @Query("SELECT count(0) AS count "
        + " FROM BizCode "
        + " WHERE projectId = :projectId "
        + " AND orgId = :orgId "
        + " AND type = :type "
        + " AND deleted = FALSE ")
    int countByOrgIdAndProjectIdAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("type") String type
    );

    List<BizCode> findByOrgIdAndProjectIdAndDeletedIsFalse(Long oldOrgId, Long oldProjectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM biz_code WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM saint_whale_auth.roles WHERE organization_id = :orgId", nativeQuery = true)
    void deleteRoleByOrgId(@Param("orgId") Long orgId);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM saint_whale_auth.organizations WHERE path LIKE :path", nativeQuery = true)
    void deleteOrgByOrgPath(@Param("path") String path);
}
