package com.ose.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AnnotationRawLine。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationResponseDTOV1 extends BaseDTO {

    public static class ContentObj {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    public static class KonvaObject{
        private KonvaObjectAttr attrs;
        private String className;
        private List<KonvaObjectChildren> children;

        public KonvaObjectAttr getAttrs() {
            return attrs;
        }

        public void setAttrs(KonvaObjectAttr attrs) {
            this.attrs = attrs;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<KonvaObjectChildren> getChildren() {
            return children;
        }

        public void setChildren(List<KonvaObjectChildren> children) {
            this.children = children;
        }
    }


    public static class KonvaObjectChildren{
        private KonvaObjectChildrenAttrs attrs;
        private String className;

        public KonvaObjectChildrenAttrs getAttrs() {
            return attrs;
        }

        public void setAttrs(KonvaObjectChildrenAttrs attrs) {
            this.attrs = attrs;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class KonvaObjectChildrenAttrs{
        private Boolean strokeScaleEnabled;
        private String stroke;
        private String fill;
        private String lineCap;
        private String lineJoin;

        private Float opacity;
        private String hitStrokeWidth;
        private String strokeWidth;
        private List<Float> points;
        private Float x;
        private Float y;
        private Float width;
        private Float height;
        private String text;
        private int fontSize;

        private String wrap;

        private Boolean bezier;

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


        public String getFill() {
            return fill;
        }

        public void setFill(String fill) {
            this.fill = fill;
        }

        public Boolean getStrokeScaleEnabled() {
            return strokeScaleEnabled;
        }

        public void setStrokeScaleEnabled(Boolean strokeScaleEnabled) {
            this.strokeScaleEnabled = strokeScaleEnabled;
        }

        public String getStroke() {
            return stroke;
        }

        public void setStroke(String stroke) {
            this.stroke = stroke;
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

        public String getHitStrokeWidth() {
            return hitStrokeWidth;
        }

        public void setHitStrokeWidth(String hitStrokeWidth) {
            this.hitStrokeWidth = hitStrokeWidth;
        }

        public List<Float> getPoints() {
            return points;
        }

        public void setPoints(List<Float> points) {
            this.points = points;
        }

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }

        public Float getY() {
            return y;
        }

        public void setY(Float y) {
            this.y = y;
        }

        public Float getWidth() {
            return width;
        }

        public void setWidth(Float width) {
            this.width = width;
        }

        public Float getHeight() {
            return height;
        }

        public void setHeight(Float height) {
            this.height = height;
        }

        public Float getOpacity() {
            return opacity;
        }

        public void setOpacity(Float opacity) {
            this.opacity = opacity;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getStrokeWidth() {
            return strokeWidth;
        }

        public void setStrokeWidth(String strokeWidth) {
            this.strokeWidth = strokeWidth;
        }
    }


    public static class KonvaObjectAttr{
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class KonvaClientRect {
        private Float x;
        private Float y;
        private Float width;
        private Float height;

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }

        public Float getY() {
            return y;
        }

        public void setY(Float y) {
            this.y = y;
        }

        public Float getWidth() {
            return width;
        }

        public void setWidth(Float width) {
            this.width = width;
        }

        public Float getHeight() {
            return height;
        }

        public void setHeight(Float height) {
            this.height = height;
        }
    }

    //id: annotation.id,
    //pageNumber: annotation.pageNumber,
    // konvastring: ghostGroup.toJSON()，
    // konvaclientRect: ghostGroup.getclientRect(),
    // title: annotation.title0bj.str,
    // type: AnnotationType.FREE HIGHLIGHT,
    // color,
    //pdfjsType: annotation.annotationType,
    // pdfjsEditorType:PdfjsAnnotationEditorType.INK
    // subtype: annotation.subtype,
    // date: annotation.modificationDate,
    //contentsObj:{
    //text: annotation.contentsObj.str,}
    //comments: this.getComments(annotation,allAnnotations)，readonly: false,


    private static final long serialVersionUID = -6572272915094877747L;

    private String id;

    private String user;

    private Long userId;

    private Integer pageNumber;
    private String konvaString;
    private String title;
    private String type;
    private String color;
    private String pdfjsType;
    private String pdfjsEditorType;
    private String subtype;

    private String date;

    private ContentObj contentsObj;

    private String[] comments;

    private boolean readonly;

    private String konvaClientRect;

    public String getKonvaClientRect() {
        return konvaClientRect;
    }

    public void setKonvaClientRect(String konvaClientRect) {
        this.konvaClientRect = konvaClientRect;
    }

    @JsonIgnore
    public void setKonvaClientRect(KonvaClientRect konvaClientRect) {
        this.konvaClientRect = StringUtils.toJSON(konvaClientRect);
    }

    @JsonProperty(value = "konvaClientRect", access = JsonProperty.Access.READ_ONLY)
    public KonvaClientRect getKonvaClientRectObj() {
        if (konvaClientRect != null && !"".equals(konvaClientRect)) {
            return StringUtils.decode(konvaClientRect, new TypeReference<KonvaClientRect>() {
            });
        } else {
            return new KonvaClientRect();
        }
    }

    @JsonCreator
    public AnnotationResponseDTOV1(@JsonProperty("konvaString") KonvaObject konvaObject) {
        this.konvaString = StringUtils.toJSON(konvaObject);
    }

    public AnnotationResponseDTOV1() {
    }

    @JsonProperty(value = "konvaString", access = JsonProperty.Access.READ_ONLY)
    public KonvaObject getKonvaObject() {
        if (konvaString != null && !"".equals(konvaString)) {
            return StringUtils.decode(konvaString, new TypeReference<KonvaObject>() {
            });
        } else {
            return new KonvaObject();
        }
    }

    @JsonIgnore
    public void setKonvastring(KonvaObject konvaObject) {
        if (konvaObject != null) {
            this.konvaString = StringUtils.toJSON(konvaObject);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getKonvastring() {
        return konvaString;
    }

    public void setKonvastring(String konvastring) {
        this.konvaString = konvastring;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPdfjsType() {
        return pdfjsType;
    }

    public void setPdfjsType(String pdfjsType) {
        this.pdfjsType = pdfjsType;
    }

    public String getPdfjsEditorType() {
        return pdfjsEditorType;
    }

    public void setPdfjsEditorType(String pdfjsEditorType) {
        this.pdfjsEditorType = pdfjsEditorType;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ContentObj getContentsObj() {
        return contentsObj;
    }

    public void setContentsObj(ContentObj contentsObj) {
        this.contentsObj = contentsObj;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
