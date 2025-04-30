package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import org.springframework.data.domain.Page;

public interface SearchTaskInterface {

    /**
     * 获取待办任务列表
     */
    Page<TodoTaskDTO> searchTodo(Long orgId, Long projectId, Long assignee, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO);

}

