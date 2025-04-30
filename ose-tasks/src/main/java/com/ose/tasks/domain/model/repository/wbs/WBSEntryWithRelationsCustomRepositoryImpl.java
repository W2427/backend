package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntryWithRelations;
import com.ose.util.SQLUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批处理任务状态记录查询操作。
 */
public class WBSEntryWithRelationsCustomRepositoryImpl extends BaseRepository implements WBSEntryWithRelationsCustomRepository {

    private static final String TABLE_ALIAS = "_wbs_entries";

    /**
     * 添加查询条件。
     *
     * @param criteriaBuilder 查询条件构造器
     * @param fromClause      FROM 字句
     * @param conditions      条件列表
     * @param key             字段名
     * @param value           字段值
     */
    private static void addPredicate(
        final CriteriaBuilder criteriaBuilder,
        final Root<WBSEntryWithRelations> fromClause,
        final List<Predicate> conditions,
        final String key,
        final Object value
    ) {
        if (value != null) {
            conditions.add(criteriaBuilder.equal(fromClause.get(key), value));
        }
    }

    /**
     * 添加范围查询条件。
     *
     * @param criteriaBuilder 查询条件构造器
     * @param fromClause      FROM 字句
     * @param conditions      条件列表
     * @param key             字段名
     * @param from            字段值
     * @param to              字段值
     */
    private static void addPredicate(
        final CriteriaBuilder criteriaBuilder,
        final Root<WBSEntryWithRelations> fromClause,
        final List<Predicate> conditions,
        final String key,
        final Date from,
        final Date to
    ) {
        if (from != null && to != null) {
            conditions.add(criteriaBuilder.between(fromClause.get(key), from, to));
        } else if (from != null) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(fromClause.get(key), from));
        } else if (to != null) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(fromClause.get(key), to));
        }
    }

    /**
     * 设置查询条件。
     *
     * @param projectId       项目 ID
     * @param rootWbsEntry    根条目
     * @param queryDTO        查询条件数据传输对象
     * @param criteriaBuilder 查询条件构造器
     * @param fromClause      FROM 字句
     * @return 查询条件
     */
    private static Predicate setCriteria(
        final Long projectId,
        final WBSEntryWithRelations rootWbsEntry,
        final WBSEntryQueryDTO queryDTO,
        final CriteriaBuilder criteriaBuilder,
        final Root<WBSEntryWithRelations> fromClause,
        final String parentPath
    ) {

        final int depth = queryDTO.getDepth();

        final int maxDepth = (rootWbsEntry == null ? 0 : (rootWbsEntry.getDepth() + 1))
            + (depth <= 0 ? Short.MAX_VALUE : (depth - 1));

        final List<Predicate> conditions = new ArrayList<>();

        fromClause.alias(TABLE_ALIAS);

        conditions.add(criteriaBuilder.equal(fromClause.get("projectId"), projectId));
        conditions.add(criteriaBuilder.equal(fromClause.get("deleted"), false));
        conditions.add(criteriaBuilder.lessThanOrEqualTo(fromClause.get("depth"), maxDepth));

        if (rootWbsEntry != null) {
            conditions.add(criteriaBuilder.like(
                fromClause.get("path"),
                parentPath + rootWbsEntry.getId() + "/%"
            ));
        }

        if (queryDTO.getEntityNo() != null) {
            conditions.add(criteriaBuilder.like(
                fromClause.get("name"),
                SQLUtils.escapeLike(queryDTO.getEntityNo()) + "%"
            ));
        }
        addPredicate(criteriaBuilder, fromClause, conditions, "type", queryDTO.getEntryType());
        addPredicate(criteriaBuilder, fromClause, conditions, "startAt", queryDTO.getStartAt());
        addPredicate(criteriaBuilder, fromClause, conditions, "startAt", queryDTO.getStartFrom(), queryDTO.getStartTo());
        addPredicate(criteriaBuilder, fromClause, conditions, "finishAt", queryDTO.getStartAt());
        addPredicate(criteriaBuilder, fromClause, conditions, "finishAt", queryDTO.getFinishFrom(), queryDTO.getFinishTo());
        addPredicate(criteriaBuilder, fromClause, conditions, "startAt", queryDTO.getStartAt());
        addPredicate(criteriaBuilder, fromClause, conditions, "finishAt", queryDTO.getFinishAt());
        addPredicate(criteriaBuilder, fromClause, conditions, "teamId", queryDTO.getTeamId());
        addPredicate(criteriaBuilder, fromClause, conditions, "workSiteId", queryDTO.getWorkSiteId());
        addPredicate(criteriaBuilder, fromClause, conditions, "moduleType", queryDTO.getModuleType());
        addPredicate(criteriaBuilder, fromClause, conditions, "moduleHierarchyNodeId", queryDTO.getModuleId());
        addPredicate(criteriaBuilder, fromClause, conditions, "sector", queryDTO.getModuleNo());
        addPredicate(criteriaBuilder, fromClause, conditions, "isoNo", queryDTO.getIsoNo());
        addPredicate(criteriaBuilder, fromClause, conditions, "stage", queryDTO.getStage());
        addPredicate(criteriaBuilder, fromClause, conditions, "process", queryDTO.getProcess());
        addPredicate(criteriaBuilder, fromClause, conditions, "entityType", queryDTO.getEntityType());
        addPredicate(criteriaBuilder, fromClause, conditions, "entitySubType", queryDTO.getEntitySubType());
        addPredicate(criteriaBuilder, fromClause, conditions, "active", queryDTO.getActive());
        addPredicate(criteriaBuilder, fromClause, conditions, "finished", queryDTO.getFinished());
        addPredicate(criteriaBuilder, fromClause, conditions, "runningStatus", queryDTO.getRunningStatus());
        addPredicate(criteriaBuilder, fromClause, conditions, "taskPackageId", queryDTO.getWorkSiteId());
        addPredicate(criteriaBuilder, fromClause, conditions, "bundleId", queryDTO.getBundleId());

        return criteriaBuilder.and(conditions.toArray(new Predicate[]{}));
    }

    /**
     * 查询 WBS 条目数据。
     *
     * @param projectId    项目 ID
     * @param rootWbsEntry 上级 WBS 条目
     * @param queryDTO     查询条件
     * @return WBS 条目分页数据
     */
    @Override
    public List<WBSEntryWithRelations> search(
        final Long projectId,
        final WBSEntryWithRelations rootWbsEntry,
        final WBSEntryQueryDTO queryDTO,
        final String parentPath
    ) {

        final EntityManager entityManager = getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<WBSEntryWithRelations> searchQuery = criteriaBuilder.createQuery(WBSEntryWithRelations.class);
        final Root<WBSEntryWithRelations> fromClause = searchQuery.from(WBSEntryWithRelations.class);

        searchQuery
            .where(
                setCriteria(
                    projectId,
                    rootWbsEntry,
                    queryDTO,
                    criteriaBuilder,
                    fromClause,
                    parentPath
                )
            )
            .orderBy(
                criteriaBuilder.asc(fromClause.get("depth"))


            );

        return entityManager.createQuery(searchQuery).getResultList();
    }

    /**
     * 查询 WBS 条目数据。
     *
     * @param projectId    项目 ID
     * @param rootWbsEntry 上级 WBS 条目
     * @param queryDTO     查询条件
     * @param pageable     分页参数
     * @return WBS 条目分页数据
     */
    @Override
    public Page<WBSEntryWithRelations> search(
        final Long projectId,
        final WBSEntryWithRelations rootWbsEntry,
        final WBSEntryQueryDTO queryDTO,
        final String parentPath,
        final Pageable pageable
    ) {

        final EntityManager entityManager = getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Root<WBSEntryWithRelations> fromClause = countQuery.from(WBSEntryWithRelations.class);

        Predicate predicate = setCriteria(
            projectId,
            rootWbsEntry,
            queryDTO,
            criteriaBuilder,
            fromClause,
            parentPath
        );


        Long count = entityManager
            .createQuery(
                countQuery
                    .select(criteriaBuilder.count(fromClause))
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


        CriteriaQuery<WBSEntryWithRelations> searchQuery = criteriaBuilder.createQuery(WBSEntryWithRelations.class);
        Root<WBSEntryWithRelations> searchFrom = searchQuery.from(WBSEntryWithRelations.class);
        searchFrom.alias(TABLE_ALIAS);

        searchQuery
            .where(predicate)
            .orderBy(
                criteriaBuilder.asc(searchFrom.get("depth")),
                criteriaBuilder.asc(searchFrom.get("sort"))
            );

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
