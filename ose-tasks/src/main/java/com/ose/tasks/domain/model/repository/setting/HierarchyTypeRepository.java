package com.ose.tasks.domain.model.repository.setting;

import com.ose.tasks.entity.setting.HierarchyType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * HierarchyType CRUD 操作接口。
 */
public interface HierarchyTypeRepository extends PagingAndSortingRepository<HierarchyType, Long> {


    HierarchyType findByProjectIdAndNameEn(Long projectId, String hierarchyType);

    List<HierarchyType> findByProjectIdAndParentId(Long projectId, Long parentId);

    List<HierarchyType> findByProjectId(Long projectId);
}
