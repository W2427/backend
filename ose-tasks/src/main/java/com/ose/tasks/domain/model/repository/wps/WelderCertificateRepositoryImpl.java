package com.ose.tasks.domain.model.repository.wps;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wps.WpsCriteriaDTO;
import com.ose.tasks.entity.wps.WelderCertificate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WelderCertificateRepositoryImpl extends BaseRepository implements WelderCertificateRepositoryCustom {

    /**
     * 查询wps列表。
     *
     * @param welderId       焊工ID
     * @param wpsCriteriaDTO 查询条件
     * @return wps列表
     */
    public List<WelderCertificate> search(Long welderId, WpsCriteriaDTO wpsCriteriaDTO) {

        EntityManager entityManager = getEntityManager();


        StringBuilder sql = new StringBuilder();


        List<WelderCertificate> welderCertificateList = new ArrayList<>();


        sql.append("SELECT `w`.* FROM `wison_tasks`.`welder_certificates` AS `w` WHERE `w`.`welder_id` = :welderId");



        if (wpsCriteriaDTO.getBaseMetal() != null && wpsCriteriaDTO.getBaseMetal2() != null) {
            sql.append(" AND ((:baseMetal regexp concat(w.actual_base_metal)) OR (:baseMetal regexp concat(w.actual_base_metal_alias))) ");
            sql.append(" AND ((:baseMetal2 regexp concat(w.actual_base_metal)) OR (:baseMetal2 regexp concat(w.actual_base_metal_alias))) ");
        }


        if (wpsCriteriaDTO.getWeldType() != null) {
            if (wpsCriteriaDTO.getWeldType().equals("BW")) {

                sql.append(" AND (`w`.`joint` = :weldType) ");


                if (wpsCriteriaDTO.getMaxThickness() != null) {
                    if (wpsCriteriaDTO.getContainMaxThickness() != null && wpsCriteriaDTO.getContainMaxThickness().equals("true")) {
                        sql.append(" AND ((`w`.`plan_thickness_min` = :maxThickness AND `w`.`plan_thickness_min_included` = TRUE) OR (`w`.`plan_thickness_min` < :maxThickness)) ");
                    } else {
                        sql.append(" AND ( `w`.`plan_thickness_min` < :maxThickness)");
                    }
                }


                if (wpsCriteriaDTO.getMinThickness() != null) {
                    if (wpsCriteriaDTO.getContainMinThickness() != null && wpsCriteriaDTO.getContainMinThickness().equals("true")) {
                        sql.append(" AND ((`w`.`plan_thickness_max` = :minThickness AND `w`.`plan_thickness_max_included` = TRUE) OR (`w`.`plan_thickness_max` > :minThickness)) ");
                    } else {
                        sql.append(" AND ( `w`.`plan_thickness_max` > :minThickness) ");
                    }
                }


                if (wpsCriteriaDTO.getMaxDiaRange() != null) {
                    if (wpsCriteriaDTO.getContainMaxDiaRange() != null && wpsCriteriaDTO.getContainMaxDiaRange().equals("true")) {
                        sql.append(" AND ((`w`.`plan_dia_min` = :maxDiaRange AND `w`.`plan_dia_min_included` = TRUE) OR (`w`.`plan_dia_min` < :maxDiaRange)) ");
                    } else {
                        sql.append(" AND ( `w`.`plan_dia_min` < :maxDiaRange )");
                    }
                }


                if (wpsCriteriaDTO.getMinDiaRange() != null) {
                    if (wpsCriteriaDTO.getContainMinDiaRange() != null && wpsCriteriaDTO.getContainMinDiaRange().equals("true")) {
                        sql.append(" AND ((`w`.`plan_dia_max` = :minDiaRange AND `w`.`plan_dia_max_included` = TRUE) OR (`w`.`plan_dia_max` > :minDiaRange)) ");
                    } else {
                        sql.append(" AND ( `w`.`plan_dia_max` > :minDiaRange )");
                    }
                }
            } else if (wpsCriteriaDTO.getWeldType().equals("SP")) {
                sql.append(" AND (`w`.`joint` <>'' AND `w`.`joint` IS NOT NULL) ");
            } else {
                sql.append(" AND (`w`.`joint` = :weldType) ");
            }
        }

        sql.append(" AND `w`.`deleted` = FALSE ");
        System.out.println(sql.toString());

        Query query = entityManager.createNativeQuery(sql.toString());


        if (wpsCriteriaDTO.getWeldType() != null) {
            if (wpsCriteriaDTO.getWeldType().equals("BW")) {
                query.setParameter("weldType", "BUTT");
            } else if (wpsCriteriaDTO.getWeldType().equals("SW") || wpsCriteriaDTO.getWeldType().equals("SUP") ||
                wpsCriteriaDTO.getWeldType().equals("LET") || wpsCriteriaDTO.getWeldType().equals("RPD")) {
                query.setParameter("weldType", "FILLET");
            } else if (wpsCriteriaDTO.getWeldType().equals("SP")) {

            }
        }

        if (wpsCriteriaDTO.getBaseMetal() != null) {
            query.setParameter("baseMetal", "%" + wpsCriteriaDTO.getBaseMetal() + "%");
        }

        if (wpsCriteriaDTO.getBaseMetal() != null) {
            query.setParameter("baseMetal2", "%" + wpsCriteriaDTO.getBaseMetal2() + "%");
        }
        if (wpsCriteriaDTO.getWeldType() != null && wpsCriteriaDTO.getWeldType().equals("BW")) {
            if (wpsCriteriaDTO.getMinDiaRange() != null) {
                query.setParameter("minDiaRange", Double.valueOf(wpsCriteriaDTO.getMinDiaRange()));
            }
            if (wpsCriteriaDTO.getMaxDiaRange() != null) {
                query.setParameter("maxDiaRange", Double.valueOf(wpsCriteriaDTO.getMaxDiaRange()));
            }
            if (wpsCriteriaDTO.getMaxThickness() != null) {
                query.setParameter("maxThickness", Double.valueOf(wpsCriteriaDTO.getMaxThickness()));
            }
            if (wpsCriteriaDTO.getMinThickness() != null) {
                query.setParameter("minThickness", Double.valueOf(wpsCriteriaDTO.getMinThickness()));
            }
        }
        query.setParameter("welderId", welderId);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        List<Map<String, Object>> queryResultList = query.getResultList();

        WelderCertificate welderCertificate;
        for (Map<String, Object> item : queryResultList) {
            welderCertificate = new WelderCertificate();
            copyProperties(item, welderCertificate);
            welderCertificateList.add(welderCertificate);
        }

        return welderCertificateList;
    }

    /**
     * 设置查询结果到对象中。
     *
     * @param result            查询
     * @param welderCertificate WPS对象
     */
    private void copyProperties(Map<String, Object> result, WelderCertificate welderCertificate) {

        if (result == null) {
            return;
        }

        if (result.get("id") != null) {
            welderCertificate.setId((Long) result.get("id"));
        }

        if (result.get("actual_position") != null) {
            welderCertificate.setActualPosition((String) result.get("actual_position"));
        }

        if (result.get("actual_process") != null) {
            welderCertificate.setActualProcess((String) result.get("actual_process"));
        }
    }
}


