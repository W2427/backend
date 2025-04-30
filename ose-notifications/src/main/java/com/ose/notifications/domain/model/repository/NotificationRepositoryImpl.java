package com.ose.notifications.domain.model.repository;

import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.entity.Notification;
import com.ose.notifications.vo.NotificationType;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通知数据仓库。
 */
public class NotificationRepositoryImpl extends BaseRepository implements NotificationRepositoryCustom {

    @Override
    public Page<Notification> search(
        NotificationSearchDTO notificationSearchDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select no.* ");
        sql.append(" from notifications no ");
        sql.append(" where no.status = 'ACTIVE' ");


        if (notificationSearchDTO.getUserType() != null && !notificationSearchDTO.getUserType().equals("administrator")
        && notificationSearchDTO.getUserName() != null && !notificationSearchDTO.getUserName().equals("")) {
            sql.append(" and (no.user_name = :userName or ISNULL(no.user_name) ) ");
        }
        if (notificationSearchDTO.getContent() != null && !"".equals(notificationSearchDTO.getContent())) {
            sql.append(" and no.content like :content ");
        }
        if (notificationSearchDTO.getType() != null) {
            sql.append(" and no.type = :type ");
        }
        if (notificationSearchDTO.getMessageStatus() != null) {
            if (notificationSearchDTO.getMessageStatus()) {
                sql.append(" and no.message_status = true ");
            } else {
                sql.append(" and no.message_status = false ");
            }

        }
        sql.append(" order by no.created_at desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (notificationSearchDTO.getUserType() != null && !notificationSearchDTO.getUserType().equals("administrator")
            && notificationSearchDTO.getUserName() != null && !notificationSearchDTO.getUserName().equals("")) {
            query.setParameter("userName", notificationSearchDTO.getUserName());
            countQuery.setParameter("userName", notificationSearchDTO.getUserName());
        }

        if (notificationSearchDTO.getContent() != null && !"".equals(notificationSearchDTO.getContent())) {
            query.setParameter("content", "%" + notificationSearchDTO.getContent() + "%");
            countQuery.setParameter("content", "%" + notificationSearchDTO.getContent() + "%");

        }

        if (notificationSearchDTO.getType() != null ) {
            query.setParameter("type", notificationSearchDTO.getType().name() );
            countQuery.setParameter("type", notificationSearchDTO.getType().name() );

        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = notificationSearchDTO.getPage().getNo();
        int pageSize = notificationSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<Notification> notifications = new ArrayList<Notification>();
        for (Map<String, Object> resultMap : queryResultList) {
            Notification notification = new Notification();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("type") != null) {
                newMap.put("type", NotificationType.valueOf((String) newMap.get("type")));
            }
            BeanUtils.copyProperties(newMap, notification);

            notifications.add(notification);

        }
        return new PageImpl<>(notifications, notificationSearchDTO.toPageable(), count.longValue());
    }
}
