package com.ose.tasks.domain.model.repository.wps;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

/**
 * 批处理任务状态记录查询操作。
 */
public class WeldWelderRelationRepositoryImpl extends BaseRepository implements WeldWelderRelationRepositoryCustom {

    /**
     * 查询批处理任务。
     *
     * @param orgId                       组织 ID
     * @param projectId                   项目 ID
     * @param weldWelderRelationSearchDTO 查询条件
     * @return 批处理任务查询结果分页数据
     */
    @Override
    public Page<WeldWelderRelation> search(
        final Long orgId,
        final Long projectId,
        final WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    ) {
        SQLQueryBuilder<WeldWelderRelation> sqlQueryBuilder =
            getSQLQueryBuilder(WeldWelderRelation.class)
                .is("orgId", orgId)
                .is("projectId", projectId)
                .is("status", EntityStatus.ACTIVE);

        if (weldWelderRelationSearchDTO.getWelderNo() != null) {
            sqlQueryBuilder.is("welderNo", weldWelderRelationSearchDTO.getWelderNo());
        }
        if (weldWelderRelationSearchDTO.getWeldNo() != null) {
            sqlQueryBuilder.is("weldNo", weldWelderRelationSearchDTO.getWeldNo());
        }
        if (weldWelderRelationSearchDTO.getProcess() != null) {
            sqlQueryBuilder.is("process", weldWelderRelationSearchDTO.getProcess());
        }
        if (weldWelderRelationSearchDTO.getDiscipline() != null) {
            sqlQueryBuilder.is("discipline", weldWelderRelationSearchDTO.getDiscipline());
        }
        return sqlQueryBuilder
            .paginate(weldWelderRelationSearchDTO.toPageable())
            .exec()
            .page();
    }

}
