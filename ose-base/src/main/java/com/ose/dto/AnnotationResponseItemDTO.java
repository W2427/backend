package com.ose.dto;
import java.util.List;

/**
 * AnnotationRawLine。
 */
public class AnnotationResponseItemDTO extends BaseDTO {

    private static final long serialVersionUID = 7289506107831435051L;

    private String id;
    private int pageNumber;
    private String konvaString;
    private KonvaObject konvaObject;
    private AnnotationRectDTO konvaClientRect;
    private String title;
    private int type;
    private int pdfjsType;
    private int pdfjsEditorType;
    private String subtype;
    private String color;
    private String date;
    private ContentsObj contentsObj;
    private Object comments;
    private Boolean readonly;

    public static class ContentsObj{
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

    public static class KonvaObjectChildrenAttrs{
        private String strokeScaleEnabled;
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

        public String getFill() {
            return fill;
        }

        public void setFill(String fill) {
            this.fill = fill;
        }

        public String getStrokeScaleEnabled() {
            return strokeScaleEnabled;
        }

        public void setStrokeScaleEnabled(String strokeScaleEnabled) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getKonvaString() {
        return konvaString;
    }

    public void setKonvaString(String konvaString) {
        this.konvaString = konvaString;
    }

    public AnnotationRectDTO getKonvaClientRect() {
        return konvaClientRect;
    }

    public void setKonvaClientRect(AnnotationRectDTO konvaClientRect) {
        this.konvaClientRect = konvaClientRect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPdfjsType() {
        return pdfjsType;
    }

    public void setPdfjsType(int pdfjsType) {
        this.pdfjsType = pdfjsType;
    }

    public int getPdfjsEditorType() {
        return pdfjsEditorType;
    }

    public void setPdfjsEditorType(int pdfjsEditorType) {
        this.pdfjsEditorType = pdfjsEditorType;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ContentsObj getContentsObj() {
        return contentsObj;
    }

    public void setContentsObj(ContentsObj contentsObj) {
        this.contentsObj = contentsObj;
    }

    public Object getComments() {
        return comments;
    }

    public void setComments(Object comments) {
        this.comments = comments;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public KonvaObject getKonvaObject() {
        return konvaObject;
    }

    public void setKonvaObject(KonvaObject konvaObject) {
        this.konvaObject = konvaObject;
    }


}
