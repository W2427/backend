package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.taskpackage.TaskPackageAUthCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageBasic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageBasicRepository extends PagingAndSortingWithCrudRepository<TaskPackageBasic, Long>,TaskPackageBasicCustomRepository{


    /**
     * 根据项目 ID 和名称取得任务包。
     *
     * @param projectId 项目 ID
     * @param id        任务包 ID
     * @return 任务包信息
     */
    Optional<TaskPackageBasic> findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long id);

    /**
     * 是否存在指定分类的任务包。
     *
     * @param projectId  项目 ID
     * @param categoryId 分类 ID
     * @return 是否存在
     */
    boolean existsByProjectIdAndCategoryIdAndDeletedIsFalse(Long projectId, Long categoryId);

    /**
     * 查询指定分类的任务包。
     *
     * @param projectId  项目 ID
     * @param categoryId 分类 ID
     * @return 任务包列表
     */
    List<TaskPackageBasic> findByProjectIdAndCategoryIdAndDeletedIsFalse(Long projectId, Long categoryId);

    @Query("SELECT new com.ose.tasks.dto.taskpackage.TaskPackageAUthCriteriaDTO(t.lastModifiedName, t.lastModifiedBy) FROM TaskPackageBasic t WHERE t.projectId = :projectId " +
        "and t.status = 'ACTIVE' GROUP BY t.lastModifiedBy,t.lastModifiedName")
    List<TaskPackageAUthCriteriaDTO> findByProjectIdAndDeletedIsFalse(@Param("projectId")Long projectId);
}
