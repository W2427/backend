package com.ose.tasks.entity.holiday;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "holiday_data")
public class HolidayData extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2228304677502340861L;

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
    private String holidayDate;

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

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }
}
