package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.SuspensionState;

/**
 * 实体管理 数据传输对象
 */
public class TodoTaskForemanDispatchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private String parentNo;

    private String parentType;

    private String stageName;

    private String processName;

    private String privilege;

    private SuspensionState suspensionState = SuspensionState.ACTIVE;

    private List<TodoTaskDTO> welds;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public List<TodoTaskDTO> getWelds() {
        return welds;
    }

    public void setWelds(List<TodoTaskDTO> welds) {
        this.welds = welds;
    }

    public SuspensionState getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(SuspensionState suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

}
