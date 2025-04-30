package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationBatch;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 通知批次数据仓库。
 */
public interface NotificationBatchRepository extends CrudRepository<NotificationBatch, Long> {

    /**
     * 取得使用指定模版的通知数。
     *
     * @param orgId      组织 ID
     * @param templateId 模版 ID
     * @return 使用指定模版的通知数
     */
    Long countByOrgIdAndTemplateId(Long orgId, Long templateId);

    /**
     * 取得通知批次详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param id        批次 ID
     * @return 批次详细信息
     */
    Optional<NotificationBatch> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

}
