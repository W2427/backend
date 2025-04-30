package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.UserNotification;
import com.ose.notifications.vo.NotificationType;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 通知数据仓库。
 */
public class UserNotificationCustomRepositoryImpl extends BaseRepository implements UserNotificationCustomRepository {

    /**
     * 取得用户的通知信息。
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
        return getSQLQueryBuilder(UserNotification.class)
            .is("userId", userId)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("type", notificationType)
            .is("read", readByUser)
            .paginate(pageable)
            .exec()
            .page();
    }

    @Override
    public Page<UserNotification> findAllOrderByCreatedAtDesc(Pageable pageable) {
        return getSQLQueryBuilder(UserNotification.class)
            .desc("createdAt")
            .paginate(pageable)
            .exec()
            .page();
    }

}
