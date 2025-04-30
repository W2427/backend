package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

import java.util.List;

public class TodoTaskWeldRelationExecuteDTO extends BaseDTO {

    private static final long serialVersionUID = -4557109398132476469L;

    private List<Long> processA;
    private List<Long> processB;
    private List<Long> processC;

    public List<Long> getProcessA() {
        return processA;
    }

    public void setProcessA(List<Long> processA) {
        this.processA = processA;
    }

    public List<Long> getProcessB() {
        return processB;
    }

    public void setProcessB(List<Long> processB) {
        this.processB = processB;
    }

    public List<Long> getProcessC() {
        return processC;
    }

    public void setProcessC(List<Long> processC) {
        this.processC = processC;
    }
}
