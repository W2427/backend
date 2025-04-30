package com.ose.report.vo;

import com.ose.vo.ValueObject;

/**
 * 统计数据归档类型。
 */
@SuppressWarnings("CheckStyle")
public enum ArchiveDataGroupKey implements ValueObject {

    AREA("area", "area", "areas", "区域 No."),
    MODULE("module", "module", "modules", "模块 No."),
    DECK("deck", "deck", "decks", "甲板 No."),
    ADDRESS("address", "address", "address", "地址"),
    PRESSURE_TEST_PACKAGE("pressure_test_package", "pressureTestPackage", "pressure-test-packages", "试压包 No."),
    SUB_SYSTEM("sub_system", "subSystem", "sub-systems", "子系统 No."),
    STAGE("stage", "stage", "stages", "工序阶段名称"),
    PROCESS("process", "process", "processes", "工序名称"),
    WELD_TYPE("weld_type", "weldType", "weld-types", "焊口类型"),
    ISSUE_LEVEL("level", "issueLevel", "issue-levels", "遗留问题等级"),
    ISSUE_TYPE("issue_type", "issueType", "issue-types", "遗留问题类型"),
    ENTITY_NPS("entity_nps", "entityNps", "entity-npses", "实体寸径"),
    ENTITY_LENGTH("entity_length", "entityLength", "entity-lengths", "实体长度"),
    ENTITY_MATERIAL("entity_material", "entityMaterial", "entity-materials", "实体材质"),
    SUBCONTRACTOR_ID("subcontractor_id", "subcontractorId", "subcontractors", "工程建造分包商 ID"),
    DEPARTMENT_ID("department_id", "departmentId", "departments", "责任部门 ID"),
    DEPARTMENT_NAME("department_name", "departmentName", "department-name", "责任部门 名"),
    WELDER_ID("welder_id", "welderId", "welder", "焊工 ID"),
    WELDER_NO("welder_no", "welderNo", "welder-no", "焊工 号"),
    WELDER_NAME("welder_name", "welderName", "welder-name", "焊工 名"),
    NDT_TYPE("ndt_type", "ndtType", "ndt-type", "NDT分类"),
    USER_NAME("user_name", "userName", "user-name", "责任者 名"),
    TEAM_NAME("team_name", "teamName", "team-name", "班组 名"),
    TASK_PACKAGE_NAME("task_package_name", "taskPackageName", "task-package-name", "任务包 名"),
    MANAGER_ID("manager_id", "managerId", "managers", "管理者用户 ID"),
    ENTITY_CATEGORY("entity_category", "entitySubType", "entity-sub-types", "实体类型"),
    DISCIPLINE_CODE("discipline_code", "disciplineCode", "discipline-code", "专业"),
    DOCUMENT_TYPE("document_type", "documentType", "document-type", "文档类型"),
    ENGINEERING_CATEGORY("engineering_category", "engineeringCategory", "engineering-categories", "设计类别"),
    WEEKLY("weekly", "weekly", "weekly", "周次"),
    DIVISION("division", "division", "division", "部门"),
    PROJECT_NAME("project_name", "projectName", "project-name", "项目");

    private String displayName;
    private String fieldName;
    private String propertyName;
    private String pathVariableName;

    ArchiveDataGroupKey(String fieldName, String propertyName, String pathVariableName, String displayName) {
        this.displayName = displayName;
        this.fieldName = fieldName;
        this.propertyName = propertyName;
        this.pathVariableName = pathVariableName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String toString() {
        return pathVariableName;
    }

    /**
     * 根据字段名取得枚举值。
     *
     * @param fieldName 字段名
     * @return 枚举值
     */
    public static ArchiveDataGroupKey getByFieldName(String fieldName) {
        for (ArchiveDataGroupKey key : ArchiveDataGroupKey.values()) {
            if (key.fieldName.equals(fieldName)) {
                return key;
            }
        }
        return null;
    }

    /**
     * 根据属性名取得枚举值。
     *
     * @param propertyName 路径参数名
     * @return 枚举值
     */
    public static ArchiveDataGroupKey getByPropertyName(String propertyName) {
        for (ArchiveDataGroupKey key : ArchiveDataGroupKey.values()) {
            if (key.propertyName.equals(propertyName)) {
                return key;
            }
        }
        return null;
    }

    /**
     * 根据路径参数名取得枚举值。
     *
     * @param pathVariableName 路径参数名
     * @return 枚举值
     */
    public static ArchiveDataGroupKey getByPathVariableName(String pathVariableName) {
        for (ArchiveDataGroupKey key : ArchiveDataGroupKey.values()) {
            if (key.pathVariableName.equals(pathVariableName)) {
                return key;
            }
        }
        return null;
    }

}
