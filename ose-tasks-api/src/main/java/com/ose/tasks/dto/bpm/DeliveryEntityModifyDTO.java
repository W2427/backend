package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeliveryEntityModifyDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    @Schema(description = "状态")
    private NGFlag ngflg;

    @Schema(description = "备注")
    private String memo;


    public String getMemo() {
        return memo;
    }


    public void setMemo(String memo) {
        this.memo = memo;
    }


    public NGFlag getNgflg() {
        return ngflg;
    }


    public void setNgflg(NGFlag ngflg) {
        this.ngflg = ngflg;
    }

    public enum NGFlag {
        OK,
        NG
    }

}
