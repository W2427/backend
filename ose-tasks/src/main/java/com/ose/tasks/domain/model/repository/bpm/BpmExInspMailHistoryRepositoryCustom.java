package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.dto.bpm.ExInspTaskEmailDTO;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BpmExInspMailHistoryRepositoryCustom{

    Page<BpmExInspMailHistory> getTaskEmailList(Long orgId, Long projectId, ExInspTaskEmailDTO criteriaDTO);

    List<ExInspTaskEmailDTO> getOperatorList(Long orgId, Long projectId);

}

