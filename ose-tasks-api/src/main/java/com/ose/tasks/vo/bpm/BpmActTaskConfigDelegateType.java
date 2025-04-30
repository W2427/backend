package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 工作流配置代理类型
 */
public enum BpmActTaskConfigDelegateType implements ValueObject {

    NEXT_ASSIGNEE("NEXT_ASSIGNEE", "下个人员指定类型"),  //com.ose.tasks.domain.model.service.delegate.NextAssigneeServiceDelegate
    CONSTRUCTION("CONSTRUCTION", "建造类型代理"),       //com.ose.tasks.domain.model.service.delegate.CutList/DeliveryList/IndividualConstruct
    CONSTRUCTION_DELIVERY("CONSTRUCTION_DELIVERY", "建造配送代理"),       //com.ose.tasks.domain.model.service.delegate.CutList/DeliveryList/IndividualConstruct
    CONSTRUCTION_CUTTING("CONSTRUCTION_CUTTING", "建造下料代理"),       //com.ose.tasks.domain.model.service.delegate.CutList/DeliveryList/IndividualConstruct
    CONSTRUCTION_MONO("CONSTRUCTION_MONO", "建造单体代理"),       //com.ose.tasks.domain.model.service.delegate.CutList/DeliveryList/IndividualConstruct
    CONSTRUCTION_PMI("CONSTRUCTION_PMI", "建造PMI类型代理"),       //com.ose.tasks.domain.model.service.delegate.pmi
    CONSTRUCTION_NDT("CONSTRUCTION_NDT", "建造NDT类型代理"),       //com.ose.tasks.domain.model.service.delegate.ndt
    NEXT_TASK("NEXT_TASK", "下一个任务节点是否自动启动"), //com.ose.tasks.domain.model.service.delegate.NextTaskServiceDelegate
    ST_MAT_NEST("ST_MAT_NEST", "结构套料返回"), //com.ose.tasks.domain.model.service.delegate.MaterialStructureNestDelegate
    EX_INSP_UPLOAD_REPORT("EX_INSP_UPLOAD_REPORT", "外检上传报告"), //com.ose.tasks.domain.model.service.delegate.ExInspUploadReportDelegate
    EX_INSP_HANDLE_REPORT("EX_INSP_HANDLE_REPORT", "外检处理报告"), //com.ose.tasks.domain.model.service.delegate.ExInspHandleReportDelegate
    EX_INSP_APPLY_MAIL("EX_INSP_APPLY_MAIL", "外检申请发送邮件"), //com.ose.tasks.domain.model.service.delegate.ExInspMailDelegate
    EX_INSP_REHANDLE_REPORT("EX_INSP_REHANDLE_REPORT", "再处理外检报告"), //com.ose.tasks.domain.model.service.delegate.ExInspReHandleReportDelegate
    EXTERNAL_INSPECTION_APPLY("EXTERNAL_INSPECTION_APPLY", "外检申请"),//com.ose.tasks.domain.model.service.delegate.ExInspApplyDelegate
    MATERIAL("MATERIAL", "材料"),//com.ose.tasks.domain.model.service.delegate.ExInspApplyDelegate
    DRAWING("DRAWING", "图纸设计"),//com.ose.tasks.domain.model.service.delegate.DrawingDelegate
    DRAWING_PACKAGE("DRAWING_PACKAGE", "图纸打包"),//com.ose.tasks.domain.model.service.delegate.DrawingDelegate
    CONSTRUCTION_LOG("CONSTRUCTION_LOG", "建造日志类型代理"),       //com.ose.tasks.domain.model.service.delegate.ConstructionLog
    NDT("NDT", "探伤");//com.ose.tasks.domain.model.service.delegate.DrawingDelegate
//    RED_MARK("RED_MARK", "图纸RED_MARK"), //com.ose.tasks.domain.model.service.delegate.DrawingDelegate
//    DRAWINGREVIEW("DRAWINGREVIEW", "图纸校审"),//com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate
//    CONSTRUCT_PIPING_CUTTING("CONSTRUCT_PIPING_CUTTING", "建造下料"), //com.ose.tasks.domain.model.service.delegate.ConstructPipingCuttingDelegate
//    MATERIAL_RECEIVE("MATERIAL_RECEIVE", "材料接收"); //com.ose.tasks.domain.model.service.delegate.DrawingReviewDelegate

    private String code;
    private String displayName;

    BpmActTaskConfigDelegateType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

}
