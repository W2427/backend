package com.ose.tasks.domain.model.repository.wps.simple;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wps.simple.WelderSimpleSearchDTO;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import com.ose.tasks.vo.WelderStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 焊工(简化） CRUD 操作接口。
 */
public class WelderSimpleRepositoryCustomImpl extends BaseRepository
    implements WelderSimpleRepositoryCustom {

    @Override
    public Page<WelderSimplified> findList(Long orgId, Long projectId, EntityStatus status, WelderSimpleSearchDTO wpsSimpleSearchDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<WelderSimplified> root = countQuery.from(WelderSimplified.class);
        Predicate predicate = criteriaBuilder.equal(root.get("status"), EntityStatus.ACTIVE);
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orgId"), orgId));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("projectId"), projectId));

        List<Predicate> predOr = new ArrayList<>();

        if (!StringUtils.isEmpty(wpsSimpleSearchDTO.getName())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + wpsSimpleSearchDTO.getName() + "%"));
        }

        if(!StringUtils.isEmpty(wpsSimpleSearchDTO.getNo())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("no"), "%" + wpsSimpleSearchDTO.getNo() + "%"));
        }

        if(!StringUtils.isEmpty(wpsSimpleSearchDTO.getIdCard())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("idCard"), wpsSimpleSearchDTO.getIdCard()));
        }

        if(!StringUtils.isEmpty(wpsSimpleSearchDTO.getSubConId())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("subConId"), wpsSimpleSearchDTO.getSubConId()));
        }

        if (!StringUtils.isEmpty(wpsSimpleSearchDTO.getWelderStatus())) {
            if ("正常".equals(wpsSimpleSearchDTO.getWelderStatus()) || wpsSimpleSearchDTO.getWelderStatus() == "正常") {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("welderStatus"), WelderStatus.NORMAL));
            } else if ("已失效".equals(wpsSimpleSearchDTO.getWelderStatus()) || wpsSimpleSearchDTO.getWelderStatus() == "已失效") {
                Predicate predicateExpired = criteriaBuilder.equal(root.get("welderStatus"), WelderStatus.EXPIRED);
                Predicate predicateDeactivate = criteriaBuilder.equal(root.get("welderStatus"), WelderStatus.DEACTIVATE);
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predicateExpired, predicateDeactivate));
            } else if ("选择".equals(wpsSimpleSearchDTO.getWelderStatus()) || wpsSimpleSearchDTO.getWelderStatus() == "选择"){
                Predicate predicateNormal = criteriaBuilder.equal(root.get("welderStatus"), WelderStatus.NORMAL);
                Predicate predicateNull = criteriaBuilder.isNull(root.get("welderStatus"));
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(predicateNormal, predicateNull));
            } else {}
        }


        if(!CollectionUtils.isEmpty(wpsSimpleSearchDTO.getUserIds())) {
            Path<Object> path = root.get("userId");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            Set<Long> userIds = wpsSimpleSearchDTO.getUserIds();











            userIds.forEach(in::value);

            predicate = criteriaBuilder.and(predicate, in);
        }


        if(!CollectionUtils.isEmpty(wpsSimpleSearchDTO.getSubConIds())) {
            Path<Object> path = root.get("subConId");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            Set<Long> subConIds = wpsSimpleSearchDTO.getSubConIds();

            subConIds.forEach(in::value);

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.in(in));
        }

        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(root))
                    .where(predicate)
            )
            .getSingleResult();


        CriteriaQuery<WelderSimplified> criteriaQuery = criteriaBuilder.createQuery(WelderSimplified.class);
        criteriaQuery.from(WelderSimplified.class);
        criteriaQuery.where(predicate);
        if ("".equals(wpsSimpleSearchDTO.getSortChange()) || wpsSimpleSearchDTO.getSortChange() == null) {}
        else if ("asc".equals(wpsSimpleSearchDTO.getSortChange()) || wpsSimpleSearchDTO.getSortChange() == "asc") {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("cerExpirationAt")));
        } else if ("desc".equals(wpsSimpleSearchDTO.getSortChange()) || wpsSimpleSearchDTO.getSortChange() == "desc") {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("cerExpirationAt")));
        }

        if (wpsSimpleSearchDTO.getFetchAll()) {
            return new PageImpl<>(
                entityManager
                    .createQuery(criteriaQuery)
                    .getResultList()
            );
        }

        PageRequest pageRequest = PageRequest.of(
            wpsSimpleSearchDTO.toPageable().getPageNumber(),
            wpsSimpleSearchDTO.toPageable().getPageSize(),
            Sort.by(Sort.Order.asc("sort"))
        );

        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }


        return new PageImpl<>(
            entityManager
                .createQuery(criteriaQuery)
                .setFirstResult(wpsSimpleSearchDTO.toPageable().getPageNumber() * wpsSimpleSearchDTO.toPageable().getPageSize())
                .setMaxResults(wpsSimpleSearchDTO.toPageable().getPageSize())
                .getResultList(),
            pageRequest,
            count
        );
    }

}
