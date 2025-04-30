package com.ose.tasks.domain.model.service.workinghour;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourCategoryPostAndPatchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * service接口
 */
public interface ProjectWorkingHourCategoryInterface {


    void postWorkingHourLargeCategory(Long orgId,
                                      Long projectId,
                                      OperatorDTO operatorDTO,
                                      ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    Page<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(Long orgId, Long projectId, PageDTO pageDTO);

    List<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(Long orgId, Long projectId);

    ProjectWorkingHourCategoryEntity getWorkingHourLargeCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId);

    void patchWorkingHourLargeCategory(
        Long orgId,
        Long projectId,
        Long workingHourLargeCategoryId,
        OperatorDTO operatorDTO,
        ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    void deleteWorkingHourLargeCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId);


    void postWorkingHourSmallCategory(
        Long orgId,
        Long projectId,
        Long largeCategoryId,
        OperatorDTO operatorDTO,
        ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    Page<ProjectWorkingHourCategoryEntity> getWorkingHourSmallCategories(Long orgId, Long projectId, Long workingHourLargeCategoryId, PageDTO pageDTO);

    List<ProjectWorkingHourCategoryEntity> getWorkingHourSmallCategories(Long orgId, Long projectId, Long workingHourLargeCategoryId);

    ProjectWorkingHourCategoryEntity getWorkingHourSmallCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, Long workingHourSmallCategoryId);

    void patchWorkingHourSmallCategory(
        Long orgId,
        Long projectId,
        Long workingHourLargeCategoryId,
        Long workingHourSmallCategoryId,
        OperatorDTO operatorDTO,
        ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    void deleteWorkingHourSmallCategory(Long orgId, Long projectId, Long workingHourLargeCategoryId, Long workingHourSmallCategoryId);
}
