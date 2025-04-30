package com.ose.tasks.vo.bpm;

import com.ose.vo.BpmTaskDefKey;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 工作流业务设计到的常量 集合
 */

public class BpmCode {

    //报验结果排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT.getType(); //网关结果
    public static final String EXCLUSIVE_GATEWAY_RESULT_ACCEPT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.getType(); // "接受"
    public static final String EXCLUSIVE_GATEWAY_RESULT_REJECT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REJECT.getType(); // 驳回，拒绝
    public static final String EXCLUSIVE_GATEWAY_RESULT_ACCEPT_SIGN = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_ACCEPT_SIGN.getType(); //有意见接受
    public static final String EXCLUSIVE_GATEWAY_RESULT_PENDING = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_PENDING.getType(); // 待定

    //NDT的排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT_REPAIR = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REPAIR.getType(); // "返修"
    public static final String EXCLUSIVE_GATEWAY_RESULT_NDT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NDT.getType(); // NDT
    public static final String EXCLUSIVE_GATEWAY_RESULT_RE_NDT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_RE_NDT.getType(); // "重新NDT"
    public static final String EXCLUSIVE_GATEWAY_RESULT_NO_NDT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NO_NDT.getType(); // NOT NDT
    public static final String EXCLUSIVE_GATEWAY_RESULT_SKIP_NDT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_SKIP_NDT.getType(); // SKIP NDT
    public static final String EXCLUSIVE_GATEWAY_RESULT_NDT_TODO = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NDT_TODO.getType(); // 100%检验 或 扩口 或 返修
    public static final String EXCLUSIVE_GATEWAY_RESULT_NDT_APPLY = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NDT_APPLY.getType(); // NDT申请
    public static final String EXCLUSIVE_GATEWAY_RESULT_NOT_NEED_EXPAND_WELD = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NOT_NEED_EXPAND_WELD.getType();// 不需要扩口
    public static final String EXCLUSIVE_GATEWAY_RESULT_NEED_EXPAND_WELD = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NEED_EXPAND_WELD.getType(); // 扩口

    //PMI结果的排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT_PMI = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_PMI.getType(); // PMI结果
    public static final String EXCLUSIVE_GATEWAY_RESULT_NO_PMI = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NO_PMI.getType(); // 不需要做 PMI
    public static final String EXCLUSIVE_GATEWAY_RESULT_PMI_TODO = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_PMI_TODO.getType(); // 100%检验 或 扩口 或 返修
    public static final String EXCLUSIVE_GATEWAY_RESULT_PMI_APPLY = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_PMI_APPLY.getType(); // NDT申请

    //报告相关的排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT_REPORT_NG = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REPORT_NG.getType(); // 报告错误
    public static final String EXCLUSIVE_GATEWAY_RESULT_REPORT_OK = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REPORT_OK.getType(); // 报告正确

    //设计变更的排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT_NEED_DESIGN_CHANGE = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NEED_DESIGN_CHANGE.getType(); // 增加设计变更

    //外检排他网关
    public static final String EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT.getType();// 无意见
    public static final String EXCLUSIVE_GATEWAY_RESULT_COMMENT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_COMMENT.getType(); //有意见
    public static final String EXCLUSIVE_GATEWAY_RESULT_NO_COORDINATE = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_NO_COORDINATE.getType();//无协调员
    public static final String EXCLUSIVE_GATEWAY_RESULT_COORDINATE = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_COORDINATE.getType();//有协调员
    public static final String EXCLUSIVE_GATEWAY_RESULT_COMPLETE = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_COMPLETE.getType();//punchlist整改完成


    //用户任务 userTask 设计
    public static final String USERTASK_DRAWING_SIGN = BpmTaskDefKey.USERTASK_DRAWING_SIGN.getType();// "会签"
    public static final String USERTASK_DRAWING_CHECK = BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType();// "用户任务-校对"
    public static final String USERTASK_DRAWING_ISSUED = BpmTaskDefKey.USERTASK_DRAWING_ISSUED.getType();// "图纸签发";
    public static final String USERTASK_DRAWING_DESIGN = BpmTaskDefKey.USERTASK_DRAWING_DESIGN.getType();//设计人员绘制图纸
    public static final String USERTASK_DESIGNER_UPLOAD_CHANGE_LIST = BpmTaskDefKey.USERTASK_DESIGNER_UPLOAD_CHANGE_LIST.getType(); // 管道主管确定是否创建建造变更
    public static final String USERTASK_DESIGN_DOCUMENT_CONTROL_EXECUTE = BpmTaskDefKey.USERTASK_DESIGN_DOCUMENT_CONTROL_EXECUTE.getType(); // 上传签字版变更评审单
    public static final String USERTASK_DESIGNER_ACCEPT_TASK = BpmTaskDefKey.USERTASK_DESIGNER_ACCEPT_TASK.getType(); //设计接收任务
    public static final String USERTASK_DRAWING_REVIEW = BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType();//图纸审核
    public static final String USERTASK_DRAWING_REGISTER = BpmTaskDefKey.USERTASK_DRAWING_REGISTER.getType();// 图纸登记
    public static final String USERTASK_DRAWING_DISPATCH = BpmTaskDefKey.USERTASK_DRAWING_DISPATCH.getType();//图纸分发
    public static final String USERTASK_CREATE_CHANGE_TASK = BpmTaskDefKey.USERTASK_CREATE_CHANGE_TASK.getType();
    public final static String USERTASK_DRAWING_MODIFY = BpmTaskDefKey.USERTASK_DRAWING_MODIFY.getType();//设计人员修改图纸
    public final static String USERTASK_USER_DRAWING_DESIGN = BpmTaskDefKey.USERTASK_USER_DRAWING_DESIGN.getType();//设计人员 画图

    //用户任务 userTask NDT
    public static final String USERTASK_NDT_SUPERVISOR_CHECK_NDT_EXEXUTE = BpmTaskDefKey.USERTASK_NDT_SUPERVISOR_CHECK_NDT_EXEXUTE.getType(); //NDT主管审核
    public static final String USERTASK_NDT_ENGINEER_EXECUTE = BpmTaskDefKey.USERTASK_NDT_ENGINEER_EXECUTE.getType(); //NDT工程师现场探伤
    public static final String USERTASK_NDT_SUPERVISOR_CONFIRM_NG = BpmTaskDefKey.USERTASK_NDT_SUPERVISOR_CONFIRM_NG.getType();//NDT主管线下审核不合格报告
    public static final String USERTASK_NDT_CHECK = BpmTaskDefKey.USERTASK_NDT_CHECK.getType(); // 系统ndt检查
    public static final String USERTASK_NDT_SUPERVISOR_CONFIRM = BpmTaskDefKey.USERTASK_NDT_SUPERVISOR_CONFIRM.getType(); // NDT主管确认
    public static final String USERTASK_INPUT_NDT_TEST_REPORT = BpmTaskDefKey.USERTASK_INPUT_NDT_TEST_REPORT.getType(); //用户任务-NDT工程师生成合格探伤报告
    public static final String USERTASK_INPUT_NDT_TEST_REPORT_NG = BpmTaskDefKey.USERTASK_INPUT_NDT_TEST_REPORT_NG.getType(); //用户任务-NDT工程师生成不合格探伤报告
    public static final String USERTASK_NDT_APPLY_CONFIRM = BpmTaskDefKey.USERTASK_NDT_APPLY_CONFIRM.getType(); // 主管NDT申请确认
    public static final String USERTASK_NDT_EXPAND_CONFIRM = BpmTaskDefKey.USERTASK_NDT_EXPAND_CONFIRM.getType();//NDT主管扩口确认

    //用户任务 userTask 报告
    public static final String USERTASK_UPLOAD_REPORT = BpmTaskDefKey.USERTASK_UPLOAD_REPORT.getType(); // QC上传报告
    public static final String USERTASK_UPLOAD_EDIT_REVIEW_LIST = BpmTaskDefKey.USERTASK_UPLOAD_EDIT_REVIEW_LIST.getType(); // 上传可编辑版最终变更评审单
    public static final String USERTASK_SUPERVISOR_UPLOAD_CHANGE_ORDER = BpmTaskDefKey.USERTASK_SUPERVISOR_UPLOAD_CHANGE_ORDER.getType(); // 上传签字版变更评审单
    public static final String USERTASK_QC_UPLOAD_REPORT = BpmTaskDefKey.USERTASK_QC_UPLOAD_REPORT.getType(); // QC上传报告
    public static final String USERTASK_QC_UPLOAD_NG_REPORT = BpmTaskDefKey.USERTASK_QC_UPLOAD_NG_REPORT.getType(); //QC 上传不合格报告

    //用户任务 userTask PMI
    public static final String USERTASK_PMI_SUPERVISOR_CONFIRM = BpmTaskDefKey.USERTASK_PMI_SUPERVISOR_CONFIRM.getType(); // QC主管确认
    public static final String USERTASK_PMI_REPORT_UPLOAD = BpmTaskDefKey.USERTASK_PMI_REPORT_UPLOAD.getType(); // QC上传报告
    public static final String USERTASK_PMI_CHECK = BpmTaskDefKey.USERTASK_PMI_CHECK.getType(); // 系统PMI检查

    //用户任务 材料
    public static final String USERTASK_MATERIAL_ISSUE_PREPARE_RECEIVE = BpmTaskDefKey.USERTASK_MATERIAL_MATCH_SOLUTION.getType();// 材料出库流程中  套料上传套料清单  节点 defkey

    //用户任务 建造
    public static final String USERTASK_WELD_EXECUTE = BpmTaskDefKey.USERTASK_WELD_EXECUTE.getType(); // 焊接执行
    public static final String USERTASK_FOREMAN_DISPATCH = BpmTaskDefKey.USERTASK_FOREMAN_DISPATCH.getType();//班长分配任务
    public static final String USERTASK_SUPERVISOR_CHECK_NG = BpmTaskDefKey.USERTASK_SUPERVISOR_CHECK_NG.getType();//主管复核不合格
    public static final String USERTASK_SUPERVISOR_CONFIRM_RELEASE_LIST = BpmTaskDefKey.USERTASK_SUPERVISOR_CONFIRM_RELEASE_LIST.getType();//生产主管确认放行单&二维码打印
    public static final String USERTASK_SUPERVISOR_CHANGE_CONFIRM = BpmTaskDefKey.USERTASK_SUPERVISOR_CHANGE_CONFIRM.getType();//用户任务-管道主管确定是否创建建造变更
    public static final String USERTASK_FITUP_EXECUTE = BpmTaskDefKey.USERTASK_FITUP_EXECUTE.getType();
    public static final String USERTASK_RECEIVE_CHECK_EXECUTE = BpmTaskDefKey.USERTASK_RECEIVE_CHECK_EXECUTE.getType();//接收检查执行
    public static final String USERTASK_PIPE_PIECE_CUTTING_EXECUTE = BpmTaskDefKey.USERTASK_PIPE_PIECE_CUTTING_EXECUTE.getType();//管子下料执行
    public static final String USERTASK_SPOOL_RELEASE_EXECUTE = BpmTaskDefKey.USERTASK_SPOOL_RELEASE_EXECUTE.getType();//单管放行执行
    public static final String USERTASK_PMI_EXECUTE = BpmTaskDefKey.USERTASK_PMI_EXECUTE.getType();//PMI执行
    public static final String USERTASK_PWHT_EXECUTE = BpmTaskDefKey.USERTASK_PWHT_EXECUTE.getType();//PWHT执行

    //用户任务 质量
    public static final String USERTASK_QC_SUPERVISOR_CONFIRM = BpmTaskDefKey.USERTASK_QC_SUPERVISOR_CONFIRM.getType(); // QC主管确认
    public static final String USERTASK_INSPECTION_RESULT_CONFIRM = BpmTaskDefKey.USERTASK_INSPECTION_RESULT_CONFIRM.getType();//内检结果判定
    public static final String USERTASK_PUNCHLIST_DELEGATE_OTHER = BpmTaskDefKey.USERTASK_PUNCHLIST_DELEGATE_OTHER.getType();
    public static final String USERTASK_PUNCHLIST_CHARGER_ASIGN = BpmTaskDefKey.USERTASK_PUNCHLIST_CHARGER_ASIGN.getType();
    public static final String USERTASK_PMI_APPLY_CONFIRM = BpmTaskDefKey.USERTASK_PMI_APPLY_CONFIRM.getType();


    //用户任务 输入外检意见
    public static final String USERTASK_ENTER_EX_COMMENT = BpmTaskDefKey.USERTASK_ENTER_EX_COMMENT.getType();//"输入外检意见";
    public static final String USERTASK_OWNER_EXTERNAL_INSPECTION = BpmTaskDefKey.USERTASK_OWNER_EXTERNAL_INSPECTION.getType();//业主外检
    public static final String USERTASK_THIRD_EXTERNAL_INSPECTION = BpmTaskDefKey.USERTASK_THIRD_EXTERNAL_INSPECTION.getType();//第三方外检
    public static final String USERTASK_OTHER_EXTERNAL_INSPECTION = BpmTaskDefKey.USERTASK_OTHER_EXTERNAL_INSPECTION.getType();//其他外检
    public static final String USERTASK_OWNER_EXTERNAL_INSPECTION_APPLY = BpmTaskDefKey.USERTASK_OWNER_EXTERNAL_INSPECTION_APPLY.getType();// 业主外检申请
    public static final String USERTASK_THIRD_EXTERNAL_INSPECTION_APPLY = BpmTaskDefKey.USERTASK_THIRD_EXTERNAL_INSPECTION_APPLY.getType();// 第三方外检申请
    public static final String USERTASK_OTHER_EXTERNAL_INSPECTION_APPLY = BpmTaskDefKey.USERTASK_OTHER_EXTERNAL_INSPECTION_APPLY.getType();// 其他外检申请
    public static final String USERTASK_OWNER_EXTERNAL_INSPECTION_COORDINATE = BpmTaskDefKey.USERTASK_OWNER_EXTERNAL_INSPECTION_APPLY.getType();// 业主外检安排
    public static final String USERTASK_THIRD_EXTERNAL_INSPECTION_COORDINATE = BpmTaskDefKey.USERTASK_THIRD_EXTERNAL_INSPECTION_COORDINATE.getType();// 第三方外检安排
    public static final String USERTASK_OTHER_EXTERNAL_INSPECTION_COORDINATE = BpmTaskDefKey.USERTASK_OTHER_EXTERNAL_INSPECTION_COORDINATE.getType();// 其他外检安排

    //材料
    public static final String USERTASK_MATERIAL_ISSUE_RECEIVE = BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_RECEIVE.getType();//仓库接收
    public static final String USERTASK_MATERIAL_ISSUE_CREATE_REQUISITION = BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CREATE_REQUISITION.getType();//领料单生成
    public static final String USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE = BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE.getType();//材料控制工程师接收
    public static final String USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_ISO_COMP = BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_ISO_COMP.getType();//材料控制工程师接收外场管附件
    public static final String USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_SP_COMP = BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_SP_COMP.getType();//材料控制工程师接收内场管附件
    public static final String UT_CONFIRM_MATERIAL_RECEIPT = BpmTaskDefKey.UT_CONFIRM_MATERIAL_RECEIPT.getType();//结构套料材料接收
    public static final String UT_CONFIRM_NESTING_PLAN = BpmTaskDefKey.UT_CONFIRM_NESTING_PLAN.getType();//结构套料材料确认套料方案
    public static final String UT_CREATE_SPM_REQUISITION = BpmTaskDefKey.UT_CREATE_SPM_REQUISITION.getType();//结构套料材料生成SPM领料单
    public static final String UT_CONFIRM_WRITEBACK_VALIDITY = BpmTaskDefKey.UT_CONFIRM_WRITEBACK_VALIDITY.getType();//结构套料材料生成SPM领料单
    public static final String UT_CONFIRM_NESTING_PLAN_AND_CUTTING = BpmTaskDefKey.UT_CONFIRM_NESTING_PLAN_AND_CUTTING.getType();//确认套料方案，直接进入切割工序


    //字符串 常量

    //设计
    public static final String DRAWING_REDMARK = BpmsProcessNameEnum.DRAWING_REDMARK.getType();
    public static final String ENGINEERING = BpmsProcessNameEnum.ENGINEERING.getType();
    public static final String DRAWING_PARTIAL_UPDATE = BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType();
    public static final String DRAWING_INTEGRAL_UPDATE = BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType();
    public static final String ENGINEERING_MTO = BpmsProcessNameEnum.ENGINEERING_MTO.getType();
    public static final String BOM_ISSUE = BpmsProcessNameEnum.BOM_ISSUE.getType();

    //材料
    public static final String MATERIAL_ISSUE = BpmsProcessNameEnum.MATERIAL_ISSUE.getType(); //材料出库
    public static final String MATERIAL_RECEIVE = BpmsProcessNameEnum.MATERIAL_RECEIVE.getType();// MATERIAL_RECEIVE

    //建造
    public static final String HD = BpmsProcessNameEnum.HD.getType();//硬度测试
    public static final String PMI = BpmsProcessNameEnum.PMI.getType();//材质分析
    public static final String FITUP = BpmsProcessNameEnum.FITUP.getType();//组对
    public static final String NDT = BpmsProcessNameEnum.NDT.getType();//NDT
    public static final String CHANGE_LEAD_BY_CONSTRUCTION = BpmsProcessNameEnum.CHANGE_LEAD_BY_CONSTRUCTION.getType(); //建造引起的变更
    public static final String CHANGE_LEAD_BY_DRAWING = BpmsProcessNameEnum.CHANGE_LEAD_BY_DRAWING.getType(); // 设计引起的变更

    public static final String PWHT = BpmsProcessNameEnum.PWHT.getType();//焊后热处理
    public static final String SURFACE_TREATMENT = BpmsProcessNameEnum.SURFACE_TREATMENT.getType();//表面处理
    public static final String WELD = BpmsProcessNameEnum.WELD.getType();//焊接
    public static final String PAINTING = BpmsProcessNameEnum.PAINTING.getType();//涂装
    public static final String SPOOL_TRANSPORT = BpmsProcessNameEnum.SPOOL_TRANSPORT.getType();//单管配送
    public static final String PIPE_PIECE_TRANSPORT = BpmsProcessNameEnum.PIPE_PIECE_TRANSPORT.getType();//管段配送
    public static final String CUTTING = BpmsProcessNameEnum.CUTTING.getType();//下料
    public static final String STRENGTH_TEST = BpmsProcessNameEnum.STRENGTH_TEST.getType();//强度试验
    public static final String SPOOL_RELEASE = BpmsProcessNameEnum.SPOOL_RELEASE.getType();//单管放行
    public static final String PRE_PAINTING_HANDOVER = BpmsProcessNameEnum.PRE_PAINTING_HANDOVER.getType();//预涂装交接
    public static final String POST_PAINTING_HANDOVER = BpmsProcessNameEnum.POST_PAINTING_HANDOVER.getType();//后涂装交接

    public static final String PUNCHLIST = BpmsProcessNameEnum.PUNCHLIST.getType();


    //质量

    public static final String CUTTING_DELIVERY_LIST = "CUTTING_DELIVERY_LIST";//下料切割单
    public static final String FABRICATION = "FABRICATION";
    public static final String SD_DWG_PIPE_FABRICATION = "SD_DWG_PIPE_FABRICATION";
    public static final String SD_DWG_PIPE_SUPPORT_FABRICATION = "SD_DWG_PIPE_SUPPORT_FABRICATION";
    public static final String PIPELINE_CONSTRUCTION = "Pipeline Construction"; // 工序分类-管线建造
    public static final String DELIVERY_LIST = "DELIVERY_LIST";//交接单
    public static final Integer MAX_FLARING_COUNT = 3; // 最大扩口次数
    public static final Integer MAX_REPAIRT_COUNT = 2; // 最大返修次数
    public static final String INTERNAL_INSPECTION_TIME = "internalInspectionTime";

    //通用
    public static final String DELIVER_ENTITY_READY = "#READY"; // 子实体READY
    public static final Long DELIVER_ENTITY_READY_L = -8L;
    public static final Long DUMMY = -100L;
    public static final String COMMA = ",";
    public static final String PC = "pc";
    public static final String PC_BATCH = "pc-batch";
    public static final String ENTITY_MARK_DELETED = "#ENTITY_MARK_DELETED#";
    public static final String YES = "YES";
    public static final String NO = "NO";

    public static final String BUSINESS = "BUSINESS";
    public static final String END = "结束";
    public static String FILE_TYPE_PDF = "pdf";
    public static String FILE_TYPE_PDF_EXTEND = "PDF";

    public static final int MAX_PAGE = 500;
    public static final int REPORT_STATUS = 1;
    public static final String SEPARATOR_MID_BAR_LINE = "-";
    public static final String MATERIAL = "MATERIAL";
    public static final String DRAWING = "DRAWING";


    public static final String CC = "CC";
    public static final String DCRF = "01-0502-PI";
    public static final String SHOP_DRAWING = "SHOP_DRAWING";


    public static final String EMDR = "EMDR";
    public static final String CURRENT = "Current";
    public static final int DD_HEADER = 3;


    public static final String PIPING = "PI";

    public static final String OTHER = "OTHER";

    public static final int SD_HEADER = 4;

    public static final String DWG_NO = "图号 Doc & DWG. NO.";

    public static final int MOD_HEADER = 2;
    public static final String EM = "EM";
    public static final String SD_DWG_CHANGE_VOR_LIST = "SD_DWG_CHANGE_VOR_LIST";

    public static final String SUB_DRAWING_LIST = "ISO图纸清单";
    public static final int SUB_DRAWING_LIST_HEADER = 4;
    public static final String SUB_DRAWING_LIST_PAGE_SPLIT = " OF ";
    public static final int SD_PP_DATA_START_ROW = 4;
    public static final int SD_PP_TEMPLATE_ROW_COUNT = 3;


    public static final String DELETE = "DELETE";

//----

    public static String FILE_TYPE_ZIP = "zip";

    public final static String SUSPEND_MARK = "#SUSPEND-";
    public final static String ACTIVE_MARK = "#ACTIVE-";

    public final static String REVOCATION = "REVOCATION";

    public final static Long VERSION_L = -123L;

    // Material Activity
    public static final String BPM_PROCESS_STAGE_NAME_EN = "STORAGE";

    public static final String BPM_PROCESS_NAME = "MATERIAL_RECEIVE";

    public static final String ENTITY_CATEGORY_RELEASE_NOTE = "RELEASE_NOTE";

    public static final String BPM_PROCESS_STAGE_STORAGE = "STORAGE";

    public static final String BPM_PROCESS_MATERIAL_DISTRIBUTION = "MATERIAL_DISTRIBUTION ";

    public static final Set<String> REJECT_SET = new HashSet<>(Arrays.asList(
        BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_REJECT.getType()
    ));

}
