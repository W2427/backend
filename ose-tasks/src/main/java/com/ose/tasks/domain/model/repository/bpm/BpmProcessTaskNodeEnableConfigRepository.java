package com.ose.tasks.domain.model.repository.bpm;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmProcessTaskNodeEnableConfig;

public interface BpmProcessTaskNodeEnableConfigRepository extends PagingAndSortingRepository<BpmProcessTaskNodeEnableConfig, Long> {

    BpmProcessTaskNodeEnableConfig findByProcessAndProcessStageAndTaskDefKeyAndType(String process, String processStage, String taskDefKey,
                                                                                    String clientType);


}
