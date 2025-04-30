package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.EntitySubTypeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据二维码查询得到外检申请的聚合数据。
 */
public class BpmEntitySubTypeRepositoryImpl extends BaseRepository
    implements BpmEntitySubTypeRepositoryCustom {

    @Override
    public Page<BpmEntitySubType> getList(Long orgId, Long projectId, EntitySubTypeCriteriaDTO page) {


        if (page.getFetchAll()) {
            EntityManager entityManager = getEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<BpmEntitySubType> criteriaQuery = criteriaBuilder.createQuery(BpmEntitySubType.class);
            Root<BpmEntitySubType> root = criteriaQuery.from(BpmEntitySubType.class);
            root.alias("_bpm_entity_sub_type");


            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<BpmEntitySubType> countRoot = countQuery.from(BpmEntitySubType.class);
            countRoot.alias("_bpm_entity_sub_type");

            Predicate predicate = criteriaBuilder.equal(root.get("status"), EntityStatus.ACTIVE);
            Predicate countPredicate = criteriaBuilder.equal(countRoot.get("status"), EntityStatus.ACTIVE);

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("orgId"), orgId));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("projectId"), projectId));

            if (page.getEntityTypeId() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("entityTypeId"), page.getEntityTypeId()));
                countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("entityTypeId"), page.getEntityTypeId()));
            }

            if (page.getEntityTypeName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("entityType.nameEn"), page.getEntityTypeName()));
                countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("entityType.nameEn"), page.getEntityTypeId()));
            }

            if (page.getEntityBusinessTypeId() != null) {

                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("entityBusinessTypeId"), page.getEntityBusinessTypeId()));
                countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("entityBusinessTypeId"), page.getEntityTypeId()));
            }

            if (page.getName() != null) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.or(criteriaBuilder.like(root.get("nameCn"), "%" + page.getName() + "%"),
                        criteriaBuilder.like(root.get("nameEn"), "%" + page.getName() + "%")));
                countPredicate = criteriaBuilder.and(countPredicate,
                    criteriaBuilder.or(criteriaBuilder.like(countRoot.get("nameCn"), "%" + page.getName() + "%"),
                        criteriaBuilder.like(countRoot.get("nameEn"), "%" + page.getName() + "%")));
            }

            // 计算总条数
            Long totalCount = entityManager
                .createQuery(
                    countQuery
                        .select(criteriaBuilder.count(countRoot))
                        .where(countPredicate)
                )
                .getSingleResult();

            criteriaQuery.where(predicate);
            criteriaQuery.orderBy(criteriaBuilder.asc(root.<String>get("orderNo")));


            if (page.getFetchAll()) {
                return new PageImpl<>(
                    entityManager
                        .createQuery(criteriaQuery)
                        .getResultList()
                );
            } else {
                PageRequest pageRequest = PageRequest.of(
                    page.toPageable().getPageNumber(),
                    page.toPageable().getPageSize(),
                    Sort.by(Sort.Order.asc("id"))
                );

                if (totalCount == 0) {
                    return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
                }

                return new PageImpl<>(
                    entityManager
                        .createQuery(criteriaQuery)
                        .setFirstResult(page.toPageable().getPageNumber() * page.toPageable().getPageSize())
                        .setMaxResults(page.toPageable().getPageSize())
                        .getResultList(),
                    pageRequest,
                    totalCount
                );
            }

        } else {
            SQLQueryBuilder<BpmEntitySubType> builder = getSQLQueryBuilder(BpmEntitySubType.class)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .is("status", EntityStatus.ACTIVE);

            if (page.getName() != null) {
                Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
                Map<String, Object> operator = new IdentityHashMap<>();
                operator.put("$like", page.getName());
                keywordCriteria.put("nameCn", operator);
                keywordCriteria.put("nameEn", operator);
                builder.orObj(keywordCriteria);
            }

            if (page.getEntityTypeId() != null) {
                builder.is("entityType.id", page.getEntityTypeId());
            }

            if (page.getEntityTypeName() != null) {
                builder.is("entityType.nameEn", page.getEntityTypeName());
            }

            if (page.getEntityBusinessTypeId() != null) {
                builder.is("entityBusinessType.id", page.getEntityBusinessTypeId());
            }

            builder.sort(new Sort.Order(Sort.Direction.ASC, "orderNo"));
            return builder.paginate(page.toPageable())
                .exec()
                .page();
        }
    }

}
