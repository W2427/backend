package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import com.ose.tasks.entity.wbs.entity.ComponentHierarchyInfoEntity;
import com.ose.tasks.vo.wbs.HierarchyType;
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
public class ComponentEntityRepositoryImpl extends BaseRepository
    implements WBSEntityCustomRepository<ComponentHierarchyInfoEntity, ComponentEntryCriteriaDTO>, ComponentEntityRepositoryCustom {

    /**
     * 查管件。
     *
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return 用户查询结果分页数据
     */
    @Override
    public Page<ComponentHierarchyInfoEntity> search(
        Long orgId,
        Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO,
        Class type) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root root = countQuery.from(ComponentHierarchyInfoEntity.class);

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
        if (criteriaDTO.getParentEntityId() != null && criteriaDTO.getParentEntityId() != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("parentEntityId"), criteriaDTO.getParentEntityId()));
        }
        if (criteriaDTO.getComponentEntityType() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("componentEntityType"),
                criteriaDTO.getComponentEntityType()));
        }
        if (!StringUtils.isEmpty(criteriaDTO.getNpsText())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("npsText"),
                criteriaDTO.getNpsText()));
        }
        if (!StringUtils.isEmpty(criteriaDTO.getThickness())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("thickness"),
                criteriaDTO.getThickness()));
        }

        if (criteriaDTO.getEntityIds() != null && criteriaDTO.getEntityIds().size() > 0) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(criteriaDTO.getEntityIds())
            );
        }

        if (criteriaDTO.getHierarchyType() != null) {

            List<Predicate> orCriteria = new ArrayList<>();

            if (HierarchyType.PIPING.toString() == criteriaDTO.getHierarchyType()) {
                if (criteriaDTO.getAncestorHierarchyIds() != null
                    && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                    for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                        orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                            root.get("pathPiping"),
                            "%/" + ancestorHierarchyId.toString() + "/%",
                            '\\')));
                    }
                }
            }
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.or(orCriteria.toArray(new Predicate[orCriteria.size()])));
        }

        if (criteriaDTO.getKeyword() != null) {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(
                root.get("parentNo"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicates.add(criteriaBuilder.like(
                root.get("shortCode"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicates.add(criteriaBuilder.like(
                root.get("materialCode"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicates.add(criteriaBuilder.like(
                root.get("material"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
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


        CriteriaQuery<ComponentHierarchyInfoEntity> criteriaQuery = criteriaBuilder.createQuery(type);
        criteriaQuery.from(type);
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

    @Override
    public Page<ComponentEntity> search(
        Long orgId,
        Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root root = countQuery.from(ComponentEntity.class);

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

//        Predicate predicate = criteriaBuilder.equal(root.get("deleted"), false);

        if (orgId != null && orgId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
        }
        if (projectId != null && projectId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));
        }
        if (criteriaDTO.getParentEntityId() != null && criteriaDTO.getParentEntityId() != 0L) {
            if (root.get("isoEntityId") != null && !root.get("isoEntityId").equals("") && (root.get("spoolEntityId") != null && !root.get("spoolEntityId").equals(""))) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("spoolEntityId"), criteriaDTO.getParentEntityId()));
            }
            if (root.get("isoEntityId") != null && !root.get("isoEntityId").equals("") && root.get("spoolEntityId") == null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isoEntityId"), criteriaDTO.getParentEntityId()));
            }
        }
        if (criteriaDTO.getComponentEntityType() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("componentEntityType"),
                criteriaDTO.getComponentEntityType()));
        }
        if (!StringUtils.isEmpty(criteriaDTO.getNpsText())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("npsText"),
                criteriaDTO.getNpsText()));
        }
        if (!StringUtils.isEmpty(criteriaDTO.getThickness())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                root.get("thickness"),
                criteriaDTO.getThickness()));
        }




















        if (criteriaDTO.getKeyword() != null) {
            List<Predicate> predicates = new ArrayList<>();
            if (root.get("isoNo") != null && !root.get("isoNo").equals("") && (root.get("spoolNo") != null && !root.get("spoolNo").equals(""))) {
                predicates.add(criteriaBuilder.like(
                    root.get("spoolNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                    '\\'));
                predicates.add(criteriaBuilder.like(
                    root.get("isoNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                    '\\'));
            }
            if (root.get("isoNo") != null && !root.get("isoNo").equals("") && root.get("spoolNo") == null) {
                predicates.add(criteriaBuilder.like(
                    root.get("isoNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                    '\\'));
            }
            predicates.add(criteriaBuilder.like(
                root.get("shortCode"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicates.add(criteriaBuilder.like(
                root.get("materialCode"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicates.add(criteriaBuilder.like(
                root.get("material"),
                "%" + SQLQueryBuilder.escapeLikeValue(criteriaDTO.getKeyword()) + "%",
                '\\'));
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
        }


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();


        CriteriaQuery<ComponentEntity> criteriaQuery = criteriaBuilder.createQuery(ComponentEntity.class);
        criteriaQuery.from(ComponentEntity.class);
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
