package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.vo.EntityStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;

/**
 * 实体 CRUD 操作接口。
 */
@Transactional
public interface BpmEntitySubTypeRepository extends PagingAndSortingWithCrudRepository<BpmEntitySubType, Long>, BpmEntitySubTypeRepositoryCustom {

    BpmEntitySubType findByProjectIdAndNameEn(@Param("projectId") Long projectId, @Param("nameEn") String nameEn);

    @Query("SELECT distinct m.entityType FROM BpmEntitySubType m WHERE m.projectId = :projectId and m.entityType.type = :type and m.status = 'ACTIVE'")
    List<BpmEntityType> findEntityTypes(@Param("projectId") Long projectId, @Param("type") String type);

    @Query("SELECT distinct m.entityBusinessType FROM BpmEntitySubType m WHERE m.projectId = :projectId and m.entityBusinessType.type = :type and m.status = 'ACTIVE'")
    List<BpmEntityType> findEntityBusinessTypes(@Param("projectId") Long projectId, @Param("type") String type);

    @Query("SELECT m FROM BpmEntitySubType m WHERE m.projectId = :projectId AND m.status = 'ACTIVE'" + " and m.entityType.id = :typeId")
    List<BpmEntitySubType> findEntityCategoriesByTypeId(@Param("projectId") Long projectId, @Param("typeId") Long typeId);

    Optional<BpmEntitySubType> findByProjectIdAndNameEnAndStatus(@Param("projectId") Long projectId, @Param("nameEn") String nameEn, @Param("entityStatus") EntityStatus entityStatus);

    @Query(value = "SELECT * FROM bpm_entity_sub_type AS bec INNER JOIN bpm_entity_type AS bect on bec.entity_category_type_id=bect.id " +
        "WHERE bec.project_id = :projectId AND bect.name_en = :entityType and  bec.name_en= :entitySubType AND bec.status = 'ACTIVE'", nativeQuery = true)
    List<BpmEntitySubType> findEntityCategoriesByEntityType(@Param("entityType") String entityType,
                                                            @Param("entitySubType") String entitySubType,
                                                            @Param("projectId") Long projectId);

    /**
     * 根据ID列表获取实体类型列表。
     *
     * @param entityCategoryIDs 实体类型ID列表
     * @return 实体类型列表
     */
    List<BpmEntitySubType> findByIdIn(List<Long> entityCategoryIDs);


    /**
     * 根据实体类型名称、所属组织 ID、所属项目 ID 取得实体类型。
     *
     * @param NameCn    实体类型名称
     * @param projectId 项目 ID
     * @param status    状态
     * @return 工序阶段
     */
    Optional<BpmEntitySubType> findByProjectIdAndNameCnAndStatus(
        Long projectId,
        String NameCn,
        EntityStatus status);

    /**
     * 返回一个项目全部的实体类型
     *
     * @param projectId 项目ID
     * @return
     */
    List<BpmEntitySubType> findByProjectIdAndStatus(Long projectId, EntityStatus status);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_entity_sub_type WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);

    @Query(value = "SELECT bect.name_en type, bec.name_en subType FROM bpm_entity_sub_type AS bec INNER JOIN bpm_entity_type AS bect on bec.entity_category_type_id=bect.id " +
        "WHERE bec.project_id = :projectId AND bec.status = 'ACTIVE'", nativeQuery = true)
    Set<Tuple> findTypesByProjectId(@Param("projectId") Long projectId);


    @Query(value = "SELECT bec.name_en FROM bpm_entity_type_process_relation pr JOIN bpm_entity_sub_type bec ON bec.id = pr.entity_sub_type_id " +
        " WHERE pr.project_id = :projectId AND " +
        "pr.process_id = :processId AND pr.status = 'ACTIVE'", nativeQuery = true)
    Set<String> findEntitySubTypesByProjectIdAndProcessIn(@Param("projectId") Long projectId,
                                                          @Param("processId") Long mcBpId);
}
