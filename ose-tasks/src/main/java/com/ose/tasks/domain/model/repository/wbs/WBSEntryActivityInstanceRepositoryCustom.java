package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryActivityInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface WBSEntryActivityInstanceRepositoryCustom {

    /**
     * 查询计划条目活动实例。
     *
     * @param userId     用户 ID
     * @param projectId  项目 ID
     * @param wbsEntryId 计划条目 ID
     * @param privileges 组织权限
     * @param pageable   分页参数
     * @return 计划条目活动实例分页数据
     */
    Page<WBSEntryActivityInstance> findByPrivileges(
        Long userId,
        Long projectId,
        Long wbsEntryId,
        Map<Long, Set<String>> privileges,
        Pageable pageable
    );

}
