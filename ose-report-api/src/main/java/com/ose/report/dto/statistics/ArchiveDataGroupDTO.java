package com.ose.report.dto.statistics;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 归档数据分组参数数据传输对象。
 */
public class ArchiveDataGroupDTO extends BaseDTO {

    private static final long serialVersionUID = 3947308925855067715L;

    @Schema(description = "聚合单位：区域 No.")
    private String area;

    @Schema(description = "聚合单位：模块 No.")
    private List<String> module;

    @Schema(description = "聚合单位：项目 ProjectName.")
    private List<String> projectName;

    @Schema(description = "聚合单位：甲板 No.")
    private String deck;

    @Schema(description = "聚合单位：片体 No.")
    private String panel;

    @Schema(description = "聚合单位：构件 No.")
    private String wp04No;

    @Schema(description = "聚合单位：试压包 No.")
    private String pressureTestPackage;

    @Schema(description = "聚合单位：子系统 No.")
    private String subSystem;

    @Schema(description = "聚合单位：工序阶段名称")
    private String stage;

    @Schema(description = "聚合单位：工序名称")
    private String process;

    @Schema(description = "聚合单位：焊口类型")
    private String weldType;

    @Schema(description = "聚合单位：遗留问题等级")
    private String issueLevel;

    @Schema(description = "聚合单位：遗留问题类型")
    private String issueType;

    @Schema(description = "聚合单位：实体寸径")
    private String entityNps;

    @Schema(description = "聚合单位：实体长度")
    private String entityLength;

    @Schema(description = "聚合单位：实体材质")
    private String entityMaterial;

    @Schema(description = "聚合单位：工程建造分包商 ID")
    private Long subcontractorId;

    @Schema(description = "聚合单位：责任部门 ID")
    private Long departmentId;

    @Schema(description = "聚合单位：责任部门 名")
    private String departmentName;

    @Schema(description = "聚合单位：焊工 ID")
    private Long welderId;

    @Schema(description = "聚合单位：焊工号")
    private String welderNo;

    @Schema(description = "聚合单位：焊工名")
    private String welderName;

    @Schema(description = "聚合单位：NDT 分类")
    private String ndtType;

    @Schema(description = "聚合单位：责任者名")
    private String userName;

    @Schema(description = "聚合单位：班组 名")
    private String teamName;

    @Schema(description = "聚合单位：地址")
    private String address;

    @Schema(description = "聚合单位：任务包名")
    private String taskPackageName;

    @Schema(description = "聚合单位：管理者用户 ID")
    private Long managerId;

    @Schema(description = "聚合单位：实体类型")
    private String entitySubType;

    @Schema(description = "聚合单位：专业")
    private String disciplineCode;

    @Schema(description = "聚合单位：文档类型")
    private String documentType;

    @Schema(description = "聚合单位：设计类别")
    private String engineeringCategory;

    @Schema(description = "周次")
    private List<String> weekly;

    @Schema(description = "部门")
    private List<String> division;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getModule() {
        return module;
    }

    public void setModule(List<String> module) {
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

    public String getIssueLevel() {
        return issueLevel;
    }

    public void setIssueLevel(String issueLevel) {
        this.issueLevel = issueLevel;
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

    public String getWelderName() {
        return welderName;
    }

    public void setWelderName(String welderName) {
        this.welderName = welderName;
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

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getNdtType() {
        return ndtType;
    }

    public void setNdtType(String ndtType) {
        this.ndtType = ndtType;
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

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getWeekly() {
        return weekly;
    }

    public void setWeekly(List<String> weekly) {
        this.weekly = weekly;
    }

    public List<String> getDivision() {
        return division;
    }

    public void setDivision(List<String> division) {
        this.division = division;
    }

    public List<String> getProjectName() {
        return projectName;
    }

    public void setProjectName(List<String> projectName) {
        this.projectName = projectName;
    }
}
