package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.dto.bpm.ExInspActInstHandleHistoryDTO;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExInspActInstHandleHistoryRepositoryCustom {
    Page<ExInspActInstHandleHistory> search(
        Long orgId,
        Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO
    );
}
