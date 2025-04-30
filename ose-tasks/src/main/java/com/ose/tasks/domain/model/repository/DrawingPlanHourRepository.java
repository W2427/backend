package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingPlanHour;

import java.util.List;

/**
 * 实体 CRUD 操作接口。
 */
public interface DrawingPlanHourRepository extends PagingAndSortingWithCrudRepository<DrawingPlanHour, Long> {
    DrawingPlanHour findByProjectIdAndDrawingIdAndPrivilegeAndMonthly(
        Long orgId,
        Long drawingId,
        String privilege,
        Integer monthly
    );
}
