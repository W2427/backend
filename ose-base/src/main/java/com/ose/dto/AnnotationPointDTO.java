package com.ose.dto;



/**
 * AnnotationPoint。
 */
public class AnnotationPointDTO extends BaseDTO {

    private static final long serialVersionUID = -5631222550361009363L;
    private float x;// x;
    private float y;// y;

    public AnnotationPointDTO(float x,float y) {
        this.setX(x);
        this.setY(y);
    }

    public AnnotationPointDTO() {
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

}
