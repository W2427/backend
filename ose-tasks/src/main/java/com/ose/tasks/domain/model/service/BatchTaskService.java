package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.feign.RequestWrapper;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.BatchConstructTaskRepository;
import com.ose.tasks.domain.model.repository.BatchTaskBasicRepository;
import com.ose.tasks.domain.model.repository.BatchTaskDrawingRepository;
import com.ose.tasks.domain.model.repository.BatchTaskRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.dto.BatchTaskThreadPoolDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingHistory;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import com.ose.util.FileUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.RedisKey;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 批处理任务管理服务。
 */
@Component
public class BatchTaskService extends StringRedisService implements BatchTaskInterface {


    private final BatchTaskRepository batchTaskRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 导出Excel数据输出开始行
    private static final int DATA_START_ROW = 3;

    private final SubSystemEntityRepository subSystemEntityRepository;

    private final BatchTaskBasicRepository batchTaskBasicRepository;

    private final BatchTaskDrawingRepository batchTaskDrawingRepository;

    private final BatchConstructTaskRepository batchConstructTaskRepository;

    @Resource
    private ThreadPoolTaskScheduler taskExecutor;

    @Resource
    private ThreadPoolTaskScheduler drawingTaskExecutor;

    @Resource
    private ThreadPoolTaskScheduler constructTaskExecutor;

    /**
     * 构造方法。
     */
    @Autowired
    public BatchTaskService(
        BatchTaskRepository batchTaskRepository,
        SubSystemEntityRepository subSystemEntityRepository,
        BatchTaskBasicRepository batchTaskBasicRepository,
        BatchTaskDrawingRepository batchTaskDrawingRepository,
        StringRedisTemplate stringRedisTemplate,
        BatchConstructTaskRepository batchConstructTaskRepository) {
        super(stringRedisTemplate);
        this.batchTaskRepository = batchTaskRepository;
        this.subSystemEntityRepository = subSystemEntityRepository;
        this.batchTaskBasicRepository = batchTaskBasicRepository;
        this.batchTaskDrawingRepository = batchTaskDrawingRepository;
        this.batchConstructTaskRepository = batchConstructTaskRepository;
    }

    /**
     * 检查是否存在冲突的任务。
     *
     * @param projectId      项目 ID
     * @param batchTaskCodes 批处理任务代码列表
     * @return 批处理服务接口实例
     */
    @Override
    public BatchTaskInterface conflictWith(
        final Long projectId,
        final BatchTaskCode... batchTaskCodes
    ) {
        if (batchTaskBasicRepository
            .existsByProjectIdAndCodeInAndRunningIsTrue(projectId, batchTaskCodes)) {
            throw new ConflictError("error.batch-task.duplicate-task-is-running");
        }

        return this;
    }

    /**
     * 通用线程池任务。
     *
     * @param context       上下文对象
     * @param project       项目信息
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    @Override
    @Async("taskExecutor")
    public void run(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BatchTaskFunction function
    ) {

        if (!context.isContextSet()) {
//            String authorization = context.getAuthorization();
//            String userAgent = context.getUserAgent();
//            final RequestAttributes attributes = new ServletRequestAttributes(
//                new RequestWrapper(context.getRequest(), authorization, userAgent),
//                context.getResponse()
//            );
            // 创建一个新的 MockHttpServletRequest 而不是包装原始请求
            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.setMethod(context.getRequestMethod());
            mockRequest.addHeader("Authorization", context.getAuthorization());
            mockRequest.addHeader("User-Agent", context.getUserAgent());

            MockHttpServletResponse mockResponse = new MockHttpServletResponse();

            final RequestAttributes attributes = new ServletRequestAttributes(mockRequest, mockResponse);

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }


        final BatchTask batchTask
            = new BatchTask(context.getOperator(), project, batchTaskCode);


//        if (batchTaskBasicRepository.existsByProjectIdAndCodeAndRunningIsTrue(
//            project.getId(),
//            batchTask.getCode()
//        )) {
//            throw new ConflictError("error.batch-task.duplicate-task-is-running");
//        }


        int activeCount = taskExecutor.getScheduledThreadPoolExecutor().getActiveCount();
        Long taskCount = taskExecutor.getScheduledThreadPoolExecutor().getTaskCount();
        int poolSize = taskExecutor.getScheduledThreadPoolExecutor().getPoolSize();
        int corePoolSize = taskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize();
        int maximumPoolSize = taskExecutor.getScheduledThreadPoolExecutor().getMaximumPoolSize();
        Long completedTaskCount = taskExecutor.getScheduledThreadPoolExecutor().getCompletedTaskCount();
        int largestPoolSize = taskExecutor.getScheduledThreadPoolExecutor().getLargestPoolSize();

        System.out.println("当前线程池中activeCount: " + activeCount);
        System.out.println("当前线程池中taskCount: " + taskCount);
        System.out.println("当前线程池中poolSize: " + poolSize);
        System.out.println("当前线程池中corePoolSize: " + corePoolSize);
        System.out.println("当前线程池中maximumPoolSize: " + maximumPoolSize);
        System.out.println("当前线程池中completedTaskCount: " + completedTaskCount);
        System.out.println("当前线程池中largestPoolSize: " + largestPoolSize);

        batchTaskRepository.save(batchTask);
        if (BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE.equals(batchTaskCode)) {
            String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString());
            setRedisKey(statusKey, "HOT");
        }


        Thread t1 = new Thread(() -> {
            BatchResultDTO result = null;


            try {


                result = function.exec(batchTask);


                batchTask.setStatus(BatchTaskStatus.FINISHED);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("main interrupt error");


                batchTask.addError(e.getMessage());
                batchTask.setStatus(BatchTaskStatus.FAILED);
                if (BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE.equals(batchTaskCode)) {
                    String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString());
                    setRedisKey(statusKey, "COLD");
                }
            }


            batchTask.setResult(result);
            batchTask.setFinishedAt(new Date());
            batchTask.setRunning(false);
            if (BatchTaskCode.ENTITY_PROCESS_WBS_GENERATE.equals(batchTaskCode)) {
                String statusKey = String.format(RedisKey.PLAN_QUEUE_STATUS.getDisplayName(), project.getId().toString());
                setRedisKey(statusKey, "COLD");
            }


            batchTaskRepository.save(batchTask);

        });
        t1.start();


        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(15000);


                    BatchTaskBasic runningTask = batchTaskBasicRepository
                        .findById(batchTask.getId())
                        .orElse(null);

                    if (runningTask == null) {

                        t1.interrupt();
                        return;
                    }

                    if (runningTask.getStatus() != BatchTaskStatus.RUNNING) {
                        batchTask.setStatus(runningTask.getStatus());


                        t1.interrupt();
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.out);
            }
        });
        t2.start();
    }

    /**
     * 查询批处理任务。
     *
     * @param companyId            公司 ID
     * @param orgId                组织 ID
     * @param projectId            项目 ID
     * @param batchTaskCriteriaDTO 查询条件
     * @param pageable             分页参数
     * @return 批处理任务分页参数
     */
    @Override
    public Page<BatchTaskBasic> search(
        Long companyId,
        Long orgId,
        Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        Pageable pageable
    ) {
        return batchTaskBasicRepository.search(
            companyId,
            orgId,
            projectId,
            batchTaskCriteriaDTO,
            pageable
        );
    }

    /**
     * 取得批处理任务详细信息。
     *
     * @param orgId  组织 ID
     * @param taskId 批处理任务 ID
     * @return 批处理任务数据实体
     */
    @Override
    public BatchTask get(Long orgId, Long taskId) {
        return batchTaskRepository.findByIdAndOrgId(taskId, orgId).orElse(null);
    }

    /**
     * 手动停止批处理任务。
     *
     * @param orgId    组织 ID
     * @param operator 操作者信息
     * @param taskId   批处理任务 ID
     */
    @Override
    public void stop(OperatorDTO operator, Long orgId, Long taskId) {

        BatchTask task = get(orgId, taskId);

        if (task == null) {
            throw new NotFoundError();
        }

        if (!task.isRunning()) {
            return;
        }

        task.setStoppedBy(operator.getId());
        task.setRunning(false);
        task.setStatus(BatchTaskStatus.STOPPED);

        batchTaskRepository.save(task);
    }


    /**
     * 图纸流程线程池任务。
     *
     * @param context       上下文对象
     * @param project       项目信息
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    @Override
    @Async("drawingTaskExecutor")
    public void runDrawingPackage(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BpmActivityInstanceBase actInst,
        final BpmRuTask ruTask,
        final List<BpmRuTask> nextRuTasks,
        final Drawing drawing,
        final DrawingDetail drawingDetail,
        final DrawingHistory drawingHistory,
        final List<SubDrawing> subDrawing,
        final BpmEntityDocsMaterials docsMaterials,
        final BatchTaskDrawingFunction function
    ) {

        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }


        int corePoolSize = drawingTaskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize();
        if (corePoolSize > 100) {
            throw new ConflictError("线程池中的线程已满，请稍后尝试");
        }


        final BatchDrawingTask batchDrawingTask
            = new BatchDrawingTask(context.getOperator(), project, batchTaskCode);


        if (drawing != null && batchTaskDrawingRepository.existsByProjectIdAndCodeAndDrawingIdAndRunningIsTrue(
            project.getId(),
            batchDrawingTask.getCode(),
            drawing.getId()
        )) {
            throw new ConflictError("error.batch-task.duplicate-drawing-package-task-is-running");
        }

        if (drawing != null) {
            batchDrawingTask.setDrawingId(drawing.getId());
            batchDrawingTask.setDrawingNo(drawing.getDwgNo());
        }

        if (ruTask != null) {
            batchDrawingTask.setTaskDefKey(ruTask.getTaskDefKey());
            batchDrawingTask.setTaskDefKeyName(ruTask.getName());
            batchDrawingTask.setJsonRuTask(ruTask);
        }
        if (actInst != null) {
            batchDrawingTask.setJsonActInst(actInst);
        }

        if (nextRuTasks != null) {
            batchDrawingTask.setJsonNextRuTask(nextRuTasks);
        }

        if (drawingDetail != null) {
            batchDrawingTask.setJsonDrawingDetail(drawingDetail);
        }

        if (drawingHistory != null) {
            batchDrawingTask.setJsonDrawingHistory(drawingHistory);
        }

        if (docsMaterials != null) {
            batchDrawingTask.setJsonDocsMaterials(docsMaterials);
        }

        if (subDrawing != null) {
            batchDrawingTask.setJsonSubDrawing(subDrawing);
        }


        batchTaskDrawingRepository.save(batchDrawingTask);
        (new Thread(() -> {
            BatchResultDTO result = null;


            try {


                (new Thread(() -> {
                    try {
                        while (true) {
                            Thread.sleep(15000);

                            BatchDrawingTask runningTask = batchTaskDrawingRepository
                                .findById(batchDrawingTask.getId())
                                .orElse(null);

                            if (runningTask == null) {
                                return;
                            }

                            if (runningTask.getStatus() != BatchTaskStatus.RUNNING) {
                                batchDrawingTask.setStatus(runningTask.getStatus());
                                return;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                    }
                })).start();


                result = function.exec(batchDrawingTask);


                batchDrawingTask.setStatus(BatchTaskStatus.FINISHED);

            } catch (Exception e) {
                e.printStackTrace(System.out);


                batchDrawingTask.addError(e.getMessage());
                batchDrawingTask.setStatus(BatchTaskStatus.FAILED);
            }


            batchDrawingTask.setResult(result);
            batchDrawingTask.setFinishedAt(new Date());
            batchDrawingTask.setRunning(false);


            batchTaskDrawingRepository.save(batchDrawingTask);
        })).start();

    }

    @Override
    @Async("drawingTaskExecutor")
    public void runPdfPackage(
        final ContextDTO context,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final BatchTaskDrawingFunction function
    ) {

        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }


        int corePoolSize = drawingTaskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize();
        if (corePoolSize > 100) {
            throw new ConflictError("线程池中的线程已满，请稍后尝试");
        }


        final BatchDrawingTask batchDrawingTask
            = new BatchDrawingTask(context.getOperator(), project, batchTaskCode);

        batchTaskDrawingRepository.save(batchDrawingTask);
        (new Thread(() -> {
            BatchResultDTO result = null;
            try {
                (new Thread(() -> {
                    try {
                        while (true) {
                            Thread.sleep(15000);

                            BatchDrawingTask runningTask = batchTaskDrawingRepository
                                .findById(batchDrawingTask.getId())
                                .orElse(null);

                            if (runningTask == null) {
                                return;
                            }

                            if (runningTask.getStatus() != BatchTaskStatus.RUNNING) {
                                batchDrawingTask.setStatus(runningTask.getStatus());
                                return;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                    }
                })).start();


                result = function.exec(batchDrawingTask);


                batchDrawingTask.setStatus(BatchTaskStatus.FINISHED);

            } catch (Exception e) {
                e.printStackTrace(System.out);


                batchDrawingTask.addError(e.getMessage());
                batchDrawingTask.setStatus(BatchTaskStatus.FAILED);
            }


            batchDrawingTask.setResult(result);
            batchDrawingTask.setFinishedAt(new Date());
            batchDrawingTask.setRunning(false);


            batchTaskDrawingRepository.save(batchDrawingTask);
        })).start();

    }
    /**
     * 建造流程线程池任务。
     *
     * @param project
     * @param batchTaskCode
     * @param isQueue
     * @param context
     * @param function
     */
    @Override
    @Async("constructTaskExecutor")
    public void runConstructTaskExecutor(
        final BpmActivityInstanceBase bpmActivityInstanceBase,
        final Project project,
        final BatchTaskCode batchTaskCode,
        final Boolean isQueue,
        final ContextDTO context,
        final BatchConstructFunction function
    ) {

        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }


        int corePoolSize = constructTaskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize();
        if (corePoolSize > 100) {
            throw new ConflictError("线程池中的线程已满，请稍后尝试");
        }


        final BatchConstructTask batchConstructTask
            = new BatchConstructTask(context.getOperator(), project, batchTaskCode);

        if (bpmActivityInstanceBase != null) {
            batchConstructTask.setEntityId(bpmActivityInstanceBase.getEntityId());
            batchConstructTask.setEntityNo(bpmActivityInstanceBase.getEntityNo());
            batchConstructTask.setJsonActInst(bpmActivityInstanceBase);
        }


        batchConstructTaskRepository.save(batchConstructTask);

        (new Thread(() -> {
            BatchResultDTO result = null;


            try {


                (new Thread(() -> {
                    try {
                        while (true) {
                            Thread.sleep(15000);

                            BatchConstructTask runningTask = batchConstructTaskRepository
                                .findById(batchConstructTask.getId())
                                .orElse(null);

                            if (runningTask == null) {
                                return;
                            }

                            if (runningTask.getStatus() != BatchTaskStatus.RUNNING) {
                                batchConstructTask.setStatus(runningTask.getStatus());
                                return;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                    }
                })).start();


                result = function.exec(batchConstructTask);


                batchConstructTask.setStatus(BatchTaskStatus.FINISHED);

            } catch (Exception e) {
                e.printStackTrace(System.out);


                batchConstructTask.addError(e.getMessage());
                batchConstructTask.setStatus(BatchTaskStatus.FAILED);
            }


            batchConstructTask.setResult(result);
            batchConstructTask.setFinishedAt(new Date());
            batchConstructTask.setRunning(false);


            batchConstructTaskRepository.save(batchConstructTask);
        })).start();

    }

    /**
     * 通过线程池名称获取线程池中线程的数量
     */
    @Override
    public List<BatchTaskThreadPoolDTO> threadPoolTaskSchedulerCount(
    ) {
        List<BatchTaskThreadPoolDTO> result = new ArrayList<>();


        ThreadPoolExecutor taskThreadPoolExecutor = taskExecutor.getScheduledThreadPoolExecutor();

        Integer queueSize = taskExecutor.getScheduledThreadPoolExecutor().getQueue().size();

        Integer activeCount = taskExecutor.getScheduledThreadPoolExecutor().getActiveCount();

        long completedTaskCount = taskExecutor.getScheduledThreadPoolExecutor().getCompletedTaskCount();

        long taskCount = taskExecutor.getScheduledThreadPoolExecutor().getTaskCount();

        BatchTaskThreadPoolDTO batchTaskThreadPoolDTO = new BatchTaskThreadPoolDTO();
        batchTaskThreadPoolDTO.setName(taskExecutor.getThreadNamePrefix());
        batchTaskThreadPoolDTO.setQueueSize(queueSize);
        batchTaskThreadPoolDTO.setActiveCount(activeCount);
        batchTaskThreadPoolDTO.setCompletedTaskCount(completedTaskCount);
        batchTaskThreadPoolDTO.setTaskCount(taskCount);
        batchTaskThreadPoolDTO.setCorePoolSize(taskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize());
        result.add(batchTaskThreadPoolDTO);


        ThreadPoolExecutor drawingThreadPoolExecutor = drawingTaskExecutor.getScheduledThreadPoolExecutor();

        Integer drawingQueueSize = drawingTaskExecutor.getScheduledThreadPoolExecutor().getQueue().size();

        Integer drawingActiveCount = drawingTaskExecutor.getScheduledThreadPoolExecutor().getActiveCount();

        long drawingCompletedTaskCount = drawingTaskExecutor.getScheduledThreadPoolExecutor().getCompletedTaskCount();

        long drawingTaskCount = drawingTaskExecutor.getScheduledThreadPoolExecutor().getTaskCount();

        BatchTaskThreadPoolDTO batchDrawingTaskThreadPoolDTO = new BatchTaskThreadPoolDTO();
        batchDrawingTaskThreadPoolDTO.setName(drawingTaskExecutor.getThreadNamePrefix());
        batchDrawingTaskThreadPoolDTO.setQueueSize(drawingQueueSize);
        batchDrawingTaskThreadPoolDTO.setActiveCount(drawingActiveCount);
        batchDrawingTaskThreadPoolDTO.setCompletedTaskCount(drawingCompletedTaskCount);
        batchDrawingTaskThreadPoolDTO.setTaskCount(drawingTaskCount);
        batchTaskThreadPoolDTO.setCorePoolSize(drawingTaskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize());
        result.add(batchDrawingTaskThreadPoolDTO);


        ThreadPoolExecutor constructThreadPoolExecutor = constructTaskExecutor.getScheduledThreadPoolExecutor();


        Integer constructQueueSize = constructTaskExecutor.getScheduledThreadPoolExecutor().getQueue().size();

        Integer constructActiveCount = constructTaskExecutor.getScheduledThreadPoolExecutor().getActiveCount();

        long constructCompletedTaskCount = constructTaskExecutor.getScheduledThreadPoolExecutor().getCompletedTaskCount();

        long constructTaskCount = constructTaskExecutor.getScheduledThreadPoolExecutor().getTaskCount();

        BatchTaskThreadPoolDTO batchConstructTaskThreadPoolDTO = new BatchTaskThreadPoolDTO();
        batchConstructTaskThreadPoolDTO.setName(constructTaskExecutor.getThreadNamePrefix());
        batchConstructTaskThreadPoolDTO.setQueueSize(constructQueueSize);
        batchConstructTaskThreadPoolDTO.setActiveCount(constructActiveCount);
        batchConstructTaskThreadPoolDTO.setCompletedTaskCount(constructCompletedTaskCount);
        batchConstructTaskThreadPoolDTO.setTaskCount(constructTaskCount);
        batchTaskThreadPoolDTO.setCorePoolSize(constructTaskExecutor.getScheduledThreadPoolExecutor().getCorePoolSize());
        result.add(batchConstructTaskThreadPoolDTO);

        return result;
    }

    /**
     * 取得批处理任务详细信息。
     */
    @Override
    public File exportEntities(Long orgId, Long projectId,  Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("samples/import-project-piping-entities.xlsx"),
            temporaryDir,
            operatorId.toString()
        );

        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        Sheet pressureTestSheet = workbook.getSheet("Pressure Test Packages");
        Sheet cleanPackageSheet = workbook.getSheet("Clean Packages");
        Sheet subSystemSheet = workbook.getSheet("Sub Systems");
        Sheet isoSheet = workbook.getSheet("ISOs");
        Sheet spoolSheet = workbook.getSheet("Spools");
        Sheet pipeSheet = workbook.getSheet("Pipe Pieces");
        Sheet weldSheet = workbook.getSheet("Welds");
        Sheet componentSheet = workbook.getSheet("Components");

        List<ISOEntity> isoList = new ArrayList<>();// isoEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<SpoolEntity> spoolList =  new ArrayList<>();//spoolEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<PipePieceEntity> pipeList =  new ArrayList<>();//pipePieceEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<WeldEntity> weldList =  new ArrayList<>();//weldEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<ComponentEntity> componentList =  new ArrayList<>();//componentEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<PressureTestPackageEntityBase> pressureTestList =  new ArrayList<>();//pressureTestPackageEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<CleanPackageEntityBase> cleanPackageList =  new ArrayList<>();//cleanPackageEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);
        List<SubSystemEntityBase> subSystemList = subSystemEntityRepository.findByProjectIdAndDeletedIsFalse(projectId);

        int isoRowNum = DATA_START_ROW;
        int spoolRowNum = DATA_START_ROW;
        int pipeRowNum = DATA_START_ROW;
        int weldRowNum = DATA_START_ROW;
        int componentRowNum = DATA_START_ROW;
        int pressureTestRowNum = DATA_START_ROW;
        int cleanPackageRowNum = DATA_START_ROW;
        int subSystemRowNum = DATA_START_ROW;

        for (ISOEntity entity: isoList) {
            Row row = WorkbookUtils.getRow(isoSheet, isoRowNum++);
//            if (entity.getModuleNo() != null) {
//                WorkbookUtils.getCell(row, 0).setCellValue(entity.getModuleNo());
//            }
//            if (entity.getLayerPackageNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getLayerPackageNo());
//            }
//            if (entity.getPressureTestPackageNo() != null) {
//                WorkbookUtils.getCell(row, 2).setCellValue(entity.getPressureTestPackageNo());
//            }
//            if (entity.getCleanPackageNo() != null) {
//                WorkbookUtils.getCell(row, 3).setCellValue(entity.getCleanPackageNo());
//            }
//            if (entity.getSubSystemNo() != null) {
//                WorkbookUtils.getCell(row, 4).setCellValue(entity.getSubSystemNo());
//            }
            if (entity.getNo() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getNo());
            }
            if (entity.getProcessLineNo() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getProcessLineNo());
            }
            if (entity.getProcessSystemNo() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getProcessSystemNo());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getRevision());
            }
            if (entity.getNpsText() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getNpsText());
            }
            if (entity.getFluid() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getFluid());
            }
            if (entity.getPipeClass() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
            }
            if (entity.getDesignPressure() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getDesignPressure());
            }
            if (entity.getDesignTemperature() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getDesignTemperature());
            }
            if (entity.getOperatePressureText() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getOperatePressureText());
            }
            if (entity.getOperateTemperatureText() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getOperateTemperatureText());
            }
            if (entity.getInsulationCode() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getInsulationCode());
            }
            if (entity.getInsulationThicknessText() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getInsulationThicknessText());
            }
            if (entity.getPressureTestMedium() != null) {
                WorkbookUtils.getCell(row, 18).setCellValue(entity.getPressureTestMedium());
            }
            if (entity.getTestPressureText() != null) {
                WorkbookUtils.getCell(row, 19).setCellValue(entity.getTestPressureText());
            }
            if (entity.getPaintingCode() != null) {
                WorkbookUtils.getCell(row, 20).setCellValue(entity.getPaintingCode());
            }
            if (entity.getPipeGrade() != null) {
                WorkbookUtils.getCell(row, 21).setCellValue(entity.getPipeGrade());
            }
            if (entity.getHeatTracingCode() != null) {
                WorkbookUtils.getCell(row, 22).setCellValue(entity.getHeatTracingCode());
            }
            if (entity.getAsmeCategory() != null) {
                WorkbookUtils.getCell(row, 23).setCellValue(entity.getAsmeCategory());
            }
            if (entity.getPidDrawing() != null) {
                WorkbookUtils.getCell(row, 24).setCellValue(entity.getPidDrawing());
            }
        }
        for (SpoolEntity entity: spoolList) {
            Row row = WorkbookUtils.getRow(spoolSheet, spoolRowNum++);

            if (entity.getIsoNo() != null) {
                WorkbookUtils.getCell(row, 0).setCellValue(entity.getIsoNo());
            }
            if (entity.getNo() != null) {
                WorkbookUtils.getCell(row, 1).setCellValue(entity.getNo());
            }
            if (entity.getShortCode() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getShortCode());
            }
            if (entity.getSheetNo() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getSheetNo());
            }
            if (entity.getSheetTotal() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getSheetTotal());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getRevision());
            }
            if (entity.getNpsText() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getNpsText());
            }
            if (entity.getLengthText() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getLengthText());
            }
            if (entity.getMaterial() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterial());
            }
            if (entity.getWeightText() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getWeightText());
            }
            if (entity.getPaintingAreaText() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getPaintingAreaText());
            }
            if (entity.getPaintingCode() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getPaintingCode());
            }
            if (entity.getSurfaceTreatment() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getSurfaceTreatment() ? "YES" : "NO");
            }
            if (entity.getPressureTestRequired() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getPressureTestRequired() ? "YES" : "NO");
            }
            if (entity.getProcessSystemNo() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getProcessSystemNo());
            }
            if (entity.getFluid() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getFluid());
            }
            if (entity.getPipeClass() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getPipeClass());
            }
            if (entity.getDesignPressureText() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getDesignPressureText());
            }
            if (entity.getDesignTemperatureText() != null) {
                WorkbookUtils.getCell(row, 18).setCellValue(entity.getDesignTemperatureText());
            }
            if (entity.getOperatePressureText() != null) {
                WorkbookUtils.getCell(row, 19).setCellValue(entity.getOperatePressureText());
            }
            if (entity.getOperateTemperatureText() != null) {
                WorkbookUtils.getCell(row, 20).setCellValue(entity.getOperateTemperatureText());
            }
            if (entity.getInsulationCode() != null) {
                WorkbookUtils.getCell(row, 21).setCellValue(entity.getInsulationCode());
            }
            if (entity.getPressureTestMedium() != null) {
                WorkbookUtils.getCell(row, 22).setCellValue(entity.getPressureTestMedium());
            }
            if (entity.getHeatTracingCode() != null) {
                WorkbookUtils.getCell(row, 23).setCellValue(entity.getHeatTracingCode());
            }
            if (entity.getPidDrawing() != null) {
                WorkbookUtils.getCell(row, 24).setCellValue(entity.getPidDrawing());
            }
            if (entity.getInternalMechanicalCleaning() != null) {
                WorkbookUtils.getCell(row, 25).setCellValue(entity.getInternalMechanicalCleaning());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 26).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 27).setCellValue(entity.getRemarks());
            }
        }
        for (PipePieceEntity entity: pipeList) {
            Row row = WorkbookUtils.getRow(pipeSheet, pipeRowNum++);

            if (entity.getSpoolNo() != null) {
                WorkbookUtils.getCell(row, 0).setCellValue(entity.getSpoolNo());
            }
            if (entity.getNo() != null) {
                WorkbookUtils.getCell(row, 1).setCellValue(entity.getNo());
            }
            if (entity.getBevelCode1() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getBevelCode1());
            }
            if (entity.getBevelCode2() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getBevelCode2());
            }
            if (entity.getBendInfo() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getBendInfo());
            }
            if (entity.getCutDrawing() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getCutDrawing());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getRevision());
            }
            if (entity.getMaterialCode() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getMaterialCode());
            }
            if (entity.getMaterial() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterial());
            }
            if (entity.getNpsText() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getNpsText());
            }
            if (entity.getLength() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getLength());
            }
            if (entity.getPipeClass() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getRemarks());
            }
        }
        for (WeldEntity entity: weldList) {
            Row row = WorkbookUtils.getRow(weldSheet, weldRowNum++);

//            if (entity.getSpoolNo() != null) {
//                WorkbookUtils.getCell(row, 0).setCellValue(entity.getSpoolNo());
//            }
//            if (entity.getIsoNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getIsoNo());
//            }
            if (entity.getNo() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getNo());
            }
//            if (entity.getShopField() != null) {
//                WorkbookUtils.getCell(row, 3).setCellValue(entity.getShopField());
//            }
//            if (entity.getWeldType() != null) {
//                WorkbookUtils.getCell(row, 4).setCellValue(entity.getWeldType());
//            }
            if (entity.getSheetNo() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getSheetNo());
            }
            if (entity.getSheetTotal() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getSheetTotal());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getRevision());
            }
            if (entity.getWpsNo() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getWpsNo());
            }
            if (entity.getRt() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getRt());
            }
            if (entity.getUt() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getUt());
            }
            if (entity.getMt() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getMt());
            }
            if (entity.getPt() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getPt());
            }
            if (entity.getPwht() != null) {
//                WorkbookUtils.getCell(row, 13).setCellValue(entity.getWeldType());
            }
            if (entity.getHardnessTest() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getHardnessTest());
            }
            if (entity.getPmiRatio() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getPmiRatio());
            }
            if (entity.getFn() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getFn());
            }
            if (entity.getPipeClass() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getPipeClass());
            }
            if (entity.getNpsText() != null) {
                WorkbookUtils.getCell(row, 18).setCellValue(entity.getNpsText());
            }
            if (entity.getThickness() != null) {
                WorkbookUtils.getCell(row, 19).setCellValue(entity.getThickness());
            }
            if (entity.getTagNo1() != null) {
                WorkbookUtils.getCell(row, 20).setCellValue(entity.getTagNo1());
            }
            if (entity.getMaterialCode1() != null) {
                WorkbookUtils.getCell(row, 21).setCellValue(entity.getMaterialCode1());
            }
            if (entity.getMaterial1() != null) {
                WorkbookUtils.getCell(row, 22).setCellValue(entity.getMaterial1());
            }
            if (entity.getRemarks1() != null) {
                WorkbookUtils.getCell(row, 23).setCellValue(entity.getRemarks1());
            }
            if (entity.getTagNo2() != null) {
                WorkbookUtils.getCell(row, 24).setCellValue(entity.getTagNo2());
            }
            if (entity.getMaterialCode2() != null) {
                WorkbookUtils.getCell(row, 25).setCellValue(entity.getMaterialCode2());
            }
            if (entity.getMaterial2() != null) {
                WorkbookUtils.getCell(row, 26).setCellValue(entity.getMaterial2());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 27).setCellValue(entity.getRemarks2());
            }
            if (entity.getPaintingCode() != null) {
                WorkbookUtils.getCell(row, 28).setCellValue(entity.getPaintingCode());
            }
            if (entity.getMaterialType() != null) {
                WorkbookUtils.getCell(row, 29).setCellValue(entity.getMaterialType());
            }
            if (entity.getMaterialTraceability() != null) {
                WorkbookUtils.getCell(row, 30).setCellValue(entity.getMaterialTraceability() ? "YES" : "NO");
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 31).setCellValue(entity.getRemarks());
            }
            if (entity.getRemarks3() != null) {
                WorkbookUtils.getCell(row, 32).setCellValue(entity.getRemarks3());
            }
        }
        for (ComponentEntity entity: componentList) {
            Row row = WorkbookUtils.getRow(componentSheet, componentRowNum++);

            if (entity.getIsoNo() != null) {
                WorkbookUtils.getCell(row, 0).setCellValue(entity.getIsoNo());
            }
            if (entity.getSpoolNo() != null) {
                WorkbookUtils.getCell(row, 1).setCellValue(entity.getSpoolNo());
            }
            if (entity.getSheetNo() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getSheetNo());
            }
            if (entity.getSheetTotal() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getSheetTotal());
            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getRevision());
            }
            if (entity.getShortCode() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getShortCode());
            }
            if (entity.getNpsText() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getNpsText());
            }
            if (entity.getThickness() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getThickness());
            }
            if (entity.getMaterialCode() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaterialCode());
            }
            if (entity.getMaterial() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getMaterial());
            }
            if (entity.getQty() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getQty());
            }
            if (entity.getPipeClass() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getPipeClass());
            }
            if (entity.getCoordinateX() != null) {
                WorkbookUtils.getCell(row, 12).setCellValue(entity.getCoordinateX());
            }
            if (entity.getCoordinateY() != null) {
                WorkbookUtils.getCell(row, 13).setCellValue(entity.getCoordinateY());
            }
            if (entity.getCoordinateZ() != null) {
                WorkbookUtils.getCell(row, 14).setCellValue(entity.getCoordinateZ());
            }
            if (entity.getFabricated() != null) {
                WorkbookUtils.getCell(row, 15).setCellValue(entity.getFabricated());
            }
            if (entity.getMaterialWithPositionalMark() != null) {
                WorkbookUtils.getCell(row, 16).setCellValue(entity.getMaterialWithPositionalMark());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 17).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 18).setCellValue(entity.getRemarks());
            }
        }
        for (PressureTestPackageEntityBase entity: pressureTestList) {
            Row row = WorkbookUtils.getRow(pressureTestSheet, pressureTestRowNum++);

//            if (entity.getModuleNo() != null) {
//                WorkbookUtils.getCell(row, 0).setCellValue(entity.getModuleNo());
//            }
//            if (entity.getPressureTestPackageNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getPressureTestPackageNo());
//            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getRevision());
            }
            if (entity.getPressureTestClass() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getPressureTestClass());
            }
            if (entity.getTestPressure() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getTestPressure());
            }
            if (entity.getTestMedium() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getTestMedium());
            }
            if (entity.getPtpDrawingNo() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getPtpDrawingNo());
            }
            if (entity.getMaxOperatingPressure() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getMaxOperatingPressure());
            }
            if (entity.getMaxDesignPressure() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getMaxDesignPressure());
            }
            if (entity.getAirBlow() != null) {
                WorkbookUtils.getCell(row, 9).setCellValue(entity.getAirBlow());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 10).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 11).setCellValue(entity.getRemarks());
            }
        }
        for (CleanPackageEntityBase entity: cleanPackageList) {
            Row row = WorkbookUtils.getRow(cleanPackageSheet, cleanPackageRowNum++);

//            if (entity.getModuleNo() != null) {
//                WorkbookUtils.getCell(row, 0).setCellValue(entity.getModuleNo());
//            }
//            if (entity.getCleanPackageNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getCleanPackageNo());
//            }
            if (entity.getRevision() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getRevision());
            }
            if (entity.getCleanPressure() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getCleanPressure());
            }
            if (entity.getCleanMedium() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getCleanMedium());
            }
            if (entity.getClpDrawingNo() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getClpDrawingNo());
            }
            if (entity.getCleanMethod() != null) {
                WorkbookUtils.getCell(row, 6).setCellValue(entity.getCleanMethod().toString());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 7).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 8).setCellValue(entity.getRemarks());
            }
        }
        for (SubSystemEntityBase entity: subSystemList) {
            Row row = WorkbookUtils.getRow(subSystemSheet, subSystemRowNum++);

            if (entity.getNo() != null) {
                WorkbookUtils.getCell(row, 0).setCellValue(entity.getNo());
            }
//            if (entity.getSubSystemNo() != null) {
//                WorkbookUtils.getCell(row, 1).setCellValue(entity.getSubSystemNo());
//            }
            if (entity.getAirTightness() != null) {
                WorkbookUtils.getCell(row, 2).setCellValue(entity.getAirTightness());
            }
            if (entity.getCheckMC() != null) {
                WorkbookUtils.getCell(row, 3).setCellValue(entity.getCheckMC());
            }
            if (entity.getRemarks2() != null) {
                WorkbookUtils.getCell(row, 4).setCellValue(entity.getRemarks2());
            }
            if (entity.getRemarks() != null) {
                WorkbookUtils.getCell(row, 5).setCellValue(entity.getRemarks());
            }
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }
}
