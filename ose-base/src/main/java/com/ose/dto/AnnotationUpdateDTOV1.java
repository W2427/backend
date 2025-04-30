package com.ose.dto;


import java.util.List;

/**
 * AnnotationRawLine。
 */
public class AnnotationUpdateDTOV1 extends BaseDTO {

    private static final long serialVersionUID = -6572272915094877747L;

    private List<AnnotationUpdateItemDTOV1> annotations;

    private Long taskId;

    private Long procInstId;

    private Integer start;

    private Integer limit;

    private String pdfUpdateVersion;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<AnnotationUpdateItemDTOV1> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationUpdateItemDTOV1> annotations) {
        this.annotations = annotations;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(Long procInstId) {
        this.procInstId = procInstId;
    }

    public String getPdfUpdateVersion() {
        return pdfUpdateVersion;
    }

    public void setPdfUpdateVersion(String pdfUpdateVersion) {
        this.pdfUpdateVersion = pdfUpdateVersion;
    }
}
