package com.ose.issues.domain.model.repository;

import com.ose.issues.dto.IssueCriteriaDTO;
import com.ose.issues.dto.PropertyDTO;
import com.ose.issues.entity.Experience;
import com.ose.issues.entity.Issue;
import com.ose.issues.entity.IssueBase;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.IssueType;
import com.ose.repository.BaseRepository;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.*;

public class IssueCustomRepositoryImpl extends BaseRepository implements IssueCustomRepository {

    /**
     * 查询问题。
     *
     * @param <T>       问题范型
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @param clazz     问题类型
     * @return 用户查询结果分页数据
     */
    @Override
    @SuppressWarnings("JpaQlInspection")
    public <T extends IssueBase> Page<T> search(
        final Long orgId,
        final Long projectId,
        final IssueCriteriaDTO criteria,
        Pageable pageable,
        final Class<T> clazz
    ) {

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("orgId", orgId);
        parameters.put("projectId", projectId);

        final IssueType issueType;
        final Long creatorId = criteria.getCreatorId();
        final Date createdTimeFrom = criteria.getCreatedAtStartTime();
        final Date createdTimeTo = criteria.getCreatedAtEndTime();
        final List<Long> issueIDs;
        final Long leaderId;
        final Date finishTimeFrom;
        final Date finishTimeTo;
        final List<IssueSource> source;
        final List<EntityStatus> status;
        final String keyword = StringUtils.trim(criteria.getKeyword());
        final Long participantId;
        final List<PropertyDTO> properties = criteria.getProperties();
        final Long memberId;
        final Long departmentId;
        final List<String> disciplines = criteria.getDisciplines();
        final List<String> modules = criteria.getModules();
        final List<String> subSystems = criteria.getSubSystems();

        if (clazz == Issue.class) {
            issueType = IssueType.ISSUE;
        } else if (clazz == Experience.class) {
            issueType = IssueType.EXPERIENCE;
        } else {
            issueType = null;
        }

        if (criteria instanceof IssueCriteriaDTO) {
            IssueCriteriaDTO issueCriteria = (IssueCriteriaDTO) criteria;
            issueIDs = issueCriteria.getIssueIDs();
            leaderId = issueCriteria.getLeaderId();
            finishTimeFrom = issueCriteria.getFinishedStartTime();
            finishTimeTo = issueCriteria.getFinishedEndTime();
            source = issueCriteria.getSource();
            status = issueCriteria.getStatus();
            participantId = issueCriteria.getParticipantId();
            memberId = issueCriteria.getMemberId();
            departmentId = issueCriteria.getDepartmentId();
        } else {
            issueIDs = null;
            leaderId = null;
            finishTimeFrom = null;
            finishTimeTo = null;
            source = null;
            status = null;
            participantId = null;
            memberId = null;
            departmentId = null;
        }

        StringBuilder from = new StringBuilder(" FROM " + clazz.getName() + " i");
        String where = " WHERE i.orgId = :orgId AND i.projectId = :projectId AND i.deleted = false ";


        if (issueType != null) {
            where += " AND i.type = :issueType";
            parameters.put("issueType", issueType);
        }

        if (clazz == Experience.class) {
            where += " AND i.currentExperience = true ";
        }

        if (createdTimeFrom != null) {
            where += " AND i.createdAt >= :createdTimeFrom";
            parameters.put("createdTimeFrom", createdTimeFrom);
        }

        if (createdTimeTo != null) {
            where += " AND i.createdAt <= :createdTimeTo";
            parameters.put("createdTimeTo", createdTimeTo);
        }

        if (issueIDs != null && issueIDs.size() > 0) {
            where += " AND i.id IN :issueIDs";
            parameters.put("issueIDs", issueIDs);
        }

        if (leaderId != null) {
            where += " AND i.punchInchargePersonId IN :leaderId";
            parameters.put("leaderId", leaderId);
        }

        if (finishTimeFrom != null) {
            where += " AND i.planFinishTime >= :finishTimeFrom";
            parameters.put("finishTimeFrom", finishTimeFrom);
        }

        if (finishTimeTo != null) {
            where += " AND i.planFinishTime <= :finishTimeTo";
            parameters.put("finishTimeTo", finishTimeTo);
        }

        if (!LongUtils.isEmpty(creatorId)) {
            where += " AND i.createdBy = :creatorId";
            parameters.put("creatorId", creatorId);
        }

        if (source != null && source.size() > 0) {
            where += " AND i.punchSource IN :source";
            parameters.put("source", source);
        }

        if (status != null && status.size() > 0) {
            where += " AND i.status IN :status";
            parameters.put("status", status);
        }

        if (!StringUtils.isEmpty(keyword)) {
            where += " AND (i.title LIKE :keyword OR i.description LIKE :keyword)";
            parameters.put("keyword", "%" + keyword + "%");
        }

        if (!LongUtils.isEmpty(participantId)) {
            where += " AND (i.personInChargeId = :participantId OR i.members LIKE :memberId)";
            parameters.put("participantId", participantId);
            parameters.put("memberId", "%" + participantId + "%");
        }

        if (!LongUtils.isEmpty(memberId)) {
            where += " AND (i.personInChargeId = :memberId OR i.qcId = :memberId)";
            parameters.put("memberId", memberId);
        }

        if (!LongUtils.isEmpty(departmentId)) {
            where += " AND i.departmentId = :departmentId";
            parameters.put("departmentId", departmentId);
        }

        if (!CollectionUtils.isEmpty(disciplines)) {
            where += " AND i.discipline IN :disciplines";
            parameters.put("disciplines", disciplines);
        }

        if (!CollectionUtils.isEmpty(modules)) {
            where += " AND i.module IN :modules";
            parameters.put("modules", modules);
        }

        if (!CollectionUtils.isEmpty(subSystems)) {
            where += " AND i.subSystem IN :subSystems";
            parameters.put("subSystems", subSystems);
        }

        if (properties != null && properties.size() > 0) {

            from.append(" INNER JOIN com.ose.issues.entity.IssueProperty p ON p.issueId = i.id");

            List<String> values;
            int propertyIndex = 0;

            for (PropertyDTO property : properties) {

                values = property.getValues();

                if (values == null || values.size() == 0) {
                    continue;
                }

                propertyIndex++;

                from
                    .append(" AND p.propertyId = :propertyId").append(propertyIndex)
                    .append(" AND p.values = :propertyValue").append(propertyIndex);

                parameters.put("propertyId" + propertyIndex, property.getPropertyDefinitionId());
                parameters.put("propertyValue" + propertyIndex, String.join("\r\n", values));
            }

        }

        String countSelect = "SELECT COUNT(DISTINCT i.id)";

        TypedQuery<Long> countQuery = getEntityManager()
            .createQuery(countSelect + from.toString() + where, Long.class);

        String searchSelect = "SELECT DISTINCT i";
        String orderBy = " ORDER BY i.createdAt DESC";

        TypedQuery<T> searchQuery = getEntityManager()
            .createQuery(searchSelect + from.toString() + where + orderBy, clazz);

        if (pageable != null) {
            searchQuery
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());
        } else {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }

        parameters.keySet().forEach(key -> {
            countQuery.setParameter(key, parameters.get(key));
            searchQuery.setParameter(key, parameters.get(key));
        });

        Long count = (Long) countQuery.getSingleResult();

        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, count);
        }

        return new PageImpl<>(searchQuery.getResultList(), pageable, count);
    }

    /**
     * 查询问题。
     *
     * @param <T>       问题范型
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param clazz     问题类型
     * @return 用户查询结果列表数据
     */
    @Override
    public <T extends IssueBase> List<T> search(
        final Long orgId,
        final Long projectId,
        final IssueCriteriaDTO criteria,
        final Class<T> clazz
    ) {
        return search(orgId, projectId, criteria, null, clazz).getContent();
    }

    @Override
    public List<List<Object>> getXlsList(Long orgId, Long projectId, String sql) {
        EntityManager entityManager = getEntityManager();

        // 查询结果
        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<List<Object>> queryResultList = query.getResultList();
        return queryResultList;
    }

    @Override
    public List<Map<String, Object>> getColumnItems(Long projectId, IssueCriteriaDTO issueCriteria, String columnName, boolean isIdRequired) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        final IssueType issueType = IssueType.ISSUE;
        final Long creatorId = issueCriteria.getCreatorId();
        final Date createdTimeFrom = issueCriteria.getCreatedAtStartTime();
        final Date createdTimeTo = issueCriteria.getCreatedAtEndTime();
        final List<Long> issueIDs;
        final Long personInChargeId;
        final Date finishTimeFrom;
        final Date finishTimeTo;
        final List<IssueSource> source;
        final List<EntityStatus> status;
        final String keyword = StringUtils.trim(issueCriteria.getKeyword());
        final Long participantId;
        final Long memberId;
        final Long departmentId;

        if(isIdRequired) {
            sql.append("SELECT DISTINCT " + columnName + ", " + columnName + "_id AS " + columnName + "Id FROM issues ");
        } else {
            sql.append("SELECT DISTINCT " + columnName + " FROM issues ");
        }

        sql.append("    WHERE ");
        sql.append("        `project_id` = :projectId AND deleted = false ");

        issueIDs = issueCriteria.getIssueIDs();
        personInChargeId = issueCriteria.getLeaderId();
        finishTimeFrom = issueCriteria.getFinishedStartTime();
        finishTimeTo = issueCriteria.getFinishedEndTime();
        source = issueCriteria.getSource();
        status = issueCriteria.getStatus();
        participantId = issueCriteria.getParticipantId();
        memberId = issueCriteria.getMemberId();
        departmentId = issueCriteria.getDepartmentId();



        if (issueType != null) {
            sql.append(" AND type = :issueType");
        }

        if (createdTimeFrom != null) {
            sql.append(" AND created_at >= :createdTimeFrom");
        }

        if (createdTimeTo != null) {
            sql.append(" AND created_at <= :createdTimeTo");
        }

        if (issueIDs != null && issueIDs.size() > 0) {
            sql.append(" AND id IN :issueIDs");
        }

        if (personInChargeId != null) {
            sql.append(" AND person_in_charge_id = :personInChargeId");
        }

        if (finishTimeFrom != null) {
            sql.append(" AND plan_finish_time >= :finishTimeFrom");
        }

        if (finishTimeTo != null) {
            sql.append(" AND plan_finish_time <= :finishTimeTo");
        }

        if (!LongUtils.isEmpty(creatorId)) {
            sql.append(" AND created_by = :creatorId");
        }

        if (source != null && source.size() > 0) {
            sql.append(" AND punch_source IN :punchSource");
        }

        if (status != null && status.size() > 0) {
            sql.append(" AND status IN :status");
        }

        if (!StringUtils.isEmpty(keyword)) {
            sql.append(" AND (no LIKE :keyword OR description LIKE :keyword)");
        }

        if (!LongUtils.isEmpty(participantId)) {
            sql.append(" AND (person_in_charge_id = :participantId OR members LIKE :memberId)");
        }

        if (!LongUtils.isEmpty(memberId)) {
            sql.append(" AND members = :memberId");
        }

        if (!LongUtils.isEmpty(departmentId)) {
            sql.append(" AND departmentId = :departmentId");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId",projectId);

        if (issueType != null) {
            query.setParameter("issueType", issueType.name());
        }

        if (createdTimeFrom != null) {
            query.setParameter("createdTimeFrom", createdTimeFrom);
        }

        if (createdTimeTo != null) {
            query.setParameter("createdTimeTo", createdTimeTo);
        }

        if (issueIDs != null && issueIDs.size() > 0) {
            query.setParameter("issueIDs", issueIDs);
        }

        if (personInChargeId != null) {
            query.setParameter("personInChargeId", personInChargeId);
        }

        if (finishTimeFrom != null) {
            query.setParameter("finishTimeFrom", finishTimeFrom);
        }

        if (finishTimeTo != null) {
            query.setParameter("finishTimeTo", finishTimeTo);
        }

        if (!LongUtils.isEmpty(creatorId)) {
            query.setParameter("creatorId", creatorId);
        }

        if (source != null && source.size() > 0) {
            query.setParameter("punchSource", source);
        }

        if (status != null && status.size() > 0) {
            query.setParameter("status", status);
        }

        if (!StringUtils.isEmpty(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }

        if (!LongUtils.isEmpty(participantId)) {
            query.setParameter("participantId", participantId);
            query.setParameter("memberId", "%" + participantId + "%");
        }

        if (!LongUtils.isEmpty(memberId)) {
            query.setParameter("memberId", memberId);
        }

        if (!LongUtils.isEmpty(departmentId)) {
            query.setParameter("departmentId", departmentId);
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

//        List<String> departments = new ArrayList<>();
//        for (Map<String, Object> peMap : queryResultList) {
//
//            if (peMap.get(columnName) != null) {
//                departments.add((String) peMap.get(columnName));
//            }
//        }

        return queryResultList;
    }

}
