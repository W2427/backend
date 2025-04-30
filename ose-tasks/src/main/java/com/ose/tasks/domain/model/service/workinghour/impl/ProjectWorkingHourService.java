package com.ose.tasks.domain.model.service.workinghour.impl;

import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.workinghour.ProjectWorkingHourCategoryRepository;
import com.ose.tasks.domain.model.repository.workinghour.ProjectWorkingHourHistoryRepository;
import com.ose.tasks.domain.model.repository.workinghour.ProjectWorkingHourRepository;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourInterface;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchStatusDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPostDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourSearchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourEntity;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourHistoryEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectWorkingHourService implements ProjectWorkingHourInterface {

    private final ProjectWorkingHourRepository projectWorkingHourRepository;

    private final ProjectWorkingHourHistoryRepository projectWorkingHourHistoryRepository;

    private final ProjectWorkingHourCategoryRepository projectWorkingHourCategoryRepository;

    /**
     * 构造方法
     *
     * @param projectWorkingHourRepository
     * @param projectWorkingHourHistoryRepository
     * @param projectWorkingHourCategoryRepository
     */
    @Autowired
    public ProjectWorkingHourService(
        ProjectWorkingHourRepository projectWorkingHourRepository,
        ProjectWorkingHourHistoryRepository projectWorkingHourHistoryRepository,
        ProjectWorkingHourCategoryRepository projectWorkingHourCategoryRepository
    ) {
        this.projectWorkingHourRepository = projectWorkingHourRepository;
        this.projectWorkingHourHistoryRepository = projectWorkingHourHistoryRepository;
        this.projectWorkingHourCategoryRepository = projectWorkingHourCategoryRepository;
    }

    @Override
    public void deleteWorkingHour(Long orgId, Long projectId, Long workingHourId) {
        Optional<ProjectWorkingHourEntity> projectWorkingHourEntityOptional =
            projectWorkingHourRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourId);
        if (!projectWorkingHourEntityOptional.isPresent()) {

            throw new NotFoundError();
        }

        ProjectWorkingHourEntity projectWorkingHourEntity = projectWorkingHourEntityOptional.get();

        if (ProjectWorkingHourStatusType.APPROVED == projectWorkingHourEntity.getWorkingHourStatus()) {

            throw new BusinessError("approved working hour can not be delete");
        }

        List<ProjectWorkingHourHistoryEntity> projectWorkingHourHistoryEntityList =
            projectWorkingHourHistoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourId(orgId, projectId, workingHourId);

        projectWorkingHourRepository.delete(projectWorkingHourEntity);
        projectWorkingHourHistoryRepository.deleteAll(projectWorkingHourHistoryEntityList);
    }

    @Override
    public ProjectWorkingHourEntity postWorkingHour(Long orgId, Long projectId, OperatorDTO operatorDTO, ProjectWorkingHourPostDTO projectWorkingHourPostDTO) {

        ProjectWorkingHourEntity projectWorkingHourEntity = new ProjectWorkingHourEntity();
        projectWorkingHourEntity.setOrgId(orgId);
        projectWorkingHourEntity.setProjectId(projectId);
        projectWorkingHourEntity.setProjectWorkingHourDate(projectWorkingHourPostDTO.getProjectWorkingHourDate());
        projectWorkingHourEntity.setProjectWorkingHoursLargeCategory(projectWorkingHourPostDTO.getProjectWorkingHoursLargeCategory());
        projectWorkingHourEntity.setProjectWorkingHoursSmallCategory(projectWorkingHourPostDTO.getProjectWorkingHoursSmallCategory());
        projectWorkingHourEntity.setWorkingHoursTaskPackage(projectWorkingHourPostDTO.getWorkingHoursTaskPackage());
        projectWorkingHourEntity.setRelatedDrawings(projectWorkingHourPostDTO.getRelatedDrawings());
        projectWorkingHourEntity.setWorkContent(projectWorkingHourPostDTO.getWorkContent());
        projectWorkingHourEntity.setWorkingHours(projectWorkingHourPostDTO.getWorkingHours());
        projectWorkingHourEntity.setWorkingHourStatus(ProjectWorkingHourStatusType.NOT_SUBMITTED);

        projectWorkingHourEntity.setApprovalId(projectWorkingHourPostDTO.getApprovalId());
        projectWorkingHourEntity.setCreatedAt();
        projectWorkingHourEntity.setCreatedBy(operatorDTO.getId());
        projectWorkingHourEntity.setStatus(EntityStatus.ACTIVE);
        projectWorkingHourEntity.setLastModifiedAt();
        projectWorkingHourEntity.setLastModifiedBy(operatorDTO.getId());

        return projectWorkingHourRepository.save(projectWorkingHourEntity);
    }

    @Override
    public List<ProjectWorkingHourEntity> getWorkingHours(
        Long orgId, Long projectId, OperatorDTO operatorDTO, ProjectWorkingHourSearchDTO projectWorkingHourSearchDTO
    ) {

        return projectWorkingHourRepository.findByOrgIdAndProjectIdAndCreatedByAndProjectWorkingHourDateBetweenOrderByProjectWorkingHourDateAscCreatedAtAsc(
            orgId,
            projectId,
            operatorDTO.getId(),
            projectWorkingHourSearchDTO.getStartDate(),
            projectWorkingHourSearchDTO.getEndDate()
        );
    }

    @Override
    public Page<ProjectWorkingHourEntity> getWorkingHoursForApproval(Long orgId, Long projectId, OperatorDTO operatorDTO, PageDTO pageDTO) {

        return projectWorkingHourRepository.findByOrgIdAndProjectIdAndApprovalIdAndWorkingHourStatusOrderByProjectWorkingHourDateAscCreatedAtAsc(
            orgId,
            projectId,
            operatorDTO.getId(),
            ProjectWorkingHourStatusType.SUBMITTED,
            pageDTO.toPageable()
        );
    }

    @Override
    public ProjectWorkingHourEntity getWorkingHour(Long orgId, Long projectId, Long workingHourId) {
        return projectWorkingHourRepository.findByOrgIdAndProjectIdAndId(
            orgId, projectId, workingHourId
        ).orElse(null);
    }

    @Override
    public void patchWorkingHour(
        Long orgId,
        Long projectId,
        Long workingHourId,
        OperatorDTO operatorDTO,
        ProjectWorkingHourPatchDTO projectWorkingHourPatchDTO
    ) {

        Optional<ProjectWorkingHourEntity> projectWorkingHourEntityOptional = projectWorkingHourRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourId);
        if (!projectWorkingHourEntityOptional.isPresent()) {
            throw new NotFoundError();
        }

        ProjectWorkingHourEntity projectWorkingHourEntity = projectWorkingHourEntityOptional.get();

        if (ProjectWorkingHourStatusType.SUBMITTED == projectWorkingHourEntity.getWorkingHourStatus()) {

            throw new BusinessError("submitted working hour can not be modified");
        } else if (ProjectWorkingHourStatusType.APPROVED == projectWorkingHourEntity.getWorkingHourStatus()) {

            throw new BusinessError("approved working hour can not be deleted");
        }

        projectWorkingHourEntity.setWorkingHoursTaskPackage(projectWorkingHourPatchDTO.getWorkingHoursTaskPackage());
        projectWorkingHourEntity.setRelatedDrawings(projectWorkingHourPatchDTO.getRelatedDrawings());
        projectWorkingHourEntity.setWorkContent(projectWorkingHourPatchDTO.getWorkContent());
        projectWorkingHourEntity.setWorkingHours(projectWorkingHourPatchDTO.getWorkingHours());
        projectWorkingHourEntity.setApprovalId(projectWorkingHourPatchDTO.getApprovalId());
        projectWorkingHourEntity.setLastModifiedAt();
        projectWorkingHourEntity.setLastModifiedBy(operatorDTO.getId());

        projectWorkingHourRepository.save(projectWorkingHourEntity);
    }

    @Override
    public void patchWorkingHourStatus(
        Long orgId,
        Long projectId,
        Long workingHourId,
        ProjectWorkingHourStatusType toProjectWorkingHourStatus,
        ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO,
        OperatorDTO operatorDTO
    ) {

        Optional<ProjectWorkingHourEntity> projectWorkingHourEntityOptional = projectWorkingHourRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourId);
        if (!projectWorkingHourEntityOptional.isPresent()) {
            throw new NotFoundError();
        }

        ProjectWorkingHourEntity projectWorkingHourEntity = projectWorkingHourEntityOptional.get();
        switch (toProjectWorkingHourStatus) {
            case SUBMITTED:

                if (ProjectWorkingHourStatusType.NOT_SUBMITTED != projectWorkingHourEntity.getWorkingHourStatus()
                    && ProjectWorkingHourStatusType.WITHDRAW != projectWorkingHourEntity.getWorkingHourStatus()
                    && ProjectWorkingHourStatusType.REJECTED != projectWorkingHourEntity.getWorkingHourStatus()) {
                    throw new BusinessError("you can submitted when status are not_submitted, withdraw or rejected");
                }
                break;
            case WITHDRAW:
            case APPROVED:
            case REJECTED:


                if (ProjectWorkingHourStatusType.SUBMITTED != projectWorkingHourEntity.getWorkingHourStatus()) {
                    throw new BusinessError("you can not withdraw, approved and rejected when status is not submitted");
                }
                break;
            default:
                break;
        }

        projectWorkingHourEntity.setWorkingHourStatus(toProjectWorkingHourStatus);
        projectWorkingHourEntity.setLastModifiedAt();
        projectWorkingHourEntity.setLastModifiedBy(operatorDTO.getId());
        projectWorkingHourRepository.save(projectWorkingHourEntity);
        addProjectWorkingHourHistory(
            orgId,
            projectId,
            workingHourId,
            toProjectWorkingHourStatus,
            projectWorkingHourPatchStatusDTO.getComments(),
            operatorDTO);
    }

    private void addProjectWorkingHourHistory(
        Long orgId,
        Long projectId,
        Long workingHourId,
        ProjectWorkingHourStatusType toProjectWorkingHourStatus,
        String comments,
        OperatorDTO operatorDTO
    ) {

        ProjectWorkingHourHistoryEntity projectWorkingHourHistoryEntity = new ProjectWorkingHourHistoryEntity();
        projectWorkingHourHistoryEntity.setOrgId(orgId);
        projectWorkingHourHistoryEntity.setProjectId(projectId);
        projectWorkingHourHistoryEntity.setProjectWorkingHourId(workingHourId);
        projectWorkingHourHistoryEntity.setWorkingHourStatus(toProjectWorkingHourStatus);
        projectWorkingHourHistoryEntity.setComments(comments);
        projectWorkingHourHistoryEntity.setCreatedAt();
        projectWorkingHourHistoryEntity.setCreatedBy(operatorDTO.getId());
        projectWorkingHourHistoryEntity.setStatus(EntityStatus.ACTIVE);
        projectWorkingHourHistoryEntity.setLastModifiedAt();

        projectWorkingHourHistoryRepository.save(projectWorkingHourHistoryEntity);
    }

    /**
     * 设置返回结果的引用数据
     *
     * @param <T>      数据实体泛型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(Map<Long, Object> included, List<T> entities) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> categoryIds = new ArrayList<>();

        for (T entity : entities) {
            if (entity instanceof ProjectWorkingHourEntity) {
                ProjectWorkingHourEntity projectWorkingHourEntity = (ProjectWorkingHourEntity) entity;
                categoryIds.add(projectWorkingHourEntity.getProjectWorkingHoursLargeCategory());
                categoryIds.add(projectWorkingHourEntity.getProjectWorkingHoursSmallCategory());
            }
        }

        if (categoryIds.size() > 0) {
            Iterable<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityList = projectWorkingHourCategoryRepository.findAllById(categoryIds);
            for (ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity : projectWorkingHourCategoryEntityList) {
                included.put(projectWorkingHourCategoryEntity.getId(), projectWorkingHourCategoryEntity);
            }
        }

        return included;
    }
}
