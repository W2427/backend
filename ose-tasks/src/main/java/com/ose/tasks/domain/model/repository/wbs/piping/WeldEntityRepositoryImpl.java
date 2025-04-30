package com.ose.tasks.domain.model.repository.wbs.piping;
import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.vo.qc.NDTExecuteFlag;
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

/**
 * 管线实体 CRUD 操作接口。
 */
public class WeldEntityRepositoryImpl extends BaseRepository implements WeldEntityRepositoryCustom {


    @Override
    public Page<WeldEntity> search(
        Long orgId,
        Long projectId,
        WeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root root = countQuery.from(WeldEntity.class);

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

        if (!StringUtils.isEmpty(criteriaDTO.getModuleNo())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.like(
                    root.<String>get("moduleNo"),
                    "%" + SQLQueryBuilder.escapeLikeValue(StringUtils.trim(criteriaDTO.getModuleNo())) + "%",
                    '\\'));
        }

        if (criteriaDTO.getEntityIds() != null && criteriaDTO.getEntityIds().size() > 0) {
            predicate = criteriaBuilder.and(
                predicate,
                root.get("id").in(criteriaDTO.getEntityIds())
            );
        }


        if (!StringUtils.isEmpty(criteriaDTO.getWeldType())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.equal(
                    root.<String>get("weldType"), criteriaDTO.getWeldType()));
        }

        if (!StringUtils.isEmpty(criteriaDTO.getWeldEntityType())) {
            predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.equal(
                    root.<String>get("weldEntityType"), criteriaDTO.getWeldEntityType()));
        }

        if (!StringUtils.isEmpty(criteriaDTO.getMaterialGroupCode())) {
            if ("EMPTY".equalsIgnoreCase(criteriaDTO.getMaterialGroupCode())) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.isNull(root.<String>get("materialGroupCode")));
            } else {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.<String>get("materialGroupCode"),
                        criteriaDTO.getMaterialGroupCode()));
            }
        }

        if (criteriaDTO.getWelderId() != null) {
            predicate = criteriaBuilder.and(
                predicate,
                criteriaBuilder.equal(root.get("welderId"), criteriaDTO.getWelderId())
            );
        }

        String ndt = criteriaDTO.getNdt();
        if (!StringUtils.isEmpty(ndt)) {

            if (ndt.equals("true")) {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.notEqual(root.get("ndtExecuteFlag"), NDTExecuteFlag.EXECUTED)
                );

            } else {
                predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.equal(root.get("ndtExecuteFlag"), NDTExecuteFlag.EXECUTED)
                );
            }

        }


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();


        CriteriaQuery<WeldEntity> criteriaQuery = criteriaBuilder.createQuery(WeldEntity.class);
        criteriaQuery.from(WeldEntity.class);
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
