package com.ose.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * AnnotationUpdateDTO。
 */
public class AnnotationUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -1697111167563902387L;

    @Schema(description = "图纸任务流程ID")
    private Long procInstId;

    @Schema(description = "图纸任务完成历史ID")
    private Long taskId;

    @Schema(description = "图纸PDF编辑")
    private List<AnnotationResponseDTO> annotionDTOs;

    @Schema(description = "图纸PDF编辑数据")
    private Object annotations;

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<AnnotationResponseDTO> getAnnotionDTOs() {
        return annotionDTOs;
    }

    public void setAnnotionDTOs(List<AnnotationResponseDTO> annotionDTOs) {
        this.annotionDTOs = annotionDTOs;
    }

    public Object getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Object annotations) {
        this.annotations = annotations;
    }
}
