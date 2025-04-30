package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * AnnotationRawLine。
 */
public class AnnotationRawLineDTO extends BaseDTO {

    private static final long serialVersionUID = -2482183563937166466L;

    private String id;
    private String points;//: points;
    private String startPointEndingStyle;//: string;
    private String endPointEndingStyle;//: string;
    private AnnotationRawColorDTO rawColor;//: AnnotationRawColor;
    private String borderStyle;//: string; //STYLE_SOLID = "S"; STYLE_DASHED = "D"; STYLE_BEVELED = "B"; STYLE_INSET = "I"; STYLE_UNDERLINE = "U";

    private String subject;
    private String user;

    private Integer pageNo;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @JsonCreator
    public AnnotationRawLineDTO(@JsonProperty("points") List<AnnotationPointDTO> points) {
        this.points = StringUtils.toJSON(points);
    }


    @JsonProperty(value = "points", access = JsonProperty.Access.READ_ONLY)
    public List<AnnotationPointDTO> getJsonPoints() {
        if (points != null && !"".equals(points)) {
            return StringUtils.decode(points, new TypeReference<List<AnnotationPointDTO>>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    @JsonIgnore
    public void setJsonPoints(List<AnnotationPointDTO> points) {
        if (points != null) {
            this.points = StringUtils.toJSON(points);
        }
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStartPointEndingStyle() {
        return startPointEndingStyle;
    }

    public void setStartPointEndingStyle(String startPointEndingStyle) {
        this.startPointEndingStyle = startPointEndingStyle;
    }

    public String getEndPointEndingStyle() {
        return endPointEndingStyle;
    }

    public void setEndPointEndingStyle(String endPointEndingStyle) {
        this.endPointEndingStyle = endPointEndingStyle;
    }

    public AnnotationRawColorDTO getRawColor() {
        return rawColor;
    }

    public void setRawColor(AnnotationRawColorDTO rawColor) {
        this.rawColor = rawColor;
    }

    public String getBorderStyle() {
        return borderStyle;
    }

    public void setBorderStyle(String borderStyle) {
        this.borderStyle = borderStyle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
