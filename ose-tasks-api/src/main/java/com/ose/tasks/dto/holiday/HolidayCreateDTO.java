package com.ose.tasks.dto.holiday;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import java.util.Date;

/**
 * 节假日清单创建传输对象。
 */
public class HolidayCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 202327586547247945L;

    @Schema(description = "节假日名称")
    @Column
    private String holidayName;

    @Schema(description = "所属国家")
    @Column
    private String country;

    @Schema(description = "日期")
    @Column
//    @JsonFormat(
//        shape = JsonFormat.Shape.STRING,
//        pattern = "yyyy-MM-dd"
//    )
    private Date holidayDate;

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }
}
