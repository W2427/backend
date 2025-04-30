package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 结构实体二维码查询接口。
 */
@Transactional
public interface StructureEntityQrCodeRepository extends PagingAndSortingWithCrudRepository<StructureEntityQrCode, Long>, StructureEntityQrCodeRepositoryCustom {

    StructureEntityQrCode findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        Long entityId
    );

    StructureEntityQrCode findByOrgIdAndProjectIdAndEntityNoAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String entityNo
    );
    @Query(value="SELECT" +
        " heat_no " +
        "FROM " +
        " structure_entity_qr_codes " +
        "WHERE " +
        " org_id = :orgId " +
        " AND project_id = :projectId  " +
        " AND `entity_no` = :entityNo " +
        " AND deleted = FALSE",nativeQuery=true)
    String findHeatNoByOrgIdAndProjectIdAndEntityNoAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entityNo")String entityNo
    );

    void deleteByOrgIdAndProjectId(Long orgId,
                                   Long projectId);

    List<StructureEntityQrCode> findByOrgIdAndProjectIdAndHeatNoIdIsNull(Long orgId, Long projectId);

    @Query("SELECT se FROM StructureEntityQrCode se WHERE se.orgId = :orgId AND se.projectId = :projectId " +
        "AND se.heatNo LIKE '%null%' OR se.heatNo NOT LIKE '%/%'")
    List<StructureEntityQrCode> findByOrgIdAndProjectIdAndHeatNo(Long orgId, Long projectId);

    List<StructureEntityQrCode> findByOrgIdAndProjectIdAndHeatNoId(Long orgId, Long projectId, Long heatNoId);


}
