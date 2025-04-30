package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.WBSBundle;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * WBS 条目打包数据仓库。
 */
public interface WBSBundleRepository extends PagingAndSortingRepository<WBSBundle, Long>, WBSBundleCustomRepository {

    Optional<WBSBundle> findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

}
