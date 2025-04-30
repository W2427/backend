package com.ose.notifications.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.notifications.dto.NotificationConfigurationPatchDTO;
import com.ose.notifications.dto.NotificationConfigurationPostDTO;
import com.ose.notifications.entity.NotificationConfiguration;
import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.vo.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 通知配置管理服务接口。
 */
public interface NotificationConfigurationInterface {

    /**
     * 创建通知配置。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param configurationDTO 配置信息
     * @return 配置数据实体
     */
    NotificationConfigurationBasic create(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        NotificationConfigurationPostDTO configurationDTO
    );

    /**
     * 查询通知配置。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 通知配置分页数据
     */
    Page<NotificationConfigurationBasic> search(
        Long orgId,
        Long projectId,
        Pageable pageable
    );

    /**
     * 取得通知配置信息。
     *
     * @param orgId           组织 ID
     * @param projectId       项目 ID
     * @param configurationId 配置 ID
     * @return 通知配置信息
     */
    NotificationConfiguration get(
        Long orgId,
        Long projectId,
        Long configurationId
    );

    /**
     * 取得通知配置信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      通知类型
     * @return 通知配置信息
     */
    NotificationConfiguration get(
        Long orgId,
        Long projectId,
        NotificationType type
    );

    /**
     * 通知配置更新。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param version          通知配置更新版本号
     * @param configurationDTO 通知配置信息
     * @return 通知配置数据实体
     */
    NotificationConfigurationBasic update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        long version,
        NotificationConfigurationPatchDTO configurationDTO
    );

    /**
     * 删除通知配置。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param version          通知配置更新版本号
     */
    void delete(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        long version
    );

}
