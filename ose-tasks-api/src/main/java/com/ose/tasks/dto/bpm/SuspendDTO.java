package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmHiTaskinst;
import com.ose.tasks.entity.bpm.BpmRuTask;

import java.util.List;

/**
 * 实体管理 数据传输对象
 */
public class SuspendDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private BpmHiTaskinst hiTaskinst;

    private List<BpmRuTask> ruTasks;

    private boolean suspendResult;

    private boolean suspendNext;

    public List<BpmRuTask> getActRuTasks() {
        return ruTasks;
    }

    public void setRuTasks(List<BpmRuTask> ruTasks) {
        this.ruTasks = ruTasks;
    }

    public BpmHiTaskinst getHiTaskinst() {
        return hiTaskinst;
    }

    public void setHiTaskinst(BpmHiTaskinst hiTaskinst) {
        this.hiTaskinst = hiTaskinst;
    }

    public boolean isSuspendResult() {
        return suspendResult;
    }

    public void setSuspendResult(boolean suspendResult) {
        this.suspendResult = suspendResult;
    }

    public boolean isSuspendNext() {
        return suspendNext;
    }

    public void setSuspendNext(boolean suspendNext) {
        this.suspendNext = suspendNext;
    }
}
