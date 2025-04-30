package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class BpmDelegateClassDTO extends BaseDTO {


    private static final long serialVersionUID = -3214040469848423348L;
    //代理类名称
    @Schema(description = "代理类名称")
    private String delegateClassName;

    //代理类全名
    @Schema(description = "代理类全名")
    private String delegateClassFullName;

    public String getDelegateClassName() {
        return delegateClassName;
    }

    public void setDelegateClassName(String delegateClassName) {
        this.delegateClassName = delegateClassName;
    }

    public String getDelegateClassFullName() {
        return delegateClassFullName;
    }

    public void setDelegateClassFullName(String delegateClassFullName) {
        this.delegateClassFullName = delegateClassFullName;
    }


}
