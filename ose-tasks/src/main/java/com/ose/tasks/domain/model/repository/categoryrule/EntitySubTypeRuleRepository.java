package com.ose.tasks.domain.model.repository.categoryrule;

import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 实体类型设置规则 CRUD 操作接口。
 */
public interface EntitySubTypeRuleRepository extends CrudRepository<EntitySubTypeRule, Long>, EntitySubTypeRuleRepositoryCustom {

    /**
     * 根据实体类型设置规则ID、所属组织 ID、所属项目 ID 取得实体信息。
     *
     * @param id        实体设定规则 ID
     * @param orgId     所属组织 ID
     * @param projectId 项目 ID
     * @return 实体类型设置规则
     */
    Optional<EntitySubTypeRule> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
        Long id,
        Long orgId,
        Long projectId);

    /**
     * 判断 实体类型设置规则是否存在。
     *
     * @param category     实体子类型
     * @param entityType 实体大分类
     * @param projectId    项目 ID
     * @return 存在:true; 不存在:false
     */
    @Query("SELECT CASE WHEN COUNT(estr) > 0 THEN true ELSE false END  FROM EntitySubTypeRule estr WHERE estr.projectId = :projectId " +
        " AND estr.entityType = :entityType AND estr.entitySubType.nameEn = :entitySubType AND estr.deleted = false")
    boolean existsByEntitySubTypeAndEntityTypeAndProjectIdAndDeletedIsFalse(
        @Param("entitySubType") String category,
        @Param("entityType") String entityType,
        @Param("projectId") Long projectId);

    /**
     * 根据实体大类型、所属组织 ID、所属项目 ID 取得实体信息。
     *
     * @param entityType 实体大类型
     * @param orgId        所属组织 ID
     * @param projectId    项目 ID
     * @return 实体信息
     */
//    @Query("SELECT estr FROM EntitySubTypeRule estr WHERE estr.orgId = :orgId AND estr.projectId = :projectId AND estr.entitySubType.nameEn = :entityType")
    Set<EntitySubTypeRule> findByEntityTypeAndOrgIdAndProjectIdAndDeletedIsFalseOrderByRuleOrder(
        @Param("entityType") String entityType,
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId);



    /**
     * 根据实体小类型、所属组织 ID、所属项目 ID 取得实体信息。
     *
     * @param category 实体子类型
     * @param orgId        所属组织 ID
     * @param projectId    项目 ID
     * @return 实体信息
     */
    @Query("SELECT estr FROM EntitySubTypeRule estr WHERE estr.orgId = :orgId AND estr.projectId = :projectId " +
        " AND estr.entitySubType.nameEn = :entitySubType AND estr.status = :status AND estr.deleted = false")
    List<EntitySubTypeRule> findByOrgIdAndProjectIdAndEntitySubTypeAndStatusAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entitySubType") String category,
        @Param("status") EntityStatus status);

    List<EntitySubTypeRule> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM entity_category_rules WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);
}
