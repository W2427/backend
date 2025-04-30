package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActInstVariableConfig;
import com.ose.vo.EntityStatus;

public interface BpmActInstVariableConfigRepository
    extends PagingAndSortingRepository<BpmActInstVariableConfig, Long> {

    BpmActInstVariableConfig findByNameAndProcessKey(String name, String processKey);

    List<BpmActInstVariableConfig> findByProcessKey(String processKey);

    List<BpmActInstVariableConfig> findByProcessKeyAndStatus(String processKey, EntityStatus status);

}
