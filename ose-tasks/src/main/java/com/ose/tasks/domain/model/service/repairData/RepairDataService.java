package com.ose.tasks.domain.model.service.repairData;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.DrawingPlanHourRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.bpm.ActInstSuspendDTO;
import com.ose.tasks.dto.rapairData.RepairBatchRevocationNodeDTO;
import com.ose.tasks.dto.rapairData.RepairBatchRevocationNodesDTO;
import com.ose.tasks.dto.rapairData.UploadFileDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.entity.drawing.DrawingPlanHour;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.rtfparserkit.rtf.Command.category;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component
@Lazy
public class RepairDataService extends StringRedisService implements RepairDataInterface {

    private final DrawingFileRepository drawingFileRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final ActivityTaskInterface activityTaskService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final DrawingRepository drawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final DrawingPlanHourRepository drawingPlanHourRepository;

    private final SubDrawingRepository subDrawingRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Autowired
    @Lazy
    public RepairDataService(
        StringRedisTemplate stringRedisTemplate,
        DrawingFileRepository drawingFileRepository,
        UploadFeignAPI uploadFeignAPI,
        ActivityTaskInterface activityTaskService,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        TodoTaskDispatchInterface todoTaskDispatchService,
        DrawingRepository drawingRepository,
        DrawingDetailRepository drawingDetailRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingPlanHourRepository drawingPlanHourRepository
    ) {
        super(stringRedisTemplate);
        this.drawingFileRepository = drawingFileRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.activityTaskService = activityTaskService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.drawingRepository = drawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingPlanHourRepository = drawingPlanHourRepository;
    }

    @Override
    public void repairPath(
        Long orgId,
        Long projectId,
        UploadFileDTO uploadFileDTO
    ) throws IOException {
        List<DrawingFile> fileList = drawingFileRepository.findByProjectIdAndStatus(projectId, EntityStatus.ACTIVE);
        if (fileList.size() > 0) {
            for (DrawingFile drawingFile : fileList) {
                String filePath = protectedDir + drawingFile.getFilePath();
                File diskFile = new File(filePath);
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());
                // 再次将文件上传到文档服务器
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);

                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                FileES fileES = fileESResBody.getData();

                drawingFile.setFileId(LongUtils.parseLong(fileES.getId()));
                drawingFile.setFilePath(fileES.getPath());
                drawingFileRepository.save(drawingFile);
            }
        }
    }

    @Override
    public void batchRevocationNode(
        Long orgId,
        Long projectId,
        RepairBatchRevocationNodesDTO repairBatchRevocationNodesDTO,
        ContextDTO contextDTO
    ) {
        if (repairBatchRevocationNodesDTO.getRepairBatchRevocationNodeDTOs() != null && repairBatchRevocationNodesDTO.getRepairBatchRevocationNodeDTOs().size() > 0) {

            for (RepairBatchRevocationNodeDTO repairBatchRevocationNodeDTO : repairBatchRevocationNodesDTO.getRepairBatchRevocationNodeDTOs()) {

                BpmActivityInstanceBase actInst = activityTaskService.findActInstById(repairBatchRevocationNodeDTO.getActInstId());

                if (actInst == null) {
                    throw new ValidationError("Not found activity instance");
                }

                BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(repairBatchRevocationNodeDTO.getActInstId());
                Long actInstId = actInst.getId();

                List<BpmRuTask> currentRuTasks = activityTaskService.findBpmRuTaskByActInstId(actInstId);
//                if (currentRuTasks.size() > 1) {
//                    throw new BusinessError("正在执行任务数量大于1，不能进行撤回操作");
//                }
                ActInstSuspendDTO actInstSuspendDTO = new ActInstSuspendDTO();
                actInstSuspendDTO.setActInst(actInst);
                actInstSuspendDTO.setActInstState(actInstState);
                actInstSuspendDTO.setTaskDefKey(repairBatchRevocationNodeDTO.getTaskDefKey());
                Boolean revocationDTO = todoTaskDispatchService.revocationAny(contextDTO, orgId, projectId, actInstSuspendDTO);
                if (revocationDTO.equals(true)) {
                    repairDrawingTask(actInst);
                }

            }
        }
    }


    /**
     * 修改图纸任务到审核节点修改的图纸相关记录。
     */
    private Boolean repairDrawingTask(
        BpmActivityInstanceBase bpmActivityInstanceBase
    ) {


        Long orgId = bpmActivityInstanceBase.getOrgId();
        Long projectId = bpmActivityInstanceBase.getProjectId();
        String drawingVersion = bpmActivityInstanceBase.getVersion();
        Long drawingId = bpmActivityInstanceBase.getEntityId();


        Drawing drawing = drawingRepository.findById(drawingId).orElse(null);
        if (drawing == null) {
            return false;
        }
        drawing.setLocked(true);
//        drawing.setLatestRev(drawingVersion);
        drawingRepository.save(drawing);

//        BpmEntityCategory bpmEntityCategory = bpmEntityCategoryRepository.findById(
//            bpmActivityInstanceBase.getEntityCategoryId()
//        ).orElse(null);
//        if (bpmEntityCategory == null) {
//            return false;
//        }
//        Boolean subDrawingFlag = bpmEntityCategory.isSubDrawingFlg();


        DrawingDetail drawingDetail = drawingDetailRepository.findByDrawingIdAndRev(
            drawingId,
            drawingVersion
        ).orElse(null);
        if (drawingDetail == null) {
            return false;
        }
        drawingDetail.setStatus(EntityStatus.PENDING);
        drawingDetailRepository.save(drawingDetail);


//        if (subDrawingFlag != null && subDrawingFlag.equals(true)) {
//            List<SubDrawing> subDrawingList = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndProcInstId(
//                orgId,
//                projectId,
//                drawingId,
//                bpmActivityInstanceBase.getProcInstId()
//            );
//            if (subDrawingList == null || subDrawingList.size() == 0) {
//                return false;
//            }
//            for (SubDrawing subDrawing : subDrawingList) {
//                subDrawing.setStatus(EntityStatus.PENDING);
//                subDrawing.setReviewStatus(DrawingReviewStatus.REVIEW);
//                subDrawingRepository.save(subDrawing);
//            }
//
//        }
        return true;
    }

    @Override
    public void batchRepairDrawingPlanHour() {
        List<Drawing> drawings = drawingRepository.findAllPlanHourDrawing();
        for (Drawing drawing : drawings){
            Date engineeringStartDate = drawing.getEngineeringStartDate();
            Date engineeringFinishDate = drawing.getEngineeringFinishDate();

            List<Integer> months = new ArrayList<>();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(engineeringStartDate);
            startCal.set(Calendar.DAY_OF_MONTH, 1); // 设置为当月1号
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(engineeringFinishDate);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            while (startCal.before(endCal) || startCal.equals(endCal)) {
                months.add(Integer.valueOf(sdf.format(startCal.getTime())));
                startCal.add(Calendar.MONTH, 1);
            }

            Long drawingId = drawing.getId();
            Long projectId = drawing.getProjectId();

            Map<String, Long> userMap = new HashMap<>();

            String design = "DESIGN_ENGINEER_EXECUTE";
            Long preparePersonId = drawing.getPreparePersonId();
            if (preparePersonId != null){
            userMap.put(design, preparePersonId);
            }

            String review = "DRAWING_REVIEW_EXECUTE";
            Long reviewPersonId = drawing.getReviewPersonId();
            if (reviewPersonId != null) {
                userMap.put(review, reviewPersonId);
            }

            String approve = "DRAWING_APPROVE_EXECUTE";
            Long approvePersonId = drawing.getApprovePersonId();
            if (approvePersonId != null) {
                userMap.put(approve, approvePersonId);
            }

            if (months.isEmpty()){
                continue;
            }

            userMap.forEach((category, userId) -> {
                //设校审631比例
                Double proportion = 0.0;
                if ("DESIGN_ENGINEER_EXECUTE".equals(category)) {
                    proportion = 0.6;
                } else if ("DRAWING_REVIEW_EXECUTE".equals(category)) {
                    proportion = 0.3;
                }else if ("DRAWING_APPROVE_EXECUTE".equals(category)){
                    proportion = 0.1;
                }

                Double planHours = BigDecimal.valueOf((drawing.getPlanHours() / (double) months.size()) * proportion)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();


                for (Integer month : months) {
                    DrawingPlanHour drawingPlanHour = drawingPlanHourRepository.findByProjectIdAndDrawingIdAndPrivilegeAndMonthly(projectId, drawingId, category, month);
                    if (drawingPlanHour == null){
                        drawingPlanHour = new DrawingPlanHour();
                    }
                    drawingPlanHour.setDrawingId(drawingId);
                    drawingPlanHour.setProjectId(projectId);
                    drawingPlanHour.setPrivilege(category);
                    drawingPlanHour.setUserId(userId);
                    drawingPlanHour.setMonthly(month);
                    drawingPlanHour.setHours(planHours);

                    drawingPlanHourRepository.save(drawingPlanHour);
                }

            });
        }
    }
}
