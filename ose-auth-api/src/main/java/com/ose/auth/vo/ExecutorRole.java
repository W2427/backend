package com.ose.auth.vo;

import com.ose.vo.ValueObject;

/**
 * 任务节点 执行人分类
 */
public enum ExecutorRole implements ValueObject {

    PROJECT_MANAGER("项目经理"),
    DCC("文控"),
    DATA_INPUTOR("数据输入人"),
    PLANNER("计划工程师"),

    ENGINEERING_MANAGER("设计经理"),
    DRAWER("画图人员"),
    DESIGNER("设计人员"),
    CHECKER("检查人员"),
    REVIEWER("复核人员"),
    APPROVOR("批准人员"),

    CONSTRUCTION_MANAGER("建造经理"),
    SUPERVISOR("主管"),
    FOREMAN("班长"),
    WELDER("焊工"),
    FITTER("组装工人"),

    PROCURE_MANAGER("采购经理"),
    PROCURE_ENGINEER("采购工程师"),
    MATERIAL_CONTROLLER("材料控制工程"),
    KEEPER("库管"),

    QC_MANAGER("QC经理"),
    INTERNAL_QC("内验人员"),
    QC("检验人员"),
    SHIPYARD_QC("船厂检验人员"),
    OWNER_QC("船东检验人员"),
    NDT_ENGINEER("NDT检验人员"),


    HSE_MANAGER("安全经理"),
    HSE("安全人员");

    private String displayName;

    ExecutorRole(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
