package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * AnnotationRawColor。
 */
public class AnnotationRawColorDTO extends BaseDTO {

    private static final long serialVersionUID = -560930464683306579L;

    private String rgb;//: rgb;

    public AnnotationRawColorDTO(String rgb) {
        this.setRgb(rgb);
    }

    @JsonCreator
    public AnnotationRawColorDTO(@JsonProperty("rgb") List<Float> rgb) {
        this.rgb = StringUtils.toJSON(rgb);
    }


    @JsonProperty(value = "rgb", access = JsonProperty.Access.READ_ONLY)
    public List<Float> getJsonRgb() {
        if (rgb != null && !"".equals(rgb)) {
            return StringUtils.decode(rgb, new TypeReference<List<Float>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonRgb(List<Float> rgb) {
        if (rgb != null) {
            this.rgb = StringUtils.toJSON(rgb);
        }
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }


    public String getRgb() {
        return rgb;
    }
}
