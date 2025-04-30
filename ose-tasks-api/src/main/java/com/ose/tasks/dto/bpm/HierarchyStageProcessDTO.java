package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 待办任务条件 数据传输对象
 */
public class HierarchyStageProcessDTO extends BaseDTO {

    public HierarchyStageProcessDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;

    private String displayName;

    private Long stageId;

    private Long processId;

    public HierarchyStageProcessDTO(String displayName, Long stageId, Long processId) {
        super();
        this.displayName = displayName;
        this.stageId = stageId;
        this.processId = processId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

}
