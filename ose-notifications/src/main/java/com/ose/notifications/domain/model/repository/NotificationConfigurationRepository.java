package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationConfiguration;
import com.ose.notifications.vo.NotificationType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 通知配置数据仓库。
 */
public interface NotificationConfigurationRepository extends CrudRepository<NotificationConfiguration, Long> {

    /**
     * 取得通知配置信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param id        通知配置 ID
     * @return 通知配置信息
     */
    Optional<NotificationConfiguration> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

    /**
     * 取得通知配置信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      通知类型
     * @return 通知配置信息
     */
    Optional<NotificationConfiguration> findByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(Long orgId, Long projectId, NotificationType type);

}
