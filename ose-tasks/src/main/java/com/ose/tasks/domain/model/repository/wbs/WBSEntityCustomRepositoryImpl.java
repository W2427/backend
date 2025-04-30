package com.ose.tasks.domain.model.repository.wbs;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.util.StringUtils;
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
import java.util.List;

/**
 * 管线实体 CRUD 操作接口。
 */
public class WBSEntityCustomRepositoryImpl<T extends WBSEntityBase, S extends WBSEntryCriteriaBaseDTO>
    extends BaseRepository implements WBSEntityCustomRepository<T, S> {


    /**
     * 查询实体。
     *
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return 用户查询结果分页数据
     */
    @Override
    public Page<T> search(Long orgId, Long projectId, S criteriaDTO, PageDTO pageDTO, Class type) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type);
//        criteriaQuery.from(type);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);


        Root root = criteriaQuery.from(type);
        Root countRoot = countQuery.from(type);

        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root relationRoot = query.from(TaskPackageEntityRelation.class);
        List<Long> relationList = entityManager.createQuery(query.select(relationRoot.get("entityId"))).getResultList();

        Predicate predicate;
        Predicate countPredicate;

        switch (criteriaDTO.getEntityStatus()) {
            case NORMAL:
                predicate = criteriaBuilder.equal(root.get("deleted"), false);
                countPredicate = criteriaBuilder.equal(countRoot.get("deleted"), false);
                break;
            case DELETED:
                predicate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("deleted"), true),
                    criteriaBuilder.equal(root.get("cancelled"), false)
                );
                countPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(countRoot.get("deleted"), true),
                    criteriaBuilder.equal(countRoot.get("cancelled"), false)
                );
                break;
            case CANCELLED:
                predicate = criteriaBuilder.equal(root.get("cancelled"), true);
                countPredicate = criteriaBuilder.equal(countRoot.get("cancelled"), true);
                break;
            case DELETED_CANCELLED:
                predicate = criteriaBuilder.equal(root.get("deleted"), true);
                countPredicate = criteriaBuilder.equal(countRoot.get("deleted"), true);
                break;
            default:
                predicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("deleted"), false),
                    criteriaBuilder.equal(root.get("cancelled"), true)
                );
                countPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(countRoot.get("deleted"), false),
                    criteriaBuilder.equal(countRoot.get("cancelled"), true)
                );
        }

        if (orgId != null && orgId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("orgId"), orgId));
        }
        if (projectId != null && projectId != 0L) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));
            countPredicate = criteriaBuilder.and(countPredicate, criteriaBuilder.equal(countRoot.get("projectId"), projectId));
        }

        if (!StringUtils.isEmpty(criteriaDTO.getNo())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.like(
                    root.<String>get("no"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getNo())) + "%",
                    '\\'));
            countPredicate = criteriaBuilder.and(countPredicate,
                criteriaBuilder.like(
                    countRoot.<String>get("no"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getNo())) + "%",
                    '\\'));
        }

        if (!StringUtils.isEmpty(criteriaDTO.getModuleParentNo())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.like(
                    root.<String>get("moduleParentNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getModuleParentNo())) + "%",
                    '\\'));
            countPredicate = criteriaBuilder.and(countPredicate,
                criteriaBuilder.like(
                    root.<String>get("moduleParentNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getModuleParentNo())) + "%",
                    '\\'));
        }
        if (criteriaDTO.getCreateDateFromTime() != null) {
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.greaterThan(
                    root.<String>get("lastModifiedAt"),
                    criteriaDTO.getCreateDateFromTime()
                ));
            countPredicate = criteriaBuilder.and(
                countPredicate,
                criteriaBuilder.greaterThan(
                    countRoot.<String>get("lastModifiedAt"),
                    criteriaDTO.getCreateDateFromTime()
                ));
        }
        if (criteriaDTO.getCreateDateUntilTime() != null) {
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.lessThan(
                    root.<String>get("lastModifiedAt"),
                    criteriaDTO.getCreateDateUntilTime()
                ));
            countPredicate = criteriaBuilder.and(
                countPredicate,
                criteriaBuilder.lessThan(
                    countRoot.<String>get("lastModifiedAt"),
                    criteriaDTO.getCreateDateUntilTime()
                ));
        }
        if (criteriaDTO.getEntityIds() != null && criteriaDTO.getEntityIds().size() > 0) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(criteriaDTO.getEntityIds())
            );
            countPredicate = criteriaBuilder.and(
                countPredicate,
                countRoot.get("id").in(criteriaDTO.getEntityIds())
            );
        }

        if (type != PressureTestPackageEntityBase.class
            && type != CleanPackageEntityBase.class
            && type != SubSystemEntityBase.class) {
            if (criteriaDTO.getHierarchyType() != null) {

                List<Predicate> orCriteria = new ArrayList<>();
                List<Predicate> countOrCriteria = new ArrayList<>();

                if ("PIPING" == criteriaDTO.getHierarchyType()) {
                    if (criteriaDTO.getAncestorHierarchyIds() != null
                        && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                        for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                            orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                root.get("pathOnPiping"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                            countOrCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                countRoot.get("pathOnPiping"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                        }
                    }
                } else if ("PRESSURE_TEST_PACKAGE" == criteriaDTO.getHierarchyType()) {
                    if (criteriaDTO.getAncestorHierarchyIds() != null
                        && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                        for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                            orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                root.get("pathOnPtp"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                            countOrCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                countRoot.get("pathOnPtp"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                        }
                    }
                } else if ("CLEAN_PACKAGE" == criteriaDTO.getHierarchyType()) {
                    if (criteriaDTO.getAncestorHierarchyIds() != null
                        && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                        for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                            orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                root.get("pathOnCp"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                            countOrCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                countRoot.get("pathOnCp"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                        }
                    }
                } else if ("SUB_SYSTEM" == criteriaDTO.getHierarchyType()) {
                    if (criteriaDTO.getAncestorHierarchyIds() != null
                        && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                        for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                            orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                root.get("pathOnSs"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                            countOrCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                countRoot.get("pathOnSs"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                        }
                    }
                } else if ("STRUCTURE" == criteriaDTO.getHierarchyType()) {
                    if (criteriaDTO.getAncestorHierarchyIds() != null
                        && criteriaDTO.getAncestorHierarchyIds().size() != 0) {
                        for (Long ancestorHierarchyId : criteriaDTO.getAncestorHierarchyIds()) {
                            orCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                root.get("pathOnStructure"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                            countOrCriteria.add(criteriaBuilder.or(criteriaBuilder.like(
                                countRoot.get("pathOnStructure"),
                                "%/" + ancestorHierarchyId.toString() + "/%",
                                '\\')));
                        }
                    }
                }
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.or(orCriteria.toArray(new Predicate[orCriteria.size()])));
                countPredicate = criteriaBuilder.and(countPredicate,
                    criteriaBuilder.or(orCriteria.toArray(new Predicate[countOrCriteria.size()])));
            }
        }

        if ("已添加".equals(criteriaDTO.getAddStatus())) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(relationList)
            );
            countPredicate = criteriaBuilder.and(
                countPredicate,
                countRoot.get("id").in(relationList)
            );
        } else if ("未添加".equals(criteriaDTO.getAddStatus())) {
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.not(
                    root.get("id").in(relationList)
                )
            );
            countPredicate = criteriaBuilder.and(
                countPredicate,
                criteriaBuilder.not(
                    countRoot.get("id").in(relationList)
                )
            );
        }



        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(countRoot))
                    .where(countPredicate)
            )
            .getSingleResult();



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
