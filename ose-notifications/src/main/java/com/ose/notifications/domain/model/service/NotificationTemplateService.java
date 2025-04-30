package com.ose.notifications.domain.model.service;

import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.notifications.domain.model.repository.NotificationBatchRepository;
import com.ose.notifications.domain.model.repository.NotificationConfigurationBasicRepository;
import com.ose.notifications.domain.model.repository.NotificationTemplateBasicRepository;
import com.ose.notifications.domain.model.repository.NotificationTemplateRepository;
import com.ose.notifications.dto.NotificationTemplateDTOInterface;
import com.ose.notifications.dto.NotificationTemplatePatchDTO;
import com.ose.notifications.dto.NotificationTemplatePostDTO;
import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.entity.NotificationTemplate;
import com.ose.notifications.entity.NotificationTemplateBasic;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.TemplateUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 通知模版服务。
 */
@Component
public class NotificationTemplateService implements NotificationTemplateInterface {

    // 模版数据仓库
    private final NotificationTemplateRepository templateRepository;

    // 模版数据仓库（基本信息）
    private final NotificationTemplateBasicRepository templateBasicRepository;

    // 批次数据仓库
    private final NotificationBatchRepository batchRepository;

    // 通知配置数据仓库
    private final NotificationConfigurationBasicRepository configurationBasicRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationTemplateService(
        NotificationTemplateRepository notificationTemplateRepository,
        NotificationTemplateBasicRepository notificationTemplateBasicRepository,
        NotificationBatchRepository notificationBatchRepository,
        NotificationConfigurationBasicRepository notificationConfigurationBasicRepository
    ) {
        templateRepository = notificationTemplateRepository;
        templateBasicRepository = notificationTemplateBasicRepository;
        batchRepository = notificationBatchRepository;
        configurationBasicRepository = notificationConfigurationBasicRepository;
    }

    /**
     * 校验模版有效性。
     *
     * @param templateDTO 通知模版数据传输对象
     */
    private void validateTemplate(NotificationTemplateDTOInterface templateDTO) {
        TemplateUtils.validate(templateDTO.getTitle());
        TemplateUtils.validate(templateDTO.getContent());
        TemplateUtils.validate(templateDTO.getText());
    }

    /**
     * 检查是否存在内容相同的通知模版。
     *
     * @param template 通知模版数据实体
     */
    private void checkDuplication(NotificationTemplate template) {

        NotificationTemplate duplication = templateRepository
            .findByOrgIdAndHashAndDeletedIsFalse(template.getOrgId(), template.getHash())
            .orElse(null);

        if (duplication != null && !duplication.getId().equals(template.getId())) {
            throw new DuplicatedError("存在内容相同的模版"); // TODO
        }

    }

    /**
     * 创建通知模版。
     *
     * @param operator    操作者信息
     * @param orgId       组织 ID
     * @param templateDTO 模版信息
     * @return 模版数据实体
     */
    @Override
    public NotificationTemplate create(
        final OperatorDTO operator,
        final Long orgId,
        final NotificationTemplatePostDTO templateDTO
    ) {

        validateTemplate(templateDTO);

        NotificationTemplate template = BeanUtils
            .copyProperties(templateDTO, new NotificationTemplate());

        template.setOrgId(orgId);
        template.setHash();
        template.setCreatedAt();
        template.setCreatedBy(operator.getId());
        template.setLastModifiedAt();
        template.setLastModifiedBy(operator.getId());

        checkDuplication(template);

        template.setStatus(EntityStatus.ACTIVE);

        return templateRepository.save(template);
    }

    /**
     * 查询通知模板。
     *
     * @param orgId    组织 ID
     * @param name     名称
     * @param tags     标签列表
     * @param pageable 分页参数
     * @return 通知模板分页参数
     */
    @Override
    public Page<NotificationTemplateBasic> search(
        final Long orgId,
        final String name,
        final Set<String> tags,
        final Pageable pageable
    ) {
        return templateBasicRepository.search(orgId, name, tags, pageable);
    }

    /**
     * 取得模板详细信息。
     *
     * @param orgId      组织 ID
     * @param templateId 模板 ID
     * @return 模板详细信息
     */
    @Override
    public NotificationTemplate get(
        final Long orgId,
        final Long templateId
    ) {

        NotificationTemplate template = templateRepository
            .findByOrgIdAndId(orgId, templateId)
            .orElse(null);

        if (template == null) {
            throw new NotFoundError(); // TODO
        }

        return template;
    }

    /**
     * 取得模板历史数据。
     *
     * @param orgId      组织 ID
     * @param templateId 模版 ID
     * @param pageable   分页参数
     * @return 模板历史数据
     */
    @Override
    public Page<NotificationTemplateBasic> history(
        final Long orgId,
        final Long templateId,
        final Pageable pageable
    ) {
        return templateBasicRepository
            .findByOrgIdAndRevisionIdAndDisabledIsTrueAndDeletedIsFalse(
                orgId,
                get(orgId, templateId).getRevisionId(),
                pageable
            );
    }

    /**
     * 更新模版信息。
     *
     * @param operator    更新者信息
     * @param orgId       组织 ID
     * @param templateId  模版 ID
     * @param templateDTO 模版信息
     * @param version     更新版本号
     * @return 模版信息
     */
    @Override
    @Transactional
    public NotificationTemplate update(
        final OperatorDTO operator,
        final Long orgId,
        final Long templateId,
        final NotificationTemplatePatchDTO templateDTO,
        final long version
    ) {

        validateTemplate(templateDTO);

        NotificationTemplate template = get(orgId, templateId);

        if (template.getDisabled() || template.getDeleted()) {
            throw new NotFoundError(); // TODO
        }

        if (template.getVersion() != version) {
            throw new ConflictError();
        }

        boolean modified = false;

        // 若模版内容被更新且存在使用该模版的通知则保存历史记录
        if (((templateDTO.getContentType() != null
            && templateDTO.getContentType() != template.getContentType())
            || (!StringUtils.isEmpty(templateDTO.getTitle(), true)
            && !StringUtils.trim(templateDTO.getTitle()).equals(template.getTitle()))
            || (!StringUtils.isEmpty(templateDTO.getContent(), true)
            && !StringUtils.trim(templateDTO.getContent()).equals(template.getContent()))
            || (!StringUtils.isEmpty(templateDTO.getText(), true)
            && !StringUtils.trim(templateDTO.getText()).equals(template.getText())))
            && batchRepository.countByOrgIdAndTemplateId(orgId, templateId) > 0
            ) {

            NotificationTemplate history = template;

            template = BeanUtils.copyProperties(
                history,
                new NotificationTemplate(),
                Collections.singleton("id")
            );

            history.setDisabled(true);

            templateRepository.save(history);

            modified = true;
        }

        // 更新模版信息
        template.setTitle(templateDTO.getTitle());
        template.setContentType(templateDTO.getContentType());
        template.setContent(templateDTO.getContent());
        template.setText(templateDTO.getText());
        template.setHash();
        template.setName(ValueUtils.notEmpty(templateDTO.getName(), template.getName()));
        template.setTags(templateDTO.getTagList());
        template.setRemarks(templateDTO.getRemarks());
        template.setLastModifiedAt();
        template.setLastModifiedBy(operator.getId());

        checkDuplication(template);

        template = templateRepository.save(template);

        // 更新配置信息中的模版 ID
        if (modified) {
            configurationBasicRepository
                .updateTemplateId(orgId, templateId, template.getId());
        }

        return template;
    }

    /**
     * 删除消息模版。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param templateId 模板 ID
     * @param version    最后更新版本号
     */
    @Override
    @Transactional
    public void delete(
        final OperatorDTO operator,
        final Long orgId,
        final Long templateId,
        final long version
    ) {

        NotificationTemplate template = get(orgId, templateId);

        if (template.getVersion() != version) {
            throw new ConflictError();
        }

        if (template.getDeleted()) {
            return;
        }

        template.setDeletedAt();
        template.setDeletedBy(operator.getId());

        templateRepository.save(template);
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        final Map<Long, Object> included,
        final List<T> entities
    ) {

        final Set<Long> templateIDs = new HashSet<>();

        entities.forEach(entity -> {

            if (!(entity instanceof NotificationConfigurationBasic)) {
                return;
            }

            NotificationConfigurationBasic config = (NotificationConfigurationBasic) entity;

            templateIDs.add(config.getTemplateId());
        });

        templateRepository.findBasicByIdIn(templateIDs).forEach(template -> {
            included.put(template.getId(), template);
        });

        return included;
    }

}
