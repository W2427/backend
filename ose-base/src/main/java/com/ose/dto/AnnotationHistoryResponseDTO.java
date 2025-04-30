package com.ose.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Annotation History Response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationHistoryResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -5238260617616981718L;


    private Long version;

    private List<AnnotationResponseDTOV1> annotations;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<AnnotationResponseDTOV1> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationResponseDTOV1> annotations) {
        this.annotations = annotations;
    }
}
