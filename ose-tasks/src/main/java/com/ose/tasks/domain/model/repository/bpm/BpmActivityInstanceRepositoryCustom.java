package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.TaskProcessDTO;
import com.ose.tasks.dto.bpm.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BpmActivityInstanceRepositoryCustom {

    Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria);

    List<Long> findActInstIdsInActivityInstance(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria);

    Page<BpmActivityInstanceDTO> findCompletedTask(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria,
                                                List<Long> actInstId, PageDTO pageDTO);

    List<EntityNoBpmActivityInstanceDTO> getEntitiyTodo(Long orgId, Long projectId, Long processId, Long entitySubTypeId, String entityCategoryNameEn, String entityCategoryTypeNameEn, String keyWord, PageDTO pageDTO);

    List<TasksCategoryAssigneeDTO> batchFindTaskCategoryAssignee(List<Long> actInstIds);

    Integer getDailySummary(Long orgId, Long projectId, SummaryCriteriaDTO criteriaDTO);

    List<TaskProcessDTO> findProcess(Long projectId);

}
