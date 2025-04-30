package com.ose.tasks.domain.model.repository.wbs;

import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryActivityInstance;
import org.springframework.data.domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WBSEntryActivityInstanceRepositoryCustomImpl
    extends BaseRepository
    implements WBSEntryActivityInstanceRepositoryCustom {

    private static final String VIEW_ALIAS = "wbs_entry_activity_instance";

    /**
     * 查询计划条目活动实例。
     *
     * @param userId       用户 ID
     * @param projectId    项目 ID
     * @param wbsEntryId   计划条目 ID
     * @param privilegeMap 组织权限
     * @param pageable     分页参数
     * @return 计划条目活动实例分页数据
     */
    @Override
    public Page<WBSEntryActivityInstance> findByPrivileges(
        Long userId,
        Long projectId,
        Long wbsEntryId,
        Map<Long, Set<String>> privilegeMap,
        Pageable pageable
    ) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();


        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<WBSEntryActivityInstance> countFrom = countQuery.from(WBSEntryActivityInstance.class);
        countFrom.alias(VIEW_ALIAS);

        Set<Long> orgIDs = privilegeMap.keySet();
        Set<String> privileges;
        List<Predicate> privilegeCriteria = new ArrayList<>();

        /*----------------------------------------------------------------------
           查询条件设置 START
             `project_id` = 'PROJECT_ID'
             AND (
               `assignee_id` LIKE '%,USER_ID,%'
               OR `privileges` IS NULL
               OR (`team_path` LIKE '%/ORG_ID_1/%' AND `privileges` LIKE 'PRIVILEGE_1.%')
               OR (`team_path` LIKE '%/ORG_ID_1/%' AND `privileges` LIKE 'PRIVILEGE_2.%')
               ...
             )
         ---------------------------------------------------------------------*/

        privilegeCriteria.add(criteriaBuilder.like(countFrom.get("assigneeId"), "%," + userId + ",%"));
        privilegeCriteria.add(criteriaBuilder.isNull(countFrom.get("privileges")));

        for (Long orgId : orgIDs) {

            privileges = privilegeMap.get(orgId);

            if (privileges.contains(UserPrivilegeDTO.ALL_PRIVILEGE_VALUE)) {
                privilegeCriteria.add(criteriaBuilder.like(countFrom.get("teamPath"), "%/" + orgId + "/%"));
                continue;
            }

            for (String privilege : privileges) {
                privilegeCriteria.add(
                    criteriaBuilder.and(
                        criteriaBuilder.like(countFrom.get("teamPath"), "%/" + orgId + "/%"),
                        criteriaBuilder.like(countFrom.get("privileges"), privilege + ".%")
                    )
                );
            }

        }

        Predicate predicate = criteriaBuilder.and(
            criteriaBuilder.equal(countFrom.get("projectId"), projectId),
            criteriaBuilder.like(countFrom.get("wbsPath"), "%/" + wbsEntryId + "/%"),
            criteriaBuilder.or(privilegeCriteria.toArray(new Predicate[]{}))
        );

        /*----------------------------------------------------------------------
           查询条件设置 END
         ---------------------------------------------------------------------*/


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
            Sort.by(Sort.Order.desc("planStartAt"))
        );

        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        }


        CriteriaQuery<WBSEntryActivityInstance> searchQuery = criteriaBuilder.createQuery(WBSEntryActivityInstance.class);
        Root<WBSEntryActivityInstance> searchFrom = searchQuery.from(WBSEntryActivityInstance.class);
        searchFrom.alias(VIEW_ALIAS);
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
