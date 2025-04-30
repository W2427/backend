package com.ose.tasks.dto.bpm;

import java.util.List;
import java.util.Map;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmActInstMaterial;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.drawing.SubDrawing;

/**
 * 待办任务条件 数据传输对象
 */
public class ActInstDetailDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;


    private BpmActivityInstanceBase actInst;

    private String diagramResource;

    private Long fileId;

    private List<ActHiTaskNodeDTO> hiTasks;

    private Map<String, Object> materials;

    private List<SubDrawing> drawings;

    private List<BpmActInstMaterial> actInstMaterials;

    private List<BpmEntityDocsDTO> actInstAttachments;

    private boolean revocationAble = false;

    private List<Map<String, Object>> variables;

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

    public List<ActHiTaskNodeDTO> getHiTasks() {
        return hiTasks;
    }

    public void setHiTasks(List<ActHiTaskNodeDTO> hiTasks) {
        this.hiTasks = hiTasks;
    }

    public List<BpmActInstMaterial> getActInstMaterials() {
        return actInstMaterials;
    }

    public void setActInstMaterials(List<BpmActInstMaterial> actInstMaterials) {
        this.actInstMaterials = actInstMaterials;
    }

    public List<BpmEntityDocsDTO> getActInstAttachments() {
        return actInstAttachments;
    }

    public void setActInstAttachments(List<BpmEntityDocsDTO> actInstAttachments) {
        this.actInstAttachments = actInstAttachments;
    }

    public boolean isRevocationAble() {
        return revocationAble;
    }

    public void setRevocationAble(boolean revocationAble) {
        this.revocationAble = revocationAble;
    }

    public List<Map<String, Object>> getVariables() {
        return variables;
    }

    public void setVariables(List<Map<String, Object>> variables) {
        this.variables = variables;
    }


    public Map<String, Object> getMaterials() {
        return materials;
    }


    public void setMaterials(Map<String, Object> materials) {
        this.materials = materials;
    }

    public List<SubDrawing> getDrawings() {
        return drawings;
    }

    public void setDrawings(List<SubDrawing> drawings) {
        this.drawings = drawings;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
