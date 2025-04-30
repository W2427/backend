package com.ose.tasks.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.PrintHeight;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrintConfigDTO extends BaseDTO {

    private static final long serialVersionUID = 5971440931157335574L;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "服务地址")
    private String service;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "打印高度")
    @Enumerated(EnumType.STRING)
    private PrintHeight height;

    @Schema(description = "打印方向（默认true）, true为横向，false为纵向")
    private boolean cuttingRFlag;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public PrintHeight getHeight() {
        return height;
    }

    public void setHeight(PrintHeight height) {
        this.height = height;
    }

    public boolean isCuttingRFlag() {
        return cuttingRFlag;
    }

    public void setCuttingRFlag(boolean cuttingRFlag) {
        this.cuttingRFlag = cuttingRFlag;
    }

}
