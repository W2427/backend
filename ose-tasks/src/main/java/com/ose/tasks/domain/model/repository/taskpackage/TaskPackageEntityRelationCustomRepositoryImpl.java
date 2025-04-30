package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.taskpackage.TaskPackageEntityRelationSearchDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 任务包数据仓库。
 */
public class TaskPackageEntityRelationCustomRepositoryImpl extends BaseRepository implements TaskPackageEntityRelationCustomRepository {

    /**
     * 查询任务包。
     *
     * @param orgId                              组织 ID
     * @param projectId                          项目 ID
     * @param taskPackageId                      查询条件
     * @param taskPackageEntityRelationSearchDTO 分页参数
     * @return 任务包分页数据
     */
    @Override
    public Page<TaskPackageEntityRelation> search(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    ) {
        SQLQueryBuilder builder = getSQLQueryBuilder(TaskPackageEntityRelation.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE)
            .is("taskPackageId", taskPackageId);
        if (taskPackageEntityRelationSearchDTO.getUsed() != null) {
            builder.is("isUsedInPipe", taskPackageEntityRelationSearchDTO.getUsed());
        }
        if (taskPackageEntityRelationSearchDTO.getNestGateWay() != null) {
            builder.is("nestGateWay",taskPackageEntityRelationSearchDTO.getNestGateWay());
        }
        if (taskPackageEntityRelationSearchDTO.getEntityNo() != null){
            builder.like("entityNo",taskPackageEntityRelationSearchDTO.getEntityNo());
        }

        return builder
            .paginate(taskPackageEntityRelationSearchDTO.toPageable())
            .exec()
            .page();
    }

    @Override
    public List<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndEntityNoAndStatus(Long orgId, Long projectId, String entityNo, EntityStatus entityStatus) {
        SQLQueryBuilder builder = getSQLQueryBuilder(TaskPackageEntityRelation.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE)
            .is("entityNo", entityNo);


        return builder.exec().list();
    }
}
