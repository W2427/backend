package com.ose.tasks.dto.drawing;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.DesignChangeDisciplines;
import com.ose.tasks.vo.DesignChangeOriginated;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DesignChangeReviewDTO extends BaseDTO {

    private static final long serialVersionUID = -5678948630841928268L;

    @Schema(description = "项目名")
    private String projectName;

    @Schema(description = "申请单号")
    private String reportNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问题提出人")
    private String raisedBy;

    @Schema(description = "问题提出人id")
    private Long raisedById;

    @Schema(description = "修改发生根源")
    private List<DesignChangeOriginated> originatedBy;

    @Schema(description = "修改原因描述")
    private String causeDescription;

    @Schema(description = "涉及专业")
    private List<DesignChangeDisciplines> involvedDisciplines;

    @Schema(description = "行动条款(图纸编号)")
    private List<String> actionItem;

    @Schema(description = "图纸变更版本")
    private List<String> itemVersion;

    @Schema(description = "vorNo")
    private String vorNo;

    @Schema(description = "设计人员工时(size=7)")
    private List<String> engineeringManhours;

    @Schema(description = "材料(size=7)")
    private List<String> materials;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public Long getRaisedById() {
        return raisedById;
    }

    public void setRaisedById(Long raisedById) {
        this.raisedById = raisedById;
    }

    public List<DesignChangeOriginated> getOriginatedBy() {
        return originatedBy;
    }

    public void setOriginatedBy(List<DesignChangeOriginated> originatedBy) {
        this.originatedBy = originatedBy;
    }

    public String getCauseDescription() {
        return causeDescription;
    }

    public void setCauseDescription(String causeDescription) {
        this.causeDescription = causeDescription;
    }

    public List<DesignChangeDisciplines> getInvolvedDisciplines() {
        return involvedDisciplines;
    }

    public void setInvolvedDisciplines(List<DesignChangeDisciplines> involvedDisciplines) {
        this.involvedDisciplines = involvedDisciplines;
    }

    public List<String> getActionItem() {
        return actionItem;
    }

    public void setActionItem(List<String> actionItem) {
        this.actionItem = actionItem;
    }

    public String getVorNo() {
        return vorNo;
    }

    public void setVorNo(String vorNo) {
        this.vorNo = vorNo;
    }

    public List<String> getEngineeringManhours() {
        return engineeringManhours;
    }

    public void setEngineeringManhours(List<String> engineeringManhours) {
        this.engineeringManhours = engineeringManhours;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(List<String> itemVersion) {
        this.itemVersion = itemVersion;
    }
}
