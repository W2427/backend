package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class SearchTaskService implements SearchTaskInterface {
    private final static Logger logger = LoggerFactory.getLogger(SearchTaskService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    private final TodoTaskBaseInterface todoTaskBaseService;


    /**
     * 构造方法。
     */
    @Autowired
    public SearchTaskService(
        TodoTaskBaseInterface todoTaskBaseService) {
        this.todoTaskBaseService = todoTaskBaseService;
    }


    /**
     * 获取待办任务列表
     */
    @Override
    public Page<TodoTaskDTO> searchTodo(Long orgId, Long projectId, Long assignee, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {

        Page<TodoTaskDTO> ruTasks = todoTaskBaseService.getRuTaskList(
            orgId,
            projectId,
            assignee,
            taskCriteria,
            pageDTO);

        return ruTasks;
    }

    /**
     * 匹配结果结合的分页功能
     *
     * @param resultList 结果集合
     * @param pageNo     分页信息
     * @param pageSize   分页信息
     * @return
     */
    private List<TodoTaskDTO> resultListPageableMatch(List<TodoTaskDTO> resultList, int pageNo, int pageSize) {

        System.out.println("开始分页处理: " + new Date());
        int fromIndex = (pageNo - 1) * pageSize;


        if (fromIndex <= resultList.size()) {

            int toIndex = fromIndex + pageSize;

            if (toIndex >= resultList.size()) {
                resultList = resultList.subList(fromIndex, resultList.size());
            } else {
                resultList = resultList.subList(fromIndex, toIndex);
            }
        } else {
            resultList.clear();
        }
        System.out.println("分页处理结束: " + new Date());
        return resultList;
    }


}
