package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntryWithRelations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * WBS 条目 CRUD 操作接口。
 */
public interface WBSEntryWithRelationsCustomRepository {

    /**
     * 查询 WBS 条目数据。
     *
     * @param projectId    项目 ID
     * @param rootWbsEntry 上级 WBS 条目
     * @param queryDTO     查询条件
     * @return WBS 条目分页数据
     */
    List<WBSEntryWithRelations> search(
        Long projectId,
        WBSEntryWithRelations rootWbsEntry,
        WBSEntryQueryDTO queryDTO,
        String parentPath
    );

    /**
     * 查询 WBS 条目数据。
     *
     * @param projectId    项目 ID
     * @param rootWbsEntry 上级 WBS 条目
     * @param queryDTO     查询条件
     * @param pageable     分页参数
     * @return WBS 条目分页数据
     */
    Page<WBSEntryWithRelations> search(
        Long projectId,
        WBSEntryWithRelations rootWbsEntry,
        WBSEntryQueryDTO queryDTO,
        String parentPath,
        Pageable pageable
    );

}
