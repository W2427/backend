package com.ose.tasks.domain.model.repository.drawing;

import com.ose.tasks.entity.drawing.SubDrawingConfig;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface SubDrawingConfigRepository extends PagingAndSortingRepository<SubDrawingConfig, Long> {

    List<SubDrawingConfig> findByOrgIdAndProjectIdAndDrawingCategoryId(Long orgId, Long projectId,
                                                                       Long categoryId);

}
