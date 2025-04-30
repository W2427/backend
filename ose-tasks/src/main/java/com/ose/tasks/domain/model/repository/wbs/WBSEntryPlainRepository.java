package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * WBS 扁平 条目 CRUD 操作接口。
 */
public interface WBSEntryPlainRepository extends PagingAndSortingRepository<WBSEntryPlain, Long>, WBSEntryPlainCustomRepository {


    @Query(value = "select stage from wbs_entry where project_id = :projectId and org_id = :orgId group by stage", nativeQuery = true)
    List<String> getProcessStages(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    List<WBSEntryPlain> findByIdIn(Set<Long> wbsEntryIDs);

}
