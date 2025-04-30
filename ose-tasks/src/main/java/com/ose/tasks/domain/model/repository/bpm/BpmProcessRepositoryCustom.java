package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.dto.bpm.ProcessCriteriaDTO;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BpmProcessRepositoryCustom {

    Page<BpmProcess> search(ProcessCriteriaDTO criteriaDTO, Long projectId, Long orgId);

    List<SelectDataDTO> findByDrawingIdAndUserId(Long drawingId, Long userId);
}
