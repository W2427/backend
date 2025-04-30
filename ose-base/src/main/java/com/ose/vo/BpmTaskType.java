package com.ose.vo;

/**
 * 工作流 任务节点类型 集合
 */

public enum BpmTaskType implements ValueObject {

    EX_INSP_APPLY("EX_INSP_APPLY", "外检申请类型任务", true, false),  //com.ose.tasks.domain.model.service.delegate.NextAssigneeServiceDelegate
    CONSTRUCTION("CONSTRUCTION", "建造类型代理", false, false),       //com.ose.tasks.domain.model.service.delegate.CutList/DeliveryList/IndividualConstruct
    NEXT_TASK("NEXT_TASK", "下一个任务节点是否自动启动", false, false), //com.ose.tasks.domain.model.service.delegate.NextTaskServiceDelegate
    ST_MAT_NEST("ST_MAT_NEST", "结构套料返回", false, false), //com.ose.tasks.domain.model.service.delegate.MaterialStructureNestDelegate
    EX_INSP_APPLY_MAIL("EX_INSP_APPLY_MAIL", "外检申请发送邮件", false, false), //com.ose.tasks.domain.model.service.delegate.ExInspMailDelegate
    DRAWING_SEND_EMAIL("DRAWING_SEND_EMAIL", "GOE发送邮件", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingGOEApproveDelegate
    RELEASE_CONFIRM("RELEASE_CONFIRM", "确认入库完成", false, false), //com.ose.tasks.domain.model.service.delegate.MaterialReleaseReceiveRunningStatusDelegate
    MATERIAL_FINAL_CONFIRM("MATERIAL_FINAL_CONFIRM", "最终盘点确认", false, false), //com.ose.tasks.domain.model.service.delegate.MaterialReleaseReceiveFinalConfirmDelegate
    EX_INSP_UPLOAD_REPORT("EX_INSP_UPLOAD_REPORT", "外检上传报告", true, false), //com.ose.tasks.domain.model.service.delegate.ExInspUploadReportDelegate
    EX_INSP_HANDLE_REPORT("EX_INSP_HANDLE_REPORT", "处理外检报告", false, false), //com.ose.tasks.domain.model.service.delegate.ExInspHandleReportDelegate
    EX_INSP_REHANDLE_REPORT("EX_INSP_REHANDLE_REPORT", "再处理外检报告", false, false), //com.ose.tasks.domain.model.service.delegate.ExInspReHandleReportDelegate
    DRAWING_REVIEW("DRAWING_REVIEW", "图纸设计校审", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    REDMARK_DESIGN("REDMARK_DESIGN", "图纸REDMARK 设计", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    REDMARK_CHECK("REDMARK_CHECK", "图纸REDMARK 校核", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    REDMARK_RECEIPT("REDMARK_RECEIPT", "图纸REDMARK 接收", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_CHECK("DRAWING_CHECK", "图纸校核CHECK", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingCheckDelegate
    DRAWING_QC_CHECK("DRAWING_QC_CHECK", "", false, false),
    DRAWING_RESULT_RECORD("DRAWING_RESULT_RECORD", "图纸结果RECORD", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingRecordResultDelegate
    DRAWING_DESIGN("DRAWING_DESIGN", "图纸设计", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_MODIFY("DRAWING_MODIFY", "图纸修改", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_ISSUE("DRAWING_ISSUE", "图纸发图", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_UPLOAD_COVER("DRAWING_UPLOAD_COVER", "图纸上传封面", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_APPROVE("DRAWING_APPROVE", "图纸审核", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_PRE_APPROVE("DRAWING_PRE_APPROVE", "图纸内部审核", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_COSIGN("DRAWING_COSIGN", "图纸会签", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_REGISTER("DRAWING_REGISTER", "图纸登记", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_DISPATCH("DRAWING_DISPATCH", "图纸发放", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_UPLOAD_REVIEW_LIST("DRAWING_UPLOAD_REVIEW_LIST", "上传图纸审查清单", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_UPLOAD_VOR("DRAWING_UPLOAD_VOR", "上传图纸VOR变更", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING_UPLOAD_CHANGE_ORDER("DRAWING_UPLOAD_CHANGE_ORDER", "上传图纸变更", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    DRAWING("DRAWING", "图纸设计", false, false), //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
    CONSTRUCT_PIPING_CUTTING("CONSTRUCT_PIPING_CUTTING", "建造下料", false, false), //com.ose.tasks.domain.model.service.delegate.ConstructPipingCuttingDelegate
    MATERIAL_RECEIVE("MATERIAL_RECEIVE", "材料接收", true, false), //com.ose.tasks.domain.model.service.delegate.MaterialReceiveDelegate
    NDT_INIT_CHECK("NDT_INIT_CHECK", "NDT初始检查", false, false), //com.ose.tasks.domain.model.service.delegate.NdtInitCheckDelegate
    PMI_INIT_CHECK("PMI_INIT_CHECK", "PMI初始检查", false, false), //com.ose.tasks.domain.model.service.delegate.PmiInitCheckDelegate
    NDT_SELECT("NDT_SELECT", "NDT焊口抽取", false, false), //com.ose.tasks.domain.model.service.delegate.NdtSelectDelegate
    NDT_EXECUTE("NDT_EXECUTE", "无损探伤执行", false, false), //com.ose.tasks.domain.model.service.delegate.NdtSelectDelegate
    PMI_EXECUTE("PMI_EXECUTE", "PMI检测判定执行", false, false), //com.ose.tasks.domain.model.service.delegate.PmiSelectDelegate
    GENERATE_NDT_REPORT("GENERATE_NDT_REPORT", "生成NDT报告", true, false), //com.ose.tasks.domain.model.service.delegate.NdtGenerateReport
    GENERATE_NG_NDT_REPORT("GENERATE_NG_NDT_REPORT", "生成不合格NDT报告", true, false), //com.ose.tasks.domain.model.service.delegate.NdtGenerateReport
    NDT_ANALYZE_NG_RESULT("NDT_ANALYZE_NG_RESULT", "NDT分析NG结果", false, false), //com.ose.tasks.domain.model.service.delegate.NdtAnalyzeNgResult
    NDT_CONFIRM_RESULT("NDT_CONFIRM_RESULT","NDT确认返修", false, false),
    PMI_ANALYZE_NG_RESULT("PMI_ANALYZE_NG_RESULT", "PMI分析NG结果", false, false), //com.ose.tasks.domain.model.service.delegate.NdtAnalyzeNgResult
    NDT_UPLOAD_REPORT("NDT_UPLOAD_REPORT", "NDT上传报告", true, false), //com.ose.tasks.domain.model.service.delegate.NdtSubUploadReportDelegate
    PMI_UPLOAD_REPORT("PMI_UPLOAD_REPORT", "PMI上传报告", true, false), //com.ose.tasks.domain.model.service.delegate.NdtSubUploadReportDelegate
    NDT_CONFIRM_REPORT("NDT_CONFIRM_REPORT", "NDT报告确认", true, false), //com.ose.tasks.domain.model.service.delegate.NdtSubConfirmReportDelegate
    MATERIAL_ASSIGNEE("MATERIAL_ASSIGNEE", "材料分配人", false, false), //com.ose.tasks.domain.model.service.delegate.MaterialReleaseReceiveAssigneeDelegate
    ST02_CONFIRM_REPORT("ST02_CONFIRM_REPORT", "ST报告确认", true, false),//com.ose.tasks.domain.model.service.delegate.ST02ConfirmReportDelegate
    FITUP_EXECUTE("FITUP_EXECUTE", "组对执行", false, false),
//    SUPERVISOR_CHECK_NG("SUPERVISOR_CHECK_NG", "主管确认不合格", false, false),
    PUNCHLIST_CHARGER_ASIGN("PUNCHLIST_CHARGER_ASIGN", "遗留问题更换负责人", false, false),
    WELD_EXECUTE("WELD_EXECUTE", "焊工执行", false, false),
    FOREMAN_DISPATCH("FOREMAN_DISPATCH", "班长执行", false, false),
    SUPERVISOR_CHANGE_CONFIRM("SUPERVISOR_CHANGE_CONFIRM", "主管创建建造变更", false, false),
    SUPERVISOR_APPLY("SUPERVISOR_APPLY", "主管申请", false, false),
    SUPERVISOR_CONFIRM("SUPERVISOR_CONFIRM", "主管确认", false, false),

    INPUT_EX_COMMENT("INPUT_EX_COMMENT", "输入外检意见", false, false),
    INPUT_IN_COMMENT("INPUT_IN_COMMENT", "输入内检意见", false, false),
    EX_INSP_COMPLETE("EX_INSP_COMPLETE", "完成外检", false, false),
    QC_EX_CONFIRM("QC_EX_CONFIRM", "QC确认接受外检", false, false),
    QC_IN_CONFIRM("QC_EX_CONFIRM", "QC确认接受外检", false, false),
    PMI_SELECT("PMI_SELECT", "主管选择PMI焊口", false, false),
    REPORT_INPUT("REPORT_INPUT", "输入报告", false, false),
    HANDOVER_EXECUTE("HANDOVER_EXECUTE", "交接执行", false, false),
    RELEASE_EXECUTE("RELEASE_EXECUTE", "放行执行", false, false),
    PWHT_EXECUTE("PWHT_EXECUTE", "焊后热处理", false, false),
    HD_EXECUTE("HD_EXECUTE", "硬度测试执行", false, false),
    CON_MANAGER_CONFIRM("CON_MANAGER_CONFIRM", "建造经理执行", false, false),
    GEN_REPORT("GEN_REPORT", "生成质检报告", false, false),
    SUPERVISOR_DISPATCH("SUPERVISOR_DISPATCH", "主管指派", false, false),
    HSE_CONFIRM("HSE_CONFIRM", "HSE主管确认", false, false),


    PMI_CONFIRM_REPORT("PMI_CONFIRM_REPORT", "PMI报告确认", true, false), //com.ose.tasks.domain.model.service.delegate.NdtSubConfirmReportDelegate
    SUB_NDT_EXECUTE("SUB_NDT_EXECUTE", "", true, false), //com.ose.tasks.domain.model.service.delegate.NdtSubProcessDelegate
    NDT_EXAMINE("NDT_EXAMINE", "", false, false), //com.ose.tasks.domain.model.service.delegate.NdtExamineDelegate
    NDT_FLAW_LENGTH("NDT_FLAW_LENGTH", "", false, false), //com.ose.tasks.domain.model.service.delegate.NdtFlawLengthDelegate
    PMI_EXAMINE("PMI_EXAMINE", "", false, false), //com.ose.tasks.domain.model.service.delegate.PmiExamineDelegate
    DELIVERY_MASTER_CONFIRM("DELIVERY_MASTER_CONFIRM", "", false, false), //com.ose.tasks.domain.model.service.delegate.PmiExamineDelegate
    DELIVERY_BATCH_CONFIRM("DELIVERY_BATCH_CONFIRM", "", false, false),//com.ose.tasks.domain.model.service.delegate.PmiExamineDelegate
    DELIVERY_INVENTORY_RECEIPT("DELIVERY_INVENTORY_RECEIPT", "", false, false),//com.ose.tasks.domain.model.service.delegate.PmiExamineDelegate
//    MATERIAL_RECEIVE("MATERIAL_RECEIVE", "材料接收", false); //com.ose.tasks.domain.model.service.delegate.MaterialReceiveDelegate
    SUB_NDT_WELD_DATE("SUB_NDT_WELD_DATE", "检查焊接时间", true, false), //
    QC_REPORT_PACKAGE("QC_REPORT_PACKAGE", "qc报告打包", true, false);//com.ose.tasks.domain.model.service.delegate.QCReportPackageDelegate

    private String code;
    private String displayName;
    private boolean checkNodeType; //如果为true 则需要在任务执行的时候校验 taskType 名称
    private boolean isAutoStart;

    BpmTaskType(String code, String displayName, boolean checkNodeType, boolean isAutoStart) {
        this.code = code;
        this.displayName = displayName;
        this.checkNodeType = checkNodeType;
        this.isAutoStart = isAutoStart;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public boolean getCheckNodeType() {
        return checkNodeType;
    }

    public boolean isAutoStart() {
        return isAutoStart;
    }
}
