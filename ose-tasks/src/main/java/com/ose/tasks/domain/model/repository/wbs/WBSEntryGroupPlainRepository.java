package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryGroupPlain;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * WBS 扁平 条目 CRUD 操作接口。
 */
public interface WBSEntryGroupPlainRepository extends PagingAndSortingRepository<WBSEntryGroupPlain, Long>, WBSEntryGroupPlainCustomRepository {


}
