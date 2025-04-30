package com.ose.report.entity.statistics;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ose.constant.JsonFormatPattern;
import com.ose.entity.BaseEntity;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * 统计数据归档记录数据实体。
 */
@MappedSuperclass
public class ArchiveDataBase extends BaseEntity {

    private static final long serialVersionUID = -250345651384680438L;

    @Schema(name = "项目 ID", hidden = true)
    @Column(nullable = false)
    private Long projectId;

    @Schema(name = "项目名", hidden = true)
    @Column
    private String projectName;

    @Schema(name = "归档数据业务类型", hidden = true)
    @Column(length = 45, nullable = false)
    @Enumerated(EnumType.STRING)
    private ArchiveDataType archiveType;

    @Schema(name = "归档数据业务类型", hidden = true)
    @Column(length = 7, nullable = false)
    @Enumerated(EnumType.STRING)
    private ArchiveScheduleType scheduleType;

    @Schema(name = "归档时间（年）", hidden = true)
    @Column
    private Integer archiveYear;

    @Schema(name = "归档时间（月）", hidden = true)
    @Column
    private Integer archiveMonth;

    @Schema(name = "归档时间（日）", hidden = true)
    @Column
    private Integer archiveDay;

    @Schema(name = "归档时间（周）", hidden = true)
    @Column
    private Integer archiveWeek;

    @Schema(description = "聚合对象期间（年）")
    @Column
    private Integer groupYear;

    @Schema(description = "聚合对象期间（月）")
    @Column
    private Integer groupMonth;

    @Schema(description = "聚合对象期间（日）")
    @Column
    private Integer groupDay;

    @Schema(description = "聚合对象期间（周）")
    @Column
    private Integer groupWeek;

    @Schema(description = "聚合对象期间（日期）")
    @Column
    private Integer groupDate;

    @Schema(name = "聚合单位：模块 No.", hidden = true)
    @Column
    private String module;

    @Schema(name = "聚合单位：试压包 No.", hidden = true)
    @Column
    private String pressureTestPackage;

    @Schema(name = "聚合单位：子系统 No.", hidden = true)
    @Column
    private String subSystem;

    @Schema(name = "聚合单位：工序阶段名称", hidden = true)
    @Column
    private String stage;

    @Schema(name = "聚合单位：工序名称", hidden = true)
    @Column
    private String process;

    @Schema(name = "聚合单位：焊口类型", hidden = true)
    @Column
    private String weldType;

    @Schema(name = "聚合单位：遗留问题类型", hidden = true)
    @Column
    private String issueType;

    @Schema(name = "聚合单位：实体寸径", hidden = true)
    @Column
    private String entityNps;

    @Schema(name = "聚合单位：实体类型", hidden = true)
    @Column
    private String entitySubType;

    @Schema(name = "聚合单位：实体长度", hidden = true)
    @Column
    private String entityLength;

    @Schema(name = "聚合单位：实体材质", hidden = true)
    @Column
    private String entityMaterial;

    @Schema(name = "聚合单位：工程建造分包商 ID", hidden = true)
    @Column
    private Long subcontractorId;

    @Schema(name = "聚合单位：责任部门 ID", hidden = true)
    @Column
    private Long departmentId;

    @Schema(name = "聚合单位：责任部门 名", hidden = true)
    @Column
    private String departmentName;

    @Schema(name = "聚合单位：责任部门 名", hidden = true)
    @Column
    private String division;

    @Schema(name = "聚合单位：焊工 ID", hidden = true)
    @Column
    private Long welderId;

    @Schema(name = "聚合单位：焊工号", hidden = true)
    @Column
    private String welderNo;

    @Schema(name = "聚合单位：焊工名", hidden = true)
    @Column
    private String welderName;

    @Schema(name = "聚合单位：NDT分类", hidden = true)
    @Column
    private String ndtType;

    @Schema(name = "聚合单位：责任者名", hidden = true)
    @Column
    private String userName;

    @Schema(name = "聚合单位：班组 名", hidden = true)
    @Column
    private String teamName;

    @Schema(name = "聚合单位：甲板", hidden = true)
    @Column
    private String deck;

    @Schema(name = "聚合单位：片体", hidden = true)
    @Column
    private String panel;

    @Schema(name = "聚合单位：构件", hidden = true)
    @Column
    private String wp04No;

    @Schema(name = "聚合单位：地址", hidden = true)
    @Column
    private String address;

    @Schema(name = "聚合单位：任务包名", hidden = true)
    @Column
    private String taskPackageName;

    @Schema(name = "聚合单位：管理者用户 ID", hidden = true)
    @Column
    private Long managerId;

    @Schema(name = "聚合单位：专业", hidden = true)
    @Column
    private String disciplineCode;

    @Schema(name = "聚合单位：文档类型", hidden = true)
    @Column
    private String documentType;

    @Schema(name = "聚合单位：设计类别", hidden = true)
    @Column
    private String engineeringCategory;

    @Schema(name = "周次", hidden = true)
//    @JsonIgnore
    @Column
    private String weekly;

    @Schema(name = "聚合值#1", hidden = true)
    @JsonIgnore
    @Column(name = "value_01")
    private Double value01 = 0.0;

    @Schema(name = "聚合值#2", hidden = true)
    @JsonIgnore
    @Column(name = "value_02")
    private Double value02 = 0.0;

    @Schema(name = "聚合值#3", hidden = true)
    @JsonIgnore
    @Column(name = "value_03")
    private Double value03 = 0.0;

    @Schema(name = "聚合值#4", hidden = true)
    @JsonIgnore
    @Column(name = "value_04")
    private Double value04 = 0.0;

    @Schema(name = "聚合值#5", hidden = true)
    @JsonIgnore
    @Column(name = "value_05")
    private Double value05 = 0.0;

    @Schema(name = "聚合值#6", hidden = true)
    @JsonIgnore
    @Column(name = "value_06")
    private Double value06 = 0.0;

    @Schema(name = "聚合值#7", hidden = true)
    @JsonIgnore
    @Column(name = "value_07")
    private Double value07 = 0.0;

    @Schema(name = "聚合值#8", hidden = true)
    @JsonIgnore
    @Column(name = "value_08")
    private Double value08 = 0.0;

    @Schema(name = "聚合值#9", hidden = true)
    @JsonIgnore
    @Column(name = "value_09")
    private Double value09 = 0.0;

    @Schema(name = "记录创建时间", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date createdAt;

    @Schema(name = "最后更新时间", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonFormatPattern.ISO_DATE)
    @Column
    private Date lastModifiedAt;

    public ArchiveDataBase() {
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ArchiveDataType getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(ArchiveDataType archiveType) {
        this.archiveType = archiveType;
    }

    public ArchiveScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ArchiveScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Integer getArchiveYear() {
        return archiveYear;
    }

    public void setArchiveYear(Integer archiveYear) {
        this.archiveYear = archiveYear;
    }

    public void setProjectId(Number projectId) {
        if (projectId instanceof BigInteger) {
            this.projectId = projectId.longValue();
        } else {
            this.projectId = (Long) projectId;
        }
    }

    public void setArchiveYear(Number archiveYear) {
        if (archiveYear instanceof BigInteger) {
            this.archiveYear = archiveYear.intValue();
        } else {
            this.archiveYear = (Integer) archiveYear;
        }
    }

    public void setArchiveMonth(Number archiveMonth) {
        if (archiveMonth instanceof BigInteger) {
            this.archiveMonth = archiveMonth.intValue();
        } else {
            this.archiveMonth = (Integer) archiveMonth;
        }
    }

    public void setArchiveDay(Number archiveDay) {
        if (archiveDay instanceof BigInteger) {
            this.archiveDay = archiveDay.intValue();
        } else {
            this.archiveDay = (Integer) archiveDay;
        }
    }

    public void setArchiveWeek(Number archiveWeek) {
        if (archiveWeek instanceof BigInteger) {
            this.archiveWeek = archiveWeek.intValue();
        } else {
            this.archiveWeek = (Integer) archiveWeek;
        }
    }

    public Integer getArchiveMonth() {
        return archiveMonth;
    }

    public void setArchiveMonth(Integer archiveMonth) {
        this.archiveMonth = archiveMonth;
    }

    public Integer getArchiveDay() {
        return archiveDay;
    }

    public void setArchiveDay(Integer archiveDay) {
        this.archiveDay = archiveDay;
    }

    public Integer getArchiveWeek() {
        return archiveWeek;
    }

    public void setArchiveWeek(Integer archiveWeek) {
        this.archiveWeek = archiveWeek;
    }

    public Integer getGroupYear() {
        return groupYear;
    }

    public void setGroupYear(Number groupYear) {
        if (groupYear instanceof BigInteger) {
            this.groupYear = groupYear.intValue();
        } else {
            this.groupYear = (Integer) groupYear;
        }
    }

    public Integer getGroupMonth() {
        return groupMonth;
    }

    public void setGroupMonth(Number groupMonth) {
        if (groupMonth instanceof BigInteger) {
            this.groupMonth = groupMonth.intValue();
        } else {
            this.groupMonth = (Integer) groupMonth;
        }
    }

    public Integer getGroupDay() {
        return groupDay;
    }

    public void setGroupDay(Number groupDay) {
        if (groupDay instanceof BigInteger) {
            this.groupDay = groupDay.intValue();
        } else {
            this.groupDay = (Integer) groupDay;
        }
    }

    public Integer getGroupWeek() {
        return groupWeek;
    }

    public void setGroupWeek(Number groupWeek) {
        if (groupWeek instanceof BigInteger) {
            this.groupWeek = groupWeek.intValue();
        } else {
            this.groupWeek = (Integer) groupWeek;
        }
    }

    public void setGroupDate(Number groupDate) {
        if (groupDate instanceof BigInteger) {
            this.groupDate = groupDate.intValue();
        } else {
            this.groupDate = (Integer) groupDate;
        }
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getWeldType() {
        return weldType;
    }

    public void setWeldType(String weldType) {
        this.weldType = weldType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getEntityNps() {
        return entityNps;
    }

    public void setEntityNps(String entityNps) {
        this.entityNps = entityNps;
    }

    public String getEntityLength() {
        return entityLength;
    }

    public void setEntityLength(String entityLength) {
        this.entityLength = entityLength;
    }

    public String getEntityMaterial() {
        return entityMaterial;
    }

    public void setEntityMaterial(String entityMaterial) {
        this.entityMaterial = entityMaterial;
    }

    public Long getSubcontractorId() {
        return subcontractorId;
    }

    public void setSubcontractorId(Long subcontractorId) {
        this.subcontractorId = subcontractorId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getWelderName() {
        return welderName;
    }

    public void setWelderName(String welderName) {
        this.welderName = welderName;
    }

    public String getNdtType() {
        return ndtType;
    }

    public void setNdtType(String ndtType) {
        this.ndtType = ndtType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTaskPackageName() {
        return taskPackageName;
    }

    public void setTaskPackageName(String taskPackageName) {
        this.taskPackageName = taskPackageName;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Double getValue01() {
        return value01;
    }

    public void setValue01(Double value01) {
        this.value01 = value01;
    }

    public Double getValue02() {
        return value02;
    }

    public void setValue02(Double value02) {
        this.value02 = value02;
    }

    public Double getValue03() {
        return value03;
    }

    public void setValue03(Double value03) {
        this.value03 = value03;
    }

    public Double getValue04() {
        return value04;
    }

    public void setValue04(Double value04) {
        this.value04 = value04;
    }

    public Double getValue05() {
        return value05;
    }

    public void setValue05(Double value05) {
        this.value05 = value05;
    }

    public Double getValue06() {
        return value06;
    }

    public void setValue06(Double value06) {
        this.value06 = value06;
    }

    public Double getValue07() {
        return value07;
    }

    public void setValue07(Double value07) {
        this.value07 = value07;
    }

    public Double getValue08() {
        return value08;
    }

    public void setValue08(Double value08) {
        this.value08 = value08;
    }

    public Double getValue09() {
        return value09;
    }

    public void setValue09(Double value09) {
        this.value09 = value09;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Integer getGroupDate() {
        return groupDate;
    }

    public void setGroupDate(Integer groupDate) {
        this.groupDate = groupDate;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getDisciplineCode() {
        return disciplineCode;
    }

    public void setDisciplineCode(String disciplineCode) {
        this.disciplineCode = disciplineCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getEngineeringCategory() {
        return engineeringCategory;
    }

    public void setEngineeringCategory(String engineeringCategory) {
        this.engineeringCategory = engineeringCategory;
    }

    public void setGroupYear(Integer groupYear) {
        this.groupYear = groupYear;
    }

    public void setGroupMonth(Integer groupMonth) {
        this.groupMonth = groupMonth;
    }

    public void setGroupDay(Integer groupDay) {
        this.groupDay = groupDay;
    }

    public void setGroupWeek(Integer groupWeek) {
        this.groupWeek = groupWeek;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getWp04No() {
        return wp04No;
    }

    public void setWp04No(String wp04No) {
        this.wp04No = wp04No;
    }

    public String getWeekly() {
        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
