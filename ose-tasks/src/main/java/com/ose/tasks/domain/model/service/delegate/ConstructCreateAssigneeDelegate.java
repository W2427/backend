package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.UserFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageAssignNodePrivilegesRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryDelegateRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户服务。
 */
@Component
public class ConstructCreateAssigneeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final UserFeignAPI userFeignAPI;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmProcessRepository processRepository;

    private final WBSEntryRepository wBSEntryRepository;

    private final WBSEntryDelegateRepository wBSEntryDelegateRepository;

    private final TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository;

    private final BpmActTaskAssigneeHistoryRepository bpmActTaskAssigneeHistoryRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final TaskPackageCategoryRepository taskPackageCategoryRepository;

    private final TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final static Logger logger = LoggerFactory.getLogger(ConstructCreateAssigneeDelegate.class);
    /**
     * 构造方法。
     */
    @Autowired
    public ConstructCreateAssigneeDelegate(
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        BpmRuTaskRepository ruTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        BpmProcessRepository processRepository,
        WBSEntryRepository wBSEntryRepository,
        WBSEntryDelegateRepository wBSEntryDelegateRepository,
        TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository,
        BpmActTaskAssigneeHistoryRepository bpmActTaskAssigneeHistoryRepository,
        TaskPackageCategoryRepository taskPackageCategoryRepository,
        TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository,
        ProjectNodeRepository projectNodeRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository,
            bpmActivityInstanceStateRepository);
        this.userFeignAPI = userFeignAPI;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.processRepository = processRepository;
        this.wBSEntryRepository = wBSEntryRepository;
        this.wBSEntryDelegateRepository = wBSEntryDelegateRepository;
        this.assignNodePrivilegesRepository = assignNodePrivilegesRepository;
        this.bpmActTaskAssigneeHistoryRepository = bpmActTaskAssigneeHistoryRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.taskPackageCategoryRepository = taskPackageCategoryRepository;
        this.taskPackageCategoryProcessRelationRepository = taskPackageCategoryProcessRelationRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }


    /**
     * * 开始创建建造工作流程任务后分配人：四级计划->三级计划->任务包->工序。
     *
     * @param createResult
     * @return
     */
    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {
        ContextDTO contextDTO = createResult.getContext();


        return createResult;
    }


}
