package com.ose.tasks.dto.holiday;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 节假日清单创建传输对象。
 */
public class WorkDayCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 3499170583932265108L;


    @Schema(description = "所属国家")
    private String country;

    @Schema(description = "日期")
    private Date workDate;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }
}
