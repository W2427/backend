package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.drawing.DrawingPackageReturnDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.*;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;

/**
 * 检查 节点 代理。
 */
@Component
public class DrawingModifiyDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(DrawingModifiyDelegate.class);

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final ProjectInterface projectService;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BatchTaskInterface batchTaskService;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final DrawingRepository drawingRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final EntitySubTypeInterface entitySubTypeService;
    /**
     * 构造方法。
     */
    @Autowired
    public DrawingModifiyDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                  BpmRuTaskRepository ruTaskRepository,
                                  SubDrawingRepository subDrawingRepository,
                                  DrawingBaseInterface drawingBaseService,
                                  ProjectInterface projectService,
                                  DrawingDetailRepository drawingDetailRepository,
                                  BatchTaskInterface batchTaskService,
                                  BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                                  DrawingHistoryRepository drawingHistoryRepository,
                                  StringRedisTemplate stringRedisTemplate,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                  DrawingRepository drawingRepository, EntitySubTypeInterface entitySubTypeService) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.subDrawingRepository = subDrawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.projectService = projectService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.batchTaskService = batchTaskService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.drawingRepository = drawingRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.entitySubTypeService = entitySubTypeService;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Map<String, Object> variables = execResult.getVariables();
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");

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

        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());

        if (best != null && best.isSubDrawingFlg()) {
            Tuple tuple = subDrawingRepository.
                findNodeModifyStatusCount(orgId, projectId, dwg.getId(), dwg.getLatestRev());

            if (tuple == null) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("不存在修改图纸");
                return execResult;
            }

            Long totalCount = ((BigDecimal) tuple.get("totalCount")).longValue();
            Long nodeModifyCount = ((BigDecimal) tuple.get("modifyCount")).longValue();

            if (totalCount == 0L) {
                execResult.setExecResult(false);
                execResult.setErrorDesc("不存在修改图纸");
                return execResult;
            } else if (nodeModifyCount > 0L) {

                execResult.setExecResult(false);
                execResult.setErrorDesc("Some sub drawing not MODIFIED");
                return execResult;
            }
        }

        return execResult;
    }

    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Map<String, Object> variables = execResult.getVariables();
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");
        Long actInstId = execResult.getRuTask().getActInstId();

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        Project project = projectService.get(orgId, projectId);
        Drawing dwg = (Drawing) variables.get("drawing");


        DrawingDetail drawingDetail = null;

        List<DrawingDetail> ops = drawingDetailRepository.findByDrawingIdAndRevNoAndStatus(dwg.getId(), dwg.getLatestRev(), EntityStatus.PENDING);
        if (ops.size() > 0) {
            drawingDetail = ops.get(0);
        } else {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸详情不存在");
            return execResult;
        }
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());

        if (best!=null && best.isSubDrawingFlg()) {
            List<ExecResultDTO> nextTasks = execResult.getNextTasks();
            List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
            List<BpmRuTask> ruTasks = new ArrayList<>();

            nextTasks.forEach(nextTask -> {
                ruTasks.add(nextTask.getRuTask());
            });
            for (BpmRuTask ruTask : ruTasks) {
                BpmRuTask bpmRuTaskOriginal = ruTaskRepository.findById(ruTask.getId()).orElse(null);
                bpmRuTaskOriginal.setHandling(true);
                bpmRuTaskList.add(bpmRuTaskOriginal);
                logger.info("更改待办任务状态为处理中 ->修改 ");
            }
            ruTaskRepository.saveAll(bpmRuTaskList);
            packSubFiles(orgId, project, actInstId, operatorDTO, dwg, execResult, drawingDetail, contextDTO);
        } else {
            DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packMonoFiles(orgId, project, operatorDTO, false, dwg, execResult.getProcessId(), execResult.getActInst().getId(), drawingDetail);

            if (drawingPackageReturnDTO.getBpmDoc() != null) {
                docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
            }

            if (drawingPackageReturnDTO.getDrawing() != null) {
                drawingRepository.save(drawingPackageReturnDTO.getDrawing());
            }

            if (drawingPackageReturnDTO.getDrawingDetail() != null) {
                drawingDetailRepository.save(drawingPackageReturnDTO.getDrawingDetail());
            }


            if (drawingPackageReturnDTO.getDrawingHisList() != null && drawingPackageReturnDTO.getDrawingHisList().size() > 0) {
                drawingHistoryRepository.saveAll(drawingPackageReturnDTO.getDrawingHisList());
            }
        }
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
        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
        List<BpmRuTask> ruTasks = new ArrayList<>();
        nextTasks.forEach(nextTask -> {
            ruTasks.add(nextTask.getRuTask());
        });
        logger.info("批处理打包开始 ->修改 ");
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

                logger.info("批处理打包开始 ->修改DOC ");
                Set<DrawingReviewStatus> inStatus = new HashSet<>();
                inStatus.add(DrawingReviewStatus.INIT);
                subDrawingRepository.updateReviewStatusByDrawingIdAndDrawingVersion(
                    orgId,
                    project.getId(),
                    DrawingReviewStatus.CHECK,
                    dwg.getId(),
                    dwg.getLatestRev(),
                    inStatus
                );
                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                    orgId,
                    project,
                    actInstId,
                    operatorDTO,
                    false,
                    dwg,
                    execResult.getProcessId(),
                    drawingDetail);
                logger.info("批处理打包完成 ->修改DOC ");

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
                List<Long> tasksIds = new ArrayList<>();
                for (BpmRuTask ruTask : ruTasks) {
                    tasksIds.add(ruTask.getId());
                }
                logger.info("更改待办任务状态为处理中，保存开始 ->修改图纸 " + tasksIds);
                ruTaskRepository.updateRunningStatus(
                    false,
                    tasksIds
                );
                logger.info("更改待办任务状态为处理中，保存完成 ->修改图纸" + tasksIds);
                return new BatchResultDTO();
            }
        );
    }
}
