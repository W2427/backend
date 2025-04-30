package com.ose.dto;



/**
 * AnnotationPath。
 */
public class AnnotationPathDTO extends BaseDTO {

    private static final long serialVersionUID = -9169667870346132370L;
    private AnnotationPointDTO pos1;// x;

    private AnnotationPointDTO pos2;// x;

    public AnnotationPointDTO getPos1() {
        return pos1;
    }

    public void setPos1(AnnotationPointDTO pos1) {
        this.pos1 = pos1;
    }

    public AnnotationPointDTO getPos2() {
        return pos2;
    }

    public void setPos2(AnnotationPointDTO pos2) {
        this.pos2 = pos2;
    }
}
