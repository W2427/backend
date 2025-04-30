package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.vo.NotificationType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 通知配置数据仓库。
 */
public interface NotificationConfigurationBasicRepository extends PagingAndSortingWithCrudRepository<NotificationConfigurationBasic, Long> {

    /**
     * 检查是否存在有效的通知配置。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      通知类型
     * @return 是否存在有效的通知配置
     */
    boolean existsByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(Long orgId, Long projectId, NotificationType type);

    /**
     * 取得通知配置信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      通知类型
     * @return 通知配置信息
     */
    Optional<NotificationConfigurationBasic> findByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(Long orgId, Long projectId, NotificationType type);

    /**
     * 查询通知配置。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 通知配置分页数据
     */
    Page<NotificationConfigurationBasic> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId, Pageable pageable);

    /**
     * 更新配置的模版 ID。
     *
     * @param orgId              组织 ID
     * @param originalTemplateId 原模版 ID
     * @param newTemplateId      新模版 ID
     */
    @Modifying
    @Query("UPDATE NotificationConfigurationBasic c SET c.templateId = :newTemplateId WHERE c.orgId = :orgId AND c.templateId = :originalTemplateId")
    void updateTemplateId(
        @Param("orgId") Long orgId,
        @Param("originalTemplateId") Long originalTemplateId,
        @Param("newTemplateId") Long newTemplateId
    );

}
