package com.ose.tasks.domain.model.repository.qc;

import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 报告设置记录数据仓库。
 */
public interface ReportConfigRepository extends CrudRepository<ReportConfig, Long>  {
    /**
     * 获取 信息。
     *
     * @return ReportConfig
     */
    ReportConfig findByOrgIdAndProjectIdAndReportNameAndStatus(Long orgId, Long projectId, String reportName, EntityStatus status);

    ReportConfig findByOrgIdAndProjectIdAndReportNameAndReportSubTypeAndStatus(Long orgId, Long projectId, String reportName, ReportSubType reportSubType, EntityStatus status);

    List<ReportConfig> findByOrgIdAndProjectIdAndProcessIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus active);

    @Query(
        value = "SELECT m FROM ReportConfig m WHERE m.status=:status and m.orgId=:orgId and m.projectId=:projectId and m.processId =:processId",
        countQuery = "SELECT count(m) FROM ReportConfig m WHERE m.status=:status and m.orgId=:orgId and m.projectId=:projectId and m.processId =:processId"
    )
    Page<ReportConfig> findByStatusAndProjectIdAndOrgIdANDProcessId(
        @Param("status") EntityStatus active, @Param("projectId") Long projectId, @Param("orgId") Long orgId, @Param("processId") Long processId, Pageable pageable);

    /**
     * 判断节点编号是否存在于项目ID。
     *
     * @param reportCode        节点编号
     * @param projectId 项目 ID
     * @param orgId 组织 ID
     * @param processId 工序 ID
     * @param status 状态
     * @return 存在：true；不存在：false；
     */
    boolean existsByReportCodeAndProjectIdAndOrgIdAndProcessIdAndStatus(
        String reportCode,
        Long projectId,
        Long orgId,
        Long processId,
        EntityStatus status);
}
