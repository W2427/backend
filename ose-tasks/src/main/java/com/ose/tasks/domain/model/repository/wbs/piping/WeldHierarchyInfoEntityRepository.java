package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.WeldHierarchyInfoEntity;
import com.ose.tasks.vo.wbs.WBSEntityType;

import java.util.List;

/**
 * 焊口实体 CRUD 操作接口。
 */
public interface WeldHierarchyInfoEntityRepository
    extends WBSEntityBaseRepository<WeldHierarchyInfoEntity>,
    WBSEntityCustomRepository<WeldHierarchyInfoEntity, WeldEntryCriteriaDTO> {

    /**
     * 根据父级实体类型和父级实体ID获取焊口数量。
     *
     * @param isoParentEntityType 父级实体类型
     * @param isoParentEntityId   父级实体ID
     * @return 焊口数量
     */
//    Integer countByParentEntityTypeAndParentEntityIdAndDeletedIsFalse(WBSEntityType isoParentEntityType,
//                                                                      Long isoParentEntityId);

//    List<WeldHierarchyInfoEntity> findByParentEntityTypeAndParentEntityIdAndDeletedIsFalse(
//        WBSEntityType entityType, Long entityId);
}
