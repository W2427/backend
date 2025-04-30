package com.ose.report.domain.repository;

import com.ose.report.entity.ChecklistItem;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查单 CRUD 操作接口。
 */
@Transactional
public interface ChecklistItemRepository extends PagingAndSortingWithCrudRepository<ChecklistItem, Long> {

    /**
     * 查询检查项（分页）
     *
     * @param checklistId 检查单ID
     * @param pageable    分页信息
     * @return 检查项列表
     */
    Page<ChecklistItem> findAllByChecklistId(Long checklistId, Pageable pageable);

    /**
     * 查询检查项（无分页）
     *
     * @param checklistId 检查单ID
     * @return 检查项列表
     */
    List<ChecklistItem> findAllByChecklistIdOrderByItemNo(Long checklistId);

    /**
     * 根据检查单ID删除
     *
     * @param checklistId 检查单ID
     */
    void deleteByChecklistId(Long checklistId);
}
