package com.ose.tasks.domain.model.service.drawing.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ose.tasks.domain.model.service.bpm.*;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.vo.bpm.BpmCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.report.api.DesignChangeReviewFormAPI;
import com.ose.report.dto.DesignChangeReviewFormDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.drawing.ConstructionChangeRegisterRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewFormRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.ConstructionChangeInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.dto.drawing.ConstructionChangeRegisterDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.ConstructionChangeRegister;
import com.ose.tasks.entity.DesignChangeReviewForm;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.DesignChangeDisciplines;
import com.ose.tasks.vo.DesignChangeOriginated;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;

@Component
public class ConstructionChangeService
    implements ConstructionChangeInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final ConstructionChangeRegisterRepository constructionChangeRegisterRepository;

    private final DesignChangeReviewFormRepository designChangeReviewFormRepository;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final ProjectInterface projectService;

    private final ActivityTaskInterface activityTaskService;

    private final DesignChangeReviewFormAPI designChangeReviewFormAPI;

    /**
     * 构造方法
     */
    @Autowired
    public ConstructionChangeService(
        ConstructionChangeRegisterRepository constructionChangeRegisterRepository,
        DesignChangeReviewFormRepository designChangeReviewFormRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TodoTaskBaseInterface todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        ProjectInterface projectService,
        ActivityTaskInterface activityTaskService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DesignChangeReviewFormAPI designChangeReviewFormAPI) {
        this.constructionChangeRegisterRepository = constructionChangeRegisterRepository;
        this.designChangeReviewFormRepository = designChangeReviewFormRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.projectService = projectService;
        this.activityTaskService = activityTaskService;
        this.designChangeReviewFormAPI = designChangeReviewFormAPI;
    }

    /**
     * 建造变更申请单列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return
     */
    @Override
    public Page<ConstructionChangeRegister> searchConstructionChangeRegisterList(Long orgId, Long projectId,
                                                                                 PageDTO page, DesignChangeCriteriaDTO criteriaDTO) {
        return constructionChangeRegisterRepository.searchConstructionChangeRegisterList(orgId, projectId, page.toPageable(), criteriaDTO);
    }

    /**
     * 添加建造变更申请。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param dTO       创建建造变更DTO
     * @return
     */
    @Override
    public ConstructionChangeRegister create(Long orgId, Long projectId, ConstructionChangeRegisterDTO dTO,
                                             OperatorDTO operatorDTO, Project project) {
        ConstructionChangeRegister ccr = new ConstructionChangeRegister();
        ccr.setRegisterNo(this.getRegisterNo(orgId, projectId, project));
        ccr.setActions(dTO.getActions());
        ccr.setChangeType(dTO.getChangeType());
        ccr.setCreateBy(operatorDTO.getName());
        ccr.setCreateById(operatorDTO.getId());
        ccr.setCreatedAt();
        ccr.setModelName(dTO.getModelName());
        ccr.setOrgId(orgId);
        ccr.setProjectId(projectId);
        ccr.setOriginatedBy(dTO.getOriginatedBy());
        ccr.setStatus(EntityStatus.ACTIVE);
        return constructionChangeRegisterRepository.save(ccr);
    }

    private String getRegisterNo(Long orgId, Long projectId, Project project) {
        String registerNo = "" + project.getName() + "-" + BpmCode.CC + "-";
        Long l = constructionChangeRegisterRepository.getCountByOrgIdAndProjectId(orgId, projectId);
        l += 1;
        String count = "" + l.intValue();
        if (count.length() <= 4) {
            for (int i = 0; i < 4 - count.length(); i++) {
                registerNo += "0";
            }
        }
        registerNo += count;
        return registerNo;
    }

    /**
     * 删除设计变更。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        设计变更id
     * @return
     */
    @Override
    public boolean delete(Long orgId, Long projectId, Long id) {
        Optional<ConstructionChangeRegister> op = constructionChangeRegisterRepository.findById(id);
        if (op.isPresent()) {
            ConstructionChangeRegister ccr = op.get();
            ccr.setStatus(EntityStatus.DELETED);
            ccr.setLastModifiedAt();
            constructionChangeRegisterRepository.save(ccr);
            return true;
        }
        return false;
    }

    /**
     * 查找建造变更。
     *
     * @param id 建造变更id
     * @return
     */
    @Override
    public ConstructionChangeRegister getById(Long id) {

        Optional<ConstructionChangeRegister> op = constructionChangeRegisterRepository.findById(id);
        if (op.isPresent()) {
            ConstructionChangeRegister ccr = op.get();
            return ccr;
        }
        return null;
    }

    /**
     * 填写建造变更评审单。
     *
     * @param contextDTO            内容
     * @param orgId                 组织id
     * @param projectId             项目id
     * @param id                    建造变更id
     * @param designChangeReviewDTO 填写内容DTO
     * @param operator              操作者
     * @return
     */
    @Override
    public boolean addDesignChangeReviewForm(ContextDTO contextDTO, Long orgId, Long projectId, Long id,
                                             DesignChangeReviewDTO designChangeReviewDTO, OperatorDTO operator) {
        String vorNo = null;
        ConstructionChangeRegister ccr = null;


        Optional<ConstructionChangeRegister> op = constructionChangeRegisterRepository.findById(id);
        if (op.isPresent()) {
            ccr = op.get();
            vorNo = ccr.getRegisterNo();
        }

        final Project project = projectService.get(orgId, projectId);


        DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(id);
        if (form == null) {
            form = new DesignChangeReviewForm();


            form.setReportNo(this.getNewReviewFormNo(orgId, projectId, project));
        }

        form.setOrgId(orgId);
        form.setProjectId(projectId);
        form.setReviewRegisterId(id);

        form.setProjectName(project.getName());
        form.setRaisedById(operator.getId());
        form.setRaisedBy(operator.getName());
        form.setVorNo(vorNo);
        form.setCauseDescription(designChangeReviewDTO.getCauseDescription());
        form.setTitle(designChangeReviewDTO.getTitle());
        form.setJsonActionItem(designChangeReviewDTO.getActionItem());
        form.setJsonItemVersion(designChangeReviewDTO.getItemVersion());
        form.setJsonEngineeringManhours(this.replaceNullValue(designChangeReviewDTO.getEngineeringManhours()));
        form.setJsonMaterials(this.replaceNullValue(designChangeReviewDTO.getMaterials()));


        List<String> originatedBy = new ArrayList<>();
        for (DesignChangeOriginated originated : designChangeReviewDTO.getOriginatedBy()) {
            originatedBy.add(originated.getDisplayName());
        }
        form.setJsonOriginatedBy(originatedBy);


        List<String> involvedDisciplines = new ArrayList<>();
        for (DesignChangeDisciplines disciplines : designChangeReviewDTO.getInvolvedDisciplines()) {
            involvedDisciplines.add(disciplines.getDisplayName());
        }
        form.setJsonInvolvedDisciplines(involvedDisciplines);
        form.setCreatedAt();
        form.setStatus(EntityStatus.ACTIVE);
        designChangeReviewFormRepository.save(form);



        if (ccr != null) {
            DesignChangeReviewFormDTO reportDTO = new DesignChangeReviewFormDTO();

            reportDTO.setOrgId(orgId);
            reportDTO.setProjectId(projectId);
            reportDTO.setReportQrCode(QrcodePrefixType.EXTERNAL_CONTROL_REPORT.getCode() + StringUtils.generateShortUuid());
            reportDTO.setReportName(form.getReportNo());
            reportDTO.setProjectName(form.getProjectName());
            reportDTO.setReportNo(form.getReportNo());
            reportDTO.setDesignChangeTitle(form.getTitle());
            reportDTO.setRaisedPerson(form.getRaisedBy());
            reportDTO.setRaisedDate(ccr.getCreatedAt());
            reportDTO.setModificationCause(form.getCauseDescription());

            String actionList = "";


            for (String item : form.getJsonActionItemReadOnly()) {
                actionList += ", " + item;
            }


            if (actionList.length() > 2) {
                actionList = actionList.substring(2);
            }
            reportDTO.setActionList(actionList);
            reportDTO.setPipeManhour(form.getJsonEngineeringManhoursReadOnly().get(3));
            reportDTO.setTotalManhour(form.getJsonEngineeringManhoursReadOnly().get(6));
            reportDTO.setPipeMaterial(form.getJsonMaterialsReadOnly().get(3));


            JsonObjectResponseBody<ReportHistory> response = designChangeReviewFormAPI.generateDesignChangeReviewForm(orgId, projectId, reportDTO);
            ReportHistory his = response.getData();

            BpmEntityDocsMaterials bedm = new BpmEntityDocsMaterials();
            bedm.setEntityNo(ccr.getRegisterNo());


            List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, BpmCode.CHANGE_LEAD_BY_CONSTRUCTION);
            if (!processes.isEmpty()) {
                bedm.setProcessId(processes.get(0).getId());
            }
            bedm.setProjectId(projectId);
            bedm.setCreatedAt();
            bedm.getLastModifiedAt();
            bedm.setStatus(EntityStatus.ACTIVE);
            bedm.setEntityId(ccr.getId());
            bedm.setType(ActInstDocType.DESIGN_CHANGE_REVIEW_FORM);
            List<ActReportDTO> list = new ArrayList<>();
            ActReportDTO dto = new ActReportDTO();
            dto.setFileId(his.getFileId());
            dto.setFilePath(his.getFilePath());
            dto.setReportQrCode(his.getReportQrCode());
            dto.setReportNo(his.getReportNo());
            list.add(dto);
            bedm.setJsonDocs(list);
            activityTaskService.saveDocsMaterials(bedm);
        }


        List<BpmActivityInstanceBase> act = activityTaskService.findActInst(orgId, projectId, ccr==null?null:ccr.getId());
        if (!act.isEmpty()) {
            Long actInstId = act.get(0).getId();


            List<BpmRuTask> ruTask = todoTaskBaseService.findBpmRuTaskByActInstId(actInstId);
            if (!ruTask.isEmpty()) {
                if (taskRuleCheckService.isCreateChangeTaskTaskNode(ruTask.get(0).getTaskDefKey())) {
                    TodoTaskExecuteDTO toDoTaskDTO = new TodoTaskExecuteDTO();
                    todoTaskDispatchService.exec(contextDTO, orgId, projectId, ruTask.get(0).getId(), toDoTaskDTO, operator);
                }
            }
        }

        return true;
    }

    /**
     * 整合数组中的非空字段。
     *
     * @param strList
     * @return
     */
    private List<String> replaceNullValue(List<String> strList) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i) == null) {
                result.add("");
            } else {
                result.add(strList.get(i));
            }
        }
        return result;
    }

    /**
     * 获得设计变更号。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param project   项目
     * @return
     */
    public String getNewReviewFormNo(Long orgId, Long projectId, Project project) {
        String reportNo = "" + project.getName() + "-" + BpmCode.DCRF;
        Long l = designChangeReviewFormRepository.getCountByOrgIdAndProjectId(orgId, projectId);
        String count = "" + l.intValue();
        if (count.length() <= 3) {
            for (int i = 0; i < 3 - count.length(); i++) {
                reportNo += "0";
            }
        }
        reportNo += count;
        return reportNo;
    }


}
