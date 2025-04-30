package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmActTaskAssignee;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;

/**
 * 待办任务条件 数据传输对象
 */
public class ActInstAssigneeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;


    private BpmActivityInstanceBase actInst;

    private String diagramResource;

    private List<BpmActTaskAssignee> taskAssignees;

    public BpmActivityInstanceBase getActInst() {
        return actInst;
    }

    public void setActInst(BpmActivityInstanceBase actInst) {
        this.actInst = actInst;
    }

    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public List<BpmActTaskAssignee> getTaskAssignees() {
        return taskAssignees;
    }

    public void setTaskAssignees(List<BpmActTaskAssignee> taskAssignees) {
        this.taskAssignees = taskAssignees;
    }


}
