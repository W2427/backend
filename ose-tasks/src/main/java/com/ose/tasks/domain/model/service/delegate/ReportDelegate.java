package com.ose.tasks.domain.model.service.delegate;

import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.vo.BpmTaskDefKey;
import com.ose.dto.ContextDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.entity.bpm.BpmEntityDocsMaterials;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 报告相关的任务执行的代理服务。
 * 包括的 任务节点如下
 * 现在的任务节点名称                  原名称                               说明
 * UT-REPORT-INPUT_QC_REPORT	    USERTASK_QC_REPORT_INPUT	        用户任务-填写测试报告
 * UT-REPORT-INPUT_NDT_NG_REPORT	USERTASK_INPUT_NDT_TEST_REPORT_NG	用户任务- NDT工程师生成不合格探伤报告
 * UT-REPORT-INPUT_NDT_REPORT	    USERTASK_NDT_REPORT_INPUT	        用户任务-填写探伤报告
 * UT-REPORT-UPLOAD_NDT_REPORT	    USERTASK_NDT_REPORT_UPLOAD	        用户任务-QC上传报告
 * EG-REPORT-NG	                    EXCLUSIVE_GATEWAY_RESULT_REPORT_NG	报告错误
 * EG-REPORT-OK	                    EXCLUSIVE_GATEWAY_RESULT_REPORT_OK	报告正确
 * UT-REPORT-UPLOAD_REPORT	        USERTASK_UPLOAD_REPORT	            用户任务-上传报告
 * UT-REPORT-UPLOAD_NG_REPORT	    USERTASK_QC_UPLOAD_NG_REPORT	    用户任务-QC上传报告
 * UT-REPORT-UPLOAD_REVIEW_LIST	    USERTASK_UPLOAD_EDIT_REVIEW_LIST	上传可编辑版最终变更评审单
 */
@Component
public class ReportDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final String HD = BpmsProcessNameEnum.HD.getType();
    private final String PMI = BpmsProcessNameEnum.PMI.getType();


    /**
     * 构造方法。
     */
    @Autowired
    public ReportDelegate(
        TodoTaskBaseInterface todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        BpmActivityInstanceRepository bpmActInstRepository,
        StringRedisTemplate stringRedisTemplate,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmRuTaskRepository ruTaskRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {









        ReportHistory reportHistory = null;
        String process = null;
        String processStage = null;

        String taskDefKey = execResult.getRuTask().getTaskDefKey();

        if (taskRuleCheckService.isOKReportUploadTaskNodes(taskDefKey)) {



            if (BpmTaskDefKey.USERTASK_NDT_REPORT_UPLOAD.getType().equalsIgnoreCase(execResult.getRuTask().getTaskDefKey())) {
                todoTaskBaseService.updateActivityExecuteResultForWeldEntity(execResult.getOrgId(),
                    execResult.getProjectId(),
                    execResult.getActInst().getEntityId(),
                    ActivityExecuteResult.OK,
                    null);
            } else if (BpmTaskDefKey.USERTASK_PMI_REPORT_UPLOAD.getType().equalsIgnoreCase(execResult.getRuTask().getTaskDefKey())) {
                todoTaskBaseService.updateActivityExecuteResultForWeldEntity(execResult.getOrgId(),
                    execResult.getProjectId(),
                    execResult.getActInst().getEntityId(),
                    null,
                    ActivityExecuteResult.OK);
            }


            BpmEntityDocsMaterials bpmDoc = new BpmEntityDocsMaterials();
            bpmDoc.setCreatedAt();
            bpmDoc.setProjectId(execResult.getActInst().getProjectId());
            bpmDoc.setProcessId(execResult.getActInst().getProcessId());
            bpmDoc.setEntityNo(execResult.getActInst().getEntityNo());
            bpmDoc.setEntityId(execResult.getActInst().getEntityId());
            bpmDoc.setActInstanceId(execResult.getActInst().getId());
            bpmDoc.setStatus(EntityStatus.ACTIVE);
            bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION);
            bpmDoc.setOperator(execResult.getContext().getOperator().getId());

            todoTaskBaseService.saveReportFromAttachment(bpmDoc, execResult.getActInst().getId(), execResult.getRuTask().getId());
        } else if (taskRuleCheckService.isNGReportUploadTaskNodes(taskDefKey)) {

            if (BpmTaskDefKey.USERTASK_NDT_REPORT_UPLOAD.getType().equalsIgnoreCase(execResult.getRuTask().getTaskDefKey())) {
                todoTaskBaseService.updateActivityExecuteResultForWeldEntity(execResult.getOrgId(),
                    execResult.getProjectId(),
                    execResult.getActInst().getEntityId(),
                    ActivityExecuteResult.NG,
                    null);
            } else if (BpmTaskDefKey.USERTASK_PMI_REPORT_UPLOAD.getType().equalsIgnoreCase(execResult.getRuTask().getTaskDefKey())) {
                todoTaskBaseService.updateActivityExecuteResultForWeldEntity(execResult.getOrgId(),
                    execResult.getProjectId(),
                    execResult.getActInst().getEntityId(),
                    null,
                    ActivityExecuteResult.NG);
            }
        }



/*        List<BpmActivityInstance> actInstList = new ArrayList<>();
        for(int i=0;i<actTaskIds.length;i++) {
            BpmRuTask ruTask = ruTaskRepository.findByTaskId(actTaskIds[i]);
            if(ruTask==null) continue;
            rutasks.add(ruTask);
            Optional<BpmActivityInstance> opActInst = bpmActInstRepository.findByActInstId(ruTask.getActInstId());
            if(opActInst.isPresent()) {
                if(process == null) {
                    process = opActInst.get().getProcess();
                    processStage = opActInst.get().getProcessStage();
                }
                actInstList.add(opActInst.get());
            }

        }*/
        return execResult;
    }
}
