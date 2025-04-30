package com.ose.tasks.domain.model.repository.bpm;

import org.springframework.data.domain.Page;

import com.ose.tasks.dto.bpm.EntitySubTypeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;

public interface BpmEntitySubTypeRepositoryCustom {

    Page<BpmEntitySubType> getList(Long orgId, Long projectId, EntitySubTypeCriteriaDTO page);

}
