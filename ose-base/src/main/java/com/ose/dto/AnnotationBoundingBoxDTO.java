package com.ose.dto;



/**
 * AnnotationRawLine。
 */
public class AnnotationBoundingBoxDTO extends BaseDTO {

    private static final long serialVersionUID = 5377775539041946549L;
    private float x1;// lowerLeftX;
    private float y1;// lowerLeftY;
    private float x2;// upperRightX;
    private float y2;// upperRightY;

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }
}
