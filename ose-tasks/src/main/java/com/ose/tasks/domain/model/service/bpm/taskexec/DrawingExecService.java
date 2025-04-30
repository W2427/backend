package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.PageDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageBasicRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.bpm.*;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingInterface;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.dto.drawing.DrawingFileDTO;
import com.ose.tasks.dto.drawing.SubDrawingCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.util.CryptoUtils;
import com.ose.util.LongUtils;
import com.ose.util.PdfUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * Ndt任务执行类。
 */
@Component
public class DrawingExecService extends TaskExecService implements DrawingExecInterface {

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;


    private final static Logger logger = LoggerFactory.getLogger(DrawingExecService.class);

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final DrawingDetailRepository drawingDetailRepository;

    private final DrawingRepository drawingRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final SubDrawingInterface subDrawingService;

    private final DrawingInterface drawingService;

    private final DrawingBaseInterface drawingBaseService;

    private final EntitySubTypeInterface entitySubTypeService;


    /**
     * 构造方法。
     */
    @Autowired
    public DrawingExecService(
        BpmRuTaskRepository ruTaskRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
        SubDrawingRepository subDrawingRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        DrawingDetailRepository drawingDetailRepository,
        DrawingRepository drawingRepository,
        BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        TaskPackageBasicRepository taskPackageBasicRepository,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        ActTaskConfigInterface actTaskConfigService,
        TaskRuleCheckService taskRuleCheckService,
        BpmHiTaskinstRepository hiTaskinstRepository,
        BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmActTaskRepository bpmActTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        ActivityTaskInterface activityTaskService,
        SubDrawingInterface subDrawingService, DrawingInterface drawingService, DrawingBaseInterface drawingBaseService,
        ProcessInterface processService,
        BpmExecInterface bpmExecService,
        WBSEntryStateRepository wbsEntryStateRepository, EntitySubTypeInterface entitySubTypeService) {

        super(todoTaskBaseService,
            ruTaskRepository, bpmExecService, uploadFeignAPI, bpmActivityInstanceBlobRepository,
            bpmActivityInstanceStateRepository, taskPackageBasicRepository,
            entitySubTypeRepository, actTaskConfigService, taskRuleCheckService,
            hiTaskinstRepository, bpmActTaskRepository, bpmActInstRepository, activityTaskService, processService,
            wbsEntryStateRepository);


        this.ruTaskRepository = ruTaskRepository;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingDetailRepository = drawingDetailRepository;
        this.drawingRepository = drawingRepository;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.subDrawingService = subDrawingService;
        this.drawingService = drawingService;
        this.drawingBaseService = drawingBaseService;
        this.entitySubTypeService = entitySubTypeService;
    }


    @Override
    public void modifySubDrawingReviewStatus(
        BpmActivityInstanceBase actInst,
        TodoTaskExecuteDTO toDoTaskDTO
    ) {
        PageDTO page = new PageDTO();
        page.setPage(new PageDTO.Page(1, Integer.MAX_VALUE));
        SubDrawingCriteriaDTO criteriaDTO = new SubDrawingCriteriaDTO();
        criteriaDTO.setDrawingVersion(actInst.getVersion());
        criteriaDTO.setStatus(EntityStatus.ACTIVE);
        Page<SubDrawing> list = subDrawingService.getList(
            actInst.getOrgId(),
            actInst.getProjectId(),
            actInst.getEntityId(),
            page,
            criteriaDTO
        );

        if (
            toDoTaskDTO.getCommand() != null
                && !toDoTaskDTO.getCommand().isEmpty()
        ) {
            if (!toDoTaskDTO.getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT)) {
                //校对节点如果不是驳回,状态改为REVIEW
                for (SubDrawing sub : list.getContent()) {
                    if (
                        sub.getReviewStatus() == DrawingReviewStatus.CHECK_DONE
                    ) {
                        sub.setReviewStatus(DrawingReviewStatus.REVIEW);
                        subDrawingRepository.save(sub);
                    }
                }
            }
        }

    }

    @Override
    public boolean signAssign(Long taskId, Long userid, TodoTaskExecuteDTO toDoTaskDTO,
                              BpmActivityInstanceBase actInst
    ) {
        //0. 根据task_id 和 代办人ID找到 图纸会签表中的任务
        BpmDrawingSignTask sign = bpmDrawingSignTaskRepository.findByTaskIdAndAssignee(taskId, userid);
        if (sign != null) {
            //1. 如果存在任务，则把此会签任务设定为 完成
            sign.setCommand((String) toDoTaskDTO.getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT));
            sign.setComment(toDoTaskDTO.getComment());
            sign.setFinished(true);
            sign.setLastModifiedAt();
            bpmDrawingSignTaskRepository.save(sign);

            //1.1. 查找当前运行任务表中的任务，根据 task_id和代办人ID
            BpmRuTask bpmRuTask = ruTaskRepository.findByIdAndAssignee(taskId, userid.toString());
            //1.2 如果存在，则移除此运行任务
            if (bpmRuTask != null) {
                ruTaskRepository.delete(bpmRuTask);
            }

            String comment = "";
            //2.0 根据任务id查找 和 完成状态，查找当前已经完成的任务
            List<BpmDrawingSignTask> finishSignTasks = bpmDrawingSignTaskRepository.findByTaskIdAndFinished(taskId,
                true);
            //2.1 设定 TODOTaskDTO中 任务（对应任务ID）的备注 comment
            for (BpmDrawingSignTask fst : finishSignTasks) {
                comment += fst.getComment() + "-" + fst.getAssigneeName() + ":" + fst.getCommand() + "\r\n";
            }
            toDoTaskDTO.setComment(comment);

            //3.0 查找 会签任务表中的 未完成的会签任务
            List<BpmDrawingSignTask> signTasks = bpmDrawingSignTaskRepository.findByTaskIdAndFinished(taskId,
                false);
            //3.1 如果会签任务为空，如果选择的 网关为 拒签，设定 会签 网关 为拒签
            if (signTasks.isEmpty()) {
                finishSignTasks = bpmDrawingSignTaskRepository.findByTaskIdAndFinished(taskId, true);
                for (BpmDrawingSignTask s : finishSignTasks) {
                    if (s.getCommand().equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT)) {
                        Map<String, Object> command = new HashMap<>();
                        command.put(BpmCode.EXCLUSIVE_GATEWAY_RESULT, BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT);
                        toDoTaskDTO.setCommand(command);
                    }
                }
                //3.2 会签驳回时设置子图纸校审状态
                if (toDoTaskDTO.getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT).equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT)) {
                    List<SubDrawing> subPipingList = drawingBaseService.findSubByDrawingId(actInst.getEntityId());
                    for (SubDrawing sub : subPipingList) {
                        sub.setReviewStatus(DrawingReviewStatus.INIT);
                        subDrawingRepository.save(sub);
                    }
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean drawingRemark(
        Long orgId,
        Long projectId,
        BpmActivityInstanceBase actInst,
        BpmRuTask bpmRuTask,
        Long userId,
        Project project
    ) {

        List<SubDrawing> redmarkList = subDrawingRepository.findByProjectIdAndDrawingIdAndIsIssuedAndStatusAndActInstId(
            actInst.getProjectId(), actInst.getEntityId(), false, EntityStatus.ACTIVE, actInst.getId());

        if (!redmarkList.isEmpty()) {

            List<ActReportDTO> documents = new ArrayList<>();

            for (SubDrawing subDrawing : redmarkList) {
                ActReportDTO fileDTO;
                try {
                    fileDTO = subDrawingService.setQrCodeIntoPdfFile(
                        orgId, projectId, subDrawing.getQrCode(), subDrawing.getFilePath(), subDrawing.getFileName(), 50, 50, 75);
                    documents.add(fileDTO);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }

            List<SubDrawing> oldList = subDrawingRepository.findByProjectIdAndDrawingIdAndIsIssuedAndStatusAndActInstId(
                actInst.getProjectId(), actInst.getEntityId(), true, EntityStatus.ACTIVE, actInst.getId());

            for (SubDrawing sub : oldList) {
                sub.setStatus(EntityStatus.DELETED);
                subDrawingRepository.save(sub);
            }

            for (SubDrawing sub : redmarkList) {
                sub.setIssued(true);
                subDrawingRepository.save(sub);
            }

            if (documents.size() > 1) {

                String[] files = new String[documents.size()];
                for (int i = 0; i < documents.size(); i++) {
                    files[i] = protectedDir + documents.get(i).getFilePath().substring(1);
                }

                String savepath = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
                try {

                    PdfUtils.mergePdfFiles(files, savepath);

                    File diskMergeFile = new File(savepath);
                    System.out.println(diskMergeFile.getName());
                    DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                        MediaType.APPLICATION_PDF_VALUE, true, diskMergeFile.getName());

                    IOUtils.copy(new FileInputStream(diskMergeFile), fileItem.getOutputStream());

                    // 将文件上传到文档服务器
                    logger.error("图纸1 上传docs服务->开始");
                    MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                            APPLICATION_PDF_VALUE, fileItem.getInputStream());
                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                        .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                    logger.error("图纸1 上传docs服务->结束");
                    logger.error("图纸1 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        tempFileResBody.getData().getName(), new FilePostDTO());
                    logger.error("图纸1 保存docs服务->结束");
                    FileES mergeFile = fileESResBody.getData();

                    documents.clear();
                    ActReportDTO report = new ActReportDTO();
                    report.setFileId(LongUtils.parseLong(mergeFile.getId()));
                    report.setFilePath(mergeFile.getPath());
                    documents.add(report);
                    bpmRuTask.setJsonDocuments(documents);
                    ruTaskRepository.save(bpmRuTask);

                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            } else {
                bpmRuTask.setJsonDocuments(documents);
            }
            ruTaskRepository.save(bpmRuTask);

            Drawing dl = drawingRepository.findById(actInst.getEntityId()).orElse(null);
            if (dl == null) {
                return false;
            }
            DrawingFileDTO dto = null;
            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dl.getEntitySubType());

            switch (best.getNameEn()) {
                case BpmCode.SD_DWG_PIPE_FABRICATION:
                    dto = drawingService.generateReportPipe(orgId, projectId, dl, project, userId, true, true);
                    break;
                case BpmCode.SD_DWG_PIPE_SUPPORT_FABRICATION:
                    dto = drawingService.generateReportPipeSupport(orgId, projectId, dl, project, userId, true, true);
                    break;
            }

            DrawingDetail drawingDetail = drawingDetailRepository.findByDrawingIdAndRevAndStatus(dl.getId(), dl.getLatestRev(), EntityStatus.ACTIVE).orElse(null);
            if (drawingDetail != null && dto != null) {
                drawingDetail.setIssueFileId(dto.getFileId());
                drawingDetail.setIssueFileName(dto.getFileName());
                drawingDetail.setIssueFilePath(dto.getFilePath());
                drawingDetail.setQrCode(dto.getQrCode());
                drawingDetail.setProjectId(projectId);
                drawingDetail.setOrgId(orgId);
                drawingDetailRepository.save(drawingDetail);
            }

            BpmEntityDocsMaterials bpmDoc = docsMaterialsRepository.findByEntityNoAndType(actInst.getEntityNo(), ActInstDocType.DESIGN_DRAWING);
            if (bpmDoc == null) {
                bpmDoc = new BpmEntityDocsMaterials();
                bpmDoc.setProjectId(actInst.getProjectId());
                bpmDoc.setCreatedAt();
                bpmDoc.setStatus(EntityStatus.ACTIVE);
            }
            bpmDoc.setEntityNo(actInst.getEntityNo());
            bpmDoc.setProcessId(actInst.getProcessId());
            bpmDoc.setEntityId(actInst.getEntityId());
            bpmDoc.setActInstanceId(actInst.getId());
            bpmDoc.setType(ActInstDocType.DESIGN_DRAWING);

            List<ActReportDTO> list = new ArrayList<>();
            ActReportDTO reportDto = new ActReportDTO();
            if (dto != null) {
                reportDto.setFileId(dto.getFileId());
                reportDto.setReportQrCode(dto.getQrCode());
                reportDto.setFilePath(dto.getFilePath());
            }
            list.add(reportDto);
            bpmDoc.setJsonDocs(list);

            docsMaterialsRepository.save(bpmDoc);

        }

        return true;
    }

    /**
     * RED-MARK 流程 并且节点为 USER-DRAWING-DESIGN，取得图纸信息。
     */
    @Override
    public List<ActReportDTO> getJsonDocument(ExecResultDTO execResult) {

        BpmActivityInstanceBase actInst = execResult.getActInst();
        Long projectId = execResult.getProjectId();
        Long orgId = execResult.getOrgId();

        List<ActReportDTO> documents = new ArrayList<>();


        List<SubDrawing> result = subDrawingRepository.
            findByDrawingIdAndCommentAndActInstIdAndStatus(projectId, orgId, actInst.getEntityId(), actInst.getId());
        String[] files = new String[result.size()];

        for (int i = 0; i < result.size(); i++) {
            SubDrawing sub = result.get(i);
            Optional<DrawingDetail> opD = drawingDetailRepository
                .findById(sub.getDrawingDetailId());
            if (opD.isPresent()) {
                sub.setDrawingVersion(opD.get().getRev());
            }
            files[i] = protectedDir + sub.getFilePath().substring(1);
        }
        String savepath = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
        try {

            PdfUtils.mergePdfFiles(files, savepath);

            File diskMergeFile = new File(savepath);
            System.out.println(diskMergeFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskMergeFile.getName());

            IOUtils.copy(new FileInputStream(diskMergeFile), fileItem.getOutputStream());

            // 将文件上传到文档服务器
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);

            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            FileES mergeFile = fileESResBody.getData();

            ActReportDTO report = new ActReportDTO();
            report.setFileId(LongUtils.parseLong(mergeFile.getId()));
            report.setFilePath(mergeFile.getPath());
            documents.add(report);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
//            execResult.getVariables().put("jsonDocuments", documents);
        return documents;
    }

}
