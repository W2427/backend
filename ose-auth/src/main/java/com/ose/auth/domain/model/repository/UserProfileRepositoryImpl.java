package com.ose.auth.domain.model.repository;

import com.ose.auth.dto.UserCriteriaDTO;
import com.ose.auth.dto.UserProjectDTO;
import com.ose.auth.dto.UserProjectSearchDTO;
import com.ose.auth.entity.UserProfile;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户查询。
 */
public class UserProfileRepositoryImpl extends BaseRepository implements UserProfileRepositoryCustom {

    /**
     * 查询用户。
     *
     * @param criteriaDTO 查询条件
     * @return 用户查询结果分页数据
     */
    @Override
    public Page<UserProfile> search(
        UserCriteriaDTO criteriaDTO,
        Pageable pageable
    ) {

        SQLQueryBuilder<UserProfile> sqlQueryBuilder = getSQLQueryBuilder(UserProfile.class)
            .in("type", criteriaDTO.getType())
            .like("name", criteriaDTO.getName())
            .is("username", criteriaDTO.getUsername())
            .is("mobile", criteriaDTO.getMobile())
            .is("email", criteriaDTO.getEmail())
            .in("status", criteriaDTO.getStatus())
            .isNot("type", "system")
            .isNot("type", "super");

        if (criteriaDTO.getCompany() != null) {
            sqlQueryBuilder.like("company", criteriaDTO.getCompany());
        }
        if (criteriaDTO.getDivision() != null) {
            sqlQueryBuilder.like("division", criteriaDTO.getDivision());
        }
        if (criteriaDTO.getDepartment() != null) {
            sqlQueryBuilder.like("department", criteriaDTO.getDepartment());
        }
        if (criteriaDTO.getTeam() != null) {
            sqlQueryBuilder.like("team", criteriaDTO.getTeam());
        }
        if (criteriaDTO.getGender() != null) {
            sqlQueryBuilder.is("gender", criteriaDTO.getGender());
        }
        if (criteriaDTO.getCompanyGM() != null) {
            sqlQueryBuilder.is("companyGM", criteriaDTO.getCompanyGM());
        }
        if (criteriaDTO.getDivisionVP() != null) {
            sqlQueryBuilder.is("divisionVP", criteriaDTO.getDivisionVP());
        }
        if (criteriaDTO.getTeamLeader() != null) {
            sqlQueryBuilder.is("teamLeader", criteriaDTO.getTeamLeader());
        }
        if (criteriaDTO.getReviewRole() != null) {
            sqlQueryBuilder.is("reviewRole", criteriaDTO.getReviewRole());
        }
        if (criteriaDTO.getNoApprovalRequired() != null) {
            sqlQueryBuilder.is("noApprovalRequired", criteriaDTO.getNoApprovalRequired());
        }
        if (criteriaDTO.getAutoFillHours() != null) {
            sqlQueryBuilder.is("autoFillHours", criteriaDTO.getAutoFillHours());
        }
        if (criteriaDTO.getTitle() != null) {
            sqlQueryBuilder.is("title", criteriaDTO.getTitle());
        }

        if (criteriaDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getKeyword());
            keywordCriteria.put("name", operator);
            keywordCriteria.put("username", operator);
            keywordCriteria.put("mobile", operator);
            keywordCriteria.put("icNumber", operator);
            keywordCriteria.put("personalEmail", operator);
            keywordCriteria.put("email", operator);
            sqlQueryBuilder.or(keywordCriteria);
        }

        if (criteriaDTO.getTransfer() != null && criteriaDTO.getTransfer()){
            Map<String, Map<String, Object>> transferCriteria = new IdentityHashMap<>();
            Map<String, Object> operator = new IdentityHashMap<>();
            operator.put("$is",true);
            transferCriteria.put("teamLeader",operator);
            transferCriteria.put("applyRole",operator);
            transferCriteria.put("companyGM",operator);
            transferCriteria.put("divisionVP",operator);
            sqlQueryBuilder.orObj(transferCriteria);
        }

        return sqlQueryBuilder
            .paginate(pageable)
            .exec()
            .page();
    }

    @Override
    public Page<UserProjectDTO> searchProject(Long userId, UserProjectSearchDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT o.name as name, o.id as projectId ");
        sql.append(" FROM `organizations` o ");
        sql.append(" LEFT JOIN user_organization_relations u ON o.id = u.organization_id ");
        sql.append(" WHERE " );
        sql.append(" u.deleted IS FALSE ");
        sql.append(" AND u.user_id = :userId ");
        sql.append(" AND o.deleted IS FALSE ");
        sql.append(" AND o.depth = 1 ");
        sql.append(" AND o.name not in ('非项目工时/投标项目', 'OceanSTAR Elite Group') ");

        if(criteriaDTO.getName() != null){
            sql.append("AND o.name like :name ");
        }
        sql.append("ORDER BY o.NAME ");
        sql.append(" LIMIT :start , :offset ");


        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getName() != null && !"".equals(criteriaDTO.getName())) {
            query.setParameter("name", "%" + criteriaDTO.getName() + "%");
            countQuery.setParameter("name", "%" + criteriaDTO.getName() + "%");
        }
        query.setParameter("userId", userId);
        countQuery.setParameter("userId", userId);

        // 查询结果
        int pageNo = criteriaDTO.getPage().getNo();
        int pageSize = criteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<UserProjectDTO> userProjectDTOS = new ArrayList<UserProjectDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            UserProjectDTO userProjectDTO = new UserProjectDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            BeanUtils.copyProperties(newMap, userProjectDTO);


            userProjectDTOS.add(userProjectDTO);

        }
        return new PageImpl<>(userProjectDTOS, criteriaDTO.toPageable(), count.longValue());

    }
}
