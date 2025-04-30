package com.ose.tasks.domain.model.service;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;
import com.ose.tasks.dto.bpm.TodoTaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

/**
 * 获取用户的顶层项目组织列表。
 */
@Component
public class UserProjectService implements UserProjectInterface {

    private final static Logger logger = LoggerFactory.getLogger(UserProjectService.class);

    private final UserFeignAPI userFeignAPI;
    private final BpmRuTaskRepository ruTaskRepository;

    /**
     * 构造方法。
     *
     * @param userFeignAPI 项目数据仓库实例
     */
    @Autowired
    public UserProjectService(UserFeignAPI userFeignAPI, BpmRuTaskRepository ruTaskRepository) {
        this.userFeignAPI = userFeignAPI;
        this.ruTaskRepository = ruTaskRepository;
    }

    /**
     * 取得项目信息。
     *
     * @param userId 用户 ID
     * @return 项目信息
     */
    @Override
    public List<Organization> getProjectOrgs(
        Long userId
    ) {
        UserProfile userProfile = userFeignAPI.get(userId).getData();
        if (null == userProfile) {
            throw new BusinessError("User is not exited.");
        }
        List<Organization> organizations = userFeignAPI.getTopProjectOrgs(userId).getData();
        if (organizations.isEmpty()) {
            throw new BusinessError("No projects in this user.");
        }

        for (Organization organization : organizations) {
//            TodoTaskCriteriaDTO taskCriteria = new TodoTaskCriteriaDTO();
//            PageDTO pageDTO = new PageDTO();
//            PageDTO.Page page = new PageDTO.Page();
//            page.setNo(1);
//            page.setSize(20);
//            pageDTO.setPage(page);
//
//            taskCriteria.setTaskNode("");
//            taskCriteria.setEntityModuleNames("");
//            taskCriteria.setTaskPackageName("");
//            taskCriteria.setWorkSiteAddress("");
//            taskCriteria.setTeamName("");
//            taskCriteria.setPageable(true);
//            taskCriteria.setClientType("pc");
//            taskCriteria.setStateSearch("ACTIVE");
//
//            Page<TodoTaskDTO> todotasks = ruTaskRepository.getTodoTaskList(
//                organization.getId(),
//                organization.getProjectId(),
//                userId.toString(),
//                null,
//                taskCriteria,
//                pageDTO,
//                null
//            );
            BigInteger count = ruTaskRepository.getCurrentExecutorRunTaskCount(organization.getId(), organization.getProjectId(), userId.toString());
            organization.setTodoTaskCount(count.intValue());
//            if (todotasks.getTotalPages() > 0) {
//                System.out.println(organization.getName() + "->" + (int) todotasks.stream().count());
//            }

        }


        return organizations;
    }

}
