package com.ose.tasks.domain.model.repository.repairdata;

import com.ose.tasks.entity.qc.PipingTestLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

public interface RepairDataRepository extends PagingAndSortingRepository<PipingTestLog, Long>, RepairDataRepositoryCustom {

    @Query(value = "SELECT we.id " +
        "FROM wbs_entry we " +
        "LEFT JOIN bpm_delivery_entity bde ON bde.entity_id = we.entity_id " +
        "LEFT JOIN bpm_activity_instance_base bai ON bai.entity_id = bde.delivery_id " +
        "LEFT JOIN bpm_activity_instance_state bais ON bai.project_id = bais.project_id AND bai.id = bais.bai_id " +
        " WHERE we.project_id =:projectId and bais.finish_state='FINISHED' and we.running_status = 'RUNNING'",
        nativeQuery = true)
    List<BigInteger> findUnUpdatedWbsEntryIds(@Param("projectId") Long projectId);

    @Query(value = "SELECT hn.id FROM " +
        " hierarchy_node hn LEFT JOIN hierarchy_node_relation hnr ON hn.id = hierarchy_id " +
        " WHERE hnr.id IS NULL AND hn.deleted = 0",nativeQuery = true)
    List<BigInteger> getMissedHierarchyNodeIds();

    @Query(value = "SELECT weld, date, a, b, c FROM patch_welder", nativeQuery = true)
    List<Tuple> getPatchWws();

    @Query(value = "SELECT " +
        "brt.task_id, brt.act_inst_id, brt.task_def_key_, brt.tenant_id_ project_id " +
        "FROM bpm_ru_task brt LEFT JOIN ose_bpm.act_ru_task art ON art.id_ = brt.task_id " +
        "WHERE brt.id > :timeStamp AND art.id_ IS NULL ", nativeQuery = true)
    List<Tuple> getPendingTasks(@Param("timeStamp") Long timeStamp);





    @Query(value = "SELECT wp02_hierarchy_id, wp03_node_id FROM entity_new_wp23", nativeQuery = true)
    List<Tuple> getWp23s();


    @Query(value = "SELECT id, node_id, parent_id, path FROM hierarchy_node WHERE node_id IN :nIds", nativeQuery = true)
    List<Tuple> getWp03Hierarchy(@Param("nIds") Collection<Long> values);
}
