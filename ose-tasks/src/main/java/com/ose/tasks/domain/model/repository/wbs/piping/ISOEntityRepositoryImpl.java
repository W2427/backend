package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 管线实体 CRUD 操作接口。
 */
public class ISOEntityRepositoryImpl extends BaseRepository implements ISOEntityRepositoryCustom {

    @Override
    public Page<ISOEntity> search(
        Long orgId,
        Long projectId,
        ISOEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root root = countQuery.from(ISOEntity.class);

        CriteriaQuery<TaskPackageEntityRelation> query = criteriaBuilder.createQuery(TaskPackageEntityRelation.class);
        Root relationRoot = query.from(TaskPackageEntityRelation.class);
        List<Long> relationList = entityManager.createQuery(query.select(relationRoot.get("entityId"))).getResultList();

        Predicate predicate = criteriaBuilder.equal(root.get("deleted"), false);
        if (orgId != null && orgId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
        }
        if (projectId != null && projectId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));
        }
        if (criteriaDTO.getEntityIds() != null && criteriaDTO.getEntityIds().size() > 0) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(criteriaDTO.getEntityIds())
            );
        }
        if (!StringUtils.isEmpty(criteriaDTO.getNo())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.like(
                    root.<String>get("no"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getNo())) + "%",
                    '\\'));
        }

        if ("已添加".equals(criteriaDTO.getAddStatus())) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(relationList)
            );
        } else if ("未添加".equals(criteriaDTO.getAddStatus())) {
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.not(
                    root.get("id").in(relationList)
                )
            );
        } else {}


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();


        CriteriaQuery<ISOEntity> criteriaQuery = criteriaBuilder.createQuery(ISOEntity.class);
        criteriaQuery.from(ISOEntity.class);
        criteriaQuery.where(predicate);

        if (pageDTO.getFetchAll()) {
            return new PageImpl<>(
                entityManager
                    .createQuery(criteriaQuery)
                    .getResultList()
            );
        } else {
            PageRequest pageRequest = PageRequest.of(
                pageDTO.toPageable().getPageNumber(),
                pageDTO.toPageable().getPageSize(),
                Sort.by(Sort.Order.asc("id"))
            );

            if (count == 0) {
                return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
            }

            return new PageImpl<>(
                entityManager
                    .createQuery(criteriaQuery)
                    .setFirstResult(pageDTO.toPageable().getPageNumber() * pageDTO.toPageable().getPageSize())
                    .setMaxResults(pageDTO.toPageable().getPageSize())
                    .getResultList(),
                pageRequest,
                count
            );
        }
    }

}
