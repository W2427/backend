package com.ose.tasks.domain.model.service.bpm;

import com.ose.service.EntityInterface;
import com.ose.tasks.dto.bpm.ReportConfigCriteriaDTO;
import com.ose.tasks.dto.bpm.ReportConfigDTO;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public interface ReportConfigInterface extends EntityInterface {

    /**
     * 删除报告配置
     *
     * @param id        工序id
     * @param projectId 项目id
     * @param orgId     组织id
     * @return 操作是否成功
     */
    boolean delete(Long id, Long projectId, Long orgId);

    /**
     * 查询报告配置
     *
     * @param page      分页信息
     * @param projectId 项目id
     * @param orgId     组织id
     * @param processId  工序 id
     */
    Page<ReportConfig> getList(ReportConfigCriteriaDTO page, Long projectId, Long orgId, Long processId);

    /**
     * 查询报告配置详细信息
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    ReportConfig get(Long id, Long projectId, Long orgId);

    /**
     * 创建报告配置
     *
     * @param reportConfigDTO 工序信息
     * @param projectId  项目id
     * @param orgId      组织id
     */
    ReportConfig create(ReportConfigDTO reportConfigDTO, Long projectId, Long orgId);

    /**
     * 编辑报告配置
     *
     * @param id             工序id
     * @param projectId      项目id
     * @param orgId          组织id
     * @return 编辑后的报告配置
     */
    ReportConfig modify(Long id, ReportConfigDTO reportConfigDTO, Long projectId, Long orgId);

    /**
     * 判断报告配置是否已经存在。
     *
     *  @param reportCode        节点编号
     *  @param projectId 项目 ID
     *  @param orgId 组织 ID
     *  @param processId 工序 ID
     *  @param status 状态
     *  @return 报告配置 是否存在  存在 ：true；不存在：false；
     */
    boolean existsByReportCode(String reportCode, Long projectId, Long orgId, Long processId, EntityStatus status);
}
