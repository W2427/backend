package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;


public interface BpmActivityInstanceRepository extends PagingAndSortingWithCrudRepository<BpmActivityInstanceBase, Long>, BpmActivityInstanceRepositoryCustom {

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.id = :actInstId AND tt.finishState = :finishState AND tt.suspensionState = :suspensionState AND t.status = 'ACTIVE'")
    Optional<BpmActivityInstanceBase> findByProjectIdAndActInstIdAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("actInstId") Long actInstId,
        @Param("finishState") ActInstFinishState finishState,
        @Param("suspensionState") SuspensionState suspensionState);


    List<BpmActivityInstanceBase> findByProjectIdAndIdInAndStatus(Long projectId, Long[] actInstIds, EntityStatus status);

    @Query("SELECT DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.entitySubTypeId, c.nameCn, c.nameEn) "
        + "FROM BpmActivityInstanceBase t LEFT JOIN BpmEntitySubType c ON t.entitySubTypeId = c.id "
        + "WHERE t.projectId = :projectId "
        + "AND t.processStageId = :processStageId AND t.processId = :processId "
        + "AND t.entityTypeId = :entityTypeId AND t.status = 'ACTIVE' ")
    List<HierarchyBaseDTO> findEntitiyCategoriesInActivity(
        @Param("projectId") Long projectId,
        @Param("processStageId") Long processStageId,
        @Param("processId") Long processId,
        @Param("entityTypeId") Long entityTypeId
    );


    @Query("SELECT DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.processId, c.nameCn, c.nameEn) "
        + "FROM BpmActivityInstanceBase t LEFT JOIN BpmProcess c ON t.processId = c.id "
        + "WHERE t.projectId = :projectId "
        + " AND t.processStageId = :processStageId AND t.status = 'ACTIVE'")
    List<HierarchyBaseDTO> findProcessesInActivity(
        @Param("projectId") Long projectId,
        @Param("processStageId") Long processStageId
    );

    @Query("SELECT  DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.entityTypeId, c.nameCn, c.nameEn) "
        + "FROM BpmActivityInstanceBase t LEFT JOIN BpmEntityType c ON t.entityTypeId = c.id "
        + "WHERE t.projectId = :projectId "
        + "and t.processStageId = :processStageId AND t.processId = :processId AND t.status = 'ACTIVE'")
    List<HierarchyBaseDTO> findEntitiyCategoryTypesInActivity(
        @Param("projectId") Long projectId,
        @Param("processStageId") Long processStageId,
        @Param("processId") Long processId
    );


    List<BpmActivityInstanceBase> findByProjectIdAndEntityNo(Long projectId, String dwgNo);

    @Query("SELECT DISTINCT new com.ose.tasks.dto.bpm.HierarchyBaseDTO(t.processCategoryId, c.nameCn, c.nameEn) "
        + "FROM BpmActivityInstanceBase t LEFT JOIN BpmProcessCategory c ON t.processCategoryId = c.id "
        + "WHERE t.projectId = :projectId AND t.status = 'ACTIVE'")
    List<HierarchyBaseDTO> findProcessCategoryByProjectId(@Param("projectId") Long projectId);

    BpmActivityInstanceBase findByProjectIdAndEntityIdAndVersion(Long projectId, Long entityId, String latestRev);

    @Query("SELECT distinct t.entityId FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState ts ON t.id = ts.baiId "
        + "WHERE t.projectId = :projectId "
        + " AND t.entityType = :entityType and ts.finishState = :finishState and ts.suspensionState = :suspensionState AND t.status = 'ACTIVE'")
    List<Long> findEntityIdsByProjectIdAndEntityTypeAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("entityType") String entityType,
        @Param("finishState") ActInstFinishState finishState,
        @Param("suspensionState") SuspensionState suspensionState
    );

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.entityId = :entityId AND t.processId = :processId AND tt.finishState = :state AND tt.suspensionState = :status AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndProcessIdAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("processId") Long processId,
        @Param("state") ActInstFinishState state,
        @Param("status") SuspensionState status);

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.entityId = :entityId AND t.processId = :processId AND tt.finishState = :state AND tt.suspensionState = :status AND t.status = 'ACTIVE'")
    BpmActivityInstanceBase findFirstByProjectIdAndEntityIdAndProcessIdAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("processId") Long processId,
        @Param("state") ActInstFinishState state,
        @Param("status") SuspensionState status);

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.entityId = :entityId AND tt.finishState = :state AND tt.suspensionState = :status AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(@Param("projectId") Long projectId,
                                                                                             @Param("entityId") Long entityId,
                                                                                             @Param("state") ActInstFinishState state,
                                                                                             @Param("status") SuspensionState status);

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.entityId = :entityId AND t.process = :process AND tt.finishState = :state and tt.suspensionState = :status AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndProcessAndFinishStateAndSuspensionState(@Param("projectId") Long projectId,
                                                                                                       @Param("entityId") Long entityId,
                                                                                                       @Param("process") String drawingRedmark,
                                                                                                       @Param("state") ActInstFinishState state,
                                                                                                       @Param("status") SuspensionState status);

    @Query("SELECT MAX(t.version) "
        + "FROM BpmActivityInstanceBase t "
        + "WHERE t.projectId = :projectId "
        + "AND t.entityId = :entityId ")
    String findMaxVersionByProjectIdAndEntityId(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId
    );

    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.entityId = :entityId AND t.version = :version AND tt.suspensionState = :suspensionState ORDER BY t.createdAt DESC ")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndVersionAndSuspensionStateOrderByCreatedAtDesc(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("version") String rev,
        @Param("suspensionState") SuspensionState suspensionState
    );


    @Query("SELECT t FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.entityId = :entityId AND t.process = :process AND t.version = :version AND tt.suspensionState = :state and t.status = :status")
    BpmActivityInstanceBase findByProjectIdAndEntityIdAndProcessAndVersionAndSuspensionStateAndStatus(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("process") String process,
        @Param("version") String latestRev,
        @Param("state") SuspensionState suspensionState,
        @Param("status") EntityStatus status
    );

    BpmActivityInstanceBase findByProjectIdAndIdAndStatus(Long projectId, Long actInstId, EntityStatus status);


    @Query(value = "SELECT t FROM BpmActivityInstanceBase t LEFT JOIN " +
        " BpmRuTask b ON t.id = b.actInstId " +
        "WHERE t.projectId = :projectId AND t.id = b.actInstId AND b.id IN :taskIds")
    List<BpmActivityInstanceBase> findByProjectIdAndTaskIdIn(@Param("projectId") Long projectId,
                                                             @Param("taskIds") List<Long> taskIds);

    @Query("SELECT distinct new com.ose.tasks.dto.bpm.HierarchyBaseDTO(0L, b.name, b.taskDefKey) "
        + "FROM BpmActivityInstanceBase t JOIN BpmRuTask b on t.id = b.actInstId "
        + "WHERE t.projectId = :projectId "
        + "And t.processStageId = :processStageId "
        + "AND t.processId = :processId "
        + "AND t.status = 'ACTIVE'")
    List<HierarchyBaseDTO> findTaskDefKeyInActivity(
        @Param("projectId") Long projectId,
        @Param("processStageId") Long processStageId,
        @Param("processId") Long processId);




    Optional<BpmActivityInstanceBase> findByProjectIdAndId(Long projectId, Long actInstId);


    List<BpmActivityInstanceBase> findByProjectIdAndEntityId(Long projectId, Long entityId);

    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndProcessStageAndProcessOrderByVersionDesc(Long projectId, Long entityId, String processStage, String process);



    @Query("SELECT t.processStageId "
        + "FROM BpmActivityInstanceBase t  "
        + "WHERE t.projectId = :projectId AND t.status = 'ACTIVE'")
    Set<Long> findDistinctProcessStageId(@Param("projectId") Long projectId);

    BpmActivityInstanceBase findByOrgIdAndProjectIdAndId(
        Long orgId,
        Long projectId,
        Long id
    );



    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.process in :processes " +
        " AND b.entityId = :entityId " +
        " AND bs.finishState = 'NOT_FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityIdAndProcessInAndStatus(@Param("projectId")Long projectId,
                                                                                  @Param("entityId")Long entityId,
                                                                                  @Param("processes")Set<String> processes);

    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.process = 'ENGINEERING' " +
        " AND bs.finishState = 'FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceBase> findByProjectIdAndProcessNameCn(
        @Param("projectId")Long projectId);

    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.process = 'ENGINEERING' " +
        " AND bs.finishState = 'FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' " +
        " AND b.planEndDate > bs.endDate ")
    List<BpmActivityInstanceBase> findByProjectIdAndPlanEndDate(
        @Param("projectId")Long projectId);

    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.process = 'NDT' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceBase> findByProjectIdAndFinishStateIsNull(
        @Param("projectId")Long projectId);

    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.process = 'NDT' " +
        " AND bs.finishState = 'FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceBase> findByProjectIdAndFinishStateIsFinished(
        @Param("projectId")Long projectId);

    @Query("SELECT b FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.entityNo = :entityNo " +
        " AND bs.finishState = 'NOT_FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceBase> findByProjectIdAndEntityNoAndFinishStateIsRunning(
        @Param("projectId")Long projectId,
        @Param("entityNo")String entityNo);
}

