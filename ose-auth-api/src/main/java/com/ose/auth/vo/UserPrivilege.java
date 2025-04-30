package com.ose.auth.vo;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限标签。
 */
public enum UserPrivilege implements ValueObject {

    NONE("无任何权限", "none"),
    ALL("全部权限", "all"),
    DEPARTMENT("部门操作全部权限", "department"),
    ROLE("角色管理全部权限", "role"),
    MEMBER("组织成员操作全部权限", "member"),
    DOCUMENT("文档操作全部权限", "document"),
    PROJECT("管理项目的权限", "project"),
    ENTITIES_READ("实体层级树只读", "/entities/read"),
    ENTITIES_IMPORT("实体数据导入", "/entities/import"),
    DRAWING_LIST_PIPING_READ("图纸列表只读", "/drawing-list/piping/read"),
    ISO_ENTITIES_READ("管线实体只读", "/iso-entities/read"),
    SPOOL_ENTITIES_READ("单管实体只读", "/spool-entities/read"),
    PIPE_PIECE_ENTITIES_READ("管段实体只读", "/pipe-piece-entities/read"),
    WELD_ENTITIES_READ("焊口实体只读", "/weld-entities/read"),
    COMPONENT_ENTITIES_READ("管件实体只读", "/component-entities/read"),
    QRCODE_SEARCH_READ("下料二维码只读", "/qrcode-search/read"),

    CONSTRUCTION_CHANGE_APPLY_READ("建造变更申请只读", "/construction-change-apply/read"),
    WELD_TEST_RESULTS_READ("焊口测试结果只读", "/weld-test-results/read"),
    ISO_TEST_RESULTS_READ("ISO测试结果只读", "/iso-test-results/read"),
    NPS_READ("NPS壁厚只读", "/nps/read"),
    HOUR_NORM_READ("定额工时只读", "/hour-norm/read"),

    ACTIVITIES_READ("任务流只读", "/activities/read"),
    TASKS_READ("任务只读", "/tasks/read"),
    SCHEDULED_TASKS_READ("定时任务只读", "/scheduled-tasks/read"),
    NDT_TASKS_READ("NDT任务只读", "/ndt-tasks/read"),
    FLARING_INSPECTION_READ("焊口扩口只读", "/penalty-inspections/read"),
    DELIVERIES_READ("交接单只读", "/deliveries/read"),
    CUTTING_READ("下料单只读", "/cutting/read"),

    EXTERNAL_INSPECTION_APPLY_READ("外检申请只读", "/external-inspection-apply/read"),
    EXTERNAL_INSPECTION_SCHEDULE_READ("外交记录只读", "/external-inspection-schedule/read"),
    EXTERNAL_INSPECTION_UPLOAD_HISTORIES_READ("外检上传历史只读", "/external-inspection-upload-histories/read"),
    EXTERNAL_INSPECTION_ISSUES_READ("外检遗留问题只读", "/external-inspection-issues/read"),
    INTERNAL_INSPECTION_ISSUES_READ("内检遗留问题只读", "/internal-inspection-issues/read"),

    WELDERS("焊工", "/welder/read"),
    SUBCONS_READ("分包商只读", "/subcon/read"),
    WPS_READ("焊接工艺规程只读", "/wps/read"),
    PQRS_READ("PQR", "/pqrs/read"),
    WPS_POSITIONS_READ("焊接工艺规程位置只读", "/wps-positions/read"),
    WPS_PROCESSES_READ("焊接工艺规程工序只读", "/wps-processes/read"),
    WPS_BEVEL_TYPES_READ("焊接工艺规程层及类型只读", "/wps-bevel-types/read"),
    WPS_JOINT_TYPES_READ("焊接工艺规程连接类型只读", "/wps-joint-types/read"),
    WPS_BASE_METALS_READ("焊接工艺规程母材只读", "/wps-base-metals/read"),
    WPS_BASE_METAL_GROUPS_READ("焊接工艺规程母材组别只读", "/wps-base-metal-groups/read"),
    WPS_FILLER_METALS_READ("焊接工艺规程填充材料只读", "/wps-filler-metals/read"),
    BOMNODES_READ("BOMNODES", "/bomnodes/read"),
    REQUISITION_READ("申请书只读", "/requisition/read"),
    CONTRACT_READ("合同只读", "/contract/read"),
    RELEASE_PERMIT_READ("许可证发行只读", "/release-permit/read"),
    MATERIAL_STOCKTAKES_READ("材料存货只读", "/material-stocktakes/read"),
    MATERIAL_OPEN_BOXES_READ("材料打开", "/material-open-boxes/read"),
    MATERIAL_RECEIVE_RECEIPTS_READ("材料接收收据只读", "/material-receive-receipts/read"),
    INVENTORY_READ("库存只读", "/inventory/read"),
    INVENTORY_SEARCH_READ("余料查询", "/inventorySearch/read"),
    INVENTORY_SURE_READ("余料确认", "/inventorySure/read"),
    INVENTORY_OUT_READ("余料出库", "/inventoryOut/read"),
    MATERIAL_PREPARES_READ("材料准备只读", "/material-prepares/read"),
    MATERIAL_ISSUE_RECEIPTS_READ("材料问题接收只读", "/material-issue-receipts/read"),
    MATERIAL_TRANSFERS_READ("材料运输只读", "/material-transfers/read"),
    MATERIAL_CODING_TEMPLATES_READ("材料模板只读", "/material-coding-templates/read"),
    MATERIAL_REQUISITION_READ("材料领料单只读", "/material-requisition/read"),
    MATERIAL_RETURN_READ("材料退库清单只读", "/material-return/read"),
    OWNER_QC_COORDINATOR("船东QC协调员", "/material-owner-qc/read"),

    FILE_READ("文件只读", "/files/read"),
    CHECKLISTS_READ("检查单只读", "/checklists/read"),
    SIMULATIONS_READ("模拟只读", "/simulations/read"),
    WBS_READ("WBS只读", "/wbs/read"),
    MODULE_PROCESS_DEFINITIONS_READ("模块工序定义", "/module-process-definitions/read"),
    MEMBERS_READ("成员只读", "/members/read"),
    ROLES_READ("角色只读", "/roles/read"),
    ORGS_READ("组织管理只读", "/orgs/read"),
    ENTITY_CATEGORY_TYPES_READ("实体类型分类只读", "/entity-types/read"),
    ENTITY_CATEGORIES_READ("实体类型只读", "/entity-sub-types/read"),
    PROCESS_STAGES_READ("工序阶段只读", "/process-stages/read"),
    STAGE_VERSION_READ("工序阶段版本只读", "/stage-version/read"),
    PROCESS_CATEGORIES_READ("工序分类只读", "/process-categories/read"),
    PROCESS_READ("工序只读", "/processes/read"),
    BIZ_CODE_TYPES_READ("业务代码类型只读", "/biz-code-types-read"),
    BIZ_CODE_READ("业务代码只读", "/biz-code/read"),
    DRAWING_LIST_PIPING_IMPORT("图纸导入权限", "/drawing-list/piping/import"),
    WBS_IMPORT("计划导入权限", "/wbs/import"),
    EXPERIENCE_READ("经验教训只读", "/experience/read"),
    NOTIFICATION_READ("消息只读", "/notification/read"),

    WORKING_HOUR_READ("工时只读", "/working-hour/read"),
    TASK_PACKAGE_READ("任务包只读", "/task-package/read"),
    WORK_SITES_READ("工作场地只读", "/work-sites/read"),
    PRINTERS_READ("打印机只读", "/printers/read"),

    DRAWING_LIST_IMPORT("图纸导入", "/drawing-list/import"),
    DRAWING_LIST_READ("图纸查看", "/drawing-list/read"),
    DRAWING_LIST_PMT_READ("PMT图纸查看", "/drawing-list-pmt/read"),
    DRAWING_TOPOLOGY_READ("图纸层级", "/drawing-topology/read"),
    DRAWING_IDC_READ("图纸IDC", "/drawing-idc/read"),
    DRAWING_PDF_READ("图纸PDF", "/drawing-pdf/read"),

    // 材料菜单
    MM_PURCHASE_PACKAGE(
        "采购包", "/mm-purchase-package/read"
    ),
    MM_PROJECT_VENDOR(
        "项目供货商", "/mm-project-vendor/read"
    ),
    MM_COMPANY_VENDOR(
        "公司供货商", "/mm-company-vendor/read"
    ),
    MM_COMPANY_MATERIAL_CODE_TYPE(
        "公司级材料代码类型", "/mm-company-material-code-type/read"
    ),
    MM_COMPANY_MATERIAL_CODE(
        "公司级材料代码", "/mm-company-material-code/read"
    ),
    MM_PROJECT_MATERIAL_CODE_TYPE(
        "项目级材料代码类型", "/mm-project-material-code-type/read"
    ),
    MM_PROJECT_MATERIAL_CODE(
        "项目级材料代码", "/mm-project-material-code/read"
    ),
    MM_PROJECT_WARE_HOUSE(
        "项目级库位货位", "/mm-project-ware-house/read"
    ),
    MM_PROJECT_SHIPPING(
        "项目级发货单", "/mm-project-shipping/read"
    ),
    MM_PROJECT_RECEIVE(
        "项目级入库单", "/mm-project-receive/read"
    ),
    MM_PROJECT_STOCK(
        "项目级在库材料", "/mm-project-stock/read"
    ),
    MM_PROJECT_ISSUE(
        "项目级出库单", "/mm-project-issue/read"
    ),
    MM_PROJECT_HEAT_BATCH(
        "项目级炉批号", "/mm-project-heat-batch/read"
    ),
    MM_PROJECT_CERTIFICATE(
        "项目级证书", "/mm-project-certificate/read"
    ),

//    COMPANY_WARE_HOUSE_READ("公司材料类型只读", "/company-ware-house/read"),
//    COMPANY_MATERIAL_CODE_READ("公司材料编码只读", "/company-material-code/read"),
//    PROJECT_WARE_HOUSE_READ("项目材料类型只读", "/project-ware-house/read"),
//    PROJECT_MATERIAL_CODE_READ("项目材料编码只读", "/project-material-code/read"),
//    WARE_HOUSE_LOCATION_READ("仓库只读", "/ware-house-location/read"),
//    SHIPPING_READ("发货单只读", "/shipping/read"),
//    RECEIVE_NOTE_READ("入库放行单只读", "/receive-note/read"),
//    MATERIAL_STOCK_READ("在库材料只读", "/material-stock/read"),
//    MATERIAL_ISSUE_READ("出库单只读", "/material-issue/read"),
//    HEAT_BATCH_NO_READ("炉批号只读", "/heat-batch-no/read"),
//    MATERIAL_CERTIFICATE("材料证书只读", "/material-certificate/read"),

    NOTIFICATION_CONFIGURATION_READ("消息配置只读", "/notification-configuration/read"),

    CLEAN_PACKAGE_ENTITIES_READ("清洁包只读", "/clean-package-entities/read"),
    PRESSURE_TEST_PACKAGE_ENTITIES_READ("试压包只读", "/pressure-test-package-entities/read"),
    SUB_SYSTEM_ENTITIES_READ("子系统只读", "/sub-system-entities/read"),


    PMI_TASK_READ("PMI任务", "/pmi-tasks/read"),
    QC_REPORT_READ("QC报告", "/qc-report/read"),
    EXTERNAL_INSPECTION_MAIL_READ("外检邮件记录", "/external-inspection-mail/read"),
    STATISTICS_READ("数据统计", "/statistics/read"),
    PROGRESS_READ("模块进度总览表", "/progress-shipment/read"),

    /*------------------WRITE Privilege---------------------*/
    ENTITY_EDIT("管线实体类型编辑", "/entity-edit"),
    ENTITY_DELETION("管线实体类型删除", "/entity-deletion"),
    TASK_DELETION("任务删除", "/task-deletion"),
    WPS_MAPPING("WPS匹配", "/wps-mapping"),
    HIERARCHY_CONFIG("层级配置", "/hierarchy-config"),
    QRCODE_PRINT("二维码打印", "/qrcode-print"),
    REPAIR_QCREPORT("qc报告修复", "/repair-qcreport"),
    CONSTRUCTION_DELETION("建造变更申请单删除", "/construction-deletion"),


    NPS_EDIT("管材壁厚编辑", "/nps-edit"),
    NPS_DELETION("管材壁厚删除", "/nps-deletion"),
    MAN_HOUR_EDIT("定额工时编辑", "/man-hour-edit"),
    MAN_HOUR_DELETION("定额工时删除", "/man-hour-deletion"),

    TASK_EDIT("任务编辑", "/task-edit"),
    TASK_ASSIGNEE("任务分配", "/task-assignee"),
    FLARING_OPERATION("扩口权限", "/penalty-operation"),
    EXTERNAL_INSPECTION_MANAGEMENT("外检管理", "/external-inspection-management"),

    ISSUE_EDIT("遗留问题编辑", "/issue-edit"),
    ISSUE_TRANSFER("遗留问题移交", "/issue-transfer"),
    WPS_IMPORT("wps导入", "/wps-import"),
    WELD_EDIT("焊接编辑", "/weld-edit"),
    WELD_DELETION("焊接删除", "/weld-deletion"),
    EXPERIENCE_EDIT("经验教训编辑", "/experience-edit"),
    EXPERIENCE_DELETION("经验教训删除", "/experience-deletion"),


    TASK_PACKAGE_EDIT("任务包编辑", "/task-package-edit"),
    TASK_PACKAGE_DELETION("任务包删除", "/task-package-deletion"),
    TASK_PACKAGE_START_PLAN("任务包启动计划", "/task-package-start-plan"),

    TASK_START("任务启动", "/task-start"),

    ENTITY_CATEGORY_EDIT("实体类型编辑", "/entity-category-edit"),
    ENTITY_CATEGORY_DELETION("实体类型删除", "/entity-category-deletion"),

    PROCESS_EDIT("工序编辑", "/process-edit"),
    PROCESS_DELETION("工序删除", "/process-deletion"),

    DRAWING_OPERATION("图纸操作", "/drawing-operation"),
    DRAWING_DELETION("图纸删除", "/drawing-deletion"),

    DRAWING_VERSION("图纸删除", "/drawing-version"),
    DRAWING_EXPORT("图纸导出", "/drawing-export"),
    START_WORK_PACKAGE_TASK("图纸导出", "/start-work-package-task"),

    TASK_AGENT("任务代理", "/task-agent/read"),
    MATERIAL_STRUCTURE_NESTING("结构套料方案", "/material-structure-nesting/read"),
    STRUCTURE_HANDLE("结构下料单生成", "/structure-handle/read"),
    STRUCTURE_LIST("结构下料单列表", "/structure-list/read"),
    DRAWING_MODIFY("图纸修改", "/drawing-modify/read"),
    DRAWING_CHECK("图纸校对", "/drawing-check/read"),
    DRAWING_REVIEW("图纸审核", "/drawing-review/read"),
    STRUCTURE_MODULE("结构模块", "/structure-module-entities/read"),
    STRUCTURE_SECTION("结构分片", "/structure-section-entities/read"),
    STRUCTURE_PANEL("结构分段", "/structure-panel-entities/read"),
    STRUCTURE_COMPONENT("结构构件", "/structure-component-entities/read"),
    STRUCTURE_PART("结构零件", "/structure-part-entities/read"),
    STRUCTURE_WELD("结构焊口", "/structure-weld-entities/read"),
    STRUCTURE_FITTING("结构杂件", "/structure-fitting-entities/read"),
    DRAWING_BATCH_TASK("图纸批处理任务", "/drawing-batch-task/read"),
    SUBDRAWING_SEARCH("子图纸查询", "/subDrawing-search/read"),
    DRAWING_APPOINT("图纸指派", "/drawing-appoint/read"),
    DRAWING_EXAMINE("图纸指派", "/drawing-examine/read"),

    REPORT_CONFIG_EDIT("报告配置编辑", "/reportConfig-edit"),
    REPORT_CONFIG_DELETION("报告配置删除", "/reportConfig-deletion"),

    START_PACKAGE("启动打包", "/start-package"),
    END_PACKAGE("停止打包", "/end-package"),

    QRCODES_RELEASE("放行查询界面", "/qrcode-release/read"),
    WELDER_WELD_SEARCH("焊接记录查询界面", "/welder-weld-search/read"),
    WELDER_GRADE("焊工等级管理界面", "/welder-grade/read"),

    MATERIAL_SURPLUS_PREPARES("余料领料单","/material-surplus-prepares/read"),
    MATERIAL_SURPLUS_JACKING("余料套料清单", "/material-surplus-jacking/read"),

    OUT_FITTING_STATUS("舾装件状态跟踪","/out-fitting-status/read"),
    EQUIPMENT_STATUS("设备状态跟踪","/equipment-status/read"),
    CABLE_STATUS("电缆状态跟踪","/cable-status/read"),
    ENTITY_STATUS("数据导入","/entity-status/read"),

    WELD_MATERIAL("焊材管理","/weld-material/read"),


    /*---------------------------WRITE -------------------------------*/

    ////工组流权限分配用 start
    BUILDING("建造工作流所有权限", "building"),
    DESIGN("设计/文控工作流所有权限", "design"),
    PURCHASE("采购/仓储工作流所有权限", "purchase"),
    QUALITY("质量工作流所有权限", "quality"),


    PURCHASE_MANAGER("采购经理", "/purchase-manager", ExecutorRole.PROCURE_MANAGER),
    PURCHASE_ENGINEER("采购工程师", "/purchase-engineer", ExecutorRole.PROCURE_ENGINEER),
    DESIGN_MANAGER("设计经理", "/design-manager", ExecutorRole.ENGINEERING_MANAGER),
    DESIGN_SUPERVISOR("设计主管", "/design-supervisor", ExecutorRole.SUPERVISOR),
    DESIGN_ENGINEER_EXECUTE("图纸设计", "/design-engineer-execute", ExecutorRole.DESIGNER),

    CONSTRUCTION_MANAGER_EXECUTE("建造经理执行", "/construction-manager-execute", ExecutorRole.CONSTRUCTION_MANAGER),
    PROJECT_MANAGER_EXECUTE("项目经理执行", "/project-manager-execute", ExecutorRole.PROJECT_MANAGER),
    DESIGN_DIRECTOR_EXECUTE("设计主任执行", "/design-director-execute", ExecutorRole.SUPERVISOR),
    DUMMY_EXECUTE("执行人", "/dummy-execute"),
    QC_MANAGER_EXECUTE("QC经理执行", "/qc-manager", ExecutorRole.QC_MANAGER),

    SAFETY_SUPERVISOR_EXECUTE("安全主管执行", "/safety-supervisor-execute", ExecutorRole.HSE),
    SUPERVISOR_EXECUTE("主管执行", "/supervisor-execute", ExecutorRole.SUPERVISOR),
    QC_EXECUTE("QC执行", "/qc-execute", ExecutorRole.QC),
    SHIPYARD_QC_EXECUTE("船厂QC执行", "/shipyard-qc-execute", ExecutorRole.SHIPYARD_QC),
    OWNER_QC_EXECUTE("船东QC", "/owner-qc-execute", ExecutorRole.OWNER_QC),
    COORDINATE_EXECUTE("协调员执行", "/coordinate-execute", ExecutorRole.QC),
    FOREMAN_EXECUTE("班组长执行", "/foreman-execute", ExecutorRole.FOREMAN),
    GROUP_LEADER_EXECUTE("小组长执行", "/group-leader-execute", ExecutorRole.FOREMAN),
    WELD_EXECUTE("焊工执行", "/weld-execute", ExecutorRole.WELDER),
    FITUP_EXECUTE("装配工执行", "/fitup-execute", ExecutorRole.FITTER),
    CUTTING_EXECUTE("下料工执行", "/cutting-execute", ExecutorRole.FITTER),
    BEVEL_EXECUTE("坡口工执行", "/bevel-execute", ExecutorRole.FITTER),
    BEND_EXECUTE("弯管工执行", "/bend-execute", ExecutorRole.FITTER),
    NDT_EXECUTE("NDT工程师执行", "/ndt-execute", ExecutorRole.NDT_ENGINEER),
    HD_EXECUTE("硬度测试工执行", "/hd-execute", ExecutorRole.QC),
    PMI_EXECUTE("材质分析质检工执行", "/pmi-execute", ExecutorRole.QC),
    // ADD WZ 20181219 START
    BEVEL_GROUND_EXECUTE("打磨工执行", "/bevel-ground-execute", ExecutorRole.FITTER),
    STRENGTH_TEST_EXECUTE("强度测试工执行", "/strength-test-execute", ExecutorRole.FITTER),
    SPOOL_INSTALL_EXECUTE("管道安装工执行", "/spool-install-execute", ExecutorRole.FITTER),
    JOINT_CONNECTION_EXECUTE("管路连接工执行", "/joint-connection-execute", ExecutorRole.FITTER),
    FLANGE_MANAGEMENT_EXECUTE("法兰管理工执行", "/flange-management-execute", ExecutorRole.FITTER),
    TOUCHUP_EXECUTE("补漆工执行", "/touchup-execute", ExecutorRole.FITTER),
    TRANSPORT_EXECUTE("配送工执行", "/transport-execute", ExecutorRole.FITTER),
    SUPPORT_CUTTING_EXECUTE("支架下料工执行", "/support-cutting-execute", ExecutorRole.FITTER),
    SUPPORT_FABRICATE_EXECUTE("支架预制工执行", "/support-fabricate-execute", ExecutorRole.FITTER),
    SUPPORT_INSTALL_EXECUTE("支架安装工执行", "/support-install-execute", ExecutorRole.FITTER),
    DECK_PERFORATION_EXECUTE("甲板开孔工执行", "/deck-perforation-execute", ExecutorRole.FITTER),
    //    PRESS_TEST_EXECUTE("试压工执行", "/press-test-execute", false),
    PRESSURE_TEST_EXECUTE("试压工执行", "/pressure-test-execute", ExecutorRole.FITTER),
    AIR_BLOW_EXECUTE("管路吹扫工执行", "/air-blow-execute", ExecutorRole.FITTER),
    WATER_FLUSH_EXECUTE("管路串水工执行", "/water-flush-execute", ExecutorRole.FITTER),
    OIL_FLUSH_EXECUTE("管路串油工执行", "/oil-flush-execute", ExecutorRole.FITTER),
    AIR_TIGHTNESS_EXECUTE("子系统密性工执行", "/air-tightness-execute", ExecutorRole.FITTER),
    SHIELD_INSTALL_EXECUTE("外护层预制工执行", "/shield-install-execute", ExecutorRole.FITTER),
    CHECK_MC_EXECUTE("机械完工检查工执行", "/check-mc-execute", ExecutorRole.FITTER),
    BOX_EXECUTE("打包工执行", "/box-execute", ExecutorRole.FITTER),
    // ADD WZ 20181219 END
    PWHT_EXECUTE("焊后热处理工执行", "/pwht-execute", ExecutorRole.FITTER),
    BPM_TASK_CATEGORY_NOT_FOUND("工作流任务权限未定义", "/bpm-task-category-not-found"),
    NDT_SUPERVISOR_EXECUTE("NDT主管执行", "/ndt-supervisor-execute", ExecutorRole.SUPERVISOR),
    PMI_SUPERVISOR_EXECUTE("PMI主管执行", "/pmi-supervisor-execute", ExecutorRole.SUPERVISOR),
    HD_SUPERVISOR_EXECUTE("HD主管执行", "/hd-supervisor-execute", ExecutorRole.SUPERVISOR),

    MATERIAL_ISSUE_WAREHOUSE_OPERATOR("库管执行", "/material-issue-warehouse-operator", ExecutorRole.KEEPER),
    NESTING_EXECUTE("套料", "/nesting-execute"),
    MATERIAL_ISSUE_CONTROL_EXECUTE("材料控制工程师执行", "/material-issue-control-execute", ExecutorRole.MATERIAL_CONTROLLER),
    MATERIAL_REQUISITION_EXECUTE("领料人执行", "/material-requisition-execute", ExecutorRole.MATERIAL_CONTROLLER),
    RETURN_INSPECTION_EXECUTE("质量检验验收执行", "/return-inspection-execute", ExecutorRole.QC),
    RETURN_SACN_EXECUTE("库存材料扫描执行", "/return-sacn-execute", ExecutorRole.KEEPER),
    CONSTRUCT_SUPERVISOR_EXECUTE("生产主管执行", "/construct-supervisor-execute", ExecutorRole.SUPERVISOR),

    TRANSPORT_SUPERVISOR_EXECUTE("配送主管执行", "/transport-supervisor-execute", ExecutorRole.SUPERVISOR),


    DRAWING_CHECK_EXECUTE("图纸校对", "/drawing-check-execute", ExecutorRole.CHECKER),
    DRAWING_QC_EXECUTE("设计QC校对", "/drawing-qc-execute", ExecutorRole.QC),
    DRAWING_COSIGN_EXECUTE("图纸会签", "/drawing-cosign-execute", ExecutorRole.QC),
    DRAWING_REVIEW_EXECUTE("图纸审核", "/drawing-review-execute", ExecutorRole.REVIEWER),
    DOCUMENT_CONTROL_EXECUTE("文档控制打印下发", "/document-control-execute", ExecutorRole.DCC),

    DRAWING_APPROVE_EXECUTE("图纸批准", "/drawing-approve-execute", ExecutorRole.APPROVOR),

    GOE_APPROVE_EXECUTE("图纸批准", "/goe-approve-execute", ExecutorRole.APPROVOR),
    DESIGN_DOCUMENT_CONTROL_EXECUTE("设计文档员执行", "/design-document-control-execute", ExecutorRole.DCC),
    CONSTRUCTION_DOCUMENT_CONTROL_EXECUTE("项目文档员执行", "/construction-document-control-execute", ExecutorRole.DCC),

    RECEIVE_CHECK_EXECUTE("配送接收盘点执行", "/receive-check-execute", ExecutorRole.FITTER), // 20190128 ADD
    DRAWING_ISSUE_EXECUTE("图纸签发执行", "/drawing-issue-execute", ExecutorRole.DCC), // 20190128 ADD
    SPOOL_RELEASE_EXECUTE("管道放行工执行", "/spool-release-execute", ExecutorRole.FITTER), // 20190128 ADD
    DATA_ENTRY_EXECUTE("数据录入员执行", "/data-entry-execute", ExecutorRole.DATA_INPUTOR), // 20190131 ADD
    PAINTING_SUPERVISOR_EXECUTE("涂装主管执行", "/painting-supervisor-execute", ExecutorRole.SUPERVISOR), // 20190208 ADD
    PAINTING_DIRECTOR_EXECUTE("涂装车间主任执行", "/painting-director-execute", ExecutorRole.SUPERVISOR), // 20190208 ADD
    PLAN_ENGINEER_EXECUTE("计划工程师执行", "/plan-engineer-execute", ExecutorRole.PLANNER),

    DOCUMENT_PRINT_CONTROL_EXECUTE("文档打印员执行", "/document-print-control-execute", ExecutorRole.DCC),

    CUTTING_SUPERVISOR_EXECUTE("下料主管执行", "/cutting-supervisor-execute", ExecutorRole.SUPERVISOR),
    CUTTING_MATERIAL_RECEIVER_EXECUTE("下料组材料员执行", "/cutting-material-receiver-execute", ExecutorRole.FITTER),
    FACILITY_SUPERVISOR_EXECUTE("机具主管执行", "/facility-supervisor-execute", ExecutorRole.SUPERVISOR),
    FACILITY_OPERATOR_EXECUTE("机具司机执行", "/facility-operator-execute", ExecutorRole.FITTER),

    HSE_SUPERVISOR_EXECUTE("安全主管执行", "/hse-supervisor-execute",ExecutorRole.HSE),

    PAINTING_EXECUTE("油漆工执行", "/painting-execute", ExecutorRole.FITTER),

    QC_SUPERVISOR_EXECUTE("QC主管执行", "/qc-supervisor-execute", ExecutorRole.QC),
    ////工组流权限分配用 end

    /*------------------global menu Privilege---------------------*/

    GLOBAL_USERS_READ("全局界面User只读", "/global/users/read"),
    GLOBAL_PROJECTS_READ("全局界面projects只读", "/global/projects/read"),
    GLOBAL_DEPARTMENTS_READ("全局界面departments只读", "/global/departments/read"),
    GLOBAL_MESSAGE_READ("全局界面message只读", "/global/message/read"),
    GLOBAL_ATTENDANCE_MONTHLY_READ("全局界面attendance monthly只读", "/global/attendance/monthly/read"),
    GLOBAL_ATTENDANCE_READ("全局界面attendance只读", "/global/attendance/read"),
    GLOBAL_DIVISION_READ("全局界面division只读", "/global/division/read"),
    GLOBAL_FILE_READ("全局界面file只读", "/global/file/read"),
    GLOBAL_HOLIDAY_READ("全局界面holiday只读", "/global/holiday/read"),
    GLOBAL_REPORTS_READ("全局界面reports只读", "/global/reports/read"),
    GLOBAL_OVERTIME_READ("全局界面overtime只读", "/global/overtime/read"),
    GLOBAL_STATISTICS_READ("全局界面statistics只读", "/global/statistics/read"),
    GLOBAL_SUGGESTION_READ("全局界面suggestion只读", "/global/suggestion/read"),
    GLOBAL_TIMESHEET_SUMMARY_READ("全局界面timesheet summary只读", "/global/timesheet/summary/read"),
    GLOBAL_VIEW_HOUR_READ("全局界面view hour只读", "/global/view/hour/read"),
    GLOBAL_VIEW_OVERTIME_READ("全局界面view overtime只读", "/global/view/overtime/read"),
    GLOBAL_WORKDAY_READ("全局界面workday只读", "/global/workday/read"),
    GLOBAL_HOUR_READ("全局界面hour只读", "/global/hour/read"),

    /*------------------global action Privilege---------------------*/
    GLOBAL_MESSAGE_SEND("全局界面 message send email", "/global/message/send"),
    GLOBAL_MESSAGE_GENERATE("全局界面 message generate", "/global/message/generate"),
    GLOBAL_SUGGESTION_OPERATION("全局界面 suggestion operation", "/global/suggestion/operation");

    private String displayName;
    private String code;
    private ExecutorRole executorRole;

    UserPrivilege(String displayName, String code) {
        this(displayName, code, null);
    }

    UserPrivilege(String displayName, String code, ExecutorRole executorRole) {
        this.displayName = displayName;
        this.code = code;
        this.executorRole = executorRole;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.code;
    }


    public String getCode() {
        return code;
    }


    public ExecutorRole getExecutorRole() {
        return this.executorRole;
    }
    /**
     * 将权限数组转为字符串集合。
     *
     * @param privileges 权限数组
     * @return 权限字符串集合
     */
    public static Set<String> toNameSet(UserPrivilege[] privileges) {

        Set<String> set = new HashSet<>();

        for (UserPrivilege privilege : privileges) {
            set.add(privilege.name());
        }

        return set;
    }

    /**
     * 根据名称获取权限值。
     *
     * @param name 权限名称
     * @return 权限值
     */
    public static UserPrivilege getByName(String name) {

        for (UserPrivilege privilege : UserPrivilege.values()) {
            if (privilege.name().equals(name)) {
                return privilege;
            }
        }
        System.out.println(name +" value not one of declared Enum");
        throw new Error(name +" value not one of declared Enum");
    }

    /**
     * 根据CODE获取权限值。
     *
     * @param code 权限CODE
     * @return 权限值
     */
    public static UserPrivilege getByCode(String code) {

        for (UserPrivilege privilege : UserPrivilege.values()) {
            if (privilege.code.equals(code)) {
                return privilege;
            }
        }

        throw new Error("value not one of declared Enum");
    }

    /**
     * 根据CODE列表获取权限值列表。
     *
     * @param codes 权限CODE列表
     * @return 权限值列表
     */
    public static List<UserPrivilege> getByCodes(List<String> codes) {

        List<UserPrivilege> userPrivileges = new ArrayList<>();

        for (UserPrivilege privilege : UserPrivilege.values()) {
            for (String code : codes) {
                if (privilege.code.equals(code)) {
                    userPrivileges.add(privilege);
                }
            }
        }

        if (userPrivileges.size() != codes.size()) {
            throw new Error("value not one of declared Enum");
        }

        return userPrivileges;
    }

    public static UserPrivilege valueof(String privilege) {
        if(StringUtils.isEmpty(privilege)) return null;
        if(privilege.contains("/")) {
            privilege = privilege.substring(0,privilege.indexOf("/"));
        }
        try {
            return UserPrivilege.valueOf(privilege);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
