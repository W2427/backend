package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.dto.PrivilegesDTO;
import com.ose.auth.entity.Organization;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.vo.UserPrivilege;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingEntryDelegateRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageAssignNodePrivilegesRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageEntityRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryDelegateRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.entity.HierarchyNodeRelation;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingEntryDelegate;
import com.ose.tasks.entity.taskpackage.TaskPackageAssignNodePrivileges;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelation;
import com.ose.tasks.entity.taskpackage.TaskPackageEntityRelation;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 用户服务。
 */
@Component
public class DrawingCreateAssigneeDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final UserFeignAPI userFeignAPI;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final DrawingRepository drawingRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;
    private final TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository;
    private final TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository;
    private final TaskPackageCategoryRepository taskPackageCategoryRepository;
    private final ProjectNodeRepository projectNodeRepository;
    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;
    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;

    private final OrganizationFeignAPI organizationFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingCreateAssigneeDelegate(
        UserFeignAPI userFeignAPI,
        BpmRuTaskRepository ruTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        DrawingEntryDelegateRepository drawingEntryDelegateRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        DrawingRepository drawingRepository,
        WBSEntryRepository wbsEntryRepository,
        BpmProcessRepository bpmProcessRepository,
        WBSEntryDelegateRepository wbsEntryDelegateRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository,
        TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository,
        TaskPackageCategoryRepository taskPackageCategoryRepository,
        ProjectNodeRepository projectNodeRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        TaskPackageEntityRelationRepository taskPackageEntityRelationRepository, OrganizationFeignAPI organizationFeignAPI) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.userFeignAPI = userFeignAPI;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.drawingRepository = drawingRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.organizationFeignAPI = organizationFeignAPI;
        this.assignNodePrivilegesRepository = assignNodePrivilegesRepository;
        this.taskPackageCategoryProcessRelationRepository = taskPackageCategoryProcessRelationRepository;
        this.taskPackageCategoryRepository = taskPackageCategoryRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
    }

    /**
     * * 图纸工作流程分配人：图纸分配->工序。
     *
     * @param createResult
     * @return
     */
    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {


        Long orgId = createResult.getOrgId();


        Long projectId = createResult.getProjectId();


        Long processId = createResult.getProcess().getId();


        BpmActivityInstanceBase actInst = createResult.getActInst();
        BpmActivityInstanceState actInstState = createResult.getActInstState();


        Set<Long> userIds = new HashSet<>();


        List<BpmActTaskAssignee> bpmActTaskAssignees = new ArrayList<BpmActTaskAssignee>();


        Map<String, BpmActTaskAssignee> bpmActTaskAssigneeMap = new HashMap<>();


        String currentExecutor = "";

        int orderNo = 1;


        List<DrawingEntryDelegate> drawingEntryDelegates;




        Long actInstId = createResult.getActInst().getId();
        if(actInstId == null) {
            createResult.setCreateResult(false);
            return createResult;
        }
        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInstId);


        List<BpmActivityTaskNodePrivilege> nodePrivileges = bpmActivityTaskNodePrivilegeRepository
            .findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, processId, EntityStatus.ACTIVE);


        drawingEntryDelegates = drawingEntryDelegateRepository.findByDrawingIdAndStatus(actInst.getEntityId(), EntityStatus.ACTIVE);
        TaskPackageCategoryProcessRelation taskPackageCategoryProcessRelation = taskPackageCategoryProcessRelationRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(orgId, projectId, processId, EntityStatus.ACTIVE);
        Long taskPackageCategoryId = taskPackageCategoryProcessRelation.getCategoryId();

        TaskPackageCategory taskPackageCategory = taskPackageCategoryRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, taskPackageCategoryId).orElse(null);
        Long entityId = actInst.getEntityId();
        TaskPackageEntityRelation taskPackageEntityRelation = null;
        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndEntityIdAndStatus(projectId, entityId, EntityStatus.ACTIVE).orElse(null);
        if(projectNode != null && taskPackageCategory != null) {

            String wbsEntityType = taskPackageCategory.getEntityType();

            List<HierarchyNodeRelation> hierarchyNodeRelations = hierarchyNodeRelationRepository.findByOrgIdAndProjectIdAndNodeId(orgId, projectId, projectNode.getId());
            Long parentEntityId = null;
            for (HierarchyNodeRelation hierarchyNodeRelation : hierarchyNodeRelations) {
                ProjectNode parentProjectNode = projectNodeRepository.findByProjectIdAndIdAndStatus(projectId, hierarchyNodeRelation.getNodeAncestorId(), EntityStatus.ACTIVE).orElse(null);
                if (parentProjectNode != null && parentProjectNode.getEntityType().equalsIgnoreCase(wbsEntityType)) {
                    parentEntityId = parentProjectNode.getId();
                    break;
                }
            }
            if (parentEntityId != null) {
                taskPackageEntityRelation = taskPackageEntityRelationRepository.findByProjectIdAndEntityIdAndTaskPackageCatogoryId(projectId, parentEntityId, taskPackageCategoryId);
            }
        }

        for (BpmActivityTaskNodePrivilege bpmActTaskNodePrivilege : nodePrivileges) {
            BpmActTaskAssignee assignee = new BpmActTaskAssignee();
            assignee.setSeq(0);
            Boolean returnStatus = false;
            assignee.setTaskDefKey(bpmActTaskNodePrivilege.getTaskDefKey());
            assignee.setTaskCategory(bpmActTaskNodePrivilege.getCategory());
            assignee.setTaskName(bpmActTaskNodePrivilege.getTaskName());
            assignee.setStatus(EntityStatus.ACTIVE);
            assignee.setOrderNo(orderNo++);
            assignee.setActInstId(actInst.getId());


            /*-----方式一 通过确定的 图纸号 <-> 权限 确定人员 ----------*/
            if (drawingEntryDelegates != null && drawingEntryDelegates.size() > 0) {
                for (DrawingEntryDelegate drawingEntryDelegate : drawingEntryDelegates) {
                    if (bpmActTaskNodePrivilege.getCategory()!= null &&
                        bpmActTaskNodePrivilege.getCategory().equals(drawingEntryDelegate.getPrivilege().name())
                        && drawingEntryDelegate.getUserId() != null && !drawingEntryDelegate.getUserId().equals(0L)) {

                        assignee.setTeamId(drawingEntryDelegate.getTeamId());
                        assignee.setAssignee(drawingEntryDelegate.getUserId());
                        userIds.add(drawingEntryDelegate.getUserId());
                        bpmActTaskAssignees.add(assignee);
                        returnStatus = true;

                        Optional<Drawing> dwgOp = drawingRepository
                                .findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, actInst.getEntityNo(), EntityStatus.ACTIVE);
                        Drawing dwg = null;
                        if (dwgOp.isPresent()) {
                            dwg = dwgOp.get();
                        }
//                        if (dwg != null && dwg.getDrawer() == null && drawingEntryDelegate != null) {
//                            dwg.setDrawer(userFeignAPI.get(drawingEntryDelegate.getUserId()).getData().getName());
//                            dwg.setDrawerId(drawingEntryDelegate.getUserId());
//                            drawingRepository.save(dwg);
//                        }
                        break;
                    }

                }
            }
            if(!returnStatus && taskPackageEntityRelation != null) {
                /*-----方式二 通过确定的 任务包上 权限对应的角色人员 ----------*/
                List<TaskPackageAssignNodePrivileges> taskPackageAssignNodePrivileges = new ArrayList<>();

                taskPackageAssignNodePrivileges = assignNodePrivilegesRepository
                                    .findByProjectIdAndTaskPackageIdAndProcessId(projectId, taskPackageEntityRelation.getTaskPackageId(), actInst.getProcessId());
                if (!returnStatus && taskPackageAssignNodePrivileges != null && taskPackageAssignNodePrivileges.size() > 0) {
                    for (TaskPackageAssignNodePrivileges taskPackageAssignNodePrivilege : taskPackageAssignNodePrivileges) {
                        if (bpmActTaskNodePrivilege.getCategory() != null &&
                            bpmActTaskNodePrivilege.getCategory().equals(taskPackageAssignNodePrivilege.getPrivilege().name())
                            && taskPackageAssignNodePrivilege.getUserId() != null && !taskPackageAssignNodePrivilege.getUserId().equals(0L)) {
                            assignee.setTeamId(taskPackageAssignNodePrivilege.getTeamId());
                            assignee.setAssignee(taskPackageAssignNodePrivilege.getUserId());
                            if(taskPackageAssignNodePrivilege.getUserId() != null)
                                userIds.add(taskPackageAssignNodePrivilege.getUserId());
                            bpmActTaskAssignees.add(assignee);
                            returnStatus = true;
                            break;
                        }
                    }
                }

            }
//            if (!returnStatus) {
//                PrivilegesDTO privilegesDTO = new PrivilegesDTO();
//                /*--------第三种方式，派送给项目经理-------------*/
//                privilegesDTO.setPrivileges(new HashSet<String>(){{add(UserPrivilege.DESIGN_MANAGER.name());}});
//                List<Organization> orgs =organizationFeignAPI.getOrgMembersByPrivileges(orgId, projectId, orgId, privilegesDTO).getData();
//                Long pmUserId = 0L;
//                for(Organization org :orgs){
//                    if(!CollectionUtils.isEmpty(org.getMembers())) {
//                        pmUserId = org.getMembers().get(0).getId();
//                        assignee.setTeamId(orgId);
//                        assignee.setAssignee(pmUserId);
//                        userIds.add(pmUserId);
//                        break;
//                    }
//                }
//                bpmActTaskAssignees.add(assignee);
//
//            }
            bpmActTaskAssignees.add(assignee);
        }


        if (userIds.size() > 0) {

            BatchGetDTO batchGetDTO = new BatchGetDTO();
            batchGetDTO.setEntityIDs(userIds);
            Map<Long, String> userNameMap = new HashMap<>();


            JsonListResponseBody<UserBasic> users = userFeignAPI.batchGet(batchGetDTO);


            if (users != null && users.getData() != null) {
                for (UserBasic userBasic : users.getData()) {
                    userNameMap.put(userBasic.getId(), userBasic.getName());
                }
            }

            for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
                bpmActTaskAssignee.setAssigneeName(userNameMap.get(bpmActTaskAssignee.getAssignee()));
            }
        }


        for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
            bpmActTaskAssigneeRepository.save(bpmActTaskAssignee);
        }


        for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssignees) {
            bpmActTaskAssigneeMap.put(bpmActTaskAssignee.getTaskDefKey(), bpmActTaskAssignee);
        }


        for (BpmRuTask ruTask : ruTasks) {
            currentExecutor = todoTaskBaseService.setAssigneeForBpmRuTask(bpmActTaskAssigneeMap, ruTask, currentExecutor, actInst.getId());
        }


        if (!currentExecutor.equals("")) {
            actInstState.setCurrentExecutor(currentExecutor);
            bpmActivityInstanceStateRepository.save(actInstState);
        }

        createResult.setActInst(actInst);
        createResult.setActInstState(actInstState);

        return createResult;
    }


}
