package com.ose.tasks.domain.model.service.workinghour.impl;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.workinghour.ProjectWorkingHourCategoryRepository;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourCategoryInterface;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourCategoryPostAndPatchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourCategoryType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectWorkingHourCategoryService implements ProjectWorkingHourCategoryInterface {

    private final ProjectWorkingHourCategoryRepository projectWorkingHourCategoryRepository;

    /**
     * 构造方法
     */
    @Autowired
    public ProjectWorkingHourCategoryService(ProjectWorkingHourCategoryRepository projectWorkingHourCategoryRepository) {
        this.projectWorkingHourCategoryRepository = projectWorkingHourCategoryRepository;
    }

    @Override
    public void postWorkingHourLargeCategory(Long orgId, Long projectId, OperatorDTO operatorDTO, ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndName(orgId, projectId, ProjectWorkingHourCategoryType.LARGE_CATEGORY, projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        if (projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new DuplicatedError();
        }

        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = new ProjectWorkingHourCategoryEntity();
        projectWorkingHourCategoryEntity.setOrgId(orgId);
        projectWorkingHourCategoryEntity.setProjectId(projectId);
        projectWorkingHourCategoryEntity.setName(projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        projectWorkingHourCategoryEntity.setProjectWorkingHourCategoryType(ProjectWorkingHourCategoryType.LARGE_CATEGORY);
        projectWorkingHourCategoryEntity.setCreatedAt();
        projectWorkingHourCategoryEntity.setLastModifiedAt();
        projectWorkingHourCategoryEntity.setCreatedBy(operatorDTO.getId());
        projectWorkingHourCategoryEntity.setLastModifiedBy(operatorDTO.getId());
        projectWorkingHourCategoryEntity.setStatus(EntityStatus.ACTIVE);
        projectWorkingHourCategoryEntity.setDeleted(false);

        projectWorkingHourCategoryRepository.save(projectWorkingHourCategoryEntity);
    }

    @Override
    public Page<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(Long orgId, Long projectId, PageDTO pageDTO) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryType(
            orgId,
            projectId,
            ProjectWorkingHourCategoryType.LARGE_CATEGORY,
            pageDTO.toPageable());
    }

    @Override
    public List<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(Long orgId, Long projectId) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryType(
            orgId,
            projectId,
            ProjectWorkingHourCategoryType.LARGE_CATEGORY);
    }

    @Override
    public ProjectWorkingHourCategoryEntity getWorkingHourLargeCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourLargeCategoryId).orElse(null);
    }

    @Override
    public void patchWorkingHourLargeCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, OperatorDTO operatorDTO, ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourLargeCategoryId);
        if (!projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new NotFoundError();
        }
        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = projectWorkingHourCategoryEntityOptional.get();

        if (ProjectWorkingHourCategoryType.LARGE_CATEGORY != projectWorkingHourCategoryEntity.getProjectWorkingHourCategoryType()) {

            throw new BusinessError();
        }
        projectWorkingHourCategoryEntity.setName(projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        projectWorkingHourCategoryEntity.setLastModifiedAt();
        projectWorkingHourCategoryEntity.setLastModifiedBy(operatorDTO.getId());

        projectWorkingHourCategoryRepository.save(projectWorkingHourCategoryEntity);
    }

    @Override
    public void deleteWorkingHourLargeCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourLargeCategoryId);
        if (!projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new NotFoundError();
        }
        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = projectWorkingHourCategoryEntityOptional.get();

        if (ProjectWorkingHourCategoryType.LARGE_CATEGORY != projectWorkingHourCategoryEntity.getProjectWorkingHourCategoryType()) {

            throw new BusinessError();
        }

        List<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityList = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentId(orgId, projectId, ProjectWorkingHourCategoryType.SMALL_CATEGORY, workingHourLargeCategoryId);

        projectWorkingHourCategoryRepository.delete(projectWorkingHourCategoryEntity);
        projectWorkingHourCategoryRepository.deleteAll(projectWorkingHourCategoryEntityList);
    }

    @Override
    public void postWorkingHourSmallCategory(Long orgId, Long projectId, Long largeCategoryId, OperatorDTO operatorDTO, ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentIdAndName(orgId, projectId, ProjectWorkingHourCategoryType.SMALL_CATEGORY, largeCategoryId, projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        if (projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new DuplicatedError();
        }

        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = new ProjectWorkingHourCategoryEntity();
        projectWorkingHourCategoryEntity.setOrgId(orgId);
        projectWorkingHourCategoryEntity.setProjectId(projectId);
        projectWorkingHourCategoryEntity.setName(projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        projectWorkingHourCategoryEntity.setProjectWorkingHourCategoryType(ProjectWorkingHourCategoryType.SMALL_CATEGORY);
        projectWorkingHourCategoryEntity.setParentId(largeCategoryId);
        projectWorkingHourCategoryEntity.setCreatedAt();
        projectWorkingHourCategoryEntity.setLastModifiedAt();
        projectWorkingHourCategoryEntity.setCreatedBy(operatorDTO.getId());
        projectWorkingHourCategoryEntity.setLastModifiedBy(operatorDTO.getId());
        projectWorkingHourCategoryEntity.setStatus(EntityStatus.ACTIVE);
        projectWorkingHourCategoryEntity.setDeleted(false);

        projectWorkingHourCategoryRepository.save(projectWorkingHourCategoryEntity);
    }

    @Override
    public Page<ProjectWorkingHourCategoryEntity> getWorkingHourSmallCategories(Long orgId, Long projectId, Long workingHourLargeCategoryId, PageDTO pageDTO) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentId(
            orgId,
            projectId,
            ProjectWorkingHourCategoryType.SMALL_CATEGORY,
            workingHourLargeCategoryId,
            pageDTO.toPageable());
    }

    @Override
    public List<ProjectWorkingHourCategoryEntity> getWorkingHourSmallCategories(Long orgId, Long projectId, Long workingHourLargeCategoryId) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndProjectWorkingHourCategoryTypeAndParentId(
            orgId,
            projectId,
            ProjectWorkingHourCategoryType.SMALL_CATEGORY,
            workingHourLargeCategoryId);
    }

    @Override
    public ProjectWorkingHourCategoryEntity getWorkingHourSmallCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, Long workingHourSmallCategoryId) {
        return projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourSmallCategoryId).orElse(null);
    }

    @Override
    public void patchWorkingHourSmallCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, Long workingHourSmallCategoryId, OperatorDTO operatorDTO, ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourSmallCategoryId);
        if (!projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new NotFoundError();
        }
        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = projectWorkingHourCategoryEntityOptional.get();

        if (ProjectWorkingHourCategoryType.SMALL_CATEGORY != projectWorkingHourCategoryEntity.getProjectWorkingHourCategoryType()) {

            throw new BusinessError();
        } else if (!workingHourLargeCategoryId.equals(projectWorkingHourCategoryEntity.getParentId())) {

            throw new BusinessError();
        }
        projectWorkingHourCategoryEntity.setName(projectWorkingHourCategoryPostAndPatchDTO.getCategoryName());
        projectWorkingHourCategoryEntity.setLastModifiedAt();
        projectWorkingHourCategoryEntity.setLastModifiedBy(operatorDTO.getId());

        projectWorkingHourCategoryRepository.save(projectWorkingHourCategoryEntity);
    }

    @Override
    public void deleteWorkingHourSmallCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, Long workingHourSmallCategoryId) {
        Optional<ProjectWorkingHourCategoryEntity> projectWorkingHourCategoryEntityOptional = projectWorkingHourCategoryRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, workingHourSmallCategoryId);
        if (!projectWorkingHourCategoryEntityOptional.isPresent()) {

            throw new NotFoundError();
        }
        ProjectWorkingHourCategoryEntity projectWorkingHourCategoryEntity = projectWorkingHourCategoryEntityOptional.get();

        if (ProjectWorkingHourCategoryType.SMALL_CATEGORY != projectWorkingHourCategoryEntity.getProjectWorkingHourCategoryType()) {

            throw new BusinessError();
        } else if (!workingHourLargeCategoryId.equals(projectWorkingHourCategoryEntity.getParentId())) {

            throw new BusinessError();
        }

        projectWorkingHourCategoryRepository.delete(projectWorkingHourCategoryEntity);
    }
}
