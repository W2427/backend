package com.ose.tasks.dto.archivedata;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 文档包生成参数DTO。
 */
public class ArchiveDataCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "聚合对象期间（年）")
    private Integer groupYear;

    @Schema(description = "聚合对象期间（月）")
    private Integer groupMonth;

    @Schema(description = "聚合对象期间（日）")
    private Integer groupDay;

    @Schema(description = "聚合对象期间（周）")
    private Integer groupWeek;

    @Schema(description = "聚合单位：区域 No.")
    private String area;

    @Schema(description = "聚合单位：模块 No.")
    private String module;

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public Integer getGroupYear() {
        return groupYear;
    }

    public void setGroupYear(Integer groupYear) {
        this.groupYear = groupYear;
    }

    public Integer getGroupMonth() {
        return groupMonth;
    }

    public void setGroupMonth(Integer groupMonth) {
        this.groupMonth = groupMonth;
    }

    public Integer getGroupDay() {
        return groupDay;
    }

    public void setGroupDay(Integer groupDay) {
        this.groupDay = groupDay;
    }

    public Integer getGroupWeek() {
        return groupWeek;
    }

    public void setGroupWeek(Integer groupWeek) {
        this.groupWeek = groupWeek;
    }

}
