package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryWithRelations;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * WBS 条目 CRUD 操作接口。
 */
public interface WBSEntryWithRelationsRepository extends PagingAndSortingWithCrudRepository<WBSEntryWithRelations, Long>, WBSEntryWithRelationsCustomRepository {

    /**
     * 取得 WBS 条目信息。
     *
     * @param projectId 项目 ID
     * @param id        WBS 条目 ID
     * @return WBS 条目信息
     */
    Optional<WBSEntryWithRelations> findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long id);

}
