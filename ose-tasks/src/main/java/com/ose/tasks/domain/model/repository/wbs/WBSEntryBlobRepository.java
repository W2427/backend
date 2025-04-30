package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryBlob;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * WBS Blob 条目 CRUD 操作接口。
 */
public interface WBSEntryBlobRepository extends PagingAndSortingWithCrudRepository<WBSEntryBlob, Long> {


    WBSEntryBlob findByWbsEntryId(Long wbsEntryId);

    List<WBSEntryBlob> findByProjectIdAndPathLike(Long projectId, String wbsEntryId);


    @Modifying
    @Transactional
    @Query("DELETE FROM WBSEntryState ws WHERE ws.wbsEntryId = :wbsEntryId")
    void deleteByWbsId(@Param("wbsEntryId") Long wbsEntryId);

    Optional<WBSEntryBlob> findFirstByProjectIdAndWbsOrderByPathAsc(Long projectId, String wbs);
}
