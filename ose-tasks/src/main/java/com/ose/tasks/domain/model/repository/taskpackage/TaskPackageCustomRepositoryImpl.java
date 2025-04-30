package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.taskpackage.TaskPackageCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackagePercent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

/**
 * 任务包数据仓库。
 */
public class TaskPackageCustomRepositoryImpl extends BaseRepository implements TaskPackageCustomRepository {

    /**
     * 查询任务包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    @Override
    public Page<TaskPackagePercent> search(
        Long orgId,
        Long projectId,
        TaskPackageCriteriaDTO criteriaDTO,
        Pageable pageable
    ) {

        SQLQueryBuilder<TaskPackagePercent> sqlQueryBuilder;
        if (criteriaDTO.getKeyWordType().equals("entity")) {
            sqlQueryBuilder = getSQLQueryBuilder(TaskPackagePercent.class)
                .is("projectId", projectId)
                .is("categoryId", criteriaDTO.getCategoryId())
                .is("teamId", criteriaDTO.getTeamId())
                .is("workSiteId", criteriaDTO.getWorkSiteId())
                .lt("percentCount", criteriaDTO.getPercentCount())
                .is("deleted", false)
                .is("lastModifiedBy", criteriaDTO.getModifyNameId())
                .in("name", criteriaDTO.getName());

            if (Objects.equals(criteriaDTO.getSortChange(), "asc")) {
                sqlQueryBuilder
                    .asc("createdAt")
                    .paginate(pageable)
                    .exec();
            } else if (Objects.equals(criteriaDTO.getSortChange(), "desc")) {
                sqlQueryBuilder
                    .desc("createdAt")
                    .paginate(pageable)
                    .exec();
            } else {
                sqlQueryBuilder
                    .asc("name")
                    .paginate(pageable)
                    .exec();
            }

            return sqlQueryBuilder.page();
        } else {
            sqlQueryBuilder = getSQLQueryBuilder(TaskPackagePercent.class)
                .is("projectId", projectId)
                .is("categoryId", criteriaDTO.getCategoryId())
                .is("teamId", criteriaDTO.getTeamId())
                .is("workSiteId", criteriaDTO.getWorkSiteId())
                .lt("percentCount", criteriaDTO.getPercentCount())
                .is("deleted", false)
                .is("lastModifiedBy", criteriaDTO.getModifyNameId())
                .like("name",criteriaDTO.getKeyword());
            if (Objects.equals(criteriaDTO.getSortChange(), "asc")) {
                sqlQueryBuilder
                    .asc("createdAt")
                    .paginate(pageable)
                    .exec();
            } else if (Objects.equals(criteriaDTO.getSortChange(), "desc")) {
                sqlQueryBuilder
                    .desc("createdAt")
                    .paginate(pageable)
                    .exec();
            } else {
                sqlQueryBuilder
                    .asc("name")
                    .paginate(pageable)
                    .exec();
            }
            return sqlQueryBuilder.page();
        }
    }


/*    public Page<TaskPackageBasic> search(
        Long orgId,
        Long projectId,
        TaskPackageCriteriaDTO criteriaDTO,
        Pageable pageable
    ) {
        return getSQLQueryBuilder(TaskPackageBasic.class)
            .is("projectId", projectId)
            .like("name", criteriaDTO.getKeyword())
            .is("categoryId", criteriaDTO.getCategoryId())
            .is("teamId", criteriaDTO.getTeamId())
            .is("workSiteId", criteriaDTO.getWorkSiteId())
            .is("deleted", false)
            .paginate(pageable)
            .exec()
            .page();
    }*/

}
