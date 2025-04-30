package com.ose.tasks.domain.model.repository.wps;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wps.WelderCriteriaDTO;
import com.ose.tasks.entity.wps.Welder;
import com.ose.vo.EntityStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelderRepositoryImpl extends BaseRepository implements WelderRepositoryCustom {

    /**
     * 获取焊工列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @param pageable          分页参数
     * @return 焊工列表
     */
    @Override
    public Page<Welder> search(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO, Pageable pageable) {

        SQLQueryBuilder<Welder> sqlQueryBuilder =
            getSQLQueryBuilder(Welder.class)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .is("subCon", welderCriteriaDTO.getSubConId())
                .is("status", EntityStatus.ACTIVE);

        if (welderCriteriaDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keyword = new HashMap<>();
            Map<String, String> operator = new HashMap<>();
            operator.put("$like", welderCriteriaDTO.getKeyword());
            keyword.put("name", operator);
            keyword.put("no", operator);
            sqlQueryBuilder.or(keyword);
        }

        return sqlQueryBuilder
            .paginate(pageable)
            .exec()
            .page();
    }

    /**
     * 获取焊工全部列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @return 焊工全部列表
     */
    @Override
    public List<Welder> getAll(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO) {

        SQLQueryBuilder<Welder> sqlQueryBuilder =
            getSQLQueryBuilder(Welder.class)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .is("subCon", welderCriteriaDTO.getSubConId())
                .is("status", EntityStatus.ACTIVE);

        if (welderCriteriaDTO.getKeyword() != null) {
            Map<String, Map<String, String>> keyword = new HashMap<>();
            Map<String, String> operator = new HashMap<>();
            operator.put("$like", welderCriteriaDTO.getKeyword());
            keyword.put("name", operator);
            keyword.put("no", operator);
            sqlQueryBuilder.or(keyword);
        }

        return sqlQueryBuilder.limit(Integer.MAX_VALUE).exec().list();
    }

    /**
     * 获取焊工列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param welderCriteriaDTO 查询条件
     * @return 焊工列表
     */
    public Page<Welder> search(Long orgId, Long projectId, WelderCriteriaDTO welderCriteriaDTO) {

        EntityManager entityManager = getEntityManager();


        StringBuilder sql = new StringBuilder();

        sql.append("SELECT `w`.* FROM `welders` AS `w` LEFT JOIN `welder_certificates` AS `ws` ON `w`.`id` = `ws`.`welder_id` AND `ws`.`deleted` = FALSE WHERE 1 = 1 ");


        if (welderCriteriaDTO.getKeyword() != null) {
            sql.append("AND (`w`.`name` LIKE :keyword OR `w`.`no` LIKE :keyword) ");
        }

        if (welderCriteriaDTO.getSubConId() != null) {
            sql.append("AND `w`.`sub_con` = :subCon ");
        }

        if (welderCriteriaDTO.getBaseMetal() != null) {
            sql.append("AND (`ws`.`actual_base_metal` LIKE :baseMetal OR `ws`.`actual_base_metal_alias` LIKE :baseMetal) ");
        }

        if (welderCriteriaDTO.getMaxThickness() != null) {

            if (welderCriteriaDTO.getContainMaxThickness() != null && welderCriteriaDTO.getContainMaxThickness().equals("true")) {
                sql.append("AND `ws`.`actual_thickness` <= :maxThickness ");
            } else {
                sql.append("AND `ws`.`actual_thickness` < :maxThickness ");
            }
        }

        if (welderCriteriaDTO.getMinThickness() != null) {

            if (welderCriteriaDTO.getContainMinThickness() != null && welderCriteriaDTO.getContainMinThickness().equals("true")) {
                sql.append("AND `ws`.`actual_thickness` >= :minThickness ");
            } else {
                sql.append("AND `ws`.`actual_thickness` > :minThickness ");
            }
        }


        if (welderCriteriaDTO.getMaxDia() != null) {

            if (welderCriteriaDTO.getContainMaxDia() != null && welderCriteriaDTO.getContainMaxDia().equals("true")) {
                sql.append("AND `ws`.`actual_dia` <= :maxDia ");
            } else {
                sql.append("AND `ws`.`actual_dia` < :maxDia ");
            }
        }

        if (welderCriteriaDTO.getMinDia() != null) {

            if (welderCriteriaDTO.getContainMinDia() != null && welderCriteriaDTO.getContainMinDia().equals("true")) {
                sql.append("AND `ws`.`actual_dia` >= :minDia ");
            } else {
                sql.append("AND `ws`.`actual_dia` > :minDia ");
            }
        }


        if (welderCriteriaDTO.getType() != null) {
            sql.append("AND `ws`.`welder_type` = :type ");
        }

        sql.append("AND `w`.`deleted` = FALSE AND `w`.`org_id` = :orgId AND `w`.`project_id` = :projectId ");
        sql.append("GROUP BY `w`.`id` ");
        sql.append("LIMIT :start , :offset ");

        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `w`";

        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (welderCriteriaDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + welderCriteriaDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + welderCriteriaDTO.getKeyword() + "%");
        }
        if (welderCriteriaDTO.getType() != null) {
            query.setParameter("type", welderCriteriaDTO.getType());
            countQuery.setParameter("type", welderCriteriaDTO.getType());
        }
        if (welderCriteriaDTO.getMaxThickness() != null) {
            query.setParameter("maxThickness", Double.parseDouble(welderCriteriaDTO.getMaxThickness()));
            countQuery.setParameter("maxThickness", Double.parseDouble(welderCriteriaDTO.getMaxThickness()));
        }
        if (welderCriteriaDTO.getMinThickness() != null) {
            query.setParameter("minThickness", Double.parseDouble(welderCriteriaDTO.getMinThickness()));
            countQuery.setParameter("minThickness", Double.parseDouble(welderCriteriaDTO.getMinThickness()));
        }
        if (welderCriteriaDTO.getMaxDia() != null) {
            query.setParameter("maxDia", Double.parseDouble(welderCriteriaDTO.getMaxDia()));
            countQuery.setParameter("maxDia", Double.parseDouble(welderCriteriaDTO.getMaxDia()));
        }
        if (welderCriteriaDTO.getMinDia() != null) {
            query.setParameter("minDia", Double.parseDouble(welderCriteriaDTO.getMinDia()));
            countQuery.setParameter("minDia", Double.parseDouble(welderCriteriaDTO.getMinDia()));
        }
        if (welderCriteriaDTO.getBaseMetal() != null) {
            query.setParameter("baseMetal", "%" + welderCriteriaDTO.getBaseMetal() + "%");
            countQuery.setParameter("baseMetal", "%" + welderCriteriaDTO.getBaseMetal() + "%");
        }
        if (welderCriteriaDTO.getSubConId() != null) {
            query.setParameter("subCon", welderCriteriaDTO.getSubConId());
            countQuery.setParameter("subCon", welderCriteriaDTO.getSubConId());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        int pageNo = welderCriteriaDTO.getPage().getNo();
        int pageSize = welderCriteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        BigInteger count = (BigInteger) countQuery.getSingleResult();


        List<Welder> welderList = new ArrayList<>();
        Welder welder;
        for (Map<String, Object> welderMap : queryResultList) {
            welder = new Welder();
            copyProperties(welderMap, welder);
            welderList.add(welder);
        }

        return new PageImpl<>(welderList, welderCriteriaDTO.toPageable(), count.longValue());
    }

    /**
     * 设置查询结果到对象中
     *
     * @param result 查询
     * @param welder 焊工对象
     */
    private void copyProperties(Map<String, Object> result, Welder welder) {

        if (result == null) {
            return;
        }


        if (result.get("id") != null) {
            welder.setId(Long.parseLong(result.get("id").toString()));
        }

        if (result.get("name") != null) {
            welder.setName((String) result.get("name"));
        }

        if (result.get("id_card") != null) {
            welder.setIdCard((String) result.get("id_card"));
        }

        if (result.get("photo") != null) {
            welder.setPhoto(Long.parseLong( result.get("photo").toString()));
        }

        if (result.get("no") != null) {
            welder.setNo((String) result.get("no"));
        }

        if (result.get("user_id") != null) {
            welder.setUserId(Long.parseLong( result.get("user_id").toString()));
        }

        if (result.get("sub_con") != null) {
            welder.setSubCon(Long.parseLong(result.get("sub_con").toString()));
        }

        if (result.get("org_id") != null) {
            welder.setOrgId(Long.parseLong(result.get("org_id").toString()));
        }

        if (result.get("project_id") != null) {
            welder.setProjectId(Long.parseLong(result.get("project_id").toString()));
        }
    }
}
