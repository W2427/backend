package com.ose.tasks.entity.drawing;

import com.ose.entity.BaseVersionedBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * 请假信息
 */
@Entity
@Table(name = "employee_information_test")
public class EmployeeDataTest{
    @Id
    private Long id;

    @Schema(description = "工号")
    private String staffId;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "上一年度剩余年假")
    private double specialAnnualLeave;

    @Schema(description = "上月剩余年假")
    private double remainingAnnualLastMth;

    @Schema(description = "上月剩余加班时长")
    private double remainingOtManhourUntilTheEndOfLastMth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpecialAnnualLeave() {
        return specialAnnualLeave;
    }

    public void setSpecialAnnualLeave(double specialAnnualLeave) {
        this.specialAnnualLeave = specialAnnualLeave;
    }

    public double getRemainingAnnualLastMth() {
        return remainingAnnualLastMth;
    }

    public void setRemainingAnnualLastMth(double remainingAnnualLastMth) {
        this.remainingAnnualLastMth = remainingAnnualLastMth;
    }

    public double getRemainingOtManhourUntilTheEndOfLastMth() {
        return remainingOtManhourUntilTheEndOfLastMth;
    }

    public void setRemainingOtManhourUntilTheEndOfLastMth(double remainingOtManhourUntilTheEndOfLastMth) {
        this.remainingOtManhourUntilTheEndOfLastMth = remainingOtManhourUntilTheEndOfLastMth;
    }
}
