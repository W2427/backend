package com.ose.tasks.domain.model.service.workinghour;

import com.ose.dto.PageDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourHistoryEntity;
import org.springframework.data.domain.Page;

/**
 * service接口
 */
public interface ProjectWorkingHourHistoryInterface {

    Page<ProjectWorkingHourHistoryEntity> getWorkingHourHistories(Long orgId, Long projectId, Long workingHourId, PageDTO pageDTO);
}
