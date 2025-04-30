package com.ose.tasks.dto.wps;


import com.ose.dto.BaseDTO;

public class WpsMatchingDTO extends BaseDTO {
    private static final long serialVersionUID = -4166590768936116737L;

    //    WPS编号
    private String wpsNo;

    //    焊接位置
    private String position;

    //    焊接工艺
    private String process;

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
