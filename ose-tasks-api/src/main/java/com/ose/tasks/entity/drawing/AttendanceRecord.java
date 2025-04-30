package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;


/**
 * 图纸记录
 */
@Entity
@Table(name = "attendance_record")
public class AttendanceRecord extends BaseVersionedBizEntity {

    private static final long serialVersionUID = 27372150972503750L;
    @Schema(description = "锁定日期")
    private String lockedDate;


    public String getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(String lockedDate) {
        this.lockedDate = lockedDate;
    }
}
