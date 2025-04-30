package com.ose.report.domain.repository.moduleProcess;

import com.ose.report.entity.moduleProcess.ModuleProcessStatics;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 模块完成进度 CRUD 操作接口。
 */
@Transactional
public interface ModuleProcessStaticsRepository extends PagingAndSortingWithCrudRepository<ModuleProcessStatics, Long>, ModuleProcessStaticsRepositoryCustom {

    ModuleProcessStatics findByOrgIdAndProjectIdAndFuncPartAndArchiveDateAndShipmentNoAndModuleNo(
        Long orgId,
        Long projectId,
        String funcPart,
        Date archiveDate,
        String shipmentNo,
        String moduleNo
    );

    @Query(value = "SELECT DISTINCT shipmentNo FROM ModuleProcessStatics WHERE orgId =:orgId AND projectId=:projectId AND archiveDate =:archiveDate AND funcPart=:funcPart ORDER BY shipmentNo ")
    List<String> findModuleRelationsNoByOrgIdAndProjectId(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("archiveDate") Date archiveDate,
        @Param("funcPart") String funcPart
    );

    @Query(value = "SELECT moduleNo FROM ModuleProcessStatics WHERE orgId =:orgId AND projectId=:projectId AND shipmentNo =:shipmentNo AND archiveDate =:archiveDate AND funcPart=:funcPart  ORDER BY moduleNo ASC ")
    List<String> findModuleNoByOrgIdAndProjectId(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("shipmentNo") String shipmentNo,
        @Param("archiveDate") Date archiveDate,
        @Param("funcPart") String funcPart
    );

    @Query(value = "SELECT m FROM ModuleProcessStatics m WHERE m.orgId =:orgId AND m.projectId =:projectId AND m.shipmentNo =:shipmentNo AND m.moduleNo IN :moduleNos AND m.archiveDate =:archiveDate  AND m.funcPart=:funcPart  ORDER BY m.moduleNo ASC ")
    List<ModuleProcessStatics> findByOrgIdAndProjectIdAndShipmentNoAndModules(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("shipmentNo") String shipmentNo,
        @Param("moduleNos") List<String> moduleNos,
        @Param("archiveDate") Date archiveDate,
        @Param("funcPart") String funcPart
    );

    @Query(value = "SELECT m FROM ModuleProcessStatics m WHERE m.orgId =:orgId AND m.projectId =:projectId AND m.shipmentNo =:shipmentNo AND m.archiveDate =:archiveDate AND m.funcPart=:funcPart ORDER BY m.moduleNo ASC ")
    List<ModuleProcessStatics> findByOrgIdAndProjectIdAndShipmentNo(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("shipmentNo") String shipmentNo,
        @Param("archiveDate") Date archiveDate,
        @Param("funcPart") String funcPart
    );

}
