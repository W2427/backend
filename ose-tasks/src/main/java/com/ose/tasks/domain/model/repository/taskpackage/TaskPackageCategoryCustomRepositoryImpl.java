package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.taskpackage.TaskPackageCategoryCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import com.ose.util.SQLUtils;
import com.ose.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 任务包数据仓库。
 */
public class TaskPackageCategoryCustomRepositoryImpl extends BaseRepository implements TaskPackageCategoryCustomRepository {

    /**
     * 查询任务包类型。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包类型分页数据
     */
    @Override
    public Page<TaskPackageCategory> search(
        Long orgId,
        Long projectId,
        TaskPackageCategoryCriteriaDTO criteriaDTO,
        Pageable pageable
    ) {
        String keyword = null;

        if (!StringUtils.isEmpty(criteriaDTO.getKeyword())) {
            keyword = String.format(
                "%s%%",
                SQLUtils.escapeLike(criteriaDTO.getKeyword())
            );
        }

        SQLQueryBuilder builder = getSQLQueryBuilder(TaskPackageCategory.class)
            .is("projectId", projectId)
            .is("entityType", criteriaDTO.getEntityType())
            .is("discipline", criteriaDTO.getDiscipline())
            .is("deleted", false);

        if (keyword != null) {
            Map<String, Map<String, String>> keywordCriteria = new IdentityHashMap<>();
            Map<String, String> operator = new IdentityHashMap<>();
            operator.put("$like", keyword);
            keywordCriteria.put("name", operator);
            keywordCriteria.put("description", operator);
            keywordCriteria.put("memo", operator);
            builder.or(keywordCriteria);
        }

        return builder
            .paginate(pageable)
            .exec()
            .page();
    }


}
