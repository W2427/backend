package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.dto.taskpackage.TaskPackageEntityRelationSearchDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageEntityRelationCustomRepository {

    /**
     * 查询任务包。
     *
     * @param orgId                              组织 ID
     * @param projectId                          项目 ID
     * @param taskPackageId                          项目 ID
     * @param taskPackageEntityRelationSearchDTO 分页参数
     * @return 任务包分页数据
     */
    Page<TaskPackageEntityRelation> search(
        Long orgId,
        Long projectId,
        Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    );


    List<TaskPackageEntityRelation> findByOrgIdAndProjectIdAndEntityNoAndStatus(Long orgId, Long projectId, String entityNo, EntityStatus entityStatus);


}
