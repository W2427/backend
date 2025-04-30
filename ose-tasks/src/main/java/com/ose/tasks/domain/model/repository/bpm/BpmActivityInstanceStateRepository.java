package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBlob;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface BpmActivityInstanceStateRepository extends PagingAndSortingWithCrudRepository<BpmActivityInstanceState, Long> {


    @Query("SELECT b FROM BpmActivityInstanceBlob b WHERE b.baiId = :actInstId")
    BpmActivityInstanceBlob findActInstByActInstId(@Param("actInstId") Long actInstId);

    @Query("SELECT tt FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.id = :actInstId AND tt.finishState = :finishState AND tt.suspensionState = :suspensionState AND t.status = 'ACTIVE'")
    Optional<BpmActivityInstanceState> findByProjectIdAndActInstIdAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("actInstId") Long actInstId,
        @Param("finishState") ActInstFinishState finishState,
        @Param("suspensionState") SuspensionState suspensionState);

    BpmActivityInstanceState findByProjectIdAndBaiId(Long actInstId, Long baiId);

    @Query("SELECT tt FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.entityId = :entityId AND t.process = :process AND tt.finishState = :finishState AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceState> findByProjectIdAndEntityIdAndFinishStateAndProcess(@Param("projectId") Long projectId,
                                                                                      @Param("entityId") Long entityId,
                                                                                      @Param("finishState") ActInstFinishState finishState,
                                                                                      @Param("process") String process);

    @Query("SELECT tt FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.entityId = :entityId AND t.process = :process AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceState> findByProjectIdAndEntityIdAndProcessOrderByVersionDesc(@Param("projectId") Long projectId,
                                                                                         @Param("entityId") Long entityId,
                                                                                         @Param("process") String process);

    List<BpmActivityInstanceState> findByProjectIdAndEntityId(Long projectId, Long entityId);

    @Query("SELECT tt FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "and t.entityId = :entityId AND t.processId = :processId AND tt.suspensionState = :suspensionState")
    List<BpmActivityInstanceState> findByProjectIdAndEntityIdAndProcessIdAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("processId") Long bpmProcessId,
        @Param("suspensionState") SuspensionState state);

    @Query("SELECT tt FROM BpmActivityInstanceBase t JOIN BpmActivityInstanceState tt ON t.id = tt.baiId "
        + "WHERE t.projectId = :projectId "
        + "AND t.processStageId = :processStageId AND t.processId = :processId "
        + "AND tt.finishState = :finishState AND tt.suspensionState = :suspensionState")
    List<BpmActivityInstanceState> findByProjectIdAndProcessIdAndFinishStateAndSuspensionState(@Param("projectId") Long projectId,
                                                                                               @Param("processStageId") Long processStageId,
                                                                                               @Param("processId") Long processId,
                                                                                               @Param("finishState") ActInstFinishState finishState,
                                                                                               @Param("suspensionState") SuspensionState status);

    @Query("SELECT DISTINCT t.workSiteAddress FROM BpmActivityInstanceState t "
        + "WHERE t.projectId = :projectId "
        + "and t.workSiteAddress IS NOT NULL")
    List<BpmActivityInstanceState> findByProjectIdAndStatus(
        @Param("projectId") Long projectId
    );
    List<BpmActivityInstanceState> findByProjectId(Long projectId);

    @Query("SELECT t FROM BpmActivityInstanceState t "
        + "WHERE t.projectId = :projectId "
        + "AND t.workSiteName IS NULL "
        + "AND t.workSiteId IS NOT NULL")
    List<BpmActivityInstanceState> findByProjectIdAndTeamIdAndTeamName(@Param("projectId") Long projectId);

    @Query("SELECT t FROM BpmActivityInstanceState t "
        + "WHERE t.projectId = :projectId "
        + "AND t.teamName IS NULL "
        + "AND t.teamId IS NOT NULL")
    List<BpmActivityInstanceState> findTeamNameByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM BpmActivityInstanceState t "
        + "WHERE t.baiId = :baiId ")
    BpmActivityInstanceState findByBaiId(@Param("baiId") Long id);

    @Query(value ="SELECT t.wps_no FROM BpmActivityInstanceState t "
        + "WHERE t.baiId = :baiId ", nativeQuery=true)
    String findWpsNoByBaiId(@Param("baiId") Long id);

    @Query("SELECT t FROM BpmActivityInstanceState t "
        + "WHERE t.baiId IN :baiIds ")
    Iterable<BpmActivityInstanceState> findByBaiIdIn(@Param("baiIds") List<Long> actInstIds);


    @Query("SELECT t FROM BpmActivityInstanceState t "
        + "WHERE t.projectId = :projectId" +
        " AND t.baiId IN :baiIds ")
    List<BpmActivityInstanceState> findByProjectIdAndBaiIdIn(
        @Param("projectId") Long projectId,
        @Param("baiIds") List<Long> actInstIds
    );

    @Query(" SELECT tt FROM BpmActivityInstanceBase t "
        + " JOIN BpmActivityInstanceState tt "
        + " ON t.id = tt.baiId "
        + " WHERE t.projectId = :projectId "
        + " AND t.entityNo IN :entityNos "
        + " AND t.processStage = :processStage "
        + " AND t.process = :process "
        + " AND t.entityNo IN :entityNos "
        + " AND tt.projectId = :projectId "
        + " AND tt.finishState = :finishState "
        + " AND tt.suspensionState = :suspensionState "
        + " AND t.status = 'ACTIVE'")
    List<BpmActivityInstanceState> findByProjectIdAndEntityNosAndFinishStateAndSuspensionState(
        @Param("projectId") Long projectId,
        @Param("entityNos") List<String> entityNos,
        @Param("processStage") String processStage,
        @Param("process") String process,
        @Param("finishState") ActInstFinishState finishState,
        @Param("suspensionState") SuspensionState suspensionState);

    @Query("SELECT bs FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.entityId = :entityId " +
        " AND b.process in :processes " +
        " AND bs.finishState = 'NOT_FINISHED' " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceState> findByProjectIdAndEntityIdAndProcessIn(
        @Param("projectId")Long projectId,
        @Param("entityId")Long entityId,
        @Param("processes")List<String> processes);

    @Query("SELECT bs FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " INNER JOIN BpmRuTask brt " +
        " ON brt.actInstId = b.id " +
        " WHERE b.projectId = :projectId " +
        " AND brt.id = :taskId ")
    BpmActivityInstanceState findByProjectIdAndTaskId(
        @Param("projectId")Long projectId,
        @Param("taskId")String taskId);

    @Query("SELECT bs FROM BpmActivityInstanceBase b " +
        " INNER JOIN BpmActivityInstanceState bs " +
        " ON b.id = bs.baiId " +
        " WHERE b.projectId = :projectId " +
        " AND b.id in :actInstIds " +
        " AND bs.suspensionState = 'ACTIVE' ")
    List<BpmActivityInstanceState> findByProjectIdAndActInstIdIn(
        @Param("projectId")Long projectId,
        @Param("actInstIds") Set<Long> actInstIds);

//    @Transactional
//    @Modifying
//    @Query("UPDATE BpmActivityInstanceState bais SET bais.currentExecutor")
//    void updateCurrentSelectForGateway(Long id, String value);
}

