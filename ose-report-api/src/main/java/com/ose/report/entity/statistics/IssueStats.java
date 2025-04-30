package com.ose.report.entity.statistics;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 遗留问题统计数据。
 */
@Entity
@Table(name = "issue_stats", schema = "ose_issues")
public class IssueStats extends BaseEntity {

    private static final long serialVersionUID = 7750588688100772802L;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "等级")
    @Column
    private String level;

    @Schema(description = "区域")
    @Column
    private String area;

    @Schema(description = "试压包")
    @Column
    private String pressureTestPackage;

    @Schema(description = "子系统")
    @Column
    private String subSystem;

    @Schema(description = "部门")
    @Column
    private String department;

    @Schema(description = "创建时间时间戳")
    @Column
    private Long createTimestamp;

    @Schema(description = "创建时间年")
    @Column
    private Integer createYear;

    @Schema(description = "创建时间月")
    @Column
    private Integer createMonth;

    @Schema(description = "创建时间日")
    @Column
    private Integer createDay;

    @Schema(description = "创建时间周")
    @Column
    private Integer createWeek;

    @Schema(description = "关闭时间时间戳")
    @Column
    private Long closeTimestamp;

    @Schema(description = "关闭时间年")
    @Column
    private Integer closeYear;

    @Schema(description = "关闭时间月")
    @Column
    private Integer closeMonth;

    @Schema(description = "关闭时间日")
    @Column
    private Integer closeDay;

    @Schema(description = "关闭时间周")
    @Column
    private Integer closeWeek;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPressureTestPackage() {
        return pressureTestPackage;
    }

    public void setPressureTestPackage(String pressureTestPackage) {
        this.pressureTestPackage = pressureTestPackage;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Integer getCreateYear() {
        return createYear;
    }

    public void setCreateYear(Integer createYear) {
        this.createYear = createYear;
    }

    public Integer getCreateMonth() {
        return createMonth;
    }

    public void setCreateMonth(Integer createMonth) {
        this.createMonth = createMonth;
    }

    public Integer getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Integer createDay) {
        this.createDay = createDay;
    }

    public Integer getCreateWeek() {
        return createWeek;
    }

    public void setCreateWeek(Integer createWeek) {
        this.createWeek = createWeek;
    }

    public Long getCloseTimestamp() {
        return closeTimestamp;
    }

    public void setCloseTimestamp(Long closeTimestamp) {
        this.closeTimestamp = closeTimestamp;
    }

    public Integer getCloseYear() {
        return closeYear;
    }

    public void setCloseYear(Integer closeYear) {
        this.closeYear = closeYear;
    }

    public Integer getCloseMonth() {
        return closeMonth;
    }

    public void setCloseMonth(Integer closeMonth) {
        this.closeMonth = closeMonth;
    }

    public Integer getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(Integer closeDay) {
        this.closeDay = closeDay;
    }

    public Integer getCloseWeek() {
        return closeWeek;
    }

    public void setCloseWeek(Integer closeWeek) {
        this.closeWeek = closeWeek;
    }
}
