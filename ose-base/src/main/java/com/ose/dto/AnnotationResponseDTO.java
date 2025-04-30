package com.ose.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * AnnotationRawLine。
 */
public class AnnotationResponseDTO extends BaseDTO {
//    export type AnnotationPath = { pos1: { x:number; y:number }; pos2: { x:number; y:number } }[];


    private static final long serialVersionUID = -2482183563937166466L;

    private String id;

    private String name;
    private String content;

    private String pageNo;

    private String type;//: string;
    private String points;//: points;
    //    private String startPointEndingStyle;//: string;
//    private String endPointEndingStyle;//: string;
    private AnnotationRawColorDTO rawColor;//: AnnotationRawColor;
    private PDColor pdColor;//: AnnotationRawColor;

    private Float opacity;

    private List<List<Float>> inkPointsList;

    private String borderStyle;//: string; //STYLE_SOLID = "S"; STYLE_DASHED = "D"; STYLE_BEVELED = "B"; STYLE_INSET = "I"; STYLE_UNDERLINE = "U";

    private int bordWidth;

    private Float width;

    private int fontSize;

    private Map<String, Object> metadata;

    private String subject;
    private String user;

    private Boolean deleted;

    private Boolean closed;

    private Map<String, AnnotationReplyDTO> replies;

    private String lineCap;//            "lineCap": "round",

    private String lineJoin;//    "lineJoin": "round",

    private Long documentId;

    private Float hitStrokeWidth;

    private Long userId;

    private Boolean bezier;

    private String wrap;

    public List<List<Float>> getInkPointsList() {
        return inkPointsList;
    }

    public void setInkPointsList(List<List<Float>> inkPointsList) {
        this.inkPointsList = inkPointsList;
    }

    public String getWrap() {
        return wrap;
    }

    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    public Boolean getBezier() {
        return bezier;
    }

    public void setBezier(Boolean bezier) {
        this.bezier = bezier;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AnnotationResponseDTO() {
    }

    @JsonCreator
    public AnnotationResponseDTO(@JsonProperty("points") List<AnnotationPointDTO> points) {
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

    //    public String getStartPointEndingStyle() {
//        return startPointEndingStyle;
//    }
//
//    public void setStartPointEndingStyle(String startPointEndingStyle) {
//        this.startPointEndingStyle = startPointEndingStyle;
//    }
//
//    public String getEndPointEndingStyle() {
//        return endPointEndingStyle;
//    }
//
//    public void setEndPointEndingStyle(String endPointEndingStyle) {
//        this.endPointEndingStyle = endPointEndingStyle;
//    }
//
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

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, AnnotationReplyDTO> getReplies() {
        return replies;
    }

    public void setReplies(Map<String, AnnotationReplyDTO> replies) {
        this.replies = replies;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public PDColor getPdColor() {
        return pdColor;
    }

    public void setPdColor(PDColor pdColor) {
        this.pdColor = pdColor;
    }

    public int getBordWidth() {
        return bordWidth;
    }

    public void setBordWidth(int bordWidth) {
        this.bordWidth = bordWidth;
    }

    public Float getOpacity() {
        return opacity;
    }

    public void setOpacity(Float opacity) {
        this.opacity = opacity;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getLineCap() {
        return lineCap;
    }

    public void setLineCap(String lineCap) {
        this.lineCap = lineCap;
    }

    public String getLineJoin() {
        return lineJoin;
    }

    public void setLineJoin(String lineJoin) {
        this.lineJoin = lineJoin;
    }

    public Float getHitStrokeWidth() {
        return hitStrokeWidth;
    }

    public void setHitStrokeWidth(Float hitStrokeWidth) {
        this.hitStrokeWidth = hitStrokeWidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
