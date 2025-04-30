package com.ose.vo;


/**
 * REDIS 的key。
 */
public enum RedisKey implements ValueObject {


    //KEY:PROJECT_ID:ASSIGNEEID
    USER("U:%s"),
    PROJECT("PROJECT:%s"),//PROJECT:PROJECT_ID
    PROCESS("PROCESS:%s"),//用于  hierarchy 任务显示
    ENTITY_TYPE("ENTITY_TYPE:%s:%s"),//projectId, entityType
    ENTITY_TYPE_RELATION("ENTITY_TYPE_RELATION:%s:%s"),//ENTITY_TYPE:projectId:entityType
    PROCESS_BPMN("PROCESS_BPMN:%s:%s"),//processId:taskDefKey,用于  process_BPMN 解析
    PROCESS_ID("PR_ID:%s"),//PR_ID:PROCESS_ID
    MODULE_NOS("MODULE_NOS:%s"),//projectId
    PROCESS_NAME("PR_NAME:%s:%s:%s"), //PR_NAME:PROJECTID:PROCESSSTAGE:PROCESS
    PROCESS_STAGE_NAME("PS_NAME:%s:%s:%s"),//PS_NAME:PROJECTID:DISCIPLINE:PROCESSSTAGE
    PROCESS_STAGE_ID("PS_ID:%s"), //PS_ID:PROCESS_STAGE_ID
    PROCESS_STAGE("PROCESS_STAGE:%s"),//用于 hierarchy task显示
    TASK_INFO_PROCESS_STAGE("TASK_INFO_PS:%s:%s"),
    TASK_INFO_PROCESS_STAGE_COUNT("TASK_INFO_PS_CNT:%s:%s"), //field-> INIT 设置为 1的时候，表示完成了初始化，如果为0 则没有初始化
    TASK_INFO_PROCESS("TASK_INFO_PR:%s:%s"),
    TASK_INFO_PROCESS_COUNT("TASK_INFO_PR_CNT:%s:%s"),
    EIT_COMPONENT("EIT_COMPONENT:%s"),
    HEAT_NO("HEAT_NO:%s"),//HEAT_NO:project_id
    BPM_ERROR("BPM_ERROR"),//BPM_ERROR:project_id
    USER_CONTEXT("USER_CONTEXT:%s:%s"),//USER_CONTEXT:project_id:userID
    PLAN_QUEUE_STATUS("PLAN_QUEUE_STATUS:%s"),//PLAN_QUEUE_STATUS:PROJECT_ID, HOT/COLD
    PLAN_QUEUE("PLAN_QUEUE:%s"),//PLAN_QUEUE:PROJECT_ID
    PROJECTS("PROJECTS"),//PLAN_QUEUE:PROJECT_ID
    ENTITY_TYPE_SUB_TYPE("ENTITY_TYPE_SUB_TYPE:%s"),//ENTITY_TYPE_SUB_TYPE:project_id
    ENTITY_SUB_TYPE("ENTITY_TYPE_SUB_TYPE:%s:%s"),//ENTITY_SUB_TYPE:project_id:entitySubType
    WP01("WP01:%s"),//WP01:PROJECT_ID
    WP02("WP02:%s"),//WP01:PROJECT_ID
    WP03("WP03:%s"),//WP01:PROJECT_ID
    WP04("WP04:%s"),//WP01:PROJECT_ID
    CABLE_TRAY("CABLE_TRAY:%s"),
    BPMN_MODEL("BPMN_MODEL:%s:%s:%s"),//projectId,processId,version
    PROCESS_NODE_BPMN("PROCESS_NODE_BPMN:%s:%s:%s"),//processId,version,taskDefKey
    REPORT_SN_MAX("REPORT_SN_MAX:%s:%s:%s"), //projectId,type,module
    REPORT_SN_SKIPPED("REPORT_SN_SKIPPED:%s:%s:%s"),//projectId,type,module
    REPORT_SN_GENERAL_MAX("REPORT_SN_GENERAL_MAX:%s:%s"), //projectId,type
    REPORT_SN_GENERAL_SKIPPED("REPORT_SN_GENERAL_SKIPPED:%s:%s"),//projectId,type
    REPORT_SN( "REPORT_SN:%s:%s:%s:%s"), // Project_id:Type:module:sn
    REPORT_SN_GENERAL( "REPORT_SN_GENERAL:%s:%s:%s"), // Project_id:Type:sn
    SERIAL_NO_AT_REDIS_KEY( "SERIES:%s:%s:%s:%s"), // Project_id:Type:module:sn
    PROJECT_USER_KEY( "PROJECT_USER_KEY"), //
    PROJECT_DISCIPLINE( "PROJECT_DISCIPLINE_KEY:%s"), //
    PROJECT_FUNC_PART( "PROJECT_FUNC_PART_KEY:%s"), //
    PROJECT_FUNC_PART_KEY( "PROJECT_FUNC_PART_KEY:%s:%s"), //
    REPORT_RUNNING_LIST("REPORT_RUNNING_LIST:%s:%s"),
    PROJECT_NODE("PROJECT_NODE:%s");
    private String displayName;

    RedisKey(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
