package com.ose.report.domain.repository.moduleProcess;

import com.ose.report.dto.moduleProcess.ModuleProcessModuleAssembledStaticItemDTO;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * NDT导出表 操作。
 *
 * @auth DengMing
 * @date 2021/4/12 10:49
 */
public class ModuleProcessStaticsRepositoryCustomImpl extends BaseRepository implements ModuleProcessStaticsRepositoryCustom{

    @Override
   public List<ModuleProcessModuleAssembledStaticItemDTO> findByOrgIdAndProjectIdAndShipmentNoModuleAssembled(
        Long orgId,
        Long projectId,
        String shipmentNo,
        List<String> moduleNos,
        Date archiveDate
    ){
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.shipment_no AS shipmentNo, ");
        sql.append(" m.module_no AS moduleNo, ");
        sql.append(" sum(m.handrail_finish_qty) AS handrailFinishQty, ");
        sql.append(" sum(m.handrail_total_qty) AS handrailTotalQty, ");
        sql.append(" m.handrail_unit AS handrailUnit, ");
        sql.append(" sum(m.grating_finish_qty) AS gratingFinishQty, ");
        sql.append(" sum(m.grating_total_qty) AS gratingTotalQty, ");
        sql.append(" m.grating_unit AS gratingUnit,  ");
        sql.append(" sum(m.floor_plate_finish_qty) AS floorPlateFinishQty, ");
        sql.append(" sum(m.floor_plate_total_qty) AS floorPlateTotalQty, ");
        sql.append(" m.floor_plate_unit AS floorPlateUnit, ");
        sql.append(" sum(m.thread_finish_qty) AS threadFinishQty, ");
        sql.append(" sum(m.thread_total_qty) AS threadTotalQty, ");
        sql.append(" m.thread_unit AS threadUnit, ");

        sql.append(" sum(m.cable_ladder_other_qty_finish) AS cableLadderOtherQtyFinish, ");
        sql.append(" sum(m.cable_ladder_other_qty_total) AS cableLadderOtherQtyTotal, ");
        sql.append(" m.cable_ladder_other_qty_unit AS cableLadderOOtherQtyUnit, ");

        sql.append(" sum(m.cable_ladder_qty_finish) AS cableLadderQtyFinish, ");
        sql.append(" sum(m.cable_ladder_qty_total) AS cableLadderQtyTotal, ");
        sql.append(" m.cable_ladder_qty_unit AS cableLadderQtyUnit, ");

        sql.append(" sum(m.conduit_qty_finish) AS conduitQtyFinish, ");
        sql.append(" sum(m.conduit_qty_total) AS conduitQtyTotal, ");
        sql.append(" m.conduit_qty_unit AS conduitQtyUnit, ");

        sql.append(" sum(m.unistrut_qty_finish) AS unistrutQtyFinish, ");
        sql.append(" sum(m.unistrut_qty_total) AS unistrutQtyTotal, ");
        sql.append(" m.unistrut_qty_unit AS unistrutQtyUnit ");

        sql.append(" FROM module_process_statistics AS  m");
        sql.append(" WHERE m.org_id =:orgId ");
        sql.append(" AND m.project_id =:projectId  ");
        sql.append(" AND m.shipment_no =:shipmentNo ");
        sql.append(" AND m.module_no IN :moduleNos ");
        sql.append(" AND m.archive_date =:archiveDate ");
        sql.append(" AND m.status ='ACTIVE' ");
        sql.append(" GROUP BY m.module_no  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("shipmentNo", shipmentNo);
        query.setParameter("moduleNos", moduleNos);
        query.setParameter("archiveDate", archiveDate);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<ModuleProcessModuleAssembledStaticItemDTO> moduleDTOs = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {

            ModuleProcessModuleAssembledStaticItemDTO moduleDTO = new ModuleProcessModuleAssembledStaticItemDTO();
            BeanUtils.copyProperties(resultMap, moduleDTO);
            moduleDTOs.add(moduleDTO);
        }
        return moduleDTOs;
    }

    @Override
    public List<ModuleProcessModuleAssembledStaticItemDTO> findByOrgIdAndProjectIdAndShipmentNoModuleAssembledAll(
        Long orgId,
        Long projectId,
        String shipmentNo,
        Date archiveDate
    ){
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.shipment_no AS shipmentNo, ");
        sql.append(" sum(m.handrail_finish_qty) AS handrailFinishQty, ");
        sql.append(" sum(m.handrail_total_qty) AS handrailTotalQty, ");
        sql.append(" m.handrail_unit AS handrailUnit, ");
        sql.append(" sum(m.grating_finish_qty) AS gratingFinishQty, ");
        sql.append(" sum(m.grating_total_qty) AS gratingTotalQty, ");
        sql.append(" m.grating_unit AS gratingUnit,  ");
        sql.append(" sum(m.floor_plate_finish_qty) AS floorPlateFinishQty, ");
        sql.append(" sum(m.floor_plate_total_qty) AS floorPlateTotalQty, ");
        sql.append(" m.floor_plate_unit AS floorPlateUnit, ");
        sql.append(" sum(m.thread_finish_qty) AS threadFinishQty, ");
        sql.append(" sum(m.thread_total_qty) AS threadTotalQty, ");
        sql.append(" m.thread_unit AS threadUnit, ");

        sql.append(" sum(m.pipe_length_finish) AS pipeLengthFinish, ");
        sql.append(" sum(m.pipe_length_total) AS pipeLengthTotal, ");
        sql.append(" m.pipe_length_unit AS pipeLengthUnit, ");

        sql.append(" sum(m.component_qty_finish) AS componentQtyFinish, ");
        sql.append(" sum(m.component_qty_total) AS componentQtyTotal, ");
        sql.append(" m.component_qty_unit AS componentQtyUnit, ");


        sql.append(" sum(m.cable_ladder_other_qty_finish) AS cableLadderOtherQtyFinish, ");
        sql.append(" sum(m.cable_ladder_other_qty_total) AS cableLadderOtherQtyTotal, ");
        sql.append(" m.cable_ladder_other_qty_unit AS cableLadderOOtherQtyUnit, ");

        sql.append(" sum(m.cable_ladder_qty_finish) AS cableLadderQtyFinish, ");
        sql.append(" sum(m.cable_ladder_qty_total) AS cableLadderQtyTotal, ");
        sql.append(" m.cable_ladder_qty_unit AS cableLadderQtyUnit, ");

        sql.append(" sum(m.conduit_qty_finish) AS conduitQtyFinish, ");
        sql.append(" sum(m.conduit_qty_total) AS conduitQtyTotal, ");
        sql.append(" m.conduit_qty_unit AS conduitQtyUnit, ");

        sql.append(" sum(m.unistrut_qty_finish) AS unistrutQtyFinish, ");
        sql.append(" sum(m.unistrut_qty_total) AS unistrutQtyTotal, ");
        sql.append(" m.unistrut_qty_unit AS unistrutQtyUnit ");


        sql.append(" FROM module_process_statistics AS  m");
        sql.append(" WHERE m.org_id =:orgId ");
        sql.append(" AND m.project_id =:projectId  ");
        sql.append(" AND m.shipment_no =:shipmentNo ");
        sql.append(" AND m.archive_date =:archiveDate ");
        sql.append(" AND m.status ='ACTIVE' ");
        sql.append(" GROUP BY m.shipment_no  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("shipmentNo", shipmentNo);
        query.setParameter("archiveDate", archiveDate);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<ModuleProcessModuleAssembledStaticItemDTO> moduleDTOs = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {

            ModuleProcessModuleAssembledStaticItemDTO moduleDTO = new ModuleProcessModuleAssembledStaticItemDTO();
            BeanUtils.copyProperties(resultMap, moduleDTO);
            moduleDTOs.add(moduleDTO);
        }
        return moduleDTOs;
    }
}
