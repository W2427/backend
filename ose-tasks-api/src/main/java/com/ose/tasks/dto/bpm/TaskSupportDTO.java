package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.BizCodeDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.vo.BpmTaskType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 实体管理 数据传输对象
 */
public class TaskSupportDTO extends BaseDTO {

    private static final long serialVersionUID = -6583515161867844357L;

    private List<String> counterIds;

    public List<String> getCounterIds() {
        return counterIds;
    }

    public void setCounterIds(List<String> counterIds) {
        this.counterIds = counterIds;
    }
}
