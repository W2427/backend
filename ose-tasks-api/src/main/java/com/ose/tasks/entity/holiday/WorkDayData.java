package com.ose.tasks.entity.holiday;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "work_day_data")
public class WorkDayData extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6958850743135468803L;

    @Schema(description = "所属国家")
    @Column
    private String country;

    @Schema(description = "日期")
    @Column
    private String workDate;
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }
}
