package com.ose.tasks.domain.model.service.wbs.piping;

import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ComponentEntityBase;

import java.util.Map;

/**
 * WBS 实体操作服务接口。
 */
public interface ComponentEntityInterface extends BaseWBSEntityInterface<ComponentEntityBase, ComponentEntryCriteriaDTO> {

    Map<String, String> setEntityTypeByRules(Long orgId,
                                             Long projectId,
                                             String parentNodeNo,
                                             String shortCode);

}
