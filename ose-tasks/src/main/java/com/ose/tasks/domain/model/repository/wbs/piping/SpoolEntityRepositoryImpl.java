package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
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
public class SpoolEntityRepositoryImpl extends BaseRepository implements SpoolEntityRepositoryCustom {

    @Override
    public Page<SpoolEntity> search(
        Long orgId,
        Long projectId,
        SpoolEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root root = countQuery.from(SpoolEntity.class);

        CriteriaQuery<TaskPackageEntityRelation> query = criteriaBuilder.createQuery(TaskPackageEntityRelation.class);
        Root relationRoot = query.from(TaskPackageEntityRelation.class);
        List<Long> relationList = entityManager.createQuery(query.select(relationRoot.get("entityId"))).getResultList();

        Predicate predicate;
        if (criteriaDTO.getStatus() == null) {
            criteriaDTO.setStatus("默认");
        }
        switch (criteriaDTO.getStatus()) {
            case "正常":
                predicate = criteriaBuilder.equal(root.get("deleted"), false);
                break;
            case "已删除":
                predicate = criteriaBuilder.equal(root.get("deleted"), true);
                break;
            default:
                predicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("deleted"), false),
                    criteriaBuilder.equal(root.get("deleted"), true)
                );
        }
        if (orgId != null && orgId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
        }
        if (projectId != null && projectId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));
        }
        if (!StringUtils.isEmpty(criteriaDTO.getNo())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.like(
                    root.<String>get("no"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getNo())) + "%",
                    '\\'));
        }
        if (criteriaDTO.getEntityIds() != null && criteriaDTO.getEntityIds().size() > 0) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(criteriaDTO.getEntityIds())
            );
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


        if (criteriaDTO.getIsoId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isoEntityId"), criteriaDTO.getIsoId()));
        }

        if (criteriaDTO.getUsed() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isUsedInPipe"), criteriaDTO.getUsed()));
        }


        if (criteriaDTO.getNestGateWay() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("nestGateWay"), criteriaDTO.getNestGateWay()));
        }


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();


        CriteriaQuery<SpoolEntity> criteriaQuery = criteriaBuilder.createQuery(SpoolEntity.class);
        criteriaQuery.from(SpoolEntity.class);
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
