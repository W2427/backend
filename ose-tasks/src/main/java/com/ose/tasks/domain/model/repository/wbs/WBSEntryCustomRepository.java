package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WBSEntryQueryDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.Tuple;
import java.util.List;

/**
 * WBS 条目 CRUD 操作接口。
 */
public interface WBSEntryCustomRepository {

    /**
     * 查询四级计划。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 四级计划分页数据
     */
    Page<WBSEntryPlain> search(
        Long projectId,
        WBSEntryCriteriaDTO criteriaDTO,
        Pageable pageable
    );

    /**
     * 取得四级计划所有工序列表。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @return 四级计划信息列表
     */
    List<EntityProcessDTO> processes(
        Long projectId,
        WBSEntryCriteriaDTO criteriaDTO
    );























    /**
     * 查询四级计划 及汇总信息。
     *
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @return 四级计划分页数据 及汇总信息
     * 返回 已完成的计划个数 计划总个数 已完成的物量 总物量
     */
    Tuple searchSum(Long projectId, WBSEntryCriteriaDTO criteriaDTO);


    List<WBSEntryPlain> searchWbsEntry(Long projectId, WBSEntry rootWbsEntry, WBSEntryQueryDTO queryDTO, String parentPath);
}
