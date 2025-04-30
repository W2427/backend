package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.ExInspScheduleCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import org.springframework.data.domain.Page;

public interface BpmExInspScheduleRepositoryCustom {

//    List<BpmExInspSchedule> findByOrgIdAndProjectIdOrderByCreatedAtDesc(Long orgId, Long projectId, PageDTO page, ExInspScheduleCriteriaDTO criteriaDTO);


    Page<BpmExInspSchedule> findByOrgIdAndProjectIdOrderByCreatedAtDesc(Long orgId, Long projectId, PageDTO page, ExInspScheduleCriteriaDTO criteriaDTO);



}
