package com.ose.dto;



/**
 * AnnotationPoint。
 */
public class AnnotationRectDTO extends BaseDTO {

    private static final long serialVersionUID = -5631222550361009363L;
    private float x;// x;
    private float y;// y;
    private float width;// y;
    private float height;// y;

    public AnnotationRectDTO(float x, float y, float width, float height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
    }

    public AnnotationRectDTO() {
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
