package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;

public interface BpmHiTaskinstRepositoryCustom {

    List<Long> findActInstIdsInHiTaskinst(Long projectId, TodoTaskCriteriaDTO taskCriteria, String assignee);

    Integer countWeldByOperator(Long orgId, Long projectId, Long operatorId);
}
