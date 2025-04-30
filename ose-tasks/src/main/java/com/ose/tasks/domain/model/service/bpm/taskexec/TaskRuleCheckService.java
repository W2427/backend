package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.util.LongUtils;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.BpmTaskType;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 任务节点是否能够向下执行，根据 TASK_DEF_KEY 进行规则校验的类，定义在这个类中
 */
@Component
public class TaskRuleCheckService {

    private final static String YOU_CAN_NOT_MODIFY_CONTENT = "you can not modify content while activity is not work on this node";

    private final BpmRuTaskRepository ruTaskRepository;

    public static final List<String> MOBILE_TASK_NODES = new ArrayList<>(
        Arrays.asList(
            BpmTaskDefKey.USERTASK_SPOOL_RELEASE_EXECUTE.getType(),
            BpmTaskDefKey.USERTASK_FITUP_EXECUTE.getType(),
            BpmTaskDefKey.USERTASK_PIPE_PIECE_CUTTING_EXECUTE.getType(),
            BpmTaskDefKey.USERTASK_SPOOL_RELEASE_EXECUTE.getType()
        )
    );

    //进行 design 设计工作的工序
    public final List<String> DESIGN_PROCESSES = new ArrayList<>(
        Arrays.asList(
            BpmsProcessNameEnum.ENGINEERING.getType(),
            BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType(),
            BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType(),
            BpmsProcessNameEnum.ENGINEERING_MTO.getType(),
            BpmsProcessNameEnum.DRAWING_REDMARK.getType(),
            BpmsProcessNameEnum.BOM_ISSUE.getType()
        )
    );


    public TaskRuleCheckService(BpmRuTaskRepository ruTaskRepository) {
        this.ruTaskRepository = ruTaskRepository;
    }


    //是否是会签节点
    public boolean isCounterSignTaskNode(String taskDefKey, String taskType) {
        Set<String> counterSignNodes = new HashSet<>(Arrays.asList(
            BpmTaskDefKey.USERTASK_DRAWING_SIGN.getType(),
            BpmTaskDefKey.USERTASK_COSIGN.getType()
//            BpmTaskDefKey.USERTASK_QC_UPLOAD_REPORT.getType(),
//            BpmTaskDefKey.USERTASK_PMI_REPORT_UPLOAD.getType(),
//            BpmTaskDefKey.USERTASK_NDT_REPORT_UPLOAD.getType()
        ));

        if (counterSignNodes.contains(taskDefKey) || BpmTaskType.DRAWING_COSIGN.name().equalsIgnoreCase(taskType)) {
            return true;
        }
        return false;

//        return BpmTaskDefKey.USERTASK_DRAWING_SIGN.getType().equalsIgnoreCase(taskDefKey);

    }

    //是否是 设计节点

    public boolean isDrawingDesignTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_USER_DRAWING_DESIGN.getType().equalsIgnoreCase(taskDefKey);
    }

    public boolean isRedMarkDesignTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType().equalsIgnoreCase(taskDefKey);
    }

    //是否是 图纸 CHECK 节点
    public boolean isDrawingCheckTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType().equalsIgnoreCase(taskDefKey);
    }

    public boolean isRedMarkCheckTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType().equalsIgnoreCase(taskDefKey);
    }

    //是否是 审核节点
    public boolean isDrawingApprovedTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType().equalsIgnoreCase(taskDefKey);
    }

    //是否是 图纸MODIFY 修改节点

    public boolean isDrawingModifyTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_DRAWING_MODIFY.getType().equalsIgnoreCase(taskDefKey)
            || BpmTaskDefKey.USERTASK_DRAWING_MODIFY_SHORTCUT.getType().equalsIgnoreCase(taskDefKey)
            || BpmTaskDefKey.USERTASK_DRAWING_REVISE.getType().equalsIgnoreCase(taskDefKey);
    }

//    public boolean isDrawingModifyShortcutTaskNode(String taskDefKey) {
//        return BpmTaskDefKey.USERTASK_DRAWING_MODIFY_SHORTCUT.getType().equalsIgnoreCase(taskDefKey);
//    }

    //是否是 设计变更节点 任务点 USERTASK_CREATE_CHANGE_TASK
    public boolean isCreateChangeTaskTaskNode(String taskDefKey) {
        return BpmTaskDefKey.USERTASK_CREATE_CHANGE_TASK.getType().equalsIgnoreCase(taskDefKey);
    }

    //是否是班长分配工人节点
    public boolean isFormanAssignWorkerTaskNode(String taskType) {
        return BpmTaskType.FOREMAN_DISPATCH.name().equalsIgnoreCase(taskType);
    }


    //是否是 用户任务-管道主管确定是否创建建造变更  任务点 USERTASK_SUPERVISOR_CHANGE_CONFIRM
    public boolean isSupervisorConfirmChangeTaskNode(String taskType) {
        return BpmTaskType.SUPERVISOR_CHANGE_CONFIRM.name().equalsIgnoreCase(taskType);
    }

    //上传OK的报告的节点
    public boolean isOKReportUploadTaskNodes(String taskDefKey) {
        Set<String> uploadReportNodes = new HashSet<>(Arrays.asList(
            BpmTaskDefKey.USERTASK_UPLOAD_REPORT.getType(),
            BpmTaskDefKey.USERTASK_QC_UPLOAD_REPORT.getType(),
            BpmTaskDefKey.USERTASK_PMI_REPORT_UPLOAD.getType(),
            BpmTaskDefKey.USERTASK_NDT_REPORT_UPLOAD.getType()
        ));

        if (uploadReportNodes.contains(taskDefKey)) {
            return true;
        }
        return false;
    }

    //上传NG的报告的节点
    public boolean isNGReportUploadTaskNodes(String taskDefKey) {
        Set<String> uploadReportNodes = new HashSet<>(Arrays.asList(
            BpmTaskDefKey.USERTASK_UPLOAD_NG_REPORT.getType(),
            BpmTaskDefKey.USERTASK_QC_UPLOAD_NG_REPORT.getType(),
            BpmTaskDefKey.USERTASK_QC_UPLOAD_NDT_NG_REPORT.getType(),
            BpmTaskDefKey.USERTASK_QC_UPLOAD_PMI_NG_REPORT.getType()
        ));

        if (uploadReportNodes.contains(taskDefKey)) {
            return true;
        }
        return false;
    }


    //是否是抽检确认节点
    public boolean isPenaltyConfirmTaskNode(String taskDefKey) {

        return BpmTaskDefKey.USERTASK_NDT_EXPAND_CONFIRM.getType().equals(taskDefKey);
    }

    //是否是焊接执行节点
    public boolean isWeldExecuteTaskNode(String taskType) {
        return BpmTaskType.WELD_EXECUTE.name().equalsIgnoreCase(taskType);
    }

    //是否是 punchlist 创建后 指定负责人的节点
    public boolean isPunchlistChargerAssignTaskNode(String taskType) {
        return BpmTaskType.PUNCHLIST_CHARGER_ASIGN.name().equalsIgnoreCase(taskType);
    }

    /**
     * 检查当前的任务节点是否 能够生成NDT报告。
     *
     * @param taskType
     * @return
     */
    public boolean isNDTGenerateReportNode(String taskType) {

        Set<String> ndtGenerateReportNodes = new HashSet<>(Arrays.asList(
            BpmTaskType.GENERATE_NDT_REPORT.name(),
            BpmTaskType.GENERATE_NG_NDT_REPORT.name()
        ));

        if (ndtGenerateReportNodes.contains(taskType)) {
            return true;
        }
        return false;
    }


    /**
     * 检查当前操作是否在 对应的任务节点上
     *
     * @param orgId
     * @param projectId
     * @param bpmTaskDefKey
     * @param actInstId
     */
    public BpmRuTask checkRuTask(Long orgId, Long projectId, BpmTaskDefKey bpmTaskDefKey, Long actInstId) {
        boolean ruTaskFlg = false;


        if (!LongUtils.isEmpty(actInstId)) {

            List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInstId);
            if (!ruTasks.isEmpty()) {

                BpmRuTask ruTask = ruTasks.get(0);
                if (bpmTaskDefKey.getType().equals(ruTask.getTaskDefKey())) {
                    ruTaskFlg = true;
                    return ruTask;
                }
            }
        }
        if (!ruTaskFlg) {
            // TODO
            throw new BusinessError(YOU_CAN_NOT_MODIFY_CONTENT);
        }
        return null;
    }
}
