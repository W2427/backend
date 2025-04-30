package com.ose.tasks.domain.model.repository.trident;

import com.ose.tasks.entity.trident.SubSystemTcpRelation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubSystemTcpRelationRepository extends PagingAndSortingRepository<SubSystemTcpRelation, Long> {

    SubSystemTcpRelation findByProjectIdAndSubSystemId(Long projectId, Long id);

    @Query("SELECT distinct m.subSystemNo FROM SubSystemTcpRelation m WHERE m.projectId = :projectId order by m.subSystemNo")
    List<SubSystemTcpRelation> findByOrgIdAndProjectIdAndSubSystemNo(@Param("projectId") Long projectId);
}
