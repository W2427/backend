package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.dto.wbs.WBSBundleCriteriaDTO;
import com.ose.tasks.entity.wbs.WBSBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * WBS 条目打包数据仓库。
 */
public interface WBSBundleCustomRepository {

    /**
     * 查询计划包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 计划包分页数据
     */
    Page<WBSBundle> search(
        Long orgId,
        Long projectId,
        WBSBundleCriteriaDTO criteriaDTO,
        Pageable pageable
    );

}
