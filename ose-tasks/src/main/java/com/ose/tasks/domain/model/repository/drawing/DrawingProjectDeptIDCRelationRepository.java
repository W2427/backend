package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingProjectDeptIDCRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: ChenQiang
 * @date: 2024/11/6
 */
@Transactional
public interface DrawingProjectDeptIDCRelationRepository extends PagingAndSortingWithCrudRepository<DrawingProjectDeptIDCRelation, Long>, DrawingProjectDeptIDCRelationRepositoryCustom {

    Optional<DrawingProjectDeptIDCRelation> findByProjectIdAndDrawingIdAndDepartmentIdAndDeletedIsFalse(Long projectId, Long drawingId, Long departmentId);

    @Query(nativeQuery = true,
        value = "SELECT " +
            "dr.user_id as userId, " +
            "u.name as username " +
            "FROM " +
            "saint_whale_tasks.drawing_project_dept_idc_relation dr " +
            "LEFT JOIN saint_whale_auth.users u ON u.id = dr.user_id " +
            "WHERE " +
            "dr.deleted is FALSE " +
            "AND dr.drawing_id = :drawingId " +
            "GROUP BY dr.user_id ")
    List<Map<String, Object>> findUserIdAndUsernameByDrawingId(
        @Param("drawingId") Long drawingId
    );
}
