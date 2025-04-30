package com.ose.tasks.domain.model.service.workinghour.impl;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.workinghour.ProjectWorkingHourHistoryRepository;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourHistoryInterface;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ProjectWorkingHourHistoryService implements ProjectWorkingHourHistoryInterface {

    private final ProjectWorkingHourHistoryRepository projectWorkingHourHistoryRepository;

    /**
     * 构造方法
     */
    @Autowired
    public ProjectWorkingHourHistoryService(ProjectWorkingHourHistoryRepository projectWorkingHourHistoryRepository) {
        this.projectWorkingHourHistoryRepository = projectWorkingHourHistoryRepository;
    }

    @Override
    public Page<ProjectWorkingHourHistoryEntity> getWorkingHourHistories(Long orgId, Long projectId, Long workingHourId, PageDTO pageDTO) {

        return projectWorkingHourHistoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourId(
            orgId, projectId, workingHourId, pageDTO.toPageable()
        );
    }
}
