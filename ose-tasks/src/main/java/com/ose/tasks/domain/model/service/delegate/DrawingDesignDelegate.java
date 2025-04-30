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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ose.tasks.vo.setting.BatchTaskCode.DRAWING_PACKAGE;

/**
 * 图纸设计 节点 代理。
 */
@Component
public class DrawingDesignDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(DrawingDesignDelegate.class);

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final ProjectInterface projectService;

    private final DrawingDetailRepository drawingDetailRepository;

    private final EntitySubTypeInterface entitySubTypeService;
    private final BatchTaskInterface batchTaskService;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingDesignDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                 DrawingRepository drawingRepository,
                                 BpmRuTaskRepository ruTaskRepository,
                                 SubDrawingRepository subDrawingRepository,
                                 DrawingBaseInterface drawingBaseService,
                                 ProjectInterface projectService,
                                 DrawingDetailRepository drawingDetailRepository,
                                 BatchTaskInterface batchTaskService,
                                 BpmEntityDocsMaterialsRepository docsMaterialsRepository,
                                 StringRedisTemplate stringRedisTemplate,
                                 BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                 EntitySubTypeInterface entitySubTypeService, DrawingHistoryRepository drawingHistoryRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingBaseService = drawingBaseService;
        this.projectService = projectService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.batchTaskService = batchTaskService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.ruTaskRepository = ruTaskRepository;
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

        Map<String, Object> variables = (Map<String, Object>) data.get("variables");
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        Project project = projectService.get(orgId, projectId);
        Long actInstId = execResult.getRuTask().getActInstId();
        DrawingDetail drawingDetail = null;


        if (variables == null) {
            execResult = drawingBaseService.getDrawingAndLatestRev(execResult);
            variables = execResult.getVariables();
            if (!execResult.isExecResult()) return execResult;
        }

        Drawing dwg = (Drawing) variables.get("drawing");
        OperatorDTO operatorDTO = (OperatorDTO) data.get("operator");


        if (dwg == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸不存在");
            return execResult;
        }


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
                logger.info("更改待办任务状态为处理中->设计");
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

        dwg.setLocked(true);
        drawingRepository.save(dwg);

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
        logger.info("打包图纸开始->设计");
        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        List<BpmRuTask> bpmRuTaskList = new ArrayList<>();
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

                logger.info("图纸批处理打包开始->设计DOC");
                DrawingPackageReturnDTO drawingPackageReturnDTO = drawingBaseService.packSubFiles(
                    orgId,
                    project,
                    actInstId,
                    operatorDTO,
                    false,
                    dwg,
                    execResult.getProcessId(),
                    drawingDetail);
                logger.info("图纸批处理打包完成->设计DOC");

                if (drawingPackageReturnDTO.getBpmDoc() != null) {
                    docsMaterialsRepository.save(drawingPackageReturnDTO.getBpmDoc());
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
                    updateReviewStatusByDrawingIdAndActInstIdAndDrawingVersion(orgId, project.getId(), DrawingReviewStatus.CHECK, dwg.getId(), actInstId, dwg.getLatestRev());

                List<Long> tasksIds = new ArrayList<>();
                for (BpmRuTask ruTask : ruTasks) {
                    tasksIds.add(ruTask.getId());
                }
                logger.info("更改待办任务状态为处理中，保存开始 ->设计 " + tasksIds);
                ruTaskRepository.updateRunningStatus(
                    false,
                    tasksIds
                );
                logger.info("更改待办任务状态为处理中，保存完成 ->设计" + tasksIds);
                return new BatchResultDTO();
            }
        );
    }
}
