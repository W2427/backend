package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActInstVariableValue;


public interface BpmActInstVariableValueRepository extends PagingAndSortingWithCrudRepository<BpmActInstVariableValue, Long> {

    BpmActInstVariableValue findByOrgIdAndProjectIdAndActInstIdAndVariableName(Long orgId, Long projectId,
                                                                                Long actInstId, String variableName);

    List<BpmActInstVariableValue> findByActInstId(Long actInstId);

}
