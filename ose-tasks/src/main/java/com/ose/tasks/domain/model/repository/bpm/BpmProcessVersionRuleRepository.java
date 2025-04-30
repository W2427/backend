package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessCategory;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.entity.bpm.BpmProcessVersionRule;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * CRUD 操作接口。
 */
@Transactional
public interface BpmProcessVersionRuleRepository extends PagingAndSortingWithCrudRepository<BpmProcessVersionRule, Long>{
    BpmProcessVersionRule findByOrgIdAndProjectIdAndProcessId(Long orgId, Long projectId, Long processId);

    @Query(value = "SELECT " +
        "  vr.*  " +
        "FROM " +
        "  `bpm_process_version_rule` vr " +
        "  LEFT JOIN bpm_process p ON p.id = vr.process_id " +
        "WHERE " +
        "  vr.org_id = :orgId " +
        "  AND vr.project_id = :projectId " +
        "  AND p.name_en = :process ",
        nativeQuery = true)
    BpmProcessVersionRule findByProcessName(@Param("orgId") Long orgId,
                                @Param("projectId") Long projectId,
                                @Param("process") String process);
}
