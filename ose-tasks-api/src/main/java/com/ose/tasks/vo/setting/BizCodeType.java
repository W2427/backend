package com.ose.tasks.vo.setting;

import com.ose.vo.BpmTaskType;
import com.ose.vo.unit.*;
import com.ose.tasks.vo.*;
import com.ose.tasks.vo.bpm.*;
import com.ose.tasks.vo.qc.DefectTypes;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportType;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import com.ose.tasks.vo.scheduled.ScheduledTaskType;
import com.ose.vo.InspectParty;
import com.ose.vo.ValueObject;

/**
 * 业务代码类型值对象。
 */
public enum BizCodeType {

    LENGTH_UNIT("长度单位", LengthUnit.values()),
    AREA_UNIT("面积单位", AreaUnit.values()),
    VOLUME_UNIT("体积单位", VolumeUnit.values()),
    WEIGHT_UNIT("重量单位", WeightUnit.values()),
    PRESSURE_UNIT("压力单位", PressureUnit.values()),
    TEMPERATURE_UNIT("温度单位", TemperatureUnit.values()),
    BATCH_TASK_CODE("批处理任务代码", BatchTaskCode.values()),
    BATCH_TASK_STATUS("批处理任务状态", BatchTaskStatus.values()),
    NDE_TYPE("NDT类型", NDEType.values()),
    ACT_INST_VARIABLE_TYPE("挂起状态", ActInstVariableType.values()),
    EXTERNAL_INSPECTION_STATUS("外检状态", ExInspStatus.values()),
    INSPECT_PARTY("外部报检方类型", InspectParty.values()),
    SCHEDULED_TASK_CODE("定时任务代码", ScheduledTaskCode.values()),
    SCHEDULED_TASK_TYPE("定时任务类型", ScheduledTaskType.values()),
    SCHEDULED_TASK_STATUS("定时任务执行状态", ScheduledTaskStatus.values()),
    DESIGN_CHANGE_ORIGINATED("设计变更根源", DesignChangeOriginated.values()),
    DESIGN_CHANGE_DISCIPLINES("设计变更专业", DesignChangeDisciplines.values()),
    ACT_INST_DOC_TYPE("流程文件类型", ActInstDocType.values()),
    CONSTRUCTION_CHANGE_TYPE("建造变更类型", ConstructionChangeType.values()),
    PRINT_HEIGHT("打印高度", PrintHeight.values()),
    CLEAN_METHOD("清洁方式", CleanMethodType.values()),
    LENGEND_OF_DEFECT_TYPES("缺陷类型", DefectTypes.values()),
    PROCESS_TYPE("工序类型", ProcessType.values()),
    TASK_DELEGATE_TYPE("任务代理类型", BpmActTaskConfigDelegateType.values()),
    TASK_DELEGATE_STAGE("任务代理阶段", BpmActTaskConfigDelegateStage.values()),
    REPORT_TYPE("报告类型", ReportType.values()),
    REPORT_STATUS("报告状态", ReportStatus.values()),
    TASK_TYPE("任务类型", BpmTaskType.values());

    private Boolean isEnum = false;

    private String displayName;

    private ValueObject[] valueObjects;

    BizCodeType() {
    }

    BizCodeType(String displayName, ValueObject[] valueObjects) {
        isEnum = true;
        this.displayName = displayName;
        this.valueObjects = valueObjects;
    }

    public Boolean isEnum() {
        return isEnum;
    }

    public ValueObject[] getValueObjects() {
        return valueObjects;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
