package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationTemplateBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * 通知模版数据仓库。
 */
public interface NotificationTemplateCustomRepository {

    Page<NotificationTemplateBasic> search(
        Long orgId,
        String name,
        Set<String> tags,
        Pageable pageable
    );

}
