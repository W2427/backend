package com.ose.baiduNormalTranslate;

import java.io.Serializable;

public class TranslateDetail implements Serializable {

    private static final long serialVersionUID = -3410325175916017878L;
    private String src;

    private String dst;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
