package com.ose.tasks.domain.model.repository.wps;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.entity.wps.Wps;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WpsRepositoryImpl extends BaseRepository implements WpsRepositoryCustom {

    /**
     * 查询wps列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsCriteriaDTO 查询条件
     * @return wps列表
     */
    public Page<Wps> search(Long orgId, Long projectId, WpsCriteriaDTO wpsCriteriaDTO) {

        EntityManager entityManager = getEntityManager();


        StringBuilder sql = new StringBuilder();


        List<Wps> wpsList = new ArrayList<>();


        sql.append("SELECT `w`.* FROM `wps` AS `w` LEFT JOIN `wps_details` AS `wd` ON `w`.`id` = `wd`.`wps_id` AND `wd`.`deleted` = FALSE WHERE 1 = 1 ");





        if (wpsCriteriaDTO.getBaseMetal() != null && wpsCriteriaDTO.getBaseMetal2() != null) {


            sql.append("AND (:baseMetal regexp concat(wd.base_metalastr,'|',wd.base_metalaalias_str) and :baseMetal2 regexp concat(wd.base_metalbstr,'|',wd.base_metalbalias_str))" +
                " or (:baseMetal2 regexp concat(wd.base_metalastr,'|',wd.base_metalaalias_str) and :baseMetal regexp concat(wd.base_metalbstr,'|',wd.base_metalbalias_str))");

        }



/*
        if (wpsCriteriaDTO.getBaseMetal2() != null) {

            sql.append("AND (:baseMetal2 LIKE concat('%',`w`.`base_metal`,'%') OR :baseMetal2 LIKE concat('%',`wma`.`alias`,'%')) ");
        }*/


        if (wpsCriteriaDTO.getKeyword() != null) {
            sql.append("AND (`w`.`code` LIKE :keyword) ");
        }


        if (wpsCriteriaDTO.getWeldType() != null) {
            if (wpsCriteriaDTO.getWeldType().equals("BW")) {

                sql.append("AND (`wd`.`joint` = :weldType) ");


                if (wpsCriteriaDTO.getMaxThickness() != null) {
                    if (wpsCriteriaDTO.getContainMaxThickness() != null && wpsCriteriaDTO.getContainMaxThickness().equals("true")) {
                        sql.append("AND ((`wd`.`min_thickness` = :maxThickness AND `wd`.`contain_min_thickness` = TRUE) OR (`wd`.`min_thickness` < :maxThickness) OR ( `wd`.`thickness` = 'ALL' )) ");
                    } else {
                        sql.append("AND ( `wd`.`min_thickness` < :maxThickness  OR `wd`.`thickness` = 'ALL' )");
                    }
                }


                if (wpsCriteriaDTO.getMinThickness() != null) {
                    if (wpsCriteriaDTO.getContainMinThickness() != null && wpsCriteriaDTO.getContainMinThickness().equals("true")) {
                        sql.append("AND ((`wd`.`max_thickness` = :minThickness AND `wd`.`contain_max_thickness` = TRUE) OR (`wd`.`max_thickness` > :minThickness) OR ( `wd`.`thickness` = 'ALL' )) ");
                    } else {
                        sql.append("AND ( `wd`.`max_thickness` > :minThickness OR `wd`.`thickness` = 'ALL' ) ");
                    }
                }


                if (wpsCriteriaDTO.getMaxDiaRange() != null) {
                    if (wpsCriteriaDTO.getContainMaxDiaRange() != null && wpsCriteriaDTO.getContainMaxDiaRange().equals("true")) {
                        sql.append("AND ((`wd`.`min_dia_range` = :maxDiaRange AND `wd`.`contain_min_dia_range` = TRUE) OR (`wd`.`min_dia_range` < :maxDiaRange) OR ( `wd`.`dia_range` = 'ALL' )) ");
                    } else {
                        sql.append("AND ( `wd`.`min_dia_range` < :maxDiaRange OR `wd`.`dia_range` = 'ALL' )");
                    }
                }


                if (wpsCriteriaDTO.getMinDiaRange() != null) {
                    if (wpsCriteriaDTO.getContainMinDiaRange() != null && wpsCriteriaDTO.getContainMinDiaRange().equals("true")) {
                        sql.append("AND ((`wd`.`max_dia_range` = :minDiaRange AND `wd`.`contain_max_dia_range` = TRUE) OR (`wd`.`max_dia_range` > :minDiaRange) OR ( `wd`.`dia_range` = 'ALL' )) ");
                    } else {
                        sql.append("AND ( `wd`.`max_dia_range` > :minDiaRange OR `wd`.`dia_range` = 'ALL' )");
                    }
                }
            } else if (wpsCriteriaDTO.getWeldType().equals("SP")) {
                sql.append("AND (`wd`.`joint` <>'' AND `wd`.`joint` IS NOT NULL) ");
            } else {
                sql.append("AND (`wd`.`joint` = :weldType) ");
            }
        }

        sql.append("AND `w`.`deleted` = FALSE AND `w`.`org_id` = :orgId AND `w`.`project_id` = :projectId ");
        sql.append("GROUP BY `w`.`id` ");
        if (!wpsCriteriaDTO.getFetchAll())
            sql.append("LIMIT :start , :offset ");


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (wpsCriteriaDTO.getWeldType() != null) {
            if (wpsCriteriaDTO.getWeldType().equals("BW")) {
                query.setParameter("weldType", "BUTT");
                countQuery.setParameter("weldType", "BUTT");
            } else if (wpsCriteriaDTO.getWeldType().equals("SW") || wpsCriteriaDTO.getWeldType().equals("SUP") ||
                wpsCriteriaDTO.getWeldType().equals("LET") || wpsCriteriaDTO.getWeldType().equals("RPD")) {
                query.setParameter("weldType", "FILLET");
                countQuery.setParameter("weldType", "FILLET");
            } else if (wpsCriteriaDTO.getWeldType().equals("SP")) {

            } else {
                query.setParameter("weldType", "FILLET");
                countQuery.setParameter("weldType", "FILLET");
            }
        }

        if (wpsCriteriaDTO.getBaseMetal() != null) {
            query.setParameter("baseMetal", "%" + wpsCriteriaDTO.getBaseMetal() + "%");
            countQuery.setParameter("baseMetal", "%" + wpsCriteriaDTO.getBaseMetal() + "%");
        }

        if (wpsCriteriaDTO.getBaseMetal2() != null) {
            query.setParameter("baseMetal2", "%" + wpsCriteriaDTO.getBaseMetal2() + "%");
            countQuery.setParameter("baseMetal2", "%" + wpsCriteriaDTO.getBaseMetal2() + "%");
        }
        if (wpsCriteriaDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + wpsCriteriaDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + wpsCriteriaDTO.getKeyword() + "%");
        }
        if (wpsCriteriaDTO.getWeldType() != null && wpsCriteriaDTO.getWeldType().equals("BW")) {
            if (wpsCriteriaDTO.getMinDiaRange() != null) {
                query.setParameter("minDiaRange", Double.valueOf(wpsCriteriaDTO.getMinDiaRange()));
                countQuery.setParameter("minDiaRange", Double.valueOf(wpsCriteriaDTO.getMinDiaRange()));
            }
            if (wpsCriteriaDTO.getMaxDiaRange() != null) {
                query.setParameter("maxDiaRange", Double.valueOf(wpsCriteriaDTO.getMaxDiaRange()));
                countQuery.setParameter("maxDiaRange", Double.valueOf(wpsCriteriaDTO.getMaxDiaRange()));
            }
            if (wpsCriteriaDTO.getMaxThickness() != null) {
                query.setParameter("maxThickness", Double.valueOf(wpsCriteriaDTO.getMaxThickness()));
                countQuery.setParameter("maxThickness", Double.valueOf(wpsCriteriaDTO.getMaxThickness()));
            }
            if (wpsCriteriaDTO.getMinThickness() != null) {
                query.setParameter("minThickness", Double.valueOf(wpsCriteriaDTO.getMinThickness()));
                countQuery.setParameter("minThickness", Double.valueOf(wpsCriteriaDTO.getMinThickness()));
            }
        }
        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        if (wpsCriteriaDTO.getPage().getSize() < 800) {

            int pageNo = wpsCriteriaDTO.getPage().getNo();
            int pageSize = wpsCriteriaDTO.getPage().getSize();

            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        BigInteger count = (BigInteger) countQuery.getSingleResult();


        Wps wps;
        for (Map<String, Object> wpsMap : queryResultList) {
            wps = new Wps();
            copyProperties(wpsMap, wps);
            wpsList.add(wps);
        }

        return new PageImpl<>(wpsList, wpsCriteriaDTO.toPageable(), count.longValue());
    }

    /**
     * 设置查询结果到对象中。
     *
     * @param result 查询
     * @param wps    WPS对象
     */
    private void copyProperties(Map<String, Object> result, Wps wps) {

        if (result == null) {
            return;
        }

        if (result.get("id") != null) {
            wps.setId(Long.parseLong(result.get("id").toString()));
        }
        if (result.get("org_id") != null) {
            wps.setOrgId(Long.parseLong(result.get("org_id").toString()));
        }
        if (result.get("project_id") != null) {
            wps.setProjectId(Long.parseLong(result.get("project_id").toString()));
        }
        if (result.get("code") != null) {
            wps.setCode((String) result.get("code"));
        }
        if (result.get("pqr_no") != null) {
            wps.setPqrNo((String) result.get("pqr_no"));
        }









        if (result.get("process") != null) {
            wps.setProcess((String) result.get("process"));
        }
        if (result.get("details") != null) {
            wps.setDetails((String) result.get("details"));
        }
        if (result.get("remark") != null) {
            wps.setRemark((String) result.get("remark"));
        }
        if (result.get("file_id") != null) {
            wps.setFileId(Long.parseLong(result.get("file_id").toString()));
        }
    }
}
