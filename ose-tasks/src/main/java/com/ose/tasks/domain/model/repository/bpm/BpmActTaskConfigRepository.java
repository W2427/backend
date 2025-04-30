package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.vo.EntityStatus;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;


/**
 * 流程代理类 CRUD 操作接口。
 */
@Transactional
public interface BpmActTaskConfigRepository extends PagingAndSortingWithCrudRepository<BpmActTaskConfig, Long>, BpmActTaskConfigRepositoryCustom {

    List<BpmActTaskConfig> findByOrgIdAndProjectIdAndProcessCategoryAndProcessTypeAndTaskDefKeyAndDelegateTypeAndStatusOrderByOrderNoAsc(
        Long orgId,
        Long projectId,
        String processCategory,
        ProcessType processType,
        String taskDefKey,
        BpmActTaskConfigDelegateType delegateType,
        EntityStatus status);

    List<BpmActTaskConfig> findByOrgIdAndProjectIdAndProcessCategoryAndProcessTypeAndTaskDefKeyAndDelegateStageAndStatusOrderByOrderNoAsc(
        Long orgId,
        Long projectId,
        String processCategory,
        ProcessType processType,
        String taskDefKey,
        BpmActTaskConfigDelegateStage delegateStage,
        EntityStatus status);


    @Query(value = "SELECT proxy,delegate_type,delegate_stage,order_no,task_def_key " +
        "FROM " +
        "( " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 1 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_def_key = :taskDefKey AND org_id = :orgId AND project_id = :projectId AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 2 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_type = :taskType AND org_id = :orgId AND project_id = :projectId AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 3 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_key = :processKey AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 4 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_type = :processType AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 5 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_category = :processCategory AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 6 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_def_key = 'UT' AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        ") aa " +
        "GROUP BY proxy,delegate_type,delegate_stage,order_no,task_def_key, colseq " +
        "ORDER BY delegate_stage desc,order_no, colseq",
        nativeQuery = true)
    List<Tuple> findAllByTaskDefKey(@Param("orgId") Long orgId,
                                    @Param("projectId") Long projectId,
                                    @Param("taskDefKey") String taskDefKey,
                                    @Param("taskType") String taskType,
                                    @Param("processKey") String processKey,
                                    @Param("processType") String processType,
                                    @Param("processCategory") String processCategory,
                                    @Param("active") String status);


    @Query(value = "SELECT proxy,delegate_type,delegate_stage,order_no,task_def_key " +
        "FROM " +
        "( " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 1 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_def_key = :taskDefKey AND org_id = :orgId AND project_id = :projectId AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 2 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_type = :taskType AND org_id = :orgId AND project_id = :projectId AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 3 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_key = :processKey AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 4 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_type = :processType AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 5 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_category = :processCategory AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 6 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_def_key = 'UT' AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        ") aa WHERE delegate_stage = 'PREPARE' " +
        "GROUP BY proxy,delegate_type,delegate_stage,order_no,task_def_key, colseq " +
        "ORDER BY delegate_stage desc,order_no, colseq",
        nativeQuery = true)
    List<Tuple> findPrepareByTaskDefKey(@Param("orgId") Long orgId,
                                    @Param("projectId") Long projectId,
                                    @Param("taskDefKey") String taskDefKey,
                                    @Param("taskType") String taskType,
                                    @Param("processKey") String processKey,
                                    @Param("processType") String processType,
                                    @Param("processCategory") String processCategory,
                                    @Param("active") String status);

    /**
     * 创建需要的代理清单
     *
     * @param orgId
     * @param projectId
     * @param processKey
     * @param processType
     * @param processCategory
     * @param status
     * @return
     */
    @Query(value = "SELECT proxy,delegate_type,delegate_stage,order_no,task_def_key " +
        "FROM " +
        "( " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 1 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_key = :processKey AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 2 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_type = :processType AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 3 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE process_category = :processCategory AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        "UNION ALL " +
        "   SELECT " +
        "       proxy,delegate_type,delegate_stage,order_no,task_def_key, 4 AS colseq " +
        "   FROM bpm_act_task_config " +
        "   WHERE task_def_key = 'UT' AND org_id = :orgId AND project_id = :projectId  AND status = :active " +
        ") aa " +
        "GROUP BY proxy,delegate_type,delegate_stage,order_no,task_def_key, colseq " +
        "ORDER BY delegate_stage desc,order_no, colseq",
        nativeQuery = true)
    List<Tuple> findAllForCreate(@Param("orgId") Long orgId,
                                 @Param("projectId") Long projectId,
                                 @Param("processKey") String processKey,
                                 @Param("processType") String processType,
                                 @Param("processCategory") String processCategory,
                                 @Param("active") String status);

    List<BpmActTaskConfig> findByOrgIdAndProjectIdAndStatus(Long oldOrgId, Long oldProjectId, EntityStatus status);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_act_task_config WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);
}
