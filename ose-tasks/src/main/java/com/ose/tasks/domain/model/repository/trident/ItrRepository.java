package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.dto.trident.ItrCreateDTO;
import com.ose.tasks.entity.trident.Itr;
import com.ose.tasks.vo.trident.CheckSheetTridentTbl;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 * BPM ITR CRUD 操作接口。
 */
public interface ItrRepository extends ItrRepositoryCustom, PagingAndSortingRepository<Itr, Long> {

    Itr findByProjectIdAndTridentItrIdAndCheckSheetTridentTbl(Long projectId, Integer tcsId, CheckSheetTridentTbl cst);

    @Modifying
    @Transactional
    @Query("UPDATE Itr itr SET itr.isInTrident = :inTrident WHERE itr.projectId = :projectId AND itr.tridentItrId = :aitrId AND itr.checkSheetTridentTbl = :cst")
    void updateInTridentStatus(@Param("projectId") Long projectId,
                               @Param("aitrId") Integer aitrid,
                               @Param("cst") CheckSheetTridentTbl cst,
                               @Param("inTrident") boolean inTrident);


    @Query("SELECT cs.tridentItrId FROM Itr cs WHERE cs.projectId = :projectId AND cs.checkSheetTridentTbl = :cst AND cs.isInTrident = :inTrident")
    Set<Integer> findItrIdByProjectIdAndCheckSheetTridentTblAndIsInTrident(@Param("projectId") Long projectId,
                                                                             @Param("cst") CheckSheetTridentTbl cst,
                                                                             @Param("inTrident") boolean inTrident);

    Itr findByIdAndStatus(Long itrId, EntityStatus active);


    @Query(value = "SELECT pn.project_id AS projectId," +
        "          etpitr.itr_template AS itrTemplate, " +
        "          pn.entity_id AS entityId, " +
        "          pn.no AS entityNo, " +
        "          etpitr.check_sheet_stage AS checksheetStage, " +
        "          etpitr.check_sheet_type AS checkSheetType " +
        " FROM project_node pn LEFT JOIN itr it ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        " JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE pn.project_id = :projectId AND etpitr.check_sheet_stage = 'M' AND " +
        "it.check_sheet_stage = 'M' AND it.id IS NULL", nativeQuery = true)
    List<ItrCreateDTO> getMcStageCreatedItrs(@Param("projectId") Long projectId);


    @Query(value = "SELECT it.id " +
        " FROM itr it LEFT JOIN project_node pn ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        "  JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE it.project_id = :projectId AND pn.id IS NULL AND etpitr.check_sheet_stage = 'M' AND " +
        "it.check_sheet_stage = 'M'", nativeQuery = true)
    List<BigInteger> getRemovedMcStageCreatedItrs(@Param("projectId") Long projectId);

    @Query(value = "SELECT pn.project_id AS projectId," +
        "          etpitr.itr_template AS itrTemplate, " +
        "          pn.entity_id AS entityId, " +
        "          pn.no AS entityNo, " +
        "          etpitr.check_sheet_stage AS checksheetStage, " +
        "          etpitr.check_sheet_type AS checkSheetType " +
        " FROM project_node pn LEFT JOIN itr it ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        " JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE pn.project_id = :projectId AND etpitr.check_sheet_stage = 'A' AND " +
        "it.check_sheet_stage = 'A' AND it.id IS NULL", nativeQuery = true)
    List<ItrCreateDTO> getPreCommStageCreatedItrs(@Param("projectId") Long projectId);

    @Query(value = "SELECT it.id " +
        " FROM itr it LEFT JOIN project_node pn ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        "  JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE it.project_id = :projectId AND pn.id IS NULL AND etpitr.check_sheet_stage = 'A' AND " +
        "it.check_sheet_stage = 'A'", nativeQuery = true)
    List<BigInteger> getRemovedPreCommStageCreatedItrs(@Param("projectId") Long projectId);

    @Query(value = "SELECT pn.project_id AS projectId," +
        "          etpitr.itr_template AS itrTemplate, " +
        "          pn.entity_id AS entityId, " +
        "          pn.no AS entityNo, " +
        "          etpitr.check_sheet_stage AS checksheetStage, " +
        "          etpitr.check_sheet_type AS checkSheetType " +
        " FROM project_node pn LEFT JOIN itr it ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        " JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE pn.project_id = :projectId AND etpitr.check_sheet_stage = 'B' AND " +
        "it.check_sheet_stage = 'B' AND it.id IS NULL", nativeQuery = true)
    List<ItrCreateDTO> getCommStageCreatedItrs(@Param("projectId") Long projectId);

    @Query(value = "SELECT it.id " +
        " FROM itr it LEFT JOIN project_node pn ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id " +
        "  JOIN entity_type_process_itr_template_relation etpitr ON etpitr.entity_sub_type = pn.entity_sub_type " +
        " AND it.itr_check_template = etpitr.itr_template " +
        " WHERE it.project_id = :projectId AND pn.id IS NULL AND etpitr.check_sheet_stage = 'B' AND " +
        "it.check_sheet_stage = 'B'", nativeQuery = true)
    List<BigInteger> getRemovedCommStageCreatedItrs(@Param("projectId") Long projectId);

    Itr findByProjectIdAndEntityIdAndItrCheckTemplate(Long projectId, Long id, String no);

    @Query("SELECT DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.entitySubTypeId, c.nameCn, c.nameEn) "
        + "FROM Itr t "
        + " LEFT JOIN BpmEntitySubType c ON t.entitySubTypeId = c.id "
        + " WHERE t.projectId = :projectId "
        + " AND t.processId = :processId "
        + " AND t.status = 'ACTIVE' ")
    List<HierarchyBaseDTO> findEntitiyCategoriesInItr(@Param("projectId") Long projectId,
                                                      @Param("processId") Long processId);

    @Query("SELECT DISTINCT c.id "
        + "FROM Itr t LEFT JOIN BpmProcess p ON p.id = t.processId" +
        "  LEFT JOIN BpmProcessStage c ON p.processStage.id = c.id "
        + "WHERE t.projectId = :projectId AND t.status = 'ACTIVE'")
    Set<Long> findDistinctProcessStageId(@Param("projectId") Long projectId);

    @Query("SELECT DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.processId, c.nameCn, c.nameEn) "
        + "FROM Itr t LEFT JOIN BpmProcess c ON t.processId = c.id " +
        "  LEFT JOIN BpmProcessStage ps ON ps.id = c.processStage.id "
        + "WHERE t.projectId = :projectId "
        + " AND ps.id = :processStageId "
        + " AND t.status = 'ACTIVE'"
        + " AND c.projectId =:projectId "
//        + " AND c.discipline =:discipline "
    )
    List<HierarchyBaseDTO> findProcessesInItr(@Param("projectId") Long projectId,
                                              @Param("processStageId") Long processStageId);
//                                              @Param("discipline") String discipline);

    @Query("SELECT  DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.entitySubTypeId, c.nameCn, c.nameEn) "
        + "FROM Itr t LEFT JOIN BpmEntitySubType bec ON bec.id = t.entitySubTypeId " +
        "  JOIN BpmEntityType c ON bec.entityTypeId = c.id "
        + "WHERE t.projectId = :projectId "
        + "and t.processId = :processId AND t.status = 'ACTIVE'")
    List<HierarchyBaseDTO> findEntitiyCategoryTypesInItr(@Param("projectId") Long projectId, @Param("processId") Long processId);


    @Query(value = "SELECT  " +
        "        sub_system AS subSystemNo, " +
        "        `code` AS discipline, " +
        "        count(0) as itrTotal, " +
        "        SUM(submit_status) AS itrSubmit,  " +
        "        SUM(signed_status) AS itrSigned,  " +
        "        SUM(closed_status) AS itrClosed   " +
        " FROM  " +
        "       project_node_detail " +
        "       WHERE project_id = :projectId AND sub_system_entity_id = :subSystemId " +
        "       GROUP BY sub_system, `code`", nativeQuery = true)
    List<Tuple> getSubSystemItrStatistic(@Param("projectId") Long projectId,
                                         @Param("subSystemId") Long subSystemId);

    @Query(value = "SELECT  " +
        "subSystemNo, " +
        "discipline, " +
        "count(0) as itrTotal, " +
        "         SUM(submitStatus) AS itrSubmit,  " +
        "         SUM(signedStatus) AS itrSigned,  " +
        "         SUM(closedStatus) AS itrClosed  " +
        " " +
        "FROM  " +
        "( " +
        "SELECT   pn.entity_id, " +
        "     ess.no AS subSystemNo, " +
        "     pn.entity_type AS discipline,  " +
        "     MAX(CASE const_status_index WHEN 0 OR NULL THEN 1 ELSE 0 END) AS initStatus, " +
        "     MAX(CASE const_status_index WHEN 10 THEN 1 ELSE 0 END) AS startStatus, " +
        "     MAX(CASE const_status_index WHEN 20 THEN 1 ELSE 0 END) AS submitStatus, " +
        "     MAX(CASE const_status_index WHEN 30 THEN 1 ELSE 0 END) AS signedStatus, " +
        "     MAX(CASE const_status_index WHEN 40 THEN 1 ELSE 0 END) AS closedStatus, " +
        "     MAX(const_status_index)" +
        "  " +
        "         FROM  " +
        "         project_node pn JOIN hierarchy_node_relation hnr ON pn.entity_id = hnr.entity_id  " +
        "  LEFT JOIN construction_log cl ON cl.entity_id = pn.entity_id " +
        "         JOIN entity_sub_system ess on ess.id = hnr.ancestor_entity_id  " +
        "         WHERE   pn.project_id = :projectId AND hnr.ancestor_entity_id = :subSystemId AND " +
        "  pn.entity_type IN " +
        "('SPOOL','PIPE_COMPONENT','VALVE','EL_CABLE','IN_CABLE','TE_CABLE','ARCH_EQ','HVAC_EQ'," +
        "'MECH_EQ','SAFETY_EQ','HVAC_COMPONENT','EL_EQ','IN_EQ','TE_EQ') AND (pn.is_in_skid = 0 OR pn.is_in_skid IS NULL) " +
        "         GROUP BY ess.no, pn.entity_type, pn.entity_id " +
        "  ) aa GROUP BY aa.subSystemNo, aa.discipline", nativeQuery = true)
    List<Tuple> getOldSubSystemItrStatistic(@Param("projectId") Long projectId,
                                         @Param("subSystemId") Long subSystemId);


    @Query(value = "SELECT  " +
        "discipline, " +
        "count(0) as itrTotal, " +
        "         SUM(submitStatus) AS itrSubmit,  " +
        "         SUM(signedStatus) AS itrSigned,  " +
        "         SUM(closedStatus) AS itrClosed  " +
        " " +
        "FROM  " +
        "( " +
        "SELECT   " +
//        "     ess.no AS subSystemNo, " +
        "     pn.entity_type AS discipline,  " +
        "     MAX(CASE const_status_index WHEN 0 OR NULL THEN 1 ELSE 0 END) AS initStatus, " +
        "     MAX(CASE const_status_index WHEN 10 THEN 1 ELSE 0 END) AS startStatus, " +
        "     MAX(CASE const_status_index WHEN 20 THEN 1 ELSE 0 END) AS submitStatus, " +
        "     MAX(CASE const_status_index WHEN 30 THEN 1 ELSE 0 END) AS signedStatus, " +
        "     MAX(CASE const_status_index WHEN 40 THEN 1 ELSE 0 END) AS closedStatus, " +
        "     MAX(const_status_index)" +
        "  " +
        "         FROM  " +
        "         project_node pn JOIN hierarchy_node_relation hnr ON pn.entity_id = hnr.entity_id  " +
        "  LEFT JOIN construction_log cl ON cl.entity_id = pn.entity_id " +
//        "         JOIN entity_sub_system ess on ess.id = hnr.ancestor_entity_id  " +
        "         WHERE   pn.project_id = :projectId AND hnr.node_ancestor_id = :nodeId AND " +
        "  pn.entity_type IN " +
        "('SPOOL','PIPE_COMPONENT','VALVE','EL_CABLE','IN_CABLE','TE_CABLE','ARCH_EQ','HVAC_EQ'," +
        "'MECH_EQ','SAFETY_EQ','HVAC_COMPONENT','EL_EQ','IN_EQ','TE_EQ')  AND (pn.is_in_skid = 0 OR pn.is_in_skid IS NULL) " +
        "         GROUP BY pn.entity_type, pn.entity_id " +
        "  ) aa GROUP BY aa.discipline", nativeQuery = true)
    List<Tuple> getSubSystemItrStatisticSummary(@Param("projectId") Long projectId, @Param("nodeId") Long nodeId);

    List<Itr> findByProjectIdAndRunningStatus(Long projectId, EntityStatus approved);

    @Modifying
    @Transactional
    @Query("DELETE FROM Itr itr WHERE itr.projectId = :projectId AND itr.tridentItrId = :aitrId " +
        "AND itr.checkSheetTridentTbl = :cst AND itr.tridentPrintDate IS null AND itr.tridentCompletedDate IS NULL")
    void deleteInTridentStatus(@Param("projectId") Long projectId,
                               @Param("aitrId") Integer aitrid,
                               @Param("cst") CheckSheetTridentTbl cst);


    @Modifying
    @Transactional
    @Query("DELETE FROM Itr itr WHERE itr.projectId = :projectId AND itr.tridentItrId IS NULL " +
        "AND itr.checkSheetTridentTbl = :cst AND itr.tridentPrintDate IS null AND itr.tridentCompletedDate IS NULL")
    void deleteInTridentStatusNull(@Param("projectId") Long projectId,
                               @Param("cst") CheckSheetTridentTbl cst);

    @Query(value = "SELECT  " +
        " sub_system,  " +
        " COUNT(0) AS plTotal, " +
        " SUM(CASE WHEN accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plClosedTotal, " +
        " SUM(CASE WHEN category = 'A' THEN 1 ELSE 0 END) AS plATotal, " +
        " SUM(CASE WHEN category = 'A' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plAClosedTotal, " +
        " SUM(CASE WHEN category = 'B' THEN 1 ELSE 0 END) AS plBTotal, " +
        " SUM(CASE WHEN category = 'B' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plBClosedTotal, " +
        " SUM(CASE WHEN category = 'C' THEN 1 ELSE 0 END) AS plCTotal, " +
        " SUM(CASE WHEN category = 'C' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plCClosedTotal " +
        "  " +
        " " +
        " FROM punchlist  " +
        " WHERE sub_system = :subSystemNo  " +
        " GROUP BY sub_system ", nativeQuery = true)
    Tuple getPunchListInfo(@Param("subSystemNo") String no);

    @Query(value = "SELECT  " +
//        " sub_system,  " +
        " COUNT(0) AS plTotal, " +
        " SUM(CASE WHEN accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plClosedTotal, " +
        " SUM(CASE WHEN category = 'A' THEN 1 ELSE 0 END) AS plATotal, " +
        " SUM(CASE WHEN category = 'A' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plAClosedTotal, " +
        " SUM(CASE WHEN category = 'B' THEN 1 ELSE 0 END) AS plBTotal, " +
        " SUM(CASE WHEN category = 'B' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plBClosedTotal, " +
        " SUM(CASE WHEN category = 'C' THEN 1 ELSE 0 END) AS plCTotal, " +
        " SUM(CASE WHEN category = 'C' AND accept_date IS NOT NULL THEN 1 ELSE 0 END) AS plCClosedTotal " +
        "  " +
        " " +
        " FROM punchlist  " +
        " WHERE sub_system IN :subSystemNos ", nativeQuery = true)
    Tuple getPunchListInfo(@Param("subSystemNos") List<String> nos);


    @Query("SELECT i FROM " +
        " Itr i LEFT JOIN ConstructionLog cl ON i.entityId = cl.entityId AND cl.constStatus = 'SIGNED' " +
        " WHERE i.projectId = :projectId AND i.checkSheetStage = 'MCHECKSHEET' AND cl.id is null and i.tridentIsComplete = TRUE")
    List<Itr> getPendingItrs(@Param("projectId") Long projectId);

    Itr findByProjectIdAndTridentItrId(Long projectId, Integer aitrid);

    @Query("SELECT i FROM Itr i WHERE i.projectId = :projectId AND i.entityId = :entityId AND i.checkSheetStage = 'MCHECKSHEET' AND i.tridentSignedDate IS NULL")
    List<Itr> getUnCompletedItrs(@Param("projectId") Long projectId,
                                 @Param("entityId") Long entityId);

    @Transactional
    @Modifying
    @Query(value = "update itr i join project_node pn on pn.no = i.entity_no set i.entity_id = pn.entity_id WHERE pn.project_id = :projectId", nativeQuery = true)
    void updateEntityId(@Param("projectId") Long id);


    List<Itr> findByProjectIdAndEntityId(Long projectId, Long entityId);
}
