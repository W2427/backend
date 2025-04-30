package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.RatedTimeImportRecord;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RatedTimeImportRecordRepository extends PagingAndSortingWithCrudRepository<RatedTimeImportRecord, Long> {
    /**
     * 根据定额工时ID获取定额工时导入报告信息。
     *
     * @param ratedTimeImportRecordId 定额工时导入报告ID
     * @return 定额工时导入报告
     */
    RatedTimeImportRecord findByIdAndDeletedIsFalse(Long ratedTimeImportRecordId);
}
