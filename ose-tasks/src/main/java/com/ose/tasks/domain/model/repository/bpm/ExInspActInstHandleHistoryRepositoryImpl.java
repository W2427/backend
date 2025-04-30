package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.ExInspActInstHandleHistoryDTO;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.IdentityHashMap;
import java.util.Map;

@Transactional
public class ExInspActInstHandleHistoryRepositoryImpl extends BaseRepository implements ExInspActInstHandleHistoryRepositoryCustom {

    public Page<ExInspActInstHandleHistory> search(
        Long orgId,
        Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO
    ) {
        SQLQueryBuilder<ExInspActInstHandleHistory> sqlQueryBuilder = getSQLQueryBuilder(ExInspActInstHandleHistory.class)
            .is("orgId", orgId)
            .is("projectId", projectId);

        if (exInspActInstHandleHistoryDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", exInspActInstHandleHistoryDTO.getKeyword());
            keywordCriteria.put("exInspScheduleNo", operator);
            keywordCriteria.put("seriesNo", operator);
            sqlQueryBuilder.or(keywordCriteria);
        }
        if (exInspActInstHandleHistoryDTO.getRunningStatus() != null) {
            if (exInspActInstHandleHistoryDTO.getRunningStatus().equals("ACTIVE")) {
                sqlQueryBuilder.is("runningStatus", EntityStatus.ACTIVE);
            } else if (exInspActInstHandleHistoryDTO.getRunningStatus().equals("APPROVED")) {
                sqlQueryBuilder.is("runningStatus", EntityStatus.APPROVED);
            } else if (exInspActInstHandleHistoryDTO.getRunningStatus().equals("DISABLED")) {
                sqlQueryBuilder.is("runningStatus", EntityStatus.DISABLED);
            }

        }
        return sqlQueryBuilder
            .paginate(exInspActInstHandleHistoryDTO.toPageable())
            .exec()
            .page();
    }

}
