package com.ose.tasks.domain.model.repository.bpm;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActInstVariableTaskConfig;
import com.ose.vo.EntityStatus;

public interface BpmActInstVariableTaskConfigRepository
    extends PagingAndSortingRepository<BpmActInstVariableTaskConfig, Long> {

    BpmActInstVariableTaskConfig findByProcessKeyAndTaskDefKeyAndVariableName(String processKey, String taskDefKey, String variableName);

    BpmActInstVariableTaskConfig findByProcessKeyAndTaskDefKeyAndVariableNameAndStatus(String processKey, String taskDefKey,
                                                                                       String variableName, EntityStatus status);

}
