package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;


/**
 * 实体类型 数据传输对象
 */
public class EntitySubTypeResponseDTO extends BpmEntitySubType {
    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private List<BpmProcess> process;

    public EntitySubTypeResponseDTO(BpmEntitySubType ec) {
        this.process = ec.getProcesses();
        this.setCreatedAt(ec.getCreatedAt());
        this.setId(ec.getId());
        this.setLastModifiedAt(ec.getLastModifiedAt());
        this.setMemo(ec.getMemo());
        this.setNameCn(ec.getNameCn());
        this.setNameEn(ec.getNameEn());
        this.setOrgId(ec.getOrgId());
        this.setProjectId(ec.getProjectId());
        this.setStatus(ec.getStatus());
        this.setEntityType(ec.getEntityType());
        this.setEntityBusinessType(ec.getEntityBusinessType());
        this.setSubDrawingFlg(ec.isSubDrawingFlg());
        this.setCheckList(ec.getCheckList());
        this.setDiscipline(ec.getDiscipline());
        this.setCoverPositionX(ec.getCoverPositionX());
        this.setCoverPositionY(ec.getCoverPositionY());
        this.setCoverScaleToFit(ec.getCoverScaleToFit());
        this.setDrawingPositionX(ec.getDrawingPositionX());
        this.setDrawingPositionY(ec.getDrawingPositionY());
        this.setDrawingScaleToFit(ec.getDrawingScaleToFit());
    }

    public List<BpmProcess> getProcess() {
        return process;
    }

    public void setProcess(List<BpmProcess> process) {
        this.process = process;
    }

}
