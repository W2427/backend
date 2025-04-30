package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.vo.bpm.ExInspApplyHandleType;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 流程代理类 CRUD 操作接口。
 */
@Transactional
public interface ExInspActInstHandleHistoryRepository extends PagingAndSortingWithCrudRepository<ExInspActInstHandleHistory, Long>, ExInspActInstHandleHistoryRepositoryCustom {

    List<ExInspActInstHandleHistory> findByOrgIdAndProjectIdAndExInspScheduleNoAndRunningStatus(
        Long orgId,
        Long projectId,
        String exInspScheduleNo,
        EntityStatus runningStatus
    );

    List<ExInspActInstHandleHistory> findByOrgIdAndProjectIdAndTypeAndRunningStatus(
        Long orgId,
        Long projectId,
        ExInspApplyHandleType type,
        EntityStatus runningStatus
    );

    Optional<ExInspActInstHandleHistory> findByProjectIdAndSeriesNoAndTypeAndRunningStatus(Long projectId, String seriesNo, ExInspApplyHandleType type, EntityStatus runningStatus);

    List<ExInspActInstHandleHistory> findByOrgIdAndProjectIdAndTypeAndRunningStatusAndEntityNosIn(
        Long orgId,
        Long projectId,
        ExInspApplyHandleType type,
        EntityStatus runningStatus,
        List<String> entityNos
    );
}
