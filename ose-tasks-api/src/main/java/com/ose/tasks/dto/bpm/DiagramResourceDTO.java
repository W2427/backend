package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

public class DiagramResourceDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4402058499392872999L;


    private String diagramResource;

    private String diagramResourceName;

    private Long fileId;

    public String getDiagramResource() {
        return diagramResource;
    }

    public void setDiagramResource(String diagramResource) {
        this.diagramResource = diagramResource;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
