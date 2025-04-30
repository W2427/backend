package com.ose.tasks.domain.model.service.taskpackage;

import com.ose.exception.*;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.util.*;
import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.OrganizationBasicDTO;
import com.ose.auth.entity.Organization;
import com.ose.auth.vo.UserPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessStageRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.process.EntityProcessRepository;
import com.ose.tasks.domain.model.repository.taskpackage.*;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.worksite.WorkSiteRepository;
import com.ose.tasks.domain.model.service.plan.WBSEntryPlainRelationInterface;
import com.ose.tasks.dto.TaskPackageEntityImportDTO;
import com.ose.tasks.dto.TaskPackageImportResultDTO;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.tasks.entity.wbs.entry.WBSEntryDelegate;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import com.ose.tasks.entity.worksite.WorkSite;
import com.ose.tasks.vo.drawing.DrawingType;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 任务包服务。
 */
@Component
public class TaskPackageService implements TaskPackageInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private UploadFeignAPI uploadFeignAPI;

    private final TaskPackageCategoryRepository taskPackageCategoryRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final TaskPackageBasicRepository taskPackageBasicRepository;
    private final TaskPackageRepository taskPackageRepository;
    private final ProjectRepository projectRepository;
    private final SubSystemEntityRepository systemEntityRepository;
    private final WBSEntryPlainRelationInterface wbsEntryPlainRelationService;
    private final TaskPackageAssignSiteTeamsRepository taskPackageAssignSiteTeamsRepository;


    private final BpmProcessStageRepository processStageRepository;


    private final EntityProcessRepository entityProcessRepository;

    private final TaskPackageEntityRelationRepository taskPackageEntityRelationRepository;

    private final TaskPackageDrawingRelationRepository taskPackageDrawingRelationRepository;

    private final WBSEntryRepository wbsEntryRepository;


    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository;

    private final TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository;

    private final UserFeignAPI userFeignAPI;


    private final OrganizationFeignAPI organizationFeignAPI;

    private final WorkSiteRepository workSiteRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    private final WBSEntryDelegateRepository wbsEntryDelegateRepository;

    private final static Logger logger = LoggerFactory.getLogger(TaskPackageService.class);


    /**
     * 构造方法。
     */
    @Autowired
    public TaskPackageService(
        TaskPackageCategoryRepository taskPackageCategoryRepository,
        TaskPackageBasicRepository taskPackageBasicRepository,
        TaskPackageRepository taskPackageRepository,
        ProjectNodeRepository projectNodeRepository,
        UploadFeignAPI uploadFeignAPI,
        SubSystemEntityRepository systemEntityRepository,
        WBSEntryPlainRelationInterface wbsEntryPlainRelationService,
        TaskPackageAssignSiteTeamsRepository taskPackageAssignSiteTeamsRepository,
        BpmProcessStageRepository processStageRepository,
        EntityProcessRepository entityProcessRepository,
        ProjectRepository projectRepository, EntityManager entityManager, TaskPackageEntityRelationRepository taskPackageEntityRelationRepository,
        TaskPackageDrawingRelationRepository taskPackageDrawingRelationRepository,
        WBSEntryRepository wbsEntryRepository,
        DrawingRepository drawingRepository,
        SubDrawingRepository subDrawingRepository,
        TaskPackageAssignNodePrivilegesRepository assignNodePrivilegesRepository,
        TaskPackageAssignSiteTeamsRepository assignSiteTeamsRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") OrganizationFeignAPI organizationFeignAPI,
        WorkSiteRepository workSiteRepository, WBSEntryStateRepository wbsEntryStateRepository, WBSEntryDelegateRepository wbsEntryDelegateRepository) {
        this.taskPackageCategoryRepository = taskPackageCategoryRepository;
        this.taskPackageBasicRepository = taskPackageBasicRepository;
        this.taskPackageRepository = taskPackageRepository;
        this.systemEntityRepository = systemEntityRepository;
        this.wbsEntryPlainRelationService = wbsEntryPlainRelationService;
        this.taskPackageAssignSiteTeamsRepository = taskPackageAssignSiteTeamsRepository;
        this.processStageRepository = processStageRepository;
        this.entityProcessRepository = entityProcessRepository;
        this.projectRepository = projectRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.taskPackageEntityRelationRepository = taskPackageEntityRelationRepository;
        this.taskPackageDrawingRelationRepository = taskPackageDrawingRelationRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.drawingRepository = drawingRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.subDrawingRepository = subDrawingRepository;
        this.assignNodePrivilegesRepository = assignNodePrivilegesRepository;
        this.assignSiteTeamsRepository = assignSiteTeamsRepository;
        this.userFeignAPI = userFeignAPI;
        this.organizationFeignAPI = organizationFeignAPI;
        this.workSiteRepository = workSiteRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryDelegateRepository = wbsEntryDelegateRepository;
    }

    /**
     * 检查指定的任务包分类是否存在。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包分类 ID
     */
    private TaskPackageCategory checkTaskPackageCategoryExistence(
        final Long orgId,
        final Long projectId,
        final Long categoryId
    ) {
        if (categoryId == null) {
            return null;
        }

        Optional<TaskPackageCategory> taskPackageCatogryOp = taskPackageCategoryRepository
            .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, categoryId);
        if (!taskPackageCatogryOp
            .isPresent()
        ) {
            throw new BusinessError("error.task-package.category-not-found");
        }

        return taskPackageCatogryOp.get();

    }

    /**
     * 创建任务包。
     *
     * @param operator       操作者信息
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param taskPackageDTO 任务包信息
     * @return 任务包信息
     */
    @Override
    public TaskPackageBasic create(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final TaskPackageCreateDTO taskPackageDTO
    ) {

        if (taskPackageDTO.getCategoryId() == null || taskPackageDTO.getCategoryId().equals(0L)) {
            throw new NotFoundError("任务包分类不为空");
        }
        TaskPackageCategory tpc = checkTaskPackageCategoryExistence(orgId, projectId, taskPackageDTO.getCategoryId());
        String discipline = tpc.getDiscipline();
        TaskPackageBasic taskPackage = BeanUtils.copyProperties(
            taskPackageDTO,
            new TaskPackageBasic()
        );

        taskPackage.setName(StringUtils.trim(taskPackage.getName()));

        if (taskPackageBasicRepository
            .findListByProjectIdAndName(projectId, taskPackage.getName()).size() > 0

        ) {
            throw new DuplicatedError("error.task-package.duplicate-name");
        }

        taskPackage.setDiscipline(discipline);
        taskPackage.setOrgId(orgId);
        taskPackage.setProjectId(projectId);
        taskPackage.setCreatedAt();
        taskPackage.setCreatedBy(operator.getId());
        taskPackage.setLastModifiedAt();
        taskPackage.setLastModifiedBy(operator.getId());
        taskPackage.setLastModifiedName(operator.getName());
        taskPackage.setStatus(EntityStatus.ACTIVE);

        return taskPackageBasicRepository.save(taskPackage);
    }

    /**
     * 查询任务包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    @Override
    public Page<TaskPackagePercent> search(
        final Long orgId,
        final Long projectId,
        final TaskPackageCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        if (criteriaDTO.getKeyword() != null) {
            List<TaskPackageEntityRelation> relationList = taskPackageEntityRelationRepository.findByOrgIdAndProjectIdAndEntityNoAndStatus(orgId, projectId, criteriaDTO.getKeyword(), EntityStatus.ACTIVE);
            List<TaskPackageBasic> taskPackageList = taskPackageBasicRepository.findListByProjectIdAndNameAndDeletedIsFalse(projectId, criteriaDTO.getKeyword());
            List<String> nameList = new ArrayList<>();

            if (relationList.size() > 0 && taskPackageList.size() > 0) {
                for (TaskPackageBasic taskPackage : taskPackageList) {
                    nameList.add(taskPackage.getName());
                }
                criteriaDTO.setName(nameList);
                criteriaDTO.setKeyWordType("taskPackage");
                return taskPackageRepository.search(orgId, projectId, criteriaDTO, pageable);
            } else if (relationList.size() > 0) {
                for (TaskPackageEntityRelation relation : relationList) {
                    nameList.add(get(orgId, projectId, relation.getTaskPackageId()).getName());
                }
                criteriaDTO.setName(nameList);
                criteriaDTO.setKeyWordType("entity");
                return taskPackageRepository.search(orgId, projectId, criteriaDTO, pageable);
            } else if (taskPackageList.size() > 0) {
                for (TaskPackageBasic taskPackage : taskPackageList) {
                    nameList.add(taskPackage.getName());
                }
                criteriaDTO.setName(nameList);
                criteriaDTO.setKeyWordType("taskPackage");
                return taskPackageRepository.search(orgId, projectId, criteriaDTO, pageable);
            }
        } else {
            criteriaDTO.setKeyWordType("taskPackage");
            return taskPackageRepository
                .search(orgId, projectId, criteriaDTO, pageable);
        }
        return null;
    }

    /**
     * 查询任务包的关联人员。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 任务包分页数据
     */
    @Override
    public List<TaskPackageAUthCriteriaDTO> searchModifyName(
        final Long orgId,
        final Long projectId
    ) {
        List<TaskPackageAUthCriteriaDTO> authList = taskPackageBasicRepository.findByProjectIdAndDeletedIsFalse(projectId);


        return authList;
    }

    /**
     * 取得任务包详细信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @return 任务包信息
     */
    @Override
    public TaskPackage get(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId
    ) {
        return taskPackageRepository
            .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, taskPackageId)
            .orElse(null);
    }

    /**
     * 取得任务包详细信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param version       任务包更新版本号
     * @return 任务包信息
     */
    private TaskPackage get(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final Long version
    ) {
        final TaskPackage taskPackage = taskPackageRepository
            .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, taskPackageId)
            .orElse(null);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        if (!taskPackage.getVersion().equals(version)) {
            throw new ConflictError();
        }

        return taskPackage;
    }

    /**
     * 更新任务包信息。
     *
     * @param operator       操作者信息
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param taskPackageId  任务包 ID
     * @param version        任务包更新版本号
     * @param taskPackageDTO 任务包信息
     * @return 任务包信息
     */
    @Override
    @Transactional
    public TaskPackageBasic update(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final Long version,
        final TaskPackageUpdateDTO taskPackageDTO
    ) {

        if (taskPackageDTO.getCategoryId() == null || taskPackageDTO.getCategoryId().equals(0L)) {
            throw new NotFoundError("任务包分类不为空");
        }

        final TaskPackageBasic taskPackage = taskPackageBasicRepository
            .findByProjectIdAndIdAndDeletedIsFalse(projectId, taskPackageId)
            .orElse(null);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        if (!taskPackage.getVersion().equals(version)) {
            throw new ConflictError();
        }

        ValueUtils.notNull(taskPackageDTO.getCategoryId(), categoryId -> {
            if (!categoryId.equals(taskPackage.getCategoryId())) {
                checkTaskPackageCategoryExistence(orgId, projectId, categoryId);
                taskPackage.setCategoryId(categoryId);
            }
        });

        ValueUtils.notNull(taskPackageDTO.getName(), taskPackage::setName);
        ValueUtils.notNull(taskPackageDTO.getSortOrder(), taskPackage::setSortOrder);
        ValueUtils.notNull(taskPackageDTO.getMemo(), taskPackage::setMemo);
        taskPackage.setLastModifiedBy(operator.getId());
        taskPackage.setLastModifiedAt();


        taskPackageBasicRepository.save(taskPackage);

        return taskPackage;
    }

    /**
     * 设置工作组和工作场地
     *
     * @param operator      操作者信息
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param processTeams  工序工作组工作场地
     */
    @Override
//    @Transactional
    public void setTeams(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final List<TaskPackageTeamDTO.TeamDTO> processTeams
    ) {
        if (processTeams == null || processTeams.size() == 0) {
            return;
        }

        for (TaskPackageTeamDTO.TeamDTO processTeam : processTeams) {
//            taskPackageRepository.updateProcessTeamByProjectIdAndWokPackageId(
//                projectId,
//                taskPackageId,
//                processTeam.getProcessId(),
//                processTeam.getTeamId(),
//                processTeam.getWorkSiteId()
//            );

            TaskPackageAssignSiteTeams st = assignSiteTeamsRepository.findByProjectIdAndTaskPackageIdAndProcessId(projectId, taskPackageId, processTeam.getProcessId());
            if (st == null) {
                st = new TaskPackageAssignSiteTeams();
                st.setCreatedAt();
                st.setCreatedBy(operator.getId());
                st.setOrgId(orgId);
                st.setProcessId(processTeam.getProcessId());
                st.setProjectId(projectId);
                st.setTaskPackageId(taskPackageId);
                st.setStatus(EntityStatus.ACTIVE);
            }
            st.setLastModifiedAt();
            st.setLastModifiedBy(operator.getId());
            st.setVersion(st.getLastModifiedAt().getTime());
            st.setTeamId(processTeam.getTeamId());
            st.setWorkSiteId(processTeam.getWorkSiteId());

            st.setPlanHours(processTeam.getPlanHours() == null ? null : processTeam.getPlanHours());
            st.setPlanStartDate(processTeam.getPlanStartDate() == null ? null : processTeam.getPlanStartDate());
            st.setPlanEndDate(processTeam.getPlanEndDate() == null ? null : processTeam.getPlanEndDate());

//            if (processTeam.getTeamId() == null || "".equals(processTeam.getTeamId())) {
//                throw new BusinessError("请先选择班组信息!");
//            }
            Organization org = organizationFeignAPI.get(processTeam.getTeamId()).getData();
            st.setTeamName(org == null ? null : org.getName());

            WorkSite workSite = workSiteRepository.findById(processTeam.getWorkSiteId()).orElse(null);

            st.setWorkSiteName(workSite == null ? null : workSite.getName());
            st.setWorkSiteAddress(workSite == null ? null : workSite.getAddress());

            assignSiteTeamsRepository.save(st);
        }
    }

    /**
     * 取得工序工作组设置。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    @Override
    public List<TaskPackageProcessTeamDTO> getTeams(
        final Long projectId,
        final Long taskPackageId
    ) {
        List<TaskPackageProcessTeamDTO> result = new ArrayList<>();

        assignSiteTeamsRepository
            .findProcessTeamByProjectIdAndTaskPackageId(projectId, taskPackageId)
            .forEach(record -> {
                TaskPackageProcessTeamDTO dto = BeanUtils.copyProperties(record, TaskPackageProcessTeamDTO.class);
                if (dto.getTeamId() != null) {
                    dto.setTeamName(organizationFeignAPI.details(dto.getTeamId(), null).getData().getName());
                }
                result.add(dto);
            });

        return result;
    }

    /**
     * 设置任务包的执行人
     *
     * @param operator         操作者信息
     * @param projectId        项目 ID
     * @param taskPackageId    任务包 ID
     * @param processDelegates 工序任务分配设置
     */
    @Override
    public void delegate(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final List<TaskPackageDelegateDTO.DelegateDTO> processDelegates
    ) {
        if (processDelegates == null || processDelegates.size() == 0) {
            return;
        }
        List<TaskPackageAssignNodePrivileges> list = assignNodePrivilegesRepository.findByProjectIdAndTaskPackageIdAndProcessId(
            projectId, taskPackageId, processDelegates.get(0).getProcessId());
        assignNodePrivilegesRepository.deleteAll(list);
        System.out.print("abc");
        for (TaskPackageDelegateDTO.DelegateDTO processDelegate : processDelegates) {
            logger.info("当前任务包执行人开始设置：" + processDelegate.getProcessId());

            // 定义即将写入的权限List
//            entityManager.clear();
            List<WBSEntryDelegate> wbsEntryDelegatesNew = wbsEntryDelegateRepository.defineWBSEntryDelegates(
                operator.getId(),
                projectId,
                taskPackageId,
                processDelegate.getProcessId(),
                processDelegate.getPrivilege().name(),
                processDelegate.getUserId()
            );

            // 找出可能被修改的权限范围(按时间排序）
            List<WBSEntryDelegate> wbsEntryDelegatesOld = wbsEntryDelegateRepository.findExistingDelegates(
                projectId,
                taskPackageId,
                processDelegate.getProcessId(),
                processDelegate.getPrivilege().name()
            );

            //把权限范围转化为哈希Map(通过覆盖的方式把老的重复数据覆盖掉）
            HashMap<Long, WBSEntryDelegate> wbsEntryDelegateHashMap = new HashMap<>();
            for (WBSEntryDelegate wbsEntryDelegate : wbsEntryDelegatesOld) {
                wbsEntryDelegateHashMap.put(wbsEntryDelegate.getWbsEntryId(), wbsEntryDelegate);
            }

            //对照哈希Map更新写入的权限List
            Map<String, Long> teamIdMap = new HashMap<>();
            for (WBSEntryDelegate wbsEntryDelegate : wbsEntryDelegatesNew) {
                Long key = wbsEntryDelegate.getWbsEntryId();
                OrganizationBasicDTO teamOrg = null;
                Long teamId = processDelegate.getTeamId();
                Long orgTeamId = null;
                if (teamId != null) {
                    orgTeamId = teamIdMap.get(orgId.toString() + processDelegate.getTeamId().toString());
                    if (orgTeamId == null) {
                        JsonObjectResponseBody<OrganizationBasicDTO> orgJson = organizationFeignAPI.getTeamByOrgId(orgId, processDelegate.getTeamId());
                        if (orgJson != null) teamOrg = orgJson.getData();
                        if (teamOrg != null) {
                            orgTeamId = teamOrg.getId();
                            teamIdMap.put(orgId.toString() + processDelegate.getTeamId().toString(), orgTeamId);
                        }
                    }
                }

                if (wbsEntryDelegateHashMap.containsKey(key)) {
                    WBSEntryDelegate wbsEntryDelegateOld = wbsEntryDelegateHashMap.get(key);
                    wbsEntryDelegateOld.setUserId(wbsEntryDelegate.getUserId());
                    wbsEntryDelegateOld.setTeamId(orgTeamId);
                    wbsEntryDelegateOld.setLastModifiedAt(new Date());
                    wbsEntryDelegateOld.setLastModifiedBy(operator.getId());
                    wbsEntryDelegateRepository.save(wbsEntryDelegateOld);
                } else {
                    WBSEntryDelegate newDelegate = new WBSEntryDelegate();
                    newDelegate.setCreatedAt(new Date());
                    newDelegate.setLastModifiedAt(new Date());
                    newDelegate.setStatus(EntityStatus.ACTIVE);
                    newDelegate.setCreatedBy(operator.getId());
                    newDelegate.setDeleted(Boolean.FALSE);
                    newDelegate.setLastModifiedBy(operator.getId());
                    newDelegate.setPrivilege(processDelegate.getPrivilege());
                    newDelegate.setTeamId(orgTeamId);
                    newDelegate.setUserId(wbsEntryDelegate.getUserId());
                    newDelegate.setWbsEntryId(wbsEntryDelegate.getWbsEntryId());
                    wbsEntryDelegateRepository.save(newDelegate);
                }
            }

            TaskPackageAssignNodePrivileges p = new TaskPackageAssignNodePrivileges();
            p.setCreatedAt();
            p.setCreatedBy(operator.getId());
            p.setOrgId(orgId);
            p.setPrivilege(processDelegate.getPrivilege());
            p.setProcessId(processDelegate.getProcessId());
            p.setProjectId(projectId);
            p.setStatus(EntityStatus.ACTIVE);
            p.setTaskPackageId(taskPackageId);
            p.setLastModifiedAt();
            p.setLastModifiedBy(operator.getId());
            p.setVersion(p.getLastModifiedAt().getTime());
            p.setTeamId(processDelegate.getTeamId());
            p.setUserId(processDelegate.getUserId());
            assignNodePrivilegesRepository.save(p);
            logger.info("当前任务包执行人设置成功：" + p.getTaskPackageId() + "-----" + p.getProcessId());
        }
    }

    /**
     * 取得工序任务委派设置。
     *
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    @Override
    public List<TaskPackageProcessDelegateDTO> getDelegates(
        final Long projectId,
        final Long taskPackageId
    ) {
        List<TaskPackageProcessDelegateDTO> result = new ArrayList<>();

        assignNodePrivilegesRepository
            .findProcessDelegateByProjectIdAndTaskPackageId(projectId, taskPackageId)
            .forEach(record -> {
                HashMap<String, Object> fields = new HashMap<>();
                record.keySet().forEach(key -> fields.put(key, record.get(key)));
                fields.put("privilege", UserPrivilege.getByName((String) fields.get("privilege")));
                TaskPackageProcessDelegateDTO dto = BeanUtils.copyProperties(fields, TaskPackageProcessDelegateDTO.class);
                if (dto.getUserId() != null) {
                    dto.setUserName(userFeignAPI.get(dto.getUserId()).getData().getName());
                }
                if (dto.getTeamId() != null) {
                    dto.setTeamName(organizationFeignAPI.details(dto.getTeamId(), null).getData().getName());
                }
                result.add(dto);
            });

        return result;
    }

    /**
     * 删除任务包。
     *
     * @param operator      操作者信息
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param version       任务包更新版本号
     */
    @Override
    @Transactional
    public void delete(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final Long version
    ) {
        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId, version);

        if (taskPackageEntityRelationRepository
            .existsByOrgIdAndProjectIdAndTaskPackageIdAndStatus(orgId, projectId, taskPackageId, EntityStatus.ACTIVE)
            || taskPackageDrawingRelationRepository
            .existsByOrgIdAndProjectIdAndTaskPackageId(orgId, projectId, taskPackageId)) {
            throw new BusinessError("error.task-package.entity-exists");
        }

        taskPackage.setStatus(EntityStatus.DELETED);
        taskPackage.setDeletedBy(operator.getId());
        taskPackage.setDeletedAt();


        taskPackageRepository.save(taskPackage);
    }

    /**
     * 导入任务包关联实体。
     *
     * @param orgId
     * @param projectId
     * @param taskPackageEntityImportDTO
     * @param contextDTO
     */
    @Override
    public TaskPackageImportResultDTO importEntities(Long orgId, Long projectId, Long taskPackageId, TaskPackageEntityImportDTO taskPackageEntityImportDTO, ContextDTO contextDTO) {

        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId);

        String operator = contextDTO.getOperator().getName();

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        final TaskPackageCategory category = taskPackage.getCategory();
        final String entityType = category.getEntityType();
        final Map<Long, Long> entityIDsAdded = new HashMap<>();
        List<TaskPackageEntityRelation> relations = new ArrayList<>();
        ProjectNode projectNode = null;

        taskPackageEntityRelationRepository
            .findByProjectIdAndCategoryId(projectId, category.getId())
            .forEach(relation -> entityIDsAdded.put(relation.getEntityId(), relation.getTaskPackageId()));

        File excel;
        Workbook workbook;
        Sheet sheet;

        try {
            excel = new File(temporaryDir, taskPackageEntityImportDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }
        if (workbook == null
            || (sheet = workbook.getSheet("Task_Package_Entities")) == null) {
            throw new BusinessError("模板错误");
        }

        int sheetNum = workbook.getNumberOfSheets();
        if (sheetNum < 1) throw new BusinessError("there is no importSheet");


        Iterator<Row> rows = sheet.rowIterator();
        List<String> errors = new ArrayList<>();
        Row row;
        int totalCount = 0;
        int skippedCount = 0;

        int successCount = 0;
        int errCol = 21;
        DecimalFormat df = new DecimalFormat("######0.000");

        TaskPackageImportResultDTO taskPackageImportResultDTO = new TaskPackageImportResultDTO();
        Set<String> projectNodesNos = projectNodeRepository.findModuleNameSet(orgId, projectId);

        Map<String, Object> map = new HashMap<>();
        int i = 0;
        while (rows.hasNext()) {
            System.out.println(i++);
            boolean updateFlag = false;
            DecimalFormat doubleFormat = new DecimalFormat("######0.000");
            row = rows.next();
            if (row.getRowNum() < 2) {
                continue;
            }
            totalCount++;
            int colIndex = 0;

            String entityNo = "";

            try {
                entityNo = WorkbookUtils.readAsString(row, colIndex++);

                if (entityNo == null) {
                    WorkbookUtils.setCellValue(sheet, row.getRowNum(), errCol, "实体编号不能为空");
                    errors.add("第 " + i + " 行" + "实体编号不能为空");
                    continue;
                }

            } catch (Exception e) {
                WorkbookUtils.setCellValue(sheet, row.getRowNum(), errCol, "实体编号不能为空");
                errors.add("第 " + i + " 行" + "实体编号不能为空");
                continue;
            }
            if (map.get(entityNo) != null) {
                skippedCount++;
                continue;
            }
            map.put(entityNo, entityNo);
            ProjectNode entity;
            try {
                Optional<ProjectNode> entityOp = projectNodeRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, entityNo);
                entity = entityOp.get();
            } catch (Exception e) {
                WorkbookUtils.setCellValue(sheet, row.getRowNum(), errCol, entityNo + "不存在于该项目");
                errors.add(entityNo + "不存在于该项目");
                continue;
            }

            projectNode = projectNodeRepository.findById(
                entity.getEntityId()
            ).orElse(null);

            if (!entityType.equals(projectNode.getEntityType())) {
                WorkbookUtils.setCellValue(sheet, row.getRowNum(), errCol, entityNo + "实体类型不匹配");
                errors.add(entityNo + "实体类型不匹配");
                continue;
            }
            if (entityIDsAdded.containsKey(projectNode.getId())) {
                WorkbookUtils.setCellValue(sheet, row.getRowNum(), errCol, entityNo + "已添加或添加到其他任务包中");
                errors.add(entityNo + "已跳过");
                skippedCount++;
                continue;
            } else {
                successCount++;
                relations.add(new TaskPackageEntityRelation(taskPackage, projectNode));
                entityIDsAdded.put(projectNode.getId(), taskPackageId);
            }
        }

        if (relations.size() > 0) {
            taskPackageEntityRelationRepository.saveAll(relations);
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        logger.error("手动导入任务包关联实体 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody
            = uploadFeignAPI.save(
            orgId.toString(),
            projectId.toString(),
            taskPackageEntityImportDTO.getFileName(),
            new FilePostDTO()
        );
        logger.error("手动导入任务包关联实体 保存docs服务->结束");
        Long fileId = Long.valueOf(responseBody.getData().getId());

        if (errors.size() > 0) {
            taskPackageImportResultDTO.setResult(errors);
        }
        taskPackageImportResultDTO.setErrorCount(errors.size() - skippedCount);
        taskPackageImportResultDTO.setSkipCount(skippedCount);
        taskPackageImportResultDTO.setSuccessCount(successCount);
        return taskPackageImportResultDTO;
    }

    /**
     * 向任务包添加实体。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param entities      实体列表
     * @return 任务包-实体关系列表
     */
    @Override

    public List<TaskPackageEntityRelation> addEntities(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final List<ProjectNode> entities
    ) {
        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        final TaskPackageCategory category = taskPackage.getCategory();
        final String entityType = category.getEntityType();
        final Map<Long, Long> entityIDsAdded = new HashMap<>();

        taskPackageEntityRelationRepository
            .findByProjectIdAndCategoryId(projectId, category.getId())
            .forEach(relation -> entityIDsAdded.put(relation.getEntityId(), relation.getTaskPackageId()));

        List<TaskPackageEntityRelation> relations = new ArrayList<>();


        for (ProjectNode entity : entities) {

            if (entityIDsAdded.containsKey(entity.getId())) {
                if (!taskPackageId.equals(entityIDsAdded.get(entity.getId()))) {
                    throw new BusinessError(
                        "error.task-package.entity-already-added-in-other-package",
                        new String[]{entity.getDisplayName()}
                    );
                }
                continue;
            }

            if (!entityType.equals(entity.getEntityType())) {
                throw new ValidationError("error.task-package.entity-type-not-matched");
            }

            relations.add(new TaskPackageEntityRelation(taskPackage, entity));
            entityIDsAdded.put(entity.getId(), taskPackageId);
        }

        if (relations.size() == 0) {
            return relations;
        }


        taskPackageEntityRelationRepository.saveAll(relations);

        return relations;
    }

    /**
     * 取得任务包关联实体。
     *
     * @param orgId                              组织 ID
     * @param projectId                          项目 ID
     * @param taskPackageId                      任务包 ID
     * @param taskPackageEntityRelationSearchDTO 查询参数
     * @return 实体分页数据
     */
    @Override
    public Page<TaskPackageEntityRelation> entities(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    ) {
        return taskPackageEntityRelationRepository.search(orgId, projectId, taskPackageId, taskPackageEntityRelationSearchDTO);


    }

    /**
     * 从任务包中删除实体。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param entityId      实体 ID
     * @return 删除实体的数量
     */
    @Override
    @Transactional
    public int deleteEntity(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final String entityId
    ) {
        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        Set<String> tmpEntityIDs = new HashSet<>(Arrays.asList(entityId.split("[^0-9A-Z]")));
        tmpEntityIDs.remove("");

        Set<Long> entityIDs = LongUtils.change2Str(tmpEntityIDs);

        if (entityIDs.size() == 0) {
            return 0;
        }

        entityIDs.forEach(eId -> {
            reCalcTaskPackageCount(projectId, eId);
        });
        return taskPackageEntityRelationRepository
            .deleteByProjectIdAndTaskPackageIdAndEntityIdIn(projectId, taskPackageId, entityIDs);
    }

    /**
     * 设置 WBS 任务包 ID。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @Override

    public void setWBSTaskPackageInfo(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId
    ) {
        setWBSTaskPackageInfo(operator, orgId, projectId, null);
    }

    /**
     * 设置 WBS 任务包 ID。
     *
     * @param operator      操作者信息
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     */
    @Override
    public void setWBSTaskPackageInfo(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long taskPackageId
    ) {
        final List<TaskPackage> taskPackages;

        if (taskPackageId == null) {
            taskPackages = taskPackageRepository
                .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        } else {
            TaskPackage taskPackage = taskPackageRepository
                .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, taskPackageId)
                .orElse(null);

            if (taskPackage == null) {
                return;
            }

            taskPackages = new ArrayList<>();
            taskPackages.add(taskPackage);
        }

        taskPackages.forEach(taskPackage -> {
            final Map<Long, List<TaskPackageProcessTeamDTO>> processTeamMap = new HashMap<>();
            final Map<String, List<TaskPackageProcessDelegateDTO>> processDelegateMap = new HashMap<>();


            getTeams(projectId, taskPackage.getId()).forEach(
                processTeamDTO -> processTeamMap
                    .computeIfAbsent(processTeamDTO.getId(), processId -> new ArrayList<>())
                    .add(processTeamDTO)
            );


/*            getDelegates(projectId, taskPackage.getId(), userFeignAPI, organizationFeignAPI).forEach(
                processDelegateDTO -> processDelegateMap
                    .computeIfAbsent(processDelegateDTO.getId(), processId -> new ArrayList<>())
                    .add(processDelegateDTO)
            );*/


            wbsEntryStateRepository.unsetTaskPackageInfo(taskPackage);
            taskPackage.setTotalWorkLoad(0.0);
            taskPackage.setTotalCount(0);
            try {
                Set<BigInteger> wbsEntryIds = new HashSet<>();
                wbsEntryIds = wbsEntryRepository.findWbsEntryByTaskPackageId(taskPackage.getProjectId(), taskPackage.getId());
                wbsEntryIds.forEach(wbsEntryId -> {
                    wbsEntryRepository.updateTaskPackageId(wbsEntryId.longValue(), taskPackage.getId());

                });
                if (!CollectionUtils.isEmpty(wbsEntryIds)) {
                    Tuple tuple = wbsEntryStateRepository.getWbsWorkLoadFromTp(projectId, taskPackage.getId());
                    if (tuple != null) {
                        Integer finishedCount = ((BigDecimal) tuple.get("finishedCount")).intValue();
                        Integer totalCount = ((BigInteger) tuple.get("totalCount")).intValue();
                        Double finishedWorkLoad = (Double) tuple.get("finishedWorkLoad");
                        Double totalWorkLoad = (Double) tuple.get("totalWorkLoad");
                        taskPackage.setTotalCount(totalCount);
                        taskPackage.setFinishedCount(finishedCount);
                        taskPackage.setTotalWorkLoad(totalWorkLoad);
                        taskPackage.setFinishedWorkLoad(finishedWorkLoad);
                        taskPackageRepository.save(taskPackage);
                    }

                }
            } catch (Exception e) {
                throw new BusinessError("TASKPACKAGE DISCIPLINE is WRONG");
            }

            Project project = projectRepository.findById(projectId).orElse(null);
            Long companyId = project.getCompanyId();
            processTeamMap.values().forEach(processTeams -> {
                TaskPackageProcessTeamDTO processTeamDTO = processTeams.get(0);
                updateProcessTeamByProjectIdAndTaskPackageId(
                    projectId,
                    taskPackage.getId(),
                    processTeamDTO.getId(),
                    processTeamDTO.getTeamId(),
                    processTeamDTO.getWorkSiteId(),
                    orgId,
                    companyId
                );
            });

        });
    }

    private void updateProcessTeamByProjectIdAndTaskPackageId(Long projectId,
                                                              Long taskPackageId,
                                                              Long processId,
                                                              Long teamId,
                                                              Long workSiteId,
                                                              Long orgId,
                                                              Long companyId) {
        List<Map<String, Object>> workTeamAndSites = taskPackageRepository.findProcessTeamByProjectIdAndTaskPackageId(projectId,
            taskPackageId);

        WorkSite ws = workSiteRepository.findByIdAndCompanyIdAndProjectIdAndDeletedIsFalse(
            companyId,
            projectId,
            workSiteId
        );
        Long nWsId = ws == null ? null : ws.getId();
        String nWsName = ws == null ? null : ws.getName();

        if (teamId == null) {
            teamId = 0L;
        }
        JsonObjectResponseBody<OrganizationBasicDTO> orgJson = organizationFeignAPI.getTeamByOrgId(orgId, teamId);
        OrganizationBasicDTO org = new OrganizationBasicDTO();
        if (orgJson != null) org = orgJson.getData();
        Long orgTeamId = null;
        String orgTeamName = null;
        if (org != null) {
            orgTeamId = org.getId();
            orgTeamName = org.getName();
        }

        for (Map<String, Object> workTeamAndSite : workTeamAndSites) {

            Long oldTeamId = null;
            Long oldWorkSiteId = null;
            if (workTeamAndSite.get("teamId") != null) {
                BigInteger oldTeamIdb = (BigInteger) workTeamAndSite.get("teamId");
                oldTeamId = oldTeamIdb.longValue();
            }
            if (workTeamAndSite.get("workSiteId") != null) {
                BigInteger oldWorkSiteIdb = (BigInteger) workTeamAndSite.get("workSiteId");
                oldWorkSiteId = oldWorkSiteIdb.longValue();
            }

            if (
                (oldTeamId == null && orgTeamId != null) ||
                    (oldTeamId != null && !oldTeamId.equals(orgTeamId)) ||
                    (oldWorkSiteId == null && nWsId != null) ||
                    (oldWorkSiteId != null && !oldWorkSiteId.equals(nWsId))) {
                taskPackageRepository.updateProcessTeamByProjectIdAndWokPackageId(
                    projectId,
                    taskPackageId,
                    processId,
                    teamId,
                    workSiteId
                );
            }
        }


    }

    /**
     * 向任务包添加图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param createDTO     图纸信息数据传输对象
     * @return 任务包-图纸关系列表
     */
    @Override
    @Transactional
    public List<TaskPackageDrawingRelation> addDrawings(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final TaskPackageDrawingRelationCreateDTO createDTO
    ) {
        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        final Set<Long> drawingsToBeAdded = new HashSet<>();
        final Set<Long> subDrawingsToBeAdded = new HashSet<>();

        createDTO.getDrawings().forEach(drawingDTO -> {
            switch (drawingDTO.getDrawingType()) {
                case DRAWING:
                    drawingsToBeAdded.add(drawingDTO.getDrawingId());
                    break;
                case SUB_DRAWING:
                    subDrawingsToBeAdded.add(drawingDTO.getDrawingId());
                    break;
                default:
                    break;
            }
        });

        List<TaskPackageDrawingRelation> relations = new ArrayList<>();

        if (drawingsToBeAdded.size() > 0) {
            relations.addAll(
                drawingRepository
                    .findAndCreateTaskPackageRelations(projectId, taskPackageId, drawingsToBeAdded)
            );
        }

        if (subDrawingsToBeAdded.size() > 0) {
            relations.addAll(
                subDrawingRepository
                    .findAndCreateTaskPackageRelations(projectId, taskPackageId, subDrawingsToBeAdded)
            );
        }

        if (relations.size() == 0) {
            return relations;
        }

        taskPackageDrawingRelationRepository.saveAll(relations);

        return relations;
    }

    /**
     * 查询任务包图纸信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件数据传输对象
     * @param pageable      分页参数
     * @return 任务包-图纸关系分页数据
     */
    @Override
    public Page<TaskPackageDrawingRelation> drawings(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final TaskPackageDrawingRelationCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        final DrawingType drawingType = criteriaDTO.getDrawingType();
        final Long drawingId = criteriaDTO.getDrawingId();
        final Page<TaskPackageDrawingRelation> page;

        if (drawingType == null && LongUtils.isEmpty(drawingId)) {
            page = taskPackageDrawingRelationRepository
                .findByOrgIdAndProjectIdAndTaskPackageId(
                    orgId,
                    projectId,
                    taskPackageId,
                    pageable
                );
        } else if (!LongUtils.isEmpty(drawingId)) {
            page = taskPackageDrawingRelationRepository
                .findByOrgIdAndProjectIdAndTaskPackageIdAndDrawingId(
                    orgId,
                    projectId,
                    taskPackageId,
                    drawingId,
                    pageable
                );
        } else {
            page = taskPackageDrawingRelationRepository
                .findByOrgIdAndProjectIdAndTaskPackageIdAndDrawingType(
                    orgId,
                    projectId,
                    taskPackageId,
                    drawingType,
                    pageable
                );
        }

        List<TaskPackageDrawingRelation> relations = page.getContent();


        if (relations.size() > 0) {

            final Set<Long> subDrawingIDs = new HashSet<>();
            relations.forEach(relation -> subDrawingIDs.add(relation.getSubDrawingId()));
            subDrawingIDs.remove(null);


            if (subDrawingIDs.size() > 0) {

                final Map<String, Map<String, Object>> subDrawings = new HashMap<>();

                subDrawingRepository
                    .findFileIdAndVersionByIDs(projectId, subDrawingIDs)
                    .forEach(subDrawing -> subDrawings.put((String) subDrawing.get("id"), subDrawing));

                relations.forEach(relation -> {

                    if (relation.getSubDrawingId() == null) {
                        return;
                    }

                    Map<String, Object> subDrawing = subDrawings.get(relation.getSubDrawingId());

                    if (subDrawing == null) {
                        return;
                    }

                    relation.setSubDrawingFileId((Long) subDrawing.get("fileId"));
                    relation.setSubDrawingVersion((String) subDrawing.get("subDrawingVersion"));
                });
            }

        }

        return page;
    }

    /**
     * 查询任务包图纸信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param pageDTO       分页参数
     * @return 任务包-图纸关系分页数据
     */
    @Override
    public Page<TaskPackageDrawingDTO> subDrawings(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final PageDTO pageDTO
    ) {
        final TaskPackage taskPackage = get(orgId, projectId, taskPackageId);

        if (taskPackage == null) {
            throw new NotFoundError();
        }

        List<Object> results;
        List<Object> resultsCount;

        switch (taskPackage.getCategory().getEntityType()) {
            case "ISO":
                resultsCount = taskPackageDrawingRelationRepository
                    .findISODrawings(taskPackage);
                results = taskPackageDrawingRelationRepository
                    .findISODrawings(taskPackage, (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize(), pageDTO.getPage().getSize());
                break;
            case "SPOOL":

                resultsCount = taskPackageDrawingRelationRepository
                    .findSpoolDrawings(taskPackage);
                results = taskPackageDrawingRelationRepository
                    .findSpoolDrawings(taskPackage, (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize(), pageDTO.getPage().getSize());
                break;
            default:
                return new PageImpl<>(Arrays.asList(new TaskPackageDrawingDTO[]{}));
        }
        List<TaskPackageDrawingDTO> taskPackageDrawingDTOS = new ArrayList<>();
        for (Object result : results) {
            TaskPackageDrawingDTO taskPackageDrawingDTO = new TaskPackageDrawingDTO((Object[]) result);
            taskPackageDrawingDTOS.add(taskPackageDrawingDTO);
        }
        return new PageImpl<>(taskPackageDrawingDTOS, pageDTO.toPageable(), resultsCount.size());
    }

    /**
     * 取得所有相关图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @return 图纸 ID 列表
     */
    @Override
    public List<Long> allDrawings(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId
    ) {
        List<Long> dwgs = taskPackageDrawingRelationRepository.findAllDrawings(projectId, taskPackageId);
        if (dwgs == null) dwgs = new ArrayList<>();
        List<Long> spoolDwgs = taskPackageDrawingRelationRepository.findSpoolDrawings(projectId, taskPackageId);
        List<Long> isoDwgs = taskPackageDrawingRelationRepository.findIsoDrawings(projectId, taskPackageId);
        dwgs.addAll(spoolDwgs);
        dwgs.addAll(isoDwgs);
        return dwgs;
    }

    /**
     * 删除任务包中的图纸。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param relationId    关系 ID
     */
    @Override
    @Transactional
    public void deleteDrawing(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final Long relationId
    ) {
        TaskPackageDrawingRelation relation = taskPackageDrawingRelationRepository
            .findByOrgIdAndProjectIdAndTaskPackageIdAndId(orgId, projectId, taskPackageId, relationId)
            .orElse(null);

        if (relation == null) {
            throw new NotFoundError();
        }

        taskPackageDrawingRelationRepository.delete(relation);
    }

    /**
     * 查询任务包关联四级计划。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @param pageable      分页参数
     * @return 四级计划分页数据
     */
    @Override
    public Page<WBSEntryPlain> wbsEntries(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final WBSEntryCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {
        criteriaDTO.setTaskPackageId(taskPackageId);
        return wbsEntryRepository.search(projectId, criteriaDTO, pageable);
    }

    /**
     * 查询任务包关联四级计划 及汇总信息。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @return 四级计划分页数据及汇总信息
     */
    @Override
    public TaskPackageDTO wbsEntries(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final WBSEntryCriteriaDTO criteriaDTO
    ) {
        TaskPackageDTO taskPackageDTO = new TaskPackageDTO();
        criteriaDTO.setTaskPackageId(taskPackageId);
        Page<WBSEntryPlain> wbsEntries = wbsEntryRepository.search(projectId, criteriaDTO, criteriaDTO.toPageable());

        Tuple sumInfo = wbsEntryRepository.searchSum(projectId, criteriaDTO);

        Long finishedCount = (Long) sumInfo.get(0);
        if (finishedCount == null) finishedCount = 0L;
        Long totalCount = (Long) sumInfo.get(1);
        if (totalCount == null) totalCount = 0L;
        Double finishedWorkLoad = Double.parseDouble(sumInfo.get(2) == null ? "0" : sumInfo.get(2).toString());
        if (finishedWorkLoad == null) finishedWorkLoad = 0.0;
        Double totalWorkLoad = (Double) sumInfo.get(3);
        if (totalWorkLoad == null) totalWorkLoad = 0.0;

        Double finishedCountPercent = 0.0;
        if (totalCount != 0) finishedCountPercent = (finishedCount * 1.0) / totalCount;
        Double finishedWorkLoadPercent = 0.0;
        if (totalWorkLoad != 0) finishedWorkLoadPercent = (finishedWorkLoad * 1.0) / totalWorkLoad;

        taskPackageDTO.setCountPercent(finishedCountPercent);
        taskPackageDTO.setWbsEntries(wbsEntries);
        taskPackageDTO.setWbsFinishedCount(finishedCount);
        taskPackageDTO.setWbsTotalCount(totalCount);
        taskPackageDTO.setWorkLoadFinished(finishedWorkLoad);
        taskPackageDTO.setWorkLoadPercent(finishedWorkLoadPercent);
        taskPackageDTO.setWorkLoadTotal(totalWorkLoad);

        return taskPackageDTO;
    }

    @Override
    public TaskPackageDTO wbsEntriesSectors(Long orgId, Long projectId, Long taskPackageId) {
        TaskPackageDTO result = new TaskPackageDTO();
        List<String> sectors = wbsEntryRepository.findSectorsByTaskPackageId(orgId, projectId, taskPackageId);
        result.setSectorList(sectors);
        return result;
    }

    /**
     * 取得四级计划所有工序列表。
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @param taskPackageId 任务包 ID
     * @param criteriaDTO   查询条件
     * @return 四级计划信息列表
     */
    @Override
    public List<EntityProcessDTO> processes(
        final Long orgId,
        final Long projectId,
        final Long taskPackageId,
        final WBSEntryCriteriaDTO criteriaDTO
    ) {
        criteriaDTO.setTaskPackageId(taskPackageId);
        return wbsEntryRepository.processes(projectId, criteriaDTO);
    }


    @Override
    public synchronized void updateTaskPackageCount(Long taskPackageId, Double workLoad) {
        taskPackageRepository.findById(taskPackageId).ifPresent(taskPackage -> {
            taskPackage.setFinishedCount(taskPackage.getFinishedCount() == null ? 0 : taskPackage.getFinishedCount() + 1);
            taskPackage.setFinishedWorkLoad(taskPackage.getFinishedWorkLoad() == null ? 0.0 : taskPackage.getFinishedWorkLoad() + (workLoad == null ? 0.0 : workLoad));
            taskPackageRepository.save(taskPackage);
        });

    }

    @Override
    public synchronized void addTaskPackageCount(Long taskPackageId, Double workLoad) {
        taskPackageRepository.findById(taskPackageId).ifPresent(taskPackage -> {
            taskPackage.setTotalCount((taskPackage.getTotalCount() == null ? 0 : taskPackage.getTotalCount()) + 1);
            taskPackage.setTotalWorkLoad(taskPackage.getTotalWorkLoad() == null ? 0.0 : taskPackage.getTotalWorkLoad() + (workLoad == null ? 0.0 : workLoad));
            taskPackageRepository.save(taskPackage);
        });

    }


    @Override
    public synchronized void reCalcTaskPackageCount(Long projectId, Long entityId) {
        List<BigInteger> taskPackageIds = wbsEntryRepository.findTpIdsByProjectIdAndEntityId(projectId, entityId);
        taskPackageIds.forEach(taskPackageId -> {
            if (taskPackageId != null) {
                Tuple tuple = wbsEntryStateRepository.findTpsByProjectIdAndEntityId(projectId, taskPackageId.longValue());
                if (tuple != null) {
                    Integer finishedCount = ((BigDecimal) tuple.get("finishedCount")).intValue();
                    Integer totalCount = ((BigInteger) tuple.get("totalCount")).intValue();
                    Double finishedWorkLoad = (Double) tuple.get("finishedWorkLoad");
                    Double totalWorkLoad = (Double) tuple.get("totalWorkLoad");
                    taskPackageRepository.findById(taskPackageId.longValue()).ifPresent(taskPackage -> {
                        taskPackage.setTotalCount(totalCount);
                        taskPackage.setFinishedCount(finishedCount);
                        taskPackage.setTotalWorkLoad(totalWorkLoad);
                        taskPackage.setFinishedWorkLoad(finishedWorkLoad);
                        taskPackageRepository.save(taskPackage);
                    });
                }
            }

        });
    }

    /**
     * 查询任务包中可添加的实体。
     *
     * @param orgId
     * @param projectId
     * @param taskPackageProjectNodeEntitySearchDTO
     * @return
     */
    @Override
    public Page<ProjectNode> projectNodeEntities(
        final Long orgId,
        final Long projectId,
        TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO
    ) {
//        if (taskPackageProjectNodeEntitySearchDTO.getHierarchyType() == null) {
//            throw new BusinessError("请填写要查找的对应层级专业");
//        }
        return projectNodeRepository.taskPackageEntities(
            orgId,
            projectId,
            taskPackageProjectNodeEntitySearchDTO
        );
    }
}
