package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationTemplateBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知模版数据仓库。
 */
public interface NotificationTemplateBasicRepository extends PagingAndSortingRepository<NotificationTemplateBasic, Long>, NotificationTemplateCustomRepository {

    /**
     * 查询通知模版历史数据。
     *
     * @param orgId      组织 ID
     * @param revisionId 修订分组 ID
     * @param pageable   分页参数
     * @return 通知模版分页数据
     */
    Page<NotificationTemplateBasic> findByOrgIdAndRevisionIdAndDisabledIsTrueAndDeletedIsFalse(Long orgId, Long revisionId, Pageable pageable);

}
