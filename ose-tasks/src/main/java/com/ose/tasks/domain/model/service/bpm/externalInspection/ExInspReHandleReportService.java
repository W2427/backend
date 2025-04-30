package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.bpm.BpmExInspConfirmsDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.bpm.TodoBatchTaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理报告服务。
 */
@Component
public class ExInspReHandleReportService implements ExInspReHandleReportInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspReHandleReportService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    /**
     * 构造方法。
     */
    @Autowired
    public ExInspReHandleReportService(
        TodoTaskDispatchInterface todoTaskDispatchService) {
        this.todoTaskDispatchService = todoTaskDispatchService;
    }


    /**
     * 确认外检报告上传确认情况，接收 拒绝
     *
     * @param contextDTO                 环境变量
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param exInspUploadFileConfirmDTO 外检上传文件确认DTO
     * @param operatorDTO                操作者DTO
     * @return 文件上传后的反馈DTO
     */
    @Override
    public BpmExInspConfirmResponseDTO handleReport(
        ContextDTO contextDTO,
        Long orgId,
        Long projectId,
        BpmExInspConfirmsDTO exInspUploadFileConfirmDTO,
        OperatorDTO operatorDTO
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);

        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(contextDTO, data, exInspUploadFileConfirmDTO);


        BpmExInspConfirmResponseDTO responseDTO = (BpmExInspConfirmResponseDTO) todoBatchTaskDTO.getMetaData().get("responseDTO");

        return responseDTO;
    }


}
