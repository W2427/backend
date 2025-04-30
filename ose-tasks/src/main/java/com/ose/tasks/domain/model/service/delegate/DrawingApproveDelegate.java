package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.drawing.DrawingFileDTO;
import com.ose.tasks.dto.drawing.DrawingPackageReturnDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.util.CollectionUtils;
import com.ose.vo.DrawingFileType;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;

/**
 * 签批 节点 代理。
 */
@Component
public class DrawingApproveDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(DrawingApproveDelegate.class);

    private final DrawingRepository drawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final SubDrawingHistoryRepository subDrawingHisRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final ProjectInterface projectService;

    private final DrawingBaseInterface drawingBaseService;


    private final BatchTaskInterface batchTaskService;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final EntitySubTypeInterface entitySubTypeService;
    /**
     * 构造方法。
     */
    @Autowired
    public DrawingApproveDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                  DrawingRepository drawingRepository,
                                  DrawingDetailRepository drawingDetailRepository,
                                  BpmRuTaskRepository ruTaskRepository,
                                  SubDrawingHistoryRepository subDrawingHisRepository,
                                  SubDrawingRepository subDrawingRepository,
                                  ProjectInterface projectService,
                                  DrawingBaseInterface drawingBaseService,
                                  BatchTaskInterface batchTaskService,
                                  BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                                  StringRedisTemplate stringRedisTemplate,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                  DrawingHistoryRepository drawingHistoryRepository, EntitySubTypeInterface entitySubTypeService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.projectService = projectService;
        this.drawingBaseService = drawingBaseService;
        this.batchTaskService = batchTaskService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.entitySubTypeService = entitySubTypeService;
    }


    /**
     * 任务处理后。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {


        Long projectId = execResult.getProjectId();
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        Long orgId = (Long) data.get("orgId");
        Map<String, Object> variables = execResult.getVariables();
        Long actInstId = execResult.getRuTask().getActInstId();
        Project project = projectService.get(orgId, projectId);


        if (variables == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            variables = execResult.getVariables();
            if (!execResult.isExecResult()) return execResult;
        }

        Drawing dwg = (Drawing) variables.get("drawing");
        if (dwg == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸不存在");
            return execResult;
        }

        DrawingDetail drawingDetail = null;


        Optional<DrawingDetail> op = drawingDetailRepository.findByDrawingIdAndRevAndStatus(dwg.getId(), dwg.getLatestRev(), EntityStatus.PENDING);
        if (op.isPresent()) {
            drawingDetail = op.get();
        } else {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸详情不存在");
            return execResult;
        }
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());

        if (best != null && best.isSubDrawingFlg()) {


            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
                && BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {


                List<ExecResultDTO> nextTasks = execResult.getNextTasks();
                List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
                for (ExecResultDTO nextTask : nextTasks) {
                    BpmRuTask bpmRuTaskOriginal = ruTaskRepository.findById(nextTask.getRuTask().getId()).orElse(null);
                    bpmRuTaskOriginal.setHandling(true);
                    bpmRuTaskList.add(bpmRuTaskOriginal);
                    logger.info("更改待办任务状态为处理中->图纸审核 ");
                }
                ruTaskRepository.saveAll(bpmRuTaskList);
                packSubFiles(orgId, project, actInstId, operatorDTO, dwg, execResult, drawingDetail, contextDTO);
            } else {

                subDrawingRepository.updateReviewStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, projectId, DrawingReviewStatus.MODIFY, dwg.getId(), actInstId, dwg.getLatestRev());
                dwg.setLocked(false);
                drawingRepository.save(dwg);
            }
        } else {

            if (execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
                && BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {

                DrawingFileDTO drawingFileDTO = drawingBaseService.packageCover(orgId, project, dwg);
                dwg.setCoverId(drawingFileDTO.getFileId());
                dwg.setCoverName(dwg.getCoverName());
                dwg.setCoverPath(drawingFileDTO.getFilePath());
                dwg.setLocked(true);
                dwg.setLatestApprovedRev(dwg.getLatestRev());
                drawingRepository.save(dwg);

                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packMonoFiles(orgId, project, operatorDTO, true, dwg, execResult.getProcessId(), execResult.getActInst().getId(), drawingDetail);

                if (drawingPackageReturnDTO.getBpmDoc() != null) {
                    docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
                }

                if (drawingPackageReturnDTO.getDrawing() != null) {
                    drawingRepository.save(drawingPackageReturnDTO.getDrawing());
                }


                if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                    drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
                }


                drawingDetailRepository.updateStatusByDrawingIdAndStatus(orgId, project.getId(), dwg.getId(), EntityStatus.SUSPEND, EntityStatus.ACTIVE);


                drawingDetailRepository.updateStatusByDrawingIdAndDrawingVersionAndStatus(orgId, project.getId(), dwg.getId(), dwg.getLatestRev(), EntityStatus.ACTIVE, EntityStatus.PENDING);
            } else {

                dwg.setLocked(false);
                drawingRepository.save(dwg);
            }
        }
        return execResult;
    }


    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
        Long projectId = execResult.getProjectId();
        Long orgId = (Long) data.get("orgId");
        Map<String, Object> variables = execResult.getVariables();
        if (variables == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            variables = execResult.getVariables();
            if (!execResult.isExecResult()) return execResult;
        }

        Drawing dwg = (Drawing) variables.get("drawing");
        if (dwg == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸不存在");
            return execResult;
        }

        DrawingDetail drawingDetail = null;

        DrawingDetail op = drawingDetailRepository.findByDrawingIdAndRevNo(dwg.getId(), dwg.getLatestRev());
        if (null != op) {
            drawingDetail = op;
        } else {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸详情不存在");
            return execResult;
        }
        try {
            if (execResult.getTodoTaskDTO().getBarCodeId() != null) {
                drawingBaseService.generateBarCode(orgId, projectId, dwg, drawingDetail, execResult.getTodoTaskDTO().getBarCodeId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新drawingDetail状态，并且主表的条形码与最新有效版本一并更新
        drawingDetail.setStatus(EntityStatus.ACTIVE);
        drawingDetailRepository.save(drawingDetail);
        dwg.setLatestApprovedRev(dwg.getLatestRev());
        dwg.setQrCode(drawingDetail.getQrCode());

        return execResult;

    }

    /**
     * 新起线程打包图纸文件。
     */
    private void packSubFiles(
        Long orgId,
        Project project,
        Long actInstId,
        OperatorDTO operatorDTO,
        Drawing dwg,
        ExecResultDTO execResult,
        DrawingDetail drawingDetail,
        ContextDTO contextDTO
    ) {
        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        logger.info("图纸打包开始->图纸审核");
        List<BpmRuTask> ruTasks = new ArrayList<>();
        nextTasks.forEach(nextTask -> {
            ruTasks.add(nextTask.getRuTask());
        });

        batchTaskService.runDrawingPackage(
            contextDTO,
            project,
            DRAWING_PACKAGE,
            execResult.getActInst(),
            execResult.getRuTask(),
            ruTasks,
            dwg,
            drawingDetail,
            null,
            null,
            null,
            batchTask -> {

                logger.info("图纸打包开始->图纸审核DOC");
                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                    orgId,
                    project,
                    actInstId,
                    operatorDTO,
                    true,
                    dwg,
                    execResult.getProcessId(),
                    drawingDetail);

                logger.info("图纸打包结束->图纸审核DOC");

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


                Set<String> subNos = subDrawingRepository.findByOrgIdAndProjectIdAndActInstIdAndStatusAndDrawingId(orgId, project.getId(), actInstId, EntityStatus.PENDING.name(), dwg.getId());

                if (!CollectionUtils.isEmpty(subNos)) {

                    if (execResult.getActInst().getProcess().equals(BpmCode.DRAWING_INTEGRAL_UPDATE)) {

                        subDrawingRepository.updateStatusByDrawingId(orgId, project.getId(), EntityStatus.DELETED, dwg.getId());
                    } else {

                        subDrawingRepository.updateStatusDeletedByDrawingIdAndSubDrawingNoInAndStatus(orgId, project.getId(), EntityStatus.DELETED, dwg.getId(), subNos, EntityStatus.ACTIVE);
                    }
                }


                subDrawingRepository.updateStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, project.getId(), EntityStatus.ACTIVE, dwg.getId(), actInstId, dwg.getLatestRev());

                subDrawingHisRepository.updateIssuedByDrawingIdAndDrawingVersion(orgId, project.getId(), true, dwg.getId(), dwg.getLatestRev());


                drawingDetailRepository.updateStatusByDrawingIdAndStatus(orgId, project.getId(), dwg.getId(), EntityStatus.SUSPEND, EntityStatus.ACTIVE);


                drawingDetailRepository.updateStatusByDrawingIdAndDrawingVersionAndStatus(orgId, project.getId(), dwg.getId(), dwg.getLatestRev(), EntityStatus.ACTIVE, EntityStatus.PENDING);

                logger.info("有效图纸打包开始->图纸审核DOC");
                drawingBaseService.packEffectiveSubFiles(
                    orgId,
                    project,
                    operatorDTO,
                    dwg);
                logger.info("有效图纸打包结束->图纸审核DOC");
                dwg.setLocked(true);
                dwg.setLatestApprovedRev(dwg.getLatestRev());
                drawingRepository.save(dwg);
                List<Long> tasksIds = new ArrayList<>();
                for (BpmRuTask ruTask : ruTasks) {
                    tasksIds.add(ruTask.getId());
                }

                logger.info("更改待办任务状态为可执行，保存开始 ->图纸审核 " + tasksIds);
                ruTaskRepository.updateRunningStatus(
                    false,
                    tasksIds
                );
                logger.info("更改待办任务状态为可执行，保存完成 ->图纸审核" + tasksIds);

                return new BatchResultDTO();
            }
        );
    }

}

