package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wbs.WBSBundleCriteriaDTO;
import com.ose.tasks.entity.wbs.WBSBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 管线实体 CRUD 操作接口。
 */
public class WBSBundleCustomRepositoryImpl extends BaseRepository implements WBSBundleCustomRepository {

    /**
     * 查询计划包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 计划包分页数据
     */
    @Override
    public Page<WBSBundle> search(
        final Long orgId,
        final Long projectId,
        final WBSBundleCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        return getSQLQueryBuilder(WBSBundle.class)
            .is("projectId", projectId)
            .is("entityType", criteriaDTO.getEntityType())
            .is("entitySubType", criteriaDTO.getEntitySubType())
            .is("stage", criteriaDTO.getStage())
            .is("process", criteriaDTO.getProcess())
            .is("workSiteId", criteriaDTO.getWorkSiteId())
            .is("teamId", criteriaDTO.getTeamId())
            .is("deleted", false)
            .paginate(pageable)
            .exec()
            .page();
    }


}
