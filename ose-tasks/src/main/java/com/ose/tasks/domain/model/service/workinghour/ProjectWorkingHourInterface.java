package com.ose.tasks.domain.model.service.workinghour;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchStatusDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPostDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourSearchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * service接口
 */
public interface ProjectWorkingHourInterface extends EntityInterface {

    void deleteWorkingHour(Long orgId, Long projectId, Long workingHourId);

    ProjectWorkingHourEntity postWorkingHour(Long orgId, Long projectId, OperatorDTO operatorDTO, ProjectWorkingHourPostDTO projectWorkingHourPostDTO);

    List<ProjectWorkingHourEntity> getWorkingHours(Long orgId, Long projectId, OperatorDTO operatorDTO, ProjectWorkingHourSearchDTO projectWorkingHourSearchDTO);

    ProjectWorkingHourEntity getWorkingHour(Long orgId, Long projectId, Long workingHourId);

    void patchWorkingHour(Long orgId, Long projectId, Long workingHourId, OperatorDTO operatorDTO, ProjectWorkingHourPatchDTO projectWorkingHourPatchDTO);

    void patchWorkingHourStatus(
        Long orgId,
        Long projectId,
        Long workingHourId,
        ProjectWorkingHourStatusType toProjectWorkingHourStatus,
        ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO,
        OperatorDTO operatorDTO);

    Page<ProjectWorkingHourEntity> getWorkingHoursForApproval(Long orgId, Long projectId, OperatorDTO operatorDTO, PageDTO pageDTO);
}
