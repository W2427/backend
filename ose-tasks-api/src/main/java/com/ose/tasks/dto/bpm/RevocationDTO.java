package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmHiTaskinst;
import com.ose.tasks.entity.bpm.BpmRuTask;

/**
 * 实体管理 数据传输对象
 */
public class RevocationDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private BpmHiTaskinst hiTaskinst;

    private List<BpmRuTask> ruTasks;

    private boolean revocationResult;

    private boolean revocationNext;

    public List<BpmRuTask> getActRuTasks() {
        return ruTasks;
    }

    public void setActRuTasks(List<BpmRuTask> ruTasks) {
        this.ruTasks = ruTasks;
    }

    public BpmHiTaskinst getHiTaskinst() {
        return hiTaskinst;
    }

    public void setHiTaskinst(BpmHiTaskinst hiTaskinst) {
        this.hiTaskinst = hiTaskinst;
    }

    public boolean isRevocationResult() {
        return revocationResult;
    }

    public void setRevocationResult(boolean revocationResult) {
        this.revocationResult = revocationResult;
    }

    public boolean isRevocationNext() {
        return revocationNext;
    }

    public void setRevocationNext(boolean revocationNext) {
        this.revocationNext = revocationNext;
    }
}
