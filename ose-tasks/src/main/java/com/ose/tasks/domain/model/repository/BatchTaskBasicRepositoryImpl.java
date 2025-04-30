package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.entity.BatchTaskBasic;
import com.ose.tasks.vo.setting.BatchTaskCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ose.tasks.vo.setting.BatchTaskCode.DETAIL_DESIGN_IMPORT;
import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_LIST_IMPORT;

/**
 * 批处理任务状态记录查询操作。
 */
public class BatchTaskBasicRepositoryImpl extends BaseRepository implements BatchTaskBasicRepositoryCustom {

    /**
     * 查询批处理任务。
     *
     * @param companyId   公司 ID
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页设置
     * @return 批处理任务查询结果分页数据
     */
    @Override
    public Page<BatchTaskBasic> search(
        final Long companyId,
        final Long orgId,
        final Long projectId,
        final BatchTaskCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        if (null != criteriaDTO.getEntityId()) {
            return getSQLQueryBuilder(BatchTaskBasic.class)
                .is("companyId", companyId)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .in("code", criteriaDTO.getCode())
                .is("status", criteriaDTO.getStatus())
                .is("entityId", criteriaDTO.getEntityId())
                .is("launchedBy", criteriaDTO.getLaunchedBy())
                .paginate(pageable)
                .exec()
                .page();
        } else {
            return getSQLQueryBuilder(BatchTaskBasic.class)
                .is("companyId", companyId)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .in("code", criteriaDTO.getCode())
                .is("status", criteriaDTO.getStatus())
                .is("launchedBy", criteriaDTO.getLaunchedBy())
                .paginate(pageable)
                .exec()
                .page();
        }

    }

    /**
     * 查询图纸导入任务。
     *
     * @param companyId            公司 ID
     * @param orgId                组织 ID
     * @param projectId            项目 ID
     * @param batchTaskCriteriaDTO
     * @param pageable             分页参数
     * @return 图纸导入任务记录
     */
    @Override
    public Page<BatchTaskBasic> searchDrawing(
        final Long companyId,
        final Long orgId,
        final Long projectId,
        final BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        final Pageable pageable
    ) {
        return getSQLQueryBuilder(BatchTaskBasic.class)
            .is("companyId", companyId)
            .is("orgId", orgId)
            .is("projectId", projectId)
//            .in("code", new BatchTaskCode[]{DETAIL_DESIGN_IMPORT, DRAWING_LIST_IMPORT})
            .in("code", batchTaskCriteriaDTO.getCode())
            .paginate(pageable)
            .exec()
            .page();
    }

}
