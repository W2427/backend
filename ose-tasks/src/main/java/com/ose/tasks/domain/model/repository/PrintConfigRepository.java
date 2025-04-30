package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.PrintConfig;
import com.ose.vo.EntityStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface PrintConfigRepository extends PagingAndSortingWithCrudRepository<PrintConfig, Long> {

    @Query("SELECT count(t) FROM PrintConfig t where t.orgId = :orgId and t.projectId = :projectId and t.name = :name and t.type = :type")
    Long getCountByOrgIdAndProjectIdAndNameAndType(
        @Param("orgId") Long orgId, @Param("projectId") Long projectId,
        @Param("name") String name, @Param("type") String type);

    Page<PrintConfig> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus active,
                                                       Pageable pageable);

    @Query("SELECT count(t) FROM PrintConfig t where t.orgId = :orgId and t.projectId = :projectId")
    Long getCountByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    List<PrintConfig> findByOrgIdAndProjectIdAndTypeAndStatus(Long orgId, Long projectId, String type,
                                                              EntityStatus active);

    PrintConfig findByOrgIdAndProjectIdAndServiceAndStatus(Long orgId, Long projectId, String service,
                                                           EntityStatus status);

    PrintConfig findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name,
                                                        EntityStatus status);

}
