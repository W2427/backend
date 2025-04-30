package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmDelivery;
import com.ose.vo.DisciplineCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BpmDeliveryRepository extends PagingAndSortingRepository<BpmDelivery, Long> {

    @Query("SELECT count(t) FROM BpmDelivery t where t.orgId = :orgId and t.projectId = :projectId and t.discipline = :discipline")
    Long getCountByOrgIdAndProjectIdAndDiscipline(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("discipline") DisciplineCode discipline
    );


}
