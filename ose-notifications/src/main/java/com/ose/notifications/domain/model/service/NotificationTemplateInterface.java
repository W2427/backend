package com.ose.notifications.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.notifications.dto.NotificationTemplatePatchDTO;
import com.ose.notifications.dto.NotificationTemplatePostDTO;
import com.ose.notifications.entity.NotificationTemplate;
import com.ose.notifications.entity.NotificationTemplateBasic;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * 通知模版服务接口。
 */
public interface NotificationTemplateInterface extends EntityInterface {

    /**
     * 创建通知模版。
     *
     * @param operator    操作者信息
     * @param orgId       组织 ID
     * @param templateDTO 模版信息
     * @return 模版数据实体
     */
    NotificationTemplate create(
        OperatorDTO operator,
        Long orgId,
        NotificationTemplatePostDTO templateDTO
    );

    /**
     * 查询通知模板。
     *
     * @param orgId    组织 ID
     * @param name     名称
     * @param tags     标签列表
     * @param pageable 分页参数
     * @return 通知模板分页参数
     */
    Page<NotificationTemplateBasic> search(
        Long orgId,
        String name,
        Set<String> tags,
        Pageable pageable
    );

    /**
     * 取得模板详细信息。
     *
     * @param orgId      组织 ID
     * @param templateId 模板 ID
     * @return 模板详细信息
     */
    NotificationTemplate get(
        Long orgId,
        Long templateId
    );

    /**
     * 取得模板历史数据。
     *
     * @param orgId      组织 ID
     * @param templateId 模版 ID
     * @param pageable   分页参数
     * @return 模板历史数据
     */
    Page<NotificationTemplateBasic> history(
        Long orgId,
        Long templateId,
        Pageable pageable
    );

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
    NotificationTemplate update(
        OperatorDTO operator,
        Long orgId,
        Long templateId,
        NotificationTemplatePatchDTO templateDTO,
        long version
    );

    /**
     * 删除消息模版。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param templateId 模板 ID
     * @param version    最后更新版本号
     */
    void delete(
        OperatorDTO operator,
        Long orgId,
        Long templateId,
        long version
    );

}
