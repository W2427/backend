package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmProcess;

public interface BpmEntityDocsMaterialsRepositoryCustom {

    List<BpmEntityDocsMaterials> searchDocsByProcessIdInAndEntityId(List<BpmProcess> processes, Long entityId);

}
