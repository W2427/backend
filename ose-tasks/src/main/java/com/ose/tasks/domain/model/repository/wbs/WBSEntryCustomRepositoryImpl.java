package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.util.SQLUtils;
import com.ose.util.ValueUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.*;

/**
 * WBS 条目 CRUD 操作接口。
 */
public class WBSEntryCustomRepositoryImpl extends BaseRepository implements WBSEntryCustomRepository {

    private static final String TABLE_ALIAS = "_wbs_entries";

    /**
     * 查询四级计划。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 四级计划分页数据
     */
    @Override
    public Page<WBSEntryPlain> search(
        final Long projectId,
        final WBSEntryCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        SQLQueryBuilder<WBSEntryPlain> wbsEntrySQLQueryBuilder = getSQLQueryBuilder(WBSEntryPlain.class)
            .is("projectId", projectId);


        if (criteriaDTO.getTaskPackageId() != null) {
            wbsEntrySQLQueryBuilder.is("sProjectId", projectId);
            wbsEntrySQLQueryBuilder.is("taskPackageId", criteriaDTO.getTaskPackageId());
        }

        if (criteriaDTO.getModuleNo() != null) {
            wbsEntrySQLQueryBuilder.is("moduleNo", criteriaDTO.getModuleNo());
        }
        if (criteriaDTO.getIsoNo() != null) {
            wbsEntrySQLQueryBuilder.is("isoNo", criteriaDTO.getIsoNo());
        }
        if (criteriaDTO.getStageName() != null) {
            wbsEntrySQLQueryBuilder.is("stage", criteriaDTO.getStageName());
        }
        if (criteriaDTO.getProcessName() != null) {
            wbsEntrySQLQueryBuilder.is("process", criteriaDTO.getProcessName());
        }
        if (criteriaDTO.getEntityType() != null) {
            wbsEntrySQLQueryBuilder.is("entityType", criteriaDTO.getEntityType());
        }
        if (criteriaDTO.getEntitySubType() != null) {
            wbsEntrySQLQueryBuilder.is("entitySubType", criteriaDTO.getEntitySubType());
        }
        if (criteriaDTO.getWorkSiteId() != null) {
            wbsEntrySQLQueryBuilder.is("workSiteId", criteriaDTO.getWorkSiteId());
        }
        if (criteriaDTO.getTeamId() != null) {
            wbsEntrySQLQueryBuilder.is("teamId", criteriaDTO.getTeamId());
        }
        if (criteriaDTO.getSector() != null) {
            wbsEntrySQLQueryBuilder.is("sector", criteriaDTO.getSector());
        }
        if (criteriaDTO.getRunningStatus() != null) {
            if (criteriaDTO.getRunningStatus() == WBSEntryRunningStatus.ISNULL) {
                wbsEntrySQLQueryBuilder.isNull("runningStatus");
            } else {
                wbsEntrySQLQueryBuilder.is("runningStatus", criteriaDTO.getRunningStatus());
            }
        }
        if (criteriaDTO.getEntityNo() != null){
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> entityNo = new IdentityHashMap<>();
            entityNo.put("$like", criteriaDTO.getEntityNo());
            keywordCriteria.put("name", entityNo);
            keywordCriteria.put("name1", entityNo);
            keywordCriteria.put("name2", entityNo);
            wbsEntrySQLQueryBuilder.or(keywordCriteria);
        }

        wbsEntrySQLQueryBuilder.is("active", true)
            .is("deleted", false);


        Pageable newPageable = null;
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        if (criteriaDTO.isPageable()) {
            newPageable = PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, sort);
        } else {
            newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return wbsEntrySQLQueryBuilder
            .paginate(newPageable)
            .exec()
            .page();
    }


    /**
     * 查询四级计划 及汇总信息。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @return 四级计划分页数据 及汇总信息
     * 返回 完成计划个数 计划总个数 完成的物量 物量总量
     */
    @Override
    public Tuple searchSum(Long projectId, WBSEntryCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> tupleQuery = criteriaBuilder.createTupleQuery();

        Root<WBSEntryPlain> root = tupleQuery.from(WBSEntryPlain.class);

        Predicate predicate = criteriaBuilder.equal(root.get("projectId"), projectId);


        if (criteriaDTO.getTaskPackageId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("taskPackageId"), criteriaDTO.getTaskPackageId()));
        }

        if (criteriaDTO.getModuleNo() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("moduleNo"), criteriaDTO.getModuleNo()));
        }

        if (criteriaDTO.getIsoNo() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isoNo"), criteriaDTO.getIsoNo()));
        }

        if (criteriaDTO.getStageName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("stage"), criteriaDTO.getStageName()));
        }

        if (criteriaDTO.getProcessName() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("process"), criteriaDTO.getProcessName()));
        }

        if (criteriaDTO.getEntityType() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("entityType"), criteriaDTO.getEntityType()));
        }

        if (criteriaDTO.getEntitySubType() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("entitySubType"), criteriaDTO.getEntitySubType()));
        }

        if (criteriaDTO.getTaskPackageId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("sProjectId"), projectId));
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("taskPackageId"), criteriaDTO.getTaskPackageId()));
        }

        if (criteriaDTO.getTeamId() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("teamId"), criteriaDTO.getTeamId()));
        }

        if (criteriaDTO.getRunningStatus() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("runningStatus"), criteriaDTO.getRunningStatus()));
        }

        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("active"), true));
        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("deleted"), false));

        Expression<Long> selectFinishedCount = criteriaBuilder.sum(criteriaBuilder.<Long>selectCase()
            .when(criteriaBuilder.equal(root.get("runningStatus"), WBSEntryRunningStatus.APPROVED), 1L)
            .<Long>otherwise(0L));

        Expression<Long> selectTotalCount = criteriaBuilder.count(root.get("projectId"));


        Expression<Double> workLoadFinished = criteriaBuilder.sum(criteriaBuilder.<Double>selectCase()
            .when(criteriaBuilder.equal(root.get("runningStatus"), WBSEntryRunningStatus.APPROVED), root.get("workLoad"))
            .<Double>otherwise(0.0));


        Expression<Double> workLoadTotal = criteriaBuilder.sum(root.get("workLoad"));


        tupleQuery.multiselect(
            selectFinishedCount,
            selectTotalCount,
            workLoadFinished,
            workLoadTotal).where(predicate);

        return entityManager.createQuery(tupleQuery).getSingleResult();
    }


    /**
     * 取得四级计划所有工序列表。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @return 四级计划信息列表
     */
    @Override
    @SuppressWarnings("JpaQlInspection")
    public List<EntityProcessDTO> processes(
        final Long projectId,
        final WBSEntryCriteriaDTO criteriaDTO
    ) {

        String sql = "SELECT new com.ose.tasks.dto.process.EntityProcessDTO(e.stage, e.process, COUNT(0))"
            + " FROM com.ose.tasks.entity.wbs.entry.WBSEntry e JOIN com.ose.tasks.entity.wbs.entry.WBSEntryState es ON e.id = es.wbsEntryId ";

        final List<String> where = new ArrayList<>();
        final Map<String, Object> parameters = new HashMap<>();

        where.add("e.projectId = :projectId");
        parameters.put("projectId", projectId);

        ValueUtils.notNull(criteriaDTO.getModuleNo(), moduleNo -> {
            where.add("e.moduleNo = :moduleNo");
            parameters.put("moduleNo", moduleNo);
        });

        ValueUtils.notNull(criteriaDTO.getIsoNo(), isoNo -> {
            where.add("e.isoNo = :isoNo");
            parameters.put("isoNo", isoNo);
        });

        ValueUtils.notNull(criteriaDTO.getStageName(), stageName -> {
            where.add("e.stage = :stageName");
            parameters.put("stageName", stageName);
        });

        ValueUtils.notNull(criteriaDTO.getProcessName(), processName -> {
            where.add("e.process = :processName");
            parameters.put("processName", processName);
        });

        ValueUtils.notNull(criteriaDTO.getEntityType(), entityType -> {
            where.add("e.entityType = :entityType");
            parameters.put("entityType", entityType);
        });

        ValueUtils.notNull(criteriaDTO.getEntitySubType(), entitySubType -> {
            where.add("e.entitySubType = :entitySubType");
            parameters.put("entitySubType", entitySubType);
        });

        ValueUtils.notNull(criteriaDTO.getTaskPackageId(), taskPackageId -> {
            where.add("es.taskPackageId = :taskPackageId");
            parameters.put("taskPackageId", taskPackageId);
        });

        ValueUtils.notNull(criteriaDTO.getWorkSiteId(), workSiteId -> {
            where.add("es.workSiteId = :workSiteId");
            parameters.put("workSiteId", workSiteId);
        });

        ValueUtils.notNull(criteriaDTO.getTeamId(), teamId -> {
            where.add("es.teamId = :teamId");
            parameters.put("teamId", teamId);
        });

        TypedQuery<EntityProcessDTO> searchQuery = getEntityManager().createQuery(
            sql
                + " WHERE " + String.join(" AND ", where)
                + " GROUP BY e.stage, e.process",
            EntityProcessDTO.class
        );

        parameters
            .keySet()
            .forEach(key -> searchQuery.setParameter(key, parameters.get(key)));

        return searchQuery.getResultList();
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
    public List<WBSEntryPlain> searchWbsEntry(
        final Long projectId,
        final WBSEntry rootWbsEntry,
        final WBSEntryQueryDTO queryDTO,
        final String parentPath
    ) {

        final EntityManager entityManager = getEntityManager();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<WBSEntryPlain> searchQuery = criteriaBuilder.createQuery(WBSEntryPlain.class);
        final Root<WBSEntryPlain> fromClause = searchQuery.from(WBSEntryPlain.class);

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
        final WBSEntry rootWbsEntry,
        final WBSEntryQueryDTO queryDTO,
        final CriteriaBuilder criteriaBuilder,
        final Root<WBSEntryPlain> fromClause,
        final String parentPath
    ) {

        final int depth = queryDTO.getDepth();

        final int maxDepth = (rootWbsEntry == null ? 0 : (rootWbsEntry.getDepth() + 1))
            + (depth <= 0 ? Short.MAX_VALUE : (depth - 1));

        final List<Predicate> conditions = new ArrayList<>();

        fromClause.alias(TABLE_ALIAS);

        conditions.add(criteriaBuilder.equal(fromClause.get("projectId"), projectId));
        if (rootWbsEntry != null) {
            conditions.add(criteriaBuilder.equal(
                fromClause.get("parentId"),
                rootWbsEntry.getId()
            ));
        }
        conditions.add(criteriaBuilder.equal(fromClause.get("deleted"), false));
        conditions.add(criteriaBuilder.lessThanOrEqualTo(fromClause.get("depth"), maxDepth));

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

        conditions.add(criteriaBuilder.equal(fromClause.get("deleted"), false));

        return criteriaBuilder.and(conditions.toArray(new Predicate[]{}));
    }


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
        final Root<WBSEntryPlain> fromClause,
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
        final Root<WBSEntryPlain> fromClause,
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


}
