package com.ose.material.domain.model.service.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.feign.RequestWrapper;
import com.ose.material.domain.model.repository.MmImportBatchTaskRepository;
import com.ose.material.domain.model.service.MmImportBatchTaskInterface;
import com.ose.material.dto.MmImportBatchResultDTO;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.vo.DisciplineCode;
import com.ose.material.vo.MaterialImportType;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * 批处理任务管理服务。
 */
@Component
public class MmImportBatchTaskService implements MmImportBatchTaskInterface {

    // 批处理任务数据仓库
    private final MmImportBatchTaskRepository mmImportBatchTaskRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public MmImportBatchTaskService(
        MmImportBatchTaskRepository mmImportBatchTaskRepository
    ) {
        this.mmImportBatchTaskRepository = mmImportBatchTaskRepository;
    }

    /**
     * 检查是否存在冲突的任务。
     *
     * @param projectId      项目 ID
     * @param batchTaskCodes 批处理任务代码列表
     * @return 批处理服务接口实例
     */
    @Override
    public MmImportBatchTaskInterface conflictWith(
        final Long projectId,
        final MaterialImportType... batchTaskCodes
    ) {
        if (mmImportBatchTaskRepository
            .existsByProjectIdAndCodeInAndRunningIsTrue(projectId, batchTaskCodes)) {
            throw new ConflictError("error.batch-task.duplicate-task-is-running");
        }

        return this;
    }

    /**
     * 开始批处理任务。
     *
     * @param context       上下文对象
     * @param batchTaskCode 批处理任务代码
     * @param function      执行逻辑
     */
    @Override
    @Async("materialExecutor")
    public void run(
        final Long orgId,
        final Long projectId,
        final Long entityId,
        final String entityNo,
        final ContextDTO context,
        final MaterialImportType batchTaskCode,
        final DisciplineCode discipline,
        final BatchTaskFunction function
    ) {
//        // 保存 HTTP 请求信息
//        String authorization = context.getAuthorization();
//        final RequestAttributes attributes = new ServletRequestAttributes(
//            new RequestWrapper(context.getRequest(), authorization),
//            null
//        );
        // 创建一个新的 MockHttpServletRequest 而不是包装原始请求
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setMethod(context.getRequestMethod());
        mockRequest.addHeader("Authorization", context.getAuthorization());
        mockRequest.addHeader("User-Agent", context.getUserAgent());

        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        final RequestAttributes attributes = new ServletRequestAttributes(mockRequest, mockResponse);


        // 创建批处理任务
        final MmImportBatchTask batchTask = new MmImportBatchTask();

        batchTask.setOrgId(orgId);
        batchTask.setProjectId(projectId);
        batchTask.setCode(batchTaskCode);
        batchTask.setName("材料导入");
        batchTask.setStartAt(new Date());
        batchTask.setLastModifiedAt(new Date());
        batchTask.setLaunchedBy(context.getOperator().getId());
        batchTask.setRunning(true);
        batchTask.setEntityId(entityId);
        batchTask.setEntityNo(entityNo);
        batchTask.setDiscipline(discipline);
        batchTask.setWareHouseType(MaterialOrganizationType.PROJECT);
        batchTask.setStatus(EntityStatus.ACTIVE);


        // 若同一项目下存在运行中的相同处理则返回错误
        if (mmImportBatchTaskRepository.existsByProjectIdAndCodeAndStatus(
            projectId,
            batchTask.getCode(),
            EntityStatus.ACTIVE
        )) {
            throw new ConflictError("error.batch-task.duplicate-task-is-running");
        }

        // 创建任务记录
        mmImportBatchTaskRepository.save(batchTask);

        // 在新线程中执行批处理任务
        Thread t1 = new Thread(() -> {
            MmImportBatchResultDTO result = null;

            // 执行批处理任务逻辑
            try {
                // 设置 Feign 客户端上下文，保持 HTTP 请求信息
                RequestContextHolder.setRequestAttributes(attributes);

                // 执行批处理业务逻辑
                result = function.exec(batchTask);

                // 将批处理任务标记为已结束
                batchTask.setStatus(EntityStatus.FINISHED);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                System.out.println("main interrupt error");

                // 将批处理任务标记为异常结束
                batchTask.addError(e.getMessage());
                batchTask.setStatus(EntityStatus.DISABLED);
            }

            // 设置批处理任务执行结果
            batchTask.setFinishedAt(new Date());
            batchTask.setRunning(false);

            // 更新任务记录为已结束
            mmImportBatchTaskRepository.save(batchTask);
        });
        t1.start();

        // 定期检查批处理执行状态
        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(15000);
//                        System.out.println("sub output");

                    MmImportBatchTask runningTask = mmImportBatchTaskRepository
                        .findById(batchTask.getId())
                        .orElse(null);

                    if (runningTask == null) {
                        t1.interrupt();
                        return;
                    }

                    if (runningTask.getStatus() != EntityStatus.ACTIVE) {
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
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 批处理任务分页参数
     */
    @Override
    public Page<MmImportBatchTask> search(
        Long orgId,
        Long projectId,
        Pageable pageable
    ) {
        return mmImportBatchTaskRepository.findByOrgIdAndProjectId(
            orgId,
            projectId,
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
    public MmImportBatchTask get(
        Long orgId,
        Long projectId,
        Long taskId
    ) {
        return mmImportBatchTaskRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, taskId).orElse(null);
    }

    /**
     * 手动停止批处理任务。
     *
     * @param orgId    组织 ID
     * @param operator 操作者信息
     * @param taskId   批处理任务 ID
     */
    @Override
    public void stop(
        Long orgId,
        Long projectId,
        Long taskId,
        OperatorDTO operator) {

        MmImportBatchTask task = get(orgId, projectId, taskId);

        if (task == null) {
            throw new NotFoundError(); // TODO
        }

        if (!task.isRunning()) {
            return;
        }

        task.setStoppedBy(operator.getId());
        task.setRunning(false);
        task.setStatus(EntityStatus.CLOSED);

        mmImportBatchTaskRepository.save(task);
    }


}
