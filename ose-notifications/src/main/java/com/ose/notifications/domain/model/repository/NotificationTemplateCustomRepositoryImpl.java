package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationTemplateBasic;
import com.ose.repository.BaseRepository;
import org.springframework.data.domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.*;

/**
 * 通知模版数据仓库。
 */
public class NotificationTemplateCustomRepositoryImpl extends BaseRepository implements NotificationTemplateCustomRepository {

    private static final String TABLE_ALIAS = "notifications";

    /**
     * 取得通知模版信息。
     *
     * @param orgId    组织 ID
     * @param name     名称
     * @param tags     标签
     * @param pageable 分页参数
     * @return 通知列表
     */
    @Override
    public Page<NotificationTemplateBasic> search(
        final Long orgId,
        final String name,
        final Set<String> tags,
        final Pageable pageable
    ) {

        final EntityManager entityManager = getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // 创建总数查询
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Root<NotificationTemplateBasic> countFrom = countQuery.from(NotificationTemplateBasic.class);
        countFrom.alias(TABLE_ALIAS);

        final List<Predicate> orgAndNameCriteria = new ArrayList<>();

        /*----------------------------------------------------------------------
           查询条件设置 START
             `org_id` = 'ORG_ID'
             AND `name` LIKE '%NAME%'
             AND (`tags` LIKE '%,TAG1,%' OR `tags` LIKE '%,TAG2,%' OR `tags` LIKE '%,TAG3,%' ...)
             AND `deleted` = 0
         ---------------------------------------------------------------------*/

        if (orgId == null) {
            orgAndNameCriteria.add(criteriaBuilder.isNull(countFrom.get("orgId")));
        } else {
            orgAndNameCriteria.add(criteriaBuilder.equal(countFrom.get("orgId"), orgId));
        }

        if (name != null && !"".equals(name)) {
            orgAndNameCriteria.add(criteriaBuilder.like(countFrom.get("name"), "%" + name + "%"));
        }

        if (tags != null && tags.size() > 0) {

            final List<Predicate> tagCriteria = new ArrayList<>();

            tags.forEach(tag -> {
                tagCriteria.add(criteriaBuilder.like(countFrom.get("tags"), "%," + tag + ",%"));
            });

            orgAndNameCriteria.add(criteriaBuilder.or(tagCriteria.toArray(new Predicate[]{})));
        }

        orgAndNameCriteria.add(criteriaBuilder.equal(countFrom.get("deleted"), 0));

        Predicate predicate = criteriaBuilder.and(orgAndNameCriteria.toArray(new Predicate[]{}));

        /*----------------------------------------------------------------------
           查询条件设置 END
         ---------------------------------------------------------------------*/

        // 执行总数查询
        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(countFrom))
                    .where(predicate)
            )
            .getSingleResult();

        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort()
        );

        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }

        // 创建并执行分页数据查询
        CriteriaQuery<NotificationTemplateBasic> searchQuery = criteriaBuilder.createQuery(NotificationTemplateBasic.class);
        Root<NotificationTemplateBasic> searchFrom = searchQuery.from(NotificationTemplateBasic.class);
        searchFrom.alias(TABLE_ALIAS);
        searchQuery.where(predicate);

        return new PageImpl<>(
            entityManager
                .createQuery(searchQuery)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList(),
            pageRequest,
            count
        );

    }

}
