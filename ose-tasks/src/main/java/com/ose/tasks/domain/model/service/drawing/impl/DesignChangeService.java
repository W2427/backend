package com.ose.tasks.domain.model.service.drawing.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.drawing.DesignChangeInterface;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.NotFoundError;
import com.ose.report.api.DesignChangeReviewFormAPI;
import com.ose.report.dto.DesignChangeReviewFormDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.report.vo.ReportExportType;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewFormRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewRegisterRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewRegisterDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.DesignChangeReviewForm;
import com.ose.tasks.entity.DesignChangeReviewRegister;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.DesignChangeDisciplines;
import com.ose.tasks.vo.DesignChangeOriginated;
import com.ose.tasks.vo.SuspensionState;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;

@Component
public class DesignChangeService implements DesignChangeInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    private DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository;

    private DesignChangeReviewFormRepository designChangeReviewFormRepository;

    private DrawingRepository drawingRepository;

    private BpmActivityInstanceRepository bpmActInstRepository;

    private TodoTaskBaseInterface todoTaskBaseService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final ProjectInterface projectService;

    private final ActivityTaskInterface activityTaskService;

    private final DesignChangeReviewFormAPI designChangeReviewFormAPI;

    /**
     * 构造方法
     */
    @Autowired
    public DesignChangeService(
        DesignChangeReviewRegisterRepository designChangeReviewRegisterRepository,
        DesignChangeReviewFormRepository designChangeReviewFormRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        DrawingRepository drawingRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        TaskRuleCheckService taskRuleCheckService,
        ProjectInterface projectService,
        ActivityTaskInterface activityTaskService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DesignChangeReviewFormAPI designChangeReviewFormAPI) {
        this.designChangeReviewRegisterRepository = designChangeReviewRegisterRepository;
        this.designChangeReviewFormRepository = designChangeReviewFormRepository;
        this.drawingRepository = drawingRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.projectService = projectService;
        this.activityTaskService = activityTaskService;
        this.designChangeReviewFormAPI = designChangeReviewFormAPI;
    }


    @Override
    public Page<DesignChangeReviewRegister> searchDesignChangeReviewRegisterList(Long orgId, Long projectId,
                                                                                 PageDTO page, DesignChangeCriteriaDTO criteriaDTO) {
        return designChangeReviewRegisterRepository.searchDesignChangeReviewRegisterList(orgId, projectId, page.toPageable(), criteriaDTO);
    }


    @Override
    public UploadDrawingFileResultDTO upload(Long orgId, Long projectId, DrawingUploadDTO uploadDTO) {

        Workbook workbook;
        File excel;


        try {
            excel = new File(temporaryDir, uploadDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }

        int errorCount = 0;
        int successCount = 0;
        List<String> errLine = new ArrayList<>();
        int sheetNum = workbook.getNumberOfSheets();

        DateFormat sdf = new SimpleDateFormat("y.M.d");
        for (int i = 0; i < sheetNum; i++) {
            if (BpmCode.SD_DWG_CHANGE_VOR_LIST.equals(workbook.getSheetAt(i).getSheetName())) {
                Row row;
                Iterator<Row> rows = workbook.getSheetAt(i).rowIterator();
                while (rows.hasNext()) {
                    row = rows.next();
                    if (row.getRowNum() < BpmCode.MOD_HEADER) {
                        continue;
                    }
                    try {
                        int colIndex = 1;

                        String transferNo = WorkbookUtils.readAsString(row, colIndex++);
                        String title = WorkbookUtils.readAsString(row, colIndex++);
                        String raisedDateStr = WorkbookUtils.readAsString(row, colIndex++);
                        String vorNo = WorkbookUtils.readAsString(row, colIndex++);
                        String originatedBy = WorkbookUtils.readAsString(row, colIndex++);

                        Date raisedDate = null;
                        if (raisedDateStr != null && !raisedDateStr.equals("")) {
                            raisedDate = sdf.parse(raisedDateStr);
                        }

                        if (vorNo != null && !vorNo.equals("")) {
                            DesignChangeReviewRegister m = designChangeReviewRegisterRepository
                                .findByOrgIdAndProjectIdAndVorNoAndStatus(orgId, projectId, vorNo, EntityStatus.ACTIVE);
                            if (m != null) {
                                m.setTransferNo(transferNo);
                                m.setTitle(title);
                                m.setRaisedDate(raisedDate);
                                m.setOriginatedBy(originatedBy);
                                m.setLastModifiedAt();
                            } else {
                                m = new DesignChangeReviewRegister();
                                m.setOrgId(orgId);
                                m.setProjectId(projectId);
                                m.setTransferNo(transferNo);
                                m.setTitle(title);
                                m.setRaisedDate(raisedDate);
                                m.setVorNo(vorNo);
                                m.setOriginatedBy(originatedBy);
                                m.setCreatedAt();
                                m.setStatus(EntityStatus.ACTIVE);
                            }
                            designChangeReviewRegisterRepository.save(m);

                            successCount++;
                        } else {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1));
                        }

                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        errorCount++;
                        errLine.add("" + (row.getRowNum() + 1));
                    }
                }
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(errLine);
        dto.setSuccessCount(successCount);
        return dto;
    }


    @Override
    public DesignChangeReviewRegister create(Long orgId, Long projectId, DesignChangeReviewRegisterDTO DTO) {
        DesignChangeReviewRegister mrr = designChangeReviewRegisterRepository
            .findByOrgIdAndProjectIdAndVorNoAndStatus(orgId, projectId, DTO.getVorNo(), EntityStatus.ACTIVE);
        /*if(mrr != null) {
            throw new ValidationError("VorNo has already exists");
        }*/
        mrr = BeanUtils.copyProperties(DTO, new DesignChangeReviewRegister());
        mrr.setOrgId(orgId);
        mrr.setProjectId(projectId);
        mrr.setStatus(EntityStatus.ACTIVE);
        mrr.setCreatedAt();
        return designChangeReviewRegisterRepository.save(mrr);
    }


    @Override
    public boolean delete(Long orgId, Long projectId, Long id) {
        Optional<DesignChangeReviewRegister> op = designChangeReviewRegisterRepository.findById(id);
        if (op.isPresent()) {
            DesignChangeReviewRegister mrr = op.get();
            mrr.setStatus(EntityStatus.DELETED);
            designChangeReviewRegisterRepository.save(mrr);
        }
        return false;
    }


    @Override
    public String getNewVorNo(Long orgId, Long projectId, Project project) {
        String vorNo = "" + project.getName() + "-01-0502-PI";
        String maxNo = designChangeReviewRegisterRepository.getMaxVorNoByProjectId(projectId);
        if (maxNo.contains(vorNo)) {
            Integer no = Integer.valueOf(maxNo.replaceAll(vorNo, ""));
            String newno = "" + (no + 1);
            if (newno.length() <= 3) {
                for (int i = 0; i < 3 - newno.length(); i++) {
                    vorNo += "0";
                }
            }
            vorNo += newno;
        } else {
            Long l = designChangeReviewRegisterRepository.getCountByOrgIdAndProjectId(orgId, projectId);
            l += 1;
            String count = "" + l.intValue();
            if (count.length() <= 3) {
                for (int i = 0; i < 3 - count.length(); i++) {
                    vorNo += "0";
                }
            }
            vorNo += count;
        }
        return vorNo;
    }


    @Override
    public DesignChangeReviewDTO getDesignChangeReviewForm(Long orgId, Long projectId, Long id) {
        DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(id);
        if (form != null) {
            DesignChangeReviewDTO result = new DesignChangeReviewDTO();
            result.setCauseDescription(form.getCauseDescription());
            result.setProjectName(form.getProjectName());
            result.setRaisedBy(form.getRaisedBy());
            result.setRaisedById(form.getRaisedById());
            result.setReportNo(form.getReportNo());
            result.setVorNo(form.getVorNo());
            result.setTitle(form.getTitle());

            result.setActionItem(form.getJsonActionItemReadOnly());
            result.setEngineeringManhours(form.getJsonEngineeringManhoursReadOnly());
            result.setMaterials(form.getJsonMaterialsReadOnly());
            result.setItemVersion(form.getJsonItemVersionReadOnly());

            List<DesignChangeOriginated> originatedBy = new ArrayList<>();
            for (String originated : form.getJsonOriginatedByReadOnly()) {
                originatedBy.add(DesignChangeOriginated.getByDisplayName(originated));
            }
            result.setOriginatedBy(originatedBy);

            List<DesignChangeDisciplines> involvedDisciplines = new ArrayList<>();
            for (String disciplines : form.getJsonInvolvedDisciplinesReadOnly()) {
                involvedDisciplines.add(DesignChangeDisciplines.getByDisplayName(disciplines));
            }
            result.setInvolvedDisciplines(involvedDisciplines);

            return result;
        }
        return new DesignChangeReviewDTO();
    }


    @Override
    public boolean addDesignChangeReviewForm(ContextDTO contextDTO, Long orgId, Long projectId, Long id,
                                             DesignChangeReviewDTO designChangeReviewDTO, OperatorDTO operator) {
        String vorNo = null;
        DesignChangeReviewRegister mrr = null;
        Optional<DesignChangeReviewRegister> op = designChangeReviewRegisterRepository.findById(id);
        if (op.isPresent()) {
            mrr = op.get();
            vorNo = mrr.getVorNo();
        }
        final Project project = projectService.get(orgId, projectId);

        DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(id);
        if (form == null) {
            form = new DesignChangeReviewForm();
            form.setReportNo(vorNo);
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

        if (mrr != null) {
            DesignChangeReviewFormDTO reportDTO = new DesignChangeReviewFormDTO();

            reportDTO.setOrgId(orgId);
            reportDTO.setProjectId(projectId);
            reportDTO.setReportQrCode(QrcodePrefixType.EXTERNAL_CONTROL_REPORT.getCode() + StringUtils.generateShortUuid());
            reportDTO.setReportName(form.getReportNo());

            reportDTO.setProjectName(form.getProjectName());
            reportDTO.setReportNo(form.getReportNo());
            reportDTO.setDesignChangeTitle(form.getTitle());
            reportDTO.setRaisedPerson(form.getRaisedBy());
            reportDTO.setRaisedDate(mrr.getRaisedDate());
            reportDTO.setModificationCause(form.getCauseDescription());

            String actionList = "";
            for (int i = 0; i < form.getJsonActionItemReadOnly().size(); i++) {
                String item = form.getJsonActionItemReadOnly().get(i);
                String version = form.getJsonItemVersionReadOnly().get(i);
                actionList += ", " + item + " R" + version;
            }
            if (actionList.length() > 2) {
                actionList = actionList.substring(2);
            }
            reportDTO.setActionList(actionList);

            reportDTO.setPipeManhour(form.getJsonEngineeringManhoursReadOnly().get(3));
            reportDTO.setTotalManhour(form.getJsonEngineeringManhoursReadOnly().get(6));
            reportDTO.setPipeMaterial(form.getJsonMaterialsReadOnly().get(3));
            reportDTO.setExportType(ReportExportType.MS_EXCEL);
            JsonObjectResponseBody<ReportHistory> response = designChangeReviewFormAPI.generateDesignChangeReviewForm(orgId, projectId, reportDTO);
            ReportHistory his = response.getData();

            BpmEntityDocsMaterials bedm = new BpmEntityDocsMaterials();
            bedm.setEntityNo(mrr.getVorNo());
            List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, BpmCode.CHANGE_LEAD_BY_DRAWING);
            if (!processes.isEmpty()) {
                bedm.setProcessId(processes.get(0).getId());
            }
            bedm.setProjectId(projectId);
            bedm.setCreatedAt();
            bedm.getLastModifiedAt();
            bedm.setStatus(EntityStatus.ACTIVE);
            bedm.setEntityId(mrr.getId());
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

            mrr.setChangeReviewFileId(his.getFileId());
            mrr.setChangeReviewFileName(his.getReportName());
            designChangeReviewRegisterRepository.save(mrr);
        }

        List<BpmActivityInstanceBase> act = activityTaskService.findActInst(orgId, projectId, mrr==null?null:mrr.getId());
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

    @Override
    public DesignChangeReviewRegister getById(Long id) {
        Optional<DesignChangeReviewRegister> op = designChangeReviewRegisterRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }


    @Override
    public Page<Drawing> searchDrawingList(Long orgId, Long projectId, PageDTO page, String keyword) {
        List<Long> entityIds = bpmActInstRepository.findEntityIdsByProjectIdAndEntityTypeAndFinishStateAndSuspensionState(
            projectId, BpmCode.SHOP_DRAWING, ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
        return drawingRepository.findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(orgId, projectId, entityIds, page.toPageable(), keyword);
    }

}
