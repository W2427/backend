package com.ose.tasks.vo.setting;

import com.ose.vo.ValueObject;

/**
 * 批处理任务代码。
 */
public enum BatchTaskCode implements ValueObject {

    PROJECT_HIERARCHY_IMPORT("Project hierarchy import"),
    PROJECT_ENTITIES_IMPORT("项目实体导入"),
    PROJECT_WP01_ENTITIES_IMPORT("WP01 entities import"),
    PROJECT_WP02_ENTITIES_IMPORT("WP02 entities import"),
    PROJECT_WP03_ENTITIES_IMPORT("WP03 entities import"),
    PROJECT_WP04_ENTITIES_IMPORT("WP04 entities import"),
    PROJECT_WP05_ENTITIES_IMPORT("WP05 entities import"),

    PROJECT_SW_ENTITIES_IMPORT("结构焊口 entities import"),

    PROJECT_ISO_ENTITIES_IMPORT("ISO entities import"),
    PROJECT_SPOOL_ENTITIES_IMPORT("SPOOL entities import"),
    PROJECT_PP_ENTITIES_IMPORT("PIPE PIECE entities import"),
    PROJECT_WELD_ENTITIES_IMPORT("WELD entities import"),
    PROJECT_COM_ENTITIES_IMPORT("COMPONENT entities import"),
    PROJECT_MODULE_RELATION_IMPORT("项目发运批次号导入"),
    WELDER_IMPORT("项目焊工导入"),
    PROJECT_PLAN_IMPORT("项目计划导入"),
    WELD_WELDER_IMPORT("焊工焊口导入"),
    PROJECT_STRUCTURE_NEST_IMPORT("结构套料导入"),
    PROJECT_STRUCTURE_NEST_UPDATE("结构套料更新"),
    PROJECT_TASK_GENERATE("Batch task generate"),
    ENTITY_PROCESS_WBS_GENERATE("项目实体工序计划生成"),
    DRAWING_LIST_IMPORT("DED list import"),
    PERFORMANCE_LIST_IMPORT("PERFORMANCE list import"),
    PED_DRAWING_LIST_IMPORT("PED list import"),
    DETAIL_DESIGN_IMPORT("详设图纸清单导入"),
    DRAWING_PACKAGE("图纸打包"),
    DRAWING("图纸"),
    EFFECTIVE_DRAWING_PACKAGE("有效的图纸打包"),
    PLAN("四级计划"),
    DELIVERY("放行"),
    EXTERNAL_INSPECTION_APPLY("外检申请"),

    GOE_APPROVE("GOE审核"),
    XLS_EXPORT("excel导出"),
    WBS_ENTRY("四级计划"),
    TASK_PACKAGE("任务包"),
    MATERIAL("材料"),
    HIERARCHY("层级"),
    REPAIR("修复功能"),
    STRUCT_ENTITY("结构实体"),
    BOM_PERCENT_UPDATE("更新BOM节点匹配百分比"),
    TEST("测试"),
    SACS_UPLOAD("sacs文件上传"),
    DOCUMENT_UPLOAD("公司文件上传"),
    ;
;//ftjftj

    private String displayName;

    BatchTaskCode(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
