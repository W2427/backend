package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.WBSEntryGroupPlainDTO;
import com.ose.tasks.dto.wbs.WBSEntryPlainQueryDTO;
import org.springframework.data.domain.Page;

/**
 * WBS 扁平 条目 CRUD 操作接口。
 */
public class WBSEntryGroupPlainCustomRepositoryImpl extends BaseRepository implements WBSEntryGroupPlainCustomRepository {

    /**
     * 查询四级计划。
     *
     * @param projectId             项目 ID
     * @param orgId                 查询条件
     * @param wbsEntryPlainQueryDTO 分页参数
     * @return 扁平计划分页数据
     */
    @Override
    public Page<WBSEntryGroupPlainDTO> search(
        final Long projectId,
        final Long orgId,
        final WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    ) {
        return null;
    }


}
