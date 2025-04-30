package com.ose.notifications.domain.model.repository;

import com.ose.notifications.dto.NotificationTemplateBasicDTO;
import com.ose.notifications.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 通知模版数据仓库。
 */
public interface NotificationTemplateRepository extends CrudRepository<NotificationTemplate, Long> {

    /**
     * 取得通知模板信息。
     *
     * @param orgId 组织 ID
     * @param id    模板 ID
     * @return 模版信息
     */
    Optional<NotificationTemplate> findByOrgIdAndId(Long orgId, Long id);

    /**
     * 取得通知模板信息。
     *
     * @param orgId 组织 ID
     * @param hash  模板摘要
     * @return 模版信息
     */
    Optional<NotificationTemplate> findByOrgIdAndHashAndDeletedIsFalse(Long orgId, String hash);

    /**
     * 检查模版是否存在且有效。
     *
     * @param orgId 组织 ID
     * @param id    模版 ID
     * @return 是否存在
     */
    boolean existsByOrgIdAndIdAndDisabledIsFalseAndDeletedIsFalse(Long orgId, Long id);

    @Query("SELECT new com.ose.notifications.dto.NotificationTemplateBasicDTO(t.id, t.name, t.title) FROM NotificationTemplate t WHERE t.id IN :templateIDs")
    List<NotificationTemplateBasicDTO> findBasicByIdIn(@Param("templateIDs") Set<Long> templateIDs);

}
