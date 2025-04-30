package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmReDeployment;


/**
 * 工序管理 数据传输对象
 */
public class ProcessResponseDTO extends BpmProcess {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private List<BpmEntitySubType> entitySubType;

    private BpmReDeployment activityModel;

    public ProcessResponseDTO(BpmProcess process) {
        if (process != null) {
            this.entitySubType = process.getEntitySubTypes();
            this.setCreatedAt(process.getCreatedAt());
            this.setId(process.getId());
            this.setLastModifiedAt(process.getLastModifiedAt());
            this.setMemo(process.getMemo());
            this.setNameCn(process.getNameCn());
            this.setNameEn(process.getNameEn());
            this.setOrderNo(process.getOrderNo());
            this.setOrgId(process.getOrgId());
            this.setProcessStage(process.getProcessStage());
            this.setProcessCategory(process.getProcessCategory());
            this.setProjectId(process.getProjectId());
            this.setStatus(process.getStatus());
            this.setCheckList(process.getCheckList());
            this.setConstructionLogClass(process.getConstructionLogClass());
            this.setProcessType(process.getProcessType());
            this.setFuncPart(process.getFuncPart());
            this.setOneOffReport(process.getOneOffReport());
        }
    }

    public List<BpmEntitySubType> getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(List<BpmEntitySubType> entitySubType) {
        this.entitySubType = entitySubType;
    }

    public BpmReDeployment getActivityModel() {
        return activityModel;
    }

    public void setActivityModel(BpmReDeployment activityModel) {
        this.activityModel = activityModel;
    }

}
