package com.ose.tasks.domain.model.service.wbs.structure;

import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntityBase;

/**
 * WBS 实体操作服务接口。 Structure Weld
 */
public interface StructureWeldEntityInterface extends BaseWBSEntityInterface<StructureWeldEntityBase, StructureWeldEntryCriteriaDTO> {






    WpsImportResultDTO updateWeldEntityWps(Long orgId, Long projectId, Long userId, WpsImportDTO uploadDTO);
}
