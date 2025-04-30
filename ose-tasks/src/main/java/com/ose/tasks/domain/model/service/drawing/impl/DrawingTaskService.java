package com.ose.tasks.domain.model.service.drawing.impl;

import java.util.*;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.BatchTaskDrawingRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.DrawingAppointDTO;
import com.ose.tasks.dto.drawing.DrawingPackageReturnDTO;
import com.ose.tasks.entity.BatchDrawingTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import com.ose.util.CollectionUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.tasks.domain.model.service.drawing.DrawingTaskInterface;
import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.vo.SuspensionState;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;

@Component
public class DrawingTaskService implements DrawingTaskInterface {


    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmProcessRepository processRepository;

    private final BatchTaskDrawingRepository batchTaskDrawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final ProjectInterface projectService;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BatchTaskInterface batchTaskService;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final DrawingRepository drawingRepository;

    private final SubDrawingHistoryRepository subDrawingHisRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingTaskService(
        BpmReDeploymentRepository bpmReDeploymentRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        BpmRuTaskRepository ruTaskRepository,
        BpmProcessRepository processRepository,
        BatchTaskDrawingRepository batchTaskDrawingRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingBaseInterface drawingBaseService,
        ProjectInterface projectService,
        DrawingDetailRepository drawingDetailRepository,
        BatchTaskInterface batchTaskService,
        BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        DrawingHistoryRepository drawingHistoryRepository,
        DrawingRepository drawingRepository,
        SubDrawingHistoryRepository subDrawingHisRepository
    ) {
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.processRepository = processRepository;
        this.batchTaskDrawingRepository = batchTaskDrawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.projectService = projectService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.batchTaskService = batchTaskService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.drawingRepository = drawingRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
    }


    /**
     * 获取管线设计流程模型列表
     */
    @Override
    public List<BpmReDeployment> getModels(Long orgId, Long projectId) {
        String category = "管线设计";
        return bpmReDeploymentRepository.findByCategoryAndOrgIdAndProjectIdAndSuspensionState(
            category, orgId, projectId, SuspensionState.ACTIVE
        );
    }

    /**
     * 根据图号获取流程实例
     */
    @Override
    public List<BpmActivityInstanceBase> findByEntityNo(Long orgId, Long projectId, String dwgNo) {
        return bpmActInstRepository.findByProjectIdAndEntityNo(projectId, dwgNo);
    }

    /**
     * 保存流程实例
     */
    @Override
    public BpmActivityInstanceBase saveActInst(BpmActivityInstanceBase actInst) {

        Optional<BpmProcess> opBpmProcess = processRepository.findById(actInst.getProcessId());
        if (opBpmProcess.isPresent()) {
            BpmProcessCategory category = opBpmProcess.get().getProcessCategory();
            if (category != null) {
                actInst.setActCategory(category.getNameCn());
                actInst.setProcessCategoryId(category.getId());
            }

            BpmProcess bpmProcess = opBpmProcess.get();
            actInst.setProcess(bpmProcess.getNameCn());
            actInst.setProcessStageId(bpmProcess.getProcessStage().getId());
            actInst.setProcessStage(bpmProcess.getProcessStage().getNameCn());
        }

        return bpmActInstRepository.save(actInst);
    }

    /**
     * 保存任务分配信息
     */
    @Override
    public BpmActTaskAssignee saveActTaskAssignee(BpmActTaskAssignee assignee) {
        return bpmActTaskAssigneeRepository.save(assignee);
    }

    /**
     * 保存运行时任务信息
     */
    @Override
    public BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask) {
        return ruTaskRepository.save(bpmRuTask);
    }

    /**
     * 获取图纸任务列表
     */
    @Override
    public Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria) {
        return bpmActInstRepository.actInstList(orgId, projectId, criteria);
    }

    /**
     * 查询图纸打包历史
     */
    @Override
    public Page<BatchDrawingTask> batchTaskDrawingList(
        Long orgId,
        Long projectId,
        PageDTO page
    ) {
        return batchTaskDrawingRepository.findByOrgIdAndProjectId(orgId, projectId, page.toPageable());
    }

    /**
     * 图纸打包停止。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
    @Override
    public void stopBatchTaskDrawing(
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    ) {
        BatchDrawingTask batchDrawingTask = batchTaskDrawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, batchTaskDrawingId);

        if (batchDrawingTask == null) {
            throw new NotFoundError("drawing package batch not found.");
        }
        if (!batchDrawingTask.getStatus().equals(BatchTaskStatus.RUNNING)) {
            throw new BusinessError("drawing package batch is not running.");
        }
        batchDrawingTask.setStatus(BatchTaskStatus.FINISHED);
        batchTaskDrawingRepository.save(batchDrawingTask);

    }

    /**
     * 图纸重新打包。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
    @Override
    public void startBatchTaskDrawing(
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    ) {
        BatchDrawingTask batchDrawingTask = batchTaskDrawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, batchTaskDrawingId);

        if (batchDrawingTask == null) {
            throw new NotFoundError("drawing package batch not found.");
        }
        if (batchDrawingTask.getStatus().equals(BatchTaskStatus.RUNNING) || batchDrawingTask.getStatus().equals(BatchTaskStatus.FINISHED)) {
            throw new BusinessError("drawing package batch can not retry package.");
        }

        BpmActivityInstanceBase actInst = batchDrawingTask.getJsonActInst();
        BpmRuTask ruTask = batchDrawingTask.getJsonRuTask();
        List<BpmRuTask> nextRuTask = batchDrawingTask.getJsonNextRuTask();
        Drawing drawing = batchDrawingTask.getJsonDrawing();
        DrawingDetail drawingDetail = batchDrawingTask.getJsonDrawingDetail();


        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();


        if (batchDrawingTask.getTaskDefKey().equals("usertask-USER-DRAWING-DESIGN")) {

            batchTaskService.runDrawingPackage(
                contextDTO,
                project,
                DRAWING_PACKAGE,
                actInst,
                ruTask,
                nextRuTask,
                drawing,
                drawingDetail,
                null,
                null,
                null,
                batchTask -> {

                    DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                        orgId,
                        project,
                        actInst.getId(),
                        operatorDTO,
                        false,
                        drawing,
                        actInst.getProcessId(),
                        drawingDetail);


                    if (drawingPackageReturnDTO.getBpmDoc() != null) {
                        docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
                    }

                    if (drawingPackageReturnDTO.getDrawing() != null) {
                        drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                    }

                    if (drawingPackageReturnDTO.getDrawingDetail() != null) {
                        drawingDetailRepository.save(drawingPackageReturnDTO.getDrawingDetail());
                    }


                    if (drawingPackageReturnDTO.getSubDrawingList() != null && drawingPackageReturnDTO.getSubDrawingList().size() > 0) {
                        subDrawingRepository.saveAll(drawingPackageReturnDTO.getSubDrawingList());
                    }


                    if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                        drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
                    }

                    subDrawingRepository.
                        updateReviewStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, project.getId(), DrawingReviewStatus.CHECK, drawing.getId(), actInst.getId(), drawing.getLatestRev());

                    for (BpmRuTask ruTaskItem : nextRuTask) {
                        BpmRuTask bpmRuTaskOriginal = ruTaskRepository.findById(ruTaskItem.getId()).orElse(null);
                        bpmRuTaskOriginal.setHandling(false);
                        bpmRuTaskList.add(bpmRuTaskOriginal);
                    }
                    ruTaskRepository.saveAll(bpmRuTaskList);
                    return new BatchResultDTO();
                }
            );
        }


        if (batchDrawingTask.getTaskDefKey().equals("usertask-USER-DRAWING-MODIFY")) {
            batchTaskService.runDrawingPackage(
                contextDTO,
                project,
                DRAWING_PACKAGE,
                actInst,
                ruTask,
                nextRuTask,
                drawing,
                drawingDetail,
                null,
                null,
                null,
                batchTask -> {

                    Set<DrawingReviewStatus> inStatus = new HashSet<>();
                    inStatus.add(DrawingReviewStatus.INIT);
                    subDrawingRepository.updateReviewStatusByDrawingIdAndDrawingVersion(
                        orgId,
                        project.getId(),
                        DrawingReviewStatus.CHECK,
                        drawing.getId(),
                        drawing.getLatestRev(),
                        inStatus
                    );
                    DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                        orgId,
                        project,
                        actInst.getId(),
                        operatorDTO,
                        false,
                        drawing,
                        actInst.getProcessId(),
                        drawingDetail);


                    if (drawingPackageReturnDTO.getBpmDoc() != null) {
                        docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
                    }

                    if (drawingPackageReturnDTO.getDrawing() != null) {
                        drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                    }

                    if (drawingPackageReturnDTO.getDrawingDetail() != null) {
                        drawingDetailRepository.save(drawingPackageReturnDTO.getDrawingDetail());
                    }


                    if (drawingPackageReturnDTO.getSubDrawingList() != null && drawingPackageReturnDTO.getSubDrawingList().size() > 0) {
                        subDrawingRepository.saveAll(drawingPackageReturnDTO.getSubDrawingList());
                    }


                    if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                        drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
                    }
                    for (BpmRuTask ruTaskItem : nextRuTask) {
                        BpmRuTask bpmRuTaskOriginal = ruTaskRepository.findById(ruTaskItem.getId()).orElse(null);
                        bpmRuTaskOriginal.setHandling(false);
                        bpmRuTaskList.add(bpmRuTaskOriginal);
                    }
                    ruTaskRepository.saveAll(bpmRuTaskList);
                    return new BatchResultDTO();
                }
            );
        }

        if (batchDrawingTask.getTaskDefKey().equals("UT-CONFIRM_DRAWING_APPROVED")) {
            batchTaskService.runDrawingPackage(
                contextDTO,
                project,
                DRAWING_PACKAGE,
                actInst,
                ruTask,
                nextRuTask,
                drawing,
                drawingDetail,
                null,
                null,
                null,
                batchTask -> {

                    DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                        orgId,
                        project,
                        actInst.getId(),
                        operatorDTO,
                        true,
                        drawing,
                        actInst.getProcessId(),
                        drawingDetail);


                    if (drawingPackageReturnDTO.getBpmDoc() != null) {
                        docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
                    }

                    if (drawingPackageReturnDTO.getDrawing() != null) {
                        drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                    }

                    if (drawingPackageReturnDTO.getDrawingDetail() != null) {
                        drawingDetailRepository.save(drawingPackageReturnDTO.getDrawingDetail());
                    }


                    if (drawingPackageReturnDTO.getSubDrawingList() != null && drawingPackageReturnDTO.getSubDrawingList().size() > 0) {
                        subDrawingRepository.saveAll(drawingPackageReturnDTO.getSubDrawingList());
                    }


                    if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                        drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
                    }


                    Set<String> subNos = subDrawingRepository.findByOrgIdAndProjectIdAndActInstIdAndStatusAndDrawingId(orgId, project.getId(), actInst.getId(), EntityStatus.ACTIVE.name(), drawing.getId());

                    if (!CollectionUtils.isEmpty(subNos)) {

                        if (actInst.getProcess().equals(BpmCode.DRAWING_INTEGRAL_UPDATE)) {

                            subDrawingRepository.updateStatusByDrawingId(orgId, project.getId(), EntityStatus.DELETED, drawing.getId());
                        } else {

                            subDrawingRepository.updateStatusDeletedByDrawingIdAndSubDrawingNoInAndStatus(orgId, project.getId(), EntityStatus.DELETED, drawing.getId(), subNos, EntityStatus.APPROVED);
                        }
                    }


                    subDrawingRepository.updateStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, project.getId(), EntityStatus.APPROVED, drawing.getId(), actInst.getId(), drawing.getLatestRev());

                    subDrawingHisRepository.updateIssuedByDrawingIdAndDrawingVersion(orgId, project.getId(), true, drawing.getId(), drawing.getLatestRev());


                    drawingDetailRepository.updateStatusByDrawingIdAndDrawingVersionAndStatus(orgId, project.getId(), drawing.getId(), drawing.getLatestRev(), EntityStatus.ACTIVE, EntityStatus.PENDING);

                    drawingBaseService.packEffectiveSubFiles(
                        orgId,
                        project,
                        operatorDTO,
                        drawing);

                    drawing.setLocked(true);
                    drawingRepository.save(drawing);
                    return new BatchResultDTO();
                }
            );
        }

    }

    /**
     * 给所有有效图纸打包二维码并生成pdf。
     *
     * @param orgId
     * @param projectId
     * @param batchTaskDrawingId
     * @return
     */
    @Override
    public void startBatchTask(
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long batchTaskDrawingId
    ) {
        BatchDrawingTask batchDrawingTask = batchTaskDrawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, batchTaskDrawingId);
        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(
            orgId,
            projectId,
            batchDrawingTask.getDrawingId()
        );
        drawingBaseService.startBatchTask(
            orgId,
            project,
            operatorDTO,
            drawing
        );
    }

    /**
     * 给单张子图纸打印二维码。
     *
     * @param orgId
     * @param projectId
     * @param subDrawingId
     * @return
     */
    @Override
    public void startSubDrawingTask(
        ContextDTO contextDTO,
        Project project,
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        Long subDrawingId
    ) {
        SubDrawing subDrawing = subDrawingRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            subDrawingId,
            EntityStatus.ACTIVE
        );

        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(
            orgId,
            projectId,
            subDrawing.getDrawingId()
        );
        drawingBaseService.startSubDrawingTask(
            orgId,
            project,
            operatorDTO,
            drawing,
            subDrawing
        );
    }

    @Override
    public void appoint(Long orgId, Long projectId, Long drawingId, DrawingAppointDTO appointDTO, OperatorDTO operator) {
        DrawingHistory drawingHistory = drawingHistoryRepository.findByDrawingId(drawingId);
//        if (drawingHistory == null ){
//            DrawingHistory dh = new DrawingHistory();
//            dh.setStatus(EntityStatus.ACTIVE);
//            dh.setDrawingId(drawingId);
//            drawingHistoryRepository.save(dh);
//        }
        if (drawingHistory.getSh() == null) {
            //设置项目副经理
            drawingHistory.setVp(operator.getName());
            drawingHistory.setVpId(operator.getId());

            //设置部门主管
            drawingHistory.setSh(appointDTO.getUsername());
            drawingHistory.setShId(appointDTO.getId());
            drawingHistoryRepository.save(drawingHistory);
        } else if (drawingHistory.getLeadEngineer() == null) {
            //设置主管工程师
            drawingHistory.setLeadEngineer(appointDTO.getUsername());
            drawingHistory.setLeadEngineerId(appointDTO.getId());
            drawingHistoryRepository.save(drawingHistory);
        } else if (drawingHistory.getEngineer() == null){
            drawingHistory.setEngineer(appointDTO.getUsername());
            drawingHistory.setEngineerId(appointDTO.getId());
            drawingHistoryRepository.save(drawingHistory);
        } else {
            throw new NotFoundError("该图纸已指派完全");
        }
    }

    @Override
    public void workHour(Long orgId, Long projectId, Long drawingId, Double workhour) {
        DrawingHistory drawingHistory = drawingHistoryRepository.findByDrawingId(drawingId);
        if (drawingHistory.getVp() == null || drawingHistory.getSh() == null ||
            drawingHistory.getLeadEngineer() == null || drawingHistory.getEngineer() == null) {
            throw new NotFoundError("图纸未指派完全");
        }else {
            drawingHistory.setWorkHour(workhour);
            drawingHistoryRepository.save(drawingHistory);
        }
    }
}
