package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.vo.bpm.ActInstDocType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface BpmEntityDocsMaterialsRepository extends PagingAndSortingWithCrudRepository<BpmEntityDocsMaterials, Long>, BpmEntityDocsMaterialsRepositoryCustom {

    List<BpmEntityDocsMaterials> findByProcessIdAndEntityIdAndActInstanceId(Long processId, Long entityId, Long actInstanceId);

    BpmEntityDocsMaterials findByProcessIdAndEntityIdAndActInstanceIdAndTypeIn(Long processId, Long entityId, Long actInstanceId, Collection<ActInstDocType> docTypes);

    /**
     * 根据实体ID，文档类型获取实体文档列表。
     *
     * @param entityIds 实体ID
     * @param type      文档类型
     * @return 实体文档列表
     */
    List<BpmEntityDocsMaterials> findByEntityIdInAndType(
        List<Long> entityIds,
        ActInstDocType type);

    BpmEntityDocsMaterials findByEntityNoAndType(String dwgNo, ActInstDocType designDrawing);

    /**
     * 通过流程id查找当前文档信息
     * @param dwgNo
     * @param actInstanceId
     * @return
     */
    BpmEntityDocsMaterials findByEntityNoAndActInstanceId(String dwgNo, Long actInstanceId);


    BpmEntityDocsMaterials findFirstByProcessIdInAndEntityIdAndTypeOrderByCreatedAtDesc(List<Long> processIds, Long entityId, ActInstDocType type);
}

