package com.ose.tasks.vo.bpm;

import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.BpmTaskType;

import java.util.*;

/**
 * 任务类型 和 task def key 对应的map
 */

public class BpmTaskTypeDefKey {

//    BpmTaskType.EX_INSP_APPLY.name();
//    BpmTaskType.EX_INSP_APPLY_MAIL.name()；
//    BpmTaskType.EX_INSP_UPLOAD_REPORT.name();
//    BpmTaskType.NDT_UPLOAD_REPORT.name();
//    BpmTaskType.PMI_UPLOAD_REPORT.name();
//    BpmTaskType.EX_INSP_HANDLE_REPORT;
//    BpmTaskType.EX_INSP_REHANDLE_REPORT.name();

    public static final Set<String> exInspApplyMails = new HashSet<String>(){{
        add(BpmTaskDefKey.USERTASK_SEND_EXTERNAL_INSPECTION_EMAIL.getType());
    }};

    public static final Set<String> exInspHandleReports = new HashSet<String>(){{
        add(BpmTaskDefKey.UT_CHECK_EXTERNAL_INSPECTION_REPORT.getType());
    }};

    public static final Set<String> exInspRehandleReports = new HashSet<String>(){{
        add(BpmTaskDefKey.UT_RECHECK_CLASS_B_REPORT.getType());
    }};

    public static final Map<String, Set<String>> taskTypeDefKeyMap = new HashMap<String, Set<String>>(){{
        put(BpmTaskType.EX_INSP_APPLY_MAIL.name(), exInspApplyMails);
        put(BpmTaskType.EX_INSP_HANDLE_REPORT.name(), exInspHandleReports);
        put(BpmTaskType.EX_INSP_REHANDLE_REPORT.name(), exInspRehandleReports);
    }};

}
