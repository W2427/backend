package com.ose.notifications.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.notifications.domain.model.repository.*;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.dto.receiver.UserReceiverDTO;
import com.ose.notifications.entity.*;
import com.ose.notifications.vo.NotificationType;
import com.ose.service.StringRedisService;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 通知管理服务。
 */
@Component
public class NotificationService extends StringRedisService implements NotificationInterface {

    // 排序设置：取得第一件数据
    private static final Pageable FETCH_FIRST = PageRequest.of(0, 1, Sort.by(Sort.Order.asc("id")));

    // 正在发送通知的组织的 ID KEY 的命名空间
    private static final String SENDING_ORG_KEY = "NOTIFICATION:SENDING:";

    // 正在发送通知的组织的 ID KEY 的有效时长（秒）
    private static final int SENDING_ORG_KEY_TTL = 60;

    // 通知配置数据仓库
    private final NotificationConfigurationRepository configurationRepository;

    // 通知批次数据仓库
    private final NotificationBatchRepository batchRepository;

    // 通知数据仓库
    private final NotificationRepository notificationRepository;

    // 通知记录数据仓库
    private final NotificationLogRepository logRepository;

    // 用户通知数据仓库
    private final UserNotificationRepository userNotificationRepository;

    // 通知批次统计信息视图操作接口
    private final NotificationBatchSummaryRepository batchSummaryRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationService(
        StringRedisTemplate stringRedisTemplate,
        NotificationConfigurationRepository configurationRepository,
        NotificationBatchRepository batchRepository,
        NotificationRepository notificationRepository,
        NotificationLogRepository logRepository,
        UserNotificationRepository userNotificationRepository,
        NotificationBatchSummaryRepository batchSummaryRepository
    ) {
        super(stringRedisTemplate);
        this.configurationRepository = configurationRepository;
        this.batchRepository = batchRepository;
        this.notificationRepository = notificationRepository;
        this.logRepository = logRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.batchSummaryRepository = batchSummaryRepository;
    }

    /**
     * 保存批次信息。
     *
     * @param operator      操作者信息
     * @param configuration 通知配置
     * @param parameters    参数对象
     * @param users         接收者列表
     * @return 通知批次记录
     */
    private NotificationBatch saveBatch(
        OperatorDTO operator,
        NotificationConfiguration configuration,
        Object parameters,
        List<UserReceiverDTO> users
    ) {

        NotificationBatch batch = new NotificationBatch(operator, configuration);
        batch.setTotalCount(users.size());
        batch.setParameters(parameters);

        return batchRepository.save(batch);
    }

    /**
     * 生成通知记录。
     *
     * @param configuration 通知配置
     * @param batch         批次信息
     * @param parameters    参数对象
     * @param sender        发送者信息
     * @param users         接收者列表
     */
    private void saveLogs(
        final NotificationConfiguration configuration,
        final NotificationBatch batch,
        final Object parameters,
        final OperatorDTO sender,
        final List<UserReceiverDTO> users
    ) {
//
//        final NotificationTemplate template = configuration.getTemplate();
//        final String titleTemplate = template.getTitle();
//        final String contentTemplate = template.getContent();
//        final String textTemplate = template.getText();
//        final List<NotificationLog> logs = new ArrayList<>();
//
//        // 若设置为发送内部消息则生成通知数据
//        if (configuration.getSendMessage()) {
//
//            Notification notification;
//
//            // 若为公告则生成统一的通知数据
//            if (configuration.getAnnouncement()) {
//
//                notification = new Notification(template, batch);
//                notification.setTitle(titleTemplate, parameters, sender);
//                notification.setContent(contentTemplate, parameters, sender);
//                notification.setText(textTemplate, parameters, sender);
//
//                Long notificationId = notificationRepository.save(notification).getId();
//
//                for (UserReceiverDTO user : users) {
//                    logs.add(new NotificationLog(batch, user, notificationId));
//                }
//
//                // 否则为每一个通知对象用户生成通知数据
//            } else {
//
//                List<Notification> notifications = new ArrayList<>();
//
//                for (UserReceiverDTO user : users) {
//
//                    notification = new Notification(template, batch);
//                    notification.setTitle(titleTemplate, parameters, sender, user.getUser());
//                    notification.setContent(contentTemplate, parameters, sender, user.getUser());
//                    notification.setText(textTemplate, parameters, sender, user.getUser());
//
//                    notifications.add(notification);
//
//                    logs.add(new NotificationLog(batch, user, notification.getId()));
//                }
//
//                notificationRepository.saveAll(notifications);
//            }
//
//            // 否则仅生成通知日志数据
//        } else {
//
//            for (UserReceiverDTO user : users) {
//                logs.add(new NotificationLog(batch, user, null));
//            }
//
//        }
//
//        logRepository.saveAll(logs);
    }

    /**
     * 根据通知日志信息中的模版及参数信息渲染通知信息。
     *
     * @param log 通知日志
     */
    private void render(NotificationLog log) {

        // 取得模版信息
//        final NotificationBatch batch = log.getBatch();
//        final NotificationTemplate template = log.getTemplate();
//
//        if (batch == null || template == null) {
//            return;
//        }
//
//        if (log.getNotification() != null) {
//            return;
//        }
//
//        // 定义模版参数
//        Object parameters = null;
//        OperatorDTO sender = new OperatorDTO(batch.getCreatedBy(), batch.getCreatorName());
//        UserBasic user = null;
//
//        if (batch.getParameterType() != null) {
//            try {
//                parameters = StringUtils.fromJSON(
//                    batch.getParameterJSON(),
//                    Class.forName(batch.getParameterType())
//                );
//            } catch (IOException | ClassNotFoundException e) {
//                // no thing to do
//            }
//        }
//
//        if (!batch.getAnnouncement()) {
//            user = new UserBasic();
//            user.setId(log.getUserId());
//            user.setName(log.getUserName());
//            user.setEmail(log.getEmail());
//            user.setMobile(log.getMobile());
//        }
//
//        Notification notification = new Notification(template, batch);
//
//        // 渲染模版
//        notification.setTitle(template.getTitle(), parameters, sender, user);
//        notification.setContent(template.getContent(), parameters, sender, user);
//        notification.setText(template.getText(), parameters, sender, user);
//
//        log.setNotification(notification);
    }

    /**
     * 发送通知。
     *
     * @param redisKey Redis Key
     * @param orgId    组织 ID
     */
    private void sendAll(
        final String redisKey,
        final Long orgId
    ) {

//        // 设置排他锁
//        setRedisKey(redisKey, "" + System.currentTimeMillis(), SENDING_ORG_KEY_TTL);
//
//        // 在新的线程中发送电子邮件/短信
//        Executors.newCachedThreadPool().execute(() -> {
//
//            // 取得【待发送】的日志
//            List<NotificationLog> logs = logRepository
//                .findBySendStatusIsPending(orgId, FETCH_FIRST);
//
//            if (logs.size() == 0) {
//                deleteRedisKey(redisKey);
//                return;
//            }
//
//            // 更新发送状态为【发送中】
//            final NotificationLog log = logs.get(0);
//            final boolean sendEmail = log.setEmailSendStatusAsSending();
//            final boolean sendSMS = log.setSmsSendStatusAsSending();
//            logRepository.save(log);
//
//            // 渲染通知模版
//            render(log);
//
//            // 发送电子邮件
//            if (sendEmail && !StringUtils.isEmpty(log.getEmail()) && !StringUtils.isEmpty(log.getContent(), true)) {
//
//                try {
//
//                    if (log.getContentType() == NotificationContentType.HTML) {
//                        MailUtils.send(log.getEmail(), log.getUserName(), log.getTitle(), log.getContent());
//                    } else {
//                        MailUtils.sendText(log.getEmail(), log.getUserName(), log.getTitle(), log.getContent());
//                    }
//
//                    log.setEmailSentAt(new Date());
//                    log.setEmailSendStatus(NotificationSendStatus.SUCCESSFUL);
//
//                } catch (Exception e) {
//                    log.setEmailSendStatus(NotificationSendStatus.FAILED);
//                }
//
//            }
//
//            // 发送短信
//            if (sendSMS && !StringUtils.isEmpty(log.getMobile()) && !StringUtils.isEmpty(log.getText(), true)) {
//
//                try {
//                    SMSUtils.send(log.getMobile(), log.getText());
//                    log.setSmsSentAt(new Date());
//                    log.setSmsSendStatus(NotificationSendStatus.SUCCESSFUL);
//                } catch (Exception e) {
//                    log.setSmsSendStatus(NotificationSendStatus.FAILED);
//                }
//
//            }
//
//            // 保存发送状态
//            logRepository.save(log);
//
//            // 继续处理下一条通知日志
//            sendAll(redisKey, orgId);
//        });

    }

    /**
     * 发送通知。
     *
     * @param orgId 组织 ID
     */
    @Override
    public void sendAll(final Long orgId) {

        String redisKey = SENDING_ORG_KEY + orgId;

        if (hasRedisKey(redisKey)) {
            return;
        }

        sendAll(redisKey, orgId);
    }

    /**
     * 保存通知发送日志。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param parameters       消息参数对象
     * @param users            发送目标用户信息
     * @return 通知批次信息
     */
    @Override
    @Transactional
    public NotificationBatch saveBatchLogs(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final NotificationType notificationType,
        final Object parameters,
        final List<UserReceiverDTO> users
    ) {

        // 取得通知配置
        NotificationConfiguration configuration = configurationRepository
            .findByOrgIdAndProjectIdAndTypeAndDeletedIsFalse(orgId, projectId, notificationType)
            .orElse(null);

        if (configuration == null) {
            throw new BusinessError("消息配置无效"); // TODO
        }

        // 保存批次记录
        NotificationBatch batch = saveBatch(operator, configuration, parameters, users);

        // 生成通知记录
        saveLogs(configuration, batch, parameters, operator, users);

        return batch;
    }

    /**
     * 取得通知批次列表。
     *
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param pageable         分页参数
     * @return 批次列表
     */
    @Override
    public Page<NotificationBatchSummary> batches(
        final Long orgId,
        final Long projectId,
        final NotificationType notificationType,
        final Pageable pageable
    ) {

        if (notificationType == null) {
            return batchSummaryRepository
                .findByOrgIdAndProjectId(orgId, projectId, pageable);
        }

        return batchSummaryRepository
            .findByOrgIdAndProjectIdAndType(orgId, projectId, notificationType, pageable);
    }

    @Override
    public Page<Notification> search(NotificationSearchDTO notificationSearchDTO) {

//        PageDTO.Page page =new PageDTO.Page(1,30);
//        pageDTO.setPage(page);
//        String[] sort = {"createdAt:desc"};
//        pageDTO.setSort(sort);

        return notificationRepository.search(notificationSearchDTO);
    }

    /**
     * 取得批次详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     * @return 批次详细信息
     */
    @Override
    public NotificationBatch getBatch(
        final Long orgId,
        final Long projectId,
        final Long batchId
    ) {
        return batchRepository
            .findByOrgIdAndProjectIdAndId(orgId, projectId, batchId)
            .orElse(null);
    }

    /**
     * 重新发送发送失败的通知。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     */
    @Override
    public void resend(
        final Long orgId,
        final Long projectId,
        final Long batchId
    ) {
        logRepository.setEmailSendStatusAsPending(orgId, projectId, batchId);
        logRepository.setSmsSendStatusAsPending(orgId, projectId, batchId);
        sendAll(orgId);
    }

    /**
     * 取得通知日志详细。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     * @return 通知日志
     */
    private NotificationLog getLog(
        final Long userId,
        final Long notificationId
    ) {

        NotificationLog log = logRepository
            .findByUserIdAndNotificationId(userId, notificationId)
            .orElse(null);

        if (log == null) {
            throw new NotFoundError();
        }

        return log;
    }

    /**
     * 取得通知详细。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     * @return 通知
     */
    @Override
    public Notification get(
        final Long userId,
        final Long notificationId
    ) {
        return getLog(userId, notificationId).getNotification();
    }

    /**
     * 标记为已读。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     */
    @Override
    public void setAsRead(
        final Long userId,
        final Long notificationId
    ) {
        NotificationLog log = getLog(userId, notificationId);
        log.setRead(true);
        logRepository.save(log);
    }

    /**
     * 标记为未读。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     */
    @Override
    public void setAsUnread(
        final Long userId,
        final Long notificationId
    ) {
        NotificationLog log = getLog(userId, notificationId);
        log.setRead(false);
        logRepository.save(log);
    }

    /**
     * 查询通知。
     *
     * @param userId           用户 ID
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param readByUser       是否已读
     * @param pageable         分页参数
     * @return 通知列表
     */
    @Override
    public Page<UserNotification> search(
        final Long userId,
        final Long orgId,
        final Long projectId,
        final NotificationType notificationType,
        final Boolean readByUser,
        final Pageable pageable
    ) {
        return userNotificationRepository
            .search(userId, orgId, projectId, notificationType, readByUser, pageable);
    }

    /**
     * @param notificationCreateDTO 通知创建传输数据 */
    @Override
    public void create(
//        Long operatorId,
//        String operatorName,
        NotificationCreateDTO notificationCreateDTO
    ) {
        if (notificationCreateDTO.getTitle() == null) {
            throw new BusinessError("Title is required.");
        }
        if (notificationCreateDTO.getContent() == null) {
            throw new BusinessError("Content is required.");
        }
        if (notificationCreateDTO.getType() == null) {
            throw new BusinessError("Type is required.");
        }
        if (notificationCreateDTO.getCreatorName() == null) {
            throw new BusinessError("publisher is required.");
        }

        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationCreateDTO, notification);
        Date now = new Date();

        notification.setCreatedAt(now);
        notification.setLastModifiedAt(now);
        notification.setStatus(EntityStatus.ACTIVE);
//        userNotification.setDeleted(false);
//        userNotification.setCreatorName(operatorName);
//        userNotification.setCreatedBy(operatorId);
//        userNotification.setLastModifiedBy(operatorId);
//        userNotification.setVersion(new Long(1));

        notificationRepository.save(notification);
    }

    @Override
    public void update(
        Long messageId,
        String operatorName,
        Long operatorId,
        NotificationCreateDTO notificationUpdateDTO
    ) {
        UserNotification userNotification = userNotificationRepository.findAllById(messageId);

        if (userNotification == null) {
            throw new NotFoundError("userNotification is not found!");
        }

//        userNotification.setLastModifiedBy(operatorId);
        userNotification.setLastModifiedAt(new Date());

        if (notificationUpdateDTO.getType() != null) {
            userNotification.setType(notificationUpdateDTO.getType());
        }

        if (notificationUpdateDTO.getTitle() != null) {
            userNotification.setTitle(notificationUpdateDTO.getTitle());
        }

        if (notificationUpdateDTO.getContent() != null) {
            userNotification.setContent(notificationUpdateDTO.getContent());
        }

        userNotificationRepository.save(userNotification);
    }

    @Override
    public void updateStatus(
        Long messageId,
        NotificationCreateDTO notificationUpdateDTO
    ) {
        Optional<Notification> notificationOptional = notificationRepository.findById(messageId);

        if (!notificationOptional.isPresent()) {
            throw new NotFoundError("userNotification is not found!");
        }
        if (notificationUpdateDTO.getMessageStatus()) {
            notificationOptional.get().setMessageStatus(true);
        }

        notificationRepository.save(notificationOptional.get());
    }

    @Override
    public void deleteAll() {
//        List<Notification> notifications = notificationRepository.findByStatus(EntityStatus.ACTIVE);
        List<Notification> notifications = notificationRepository.findByStatusAndType(EntityStatus.ACTIVE,NotificationType.WORKING_HOURS);
        if (notifications.size() > 0) {
            for (Notification notification: notifications) {
                notification.setStatus(EntityStatus.DELETED);
                notification.setLastModifiedAt();
                notificationRepository.save(notification);
            }

        }
    }
}
