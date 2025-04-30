package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryActivityInstance;
import org.springframework.data.repository.CrudRepository;

/**
 * WBS 实体条目抽检比例视图接口。
 */
public interface WBSEntryActivityInstanceRepository
    extends
    CrudRepository<WBSEntryActivityInstance, Long>,
    WBSEntryActivityInstanceRepositoryCustom {
}
