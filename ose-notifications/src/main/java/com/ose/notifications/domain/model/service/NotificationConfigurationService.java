package com.ose.notifications.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.notifications.domain.model.repository.NotificationConfigurationBasicRepository;
import com.ose.notifications.domain.model.repository.NotificationConfigurationRepository;
import com.ose.notifications.domain.model.repository.NotificationTemplateRepository;
import com.ose.notifications.dto.NotificationConfigurationPatchDTO;
import com.ose.notifications.dto.NotificationConfigurationPostDTO;
import com.ose.notifications.entity.NotificationConfiguration;
import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.vo.NotificationType;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知配置管理服务。
 */
@Component
public class NotificationConfigurationService implements NotificationConfigurationInterface {

    // 模版数据仓库
    private final NotificationTemplateRepository templateRepository;

    // 通知配置数据仓库（基本信息）
    private final NotificationConfigurationBasicRepository configurationBasicRepository;

    // 通知配置数据仓库
    private final NotificationConfigurationRepository configurationRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationConfigurationService(
        NotificationTemplateRepository notificationTemplateRepository,
        NotificationConfigurationBasicRepository notificationConfigurationBasicRepository,
        NotificationConfigurationRepository notificationConfigurationRepository
    ) {
        templateRepository = notificationTemplateRepository;
        configurationBasicRepository = notificationConfigurationBasicRepository;
        configurationRepository = notificationConfigurationRepository;
    }

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
    @Override
    public NotificationConfigurationBasic create(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final NotificationType notificationType,
        final NotificationConfigurationPostDTO configurationDTO
    ) {

        if (configurationBasicRepository
            .existsByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(orgId, projectId, notificationType)) {
            throw new DuplicatedError("已存在该类型的通知配置"); // TODO
        }

        if (!templateRepository
            .existsByOrgIdAndIdAndDisabledIsFalseAndDeletedIsFalse(orgId, configurationDTO.getTemplateId())) {
            throw new BusinessError("指定的模版不存在"); // TODO
        }

        NotificationConfigurationBasic configuration = new NotificationConfigurationBasic();

        BeanUtils.copyProperties(configurationDTO, configuration);

        configuration.setOrgId(orgId);
        configuration.setProjectId(projectId);
        configuration.setType(notificationType);
        configuration.setMemberPrivileges(configurationDTO.getMemberPrivileges());
        configuration.setCreatedAt();
        configuration.setCreatedBy(operator.getId());
        configuration.setLastModifiedAt();
        configuration.setLastModifiedBy(operator.getId());
        configuration.setStatus(EntityStatus.ACTIVE);

        return configurationBasicRepository.save(configuration);
    }

    /**
     * 查询通知配置。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 通知配置分页数据
     */
    @Override
    public Page<NotificationConfigurationBasic> search(
        final Long orgId,
        final Long projectId,
        final Pageable pageable
    ) {
        return configurationBasicRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId, pageable);
    }

    /**
     * 取得通知配置信息。
     *
     * @param orgId           组织 ID
     * @param projectId       项目 ID
     * @param configurationId 配置 ID
     * @return 通知配置信息
     */
    @Override
    public NotificationConfiguration get(
        final Long orgId,
        final Long projectId,
        final Long configurationId
    ) {

        NotificationConfiguration configuration = configurationRepository
            .findByOrgIdAndProjectIdAndId(orgId, projectId, configurationId)
            .orElse(null);

        if (configuration == null) {
            throw new NotFoundError(); // TODO
        }

        return configuration;
    }

    /**
     * 取得通知配置信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param type      通知类型
     * @return 通知配置信息
     */
    @Override
    public NotificationConfiguration get(
        Long orgId,
        Long projectId,
        NotificationType type
    ) {
        return configurationRepository
            .findByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(orgId, projectId, type)
            .orElse(null);
    }

    /**
     * 取得通知配置信息。
     *
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param version          通知配置更新版本号
     * @return 通知配置信息
     */
    private NotificationConfigurationBasic get(
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        long version
    ) {

        NotificationConfigurationBasic configuration = configurationBasicRepository
            .findByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(orgId, projectId, notificationType)
            .orElse(null);

        if (configuration == null) {
            throw new NotFoundError(); // TODO
        }

        if (configuration.getVersion() != version) {
            throw new ConflictError(); // TODO
        }

        return configuration;
    }

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
    @Override
    @Transactional
    public NotificationConfigurationBasic update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        long version,
        NotificationConfigurationPatchDTO configurationDTO
    ) {

        NotificationConfigurationBasic configuration = get(orgId, projectId, notificationType, version);

        if (!LongUtils.isEmpty(configurationDTO.getTemplateId())
            && !templateRepository
            .existsByOrgIdAndIdAndDisabledIsFalseAndDeletedIsFalse(orgId, configurationDTO.getTemplateId())) {
            throw new BusinessError("指定的模版不存在"); // TODO
        }

        if (configurationDTO.getMemberPrivileges() != null) {
            configuration.setMemberPrivileges(configurationDTO.getMemberPrivileges());
        }

        configuration.setTemplateId(ValueUtils.notNull(
            configurationDTO.getTemplateId(),
            configuration.getTemplateId()
        ));

        configuration.setAnnouncement(ValueUtils.notNull(
            configurationDTO.getAnnouncement(),
            configuration.getAnnouncement()
        ));

        configuration.setSendMessage(ValueUtils.notNull(
            configurationDTO.getSendMessage(),
            configuration.getSendMessage()
        ));

        configuration.setSendEmail(ValueUtils.notNull(
            configurationDTO.getSendEmail(),
            configuration.getSendEmail()
        ));

        configuration.setSendSMS(ValueUtils.notNull(
            configurationDTO.getSendSMS(),
            configuration.getSendSMS()
        ));

        configuration.setRemarks(ValueUtils.notNull(
            configurationDTO.getRemarks(),
            configuration.getRemarks()
        ));

        configuration.setLastModifiedAt();

        configuration.setLastModifiedBy(operator.getId());

        return configurationBasicRepository.save(configuration);
    }

    /**
     * 删除通知配置。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param version          通知配置更新版本号
     */
    @Override
    @Transactional
    public void delete(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        long version
    ) {

        NotificationConfigurationBasic configuration = get(orgId, projectId, notificationType, version);

        configuration.setDeletedAt();
        configuration.setDeletedBy(operator.getId());

        configurationBasicRepository.save(configuration);
    }

}
