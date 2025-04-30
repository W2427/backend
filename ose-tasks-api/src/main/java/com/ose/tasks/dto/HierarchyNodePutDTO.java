package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HierarchyNodePutDTO extends BaseDTO implements Comparable<HierarchyNodePutDTO> {

    private static final long serialVersionUID = -1598120122544566292L;

    @Schema(description = "已导入节点的 ID")
    private Long id;

    @Schema(description = "节点编号")
    private String no;

    // 上级节点 ID
    @Schema(description = "节点父级的层级 ID")
    private Long parentId;

    // 上级节点 ID
    @Schema(description = "节点父级 No")
    private String parentNo;

    @Schema(description = "模块类型")
    private String moduleType;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "实体子类型")
    private String entitySubType;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "是否是固定层级")
    private Boolean isFixedLevel;

    @Schema(description = "批次编号")
    private String batchNo;

    @Schema(description = "是否打包")
    private boolean pack;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "子节点")
    private List<HierarchyNodePutDTO> children = null;

    @JsonIgnore
    private int depth;

    @Schema(description = "整体完成进度")
    private Double overallFinishedScore;

    @Schema(description = "所占整体进度")
    private Double overallTotalScore;

    @Schema(description = "整体计划工时")
    private Double overallPlanHours;

    @Schema(description = "整体实际工时")
    private Double overallActualHours;

    @Schema(description = "安装完成进度")
    private Double installationFinishedScore;

    @Schema(description = "安装整体进度")
    private Double installationTotalScore;

    @Schema(description = "安装计划工时")
    private Double installationPlanHours;

    @Schema(description = "安装实际工时")
    private Double installationActualHours;

    @Schema(description = "试压包完成进度")
    private Double ptpFinishedScore;

    @Schema(description = "试压包整体进度")
    private Double ptpTotalScore;


    @Schema(description = "试压包计划工时")
    private Double ptpPlanHours;

    @Schema(description = "试压包实际共识")
    private Double ptpActualHours;

    @Schema(description = "清洁完成进度")
    private Double cleannessFinishedScore;

    @Schema(description = "清洁整体进度")
    private Double cleannessTotalScore;

    @Schema(description = "清洁计划工时")
    private Double cleannessPlanHours;

    @Schema(description = "清洁实际工时")
    private Double cleannessActualHours;

    @Schema(description = "子系统完成进度")
    private Double mcFinishedScore;

    @Schema(description = "子系统整体进度")
    private Double mcTotalScore;

    @Schema(description = "子系统计划工时")
    private Double mcPlanHours;

    @Schema(description = "子系统实际工时")
    private Double mcActualHours;

    @Schema(hidden = true)
    @JsonIgnore
    private Cell errorMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public boolean isPack() {
        return pack;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HierarchyNodePutDTO> getChildren() {
        return children;
    }

    public void setChildren(List<HierarchyNodePutDTO> children) {

        if (children != null && children.size() == 0) {
            children = null;
        }

        this.children = children;
    }

    public void initChildren() {
        this.children = new ArrayList<>();
    }

    public void addChild(HierarchyNodePutDTO child) {

        if (child == null) {
            return;
        }

        if (children == null) {
            children = new ArrayList<>();
        }

        children.add(child);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Double getOverallFinishedScore() {
        return overallFinishedScore;
    }

    public void setOverallFinishedScore(Double overallFinishedScore) {
        this.overallFinishedScore = overallFinishedScore;
    }

    public Double getOverallTotalScore() {
        return overallTotalScore;
    }

    public void setOverallTotalScore(Double overallTotalScore) {
        this.overallTotalScore = overallTotalScore;
    }

    public Double getOverallPlanHours() {
        return overallPlanHours;
    }

    public void setOverallPlanHours(Double overallPlanHours) {
        this.overallPlanHours = overallPlanHours;
    }

    public Double getOverallActualHours() {
        return overallActualHours;
    }

    public void setOverallActualHours(Double overallActualHours) {
        this.overallActualHours = overallActualHours;
    }

    public Double getInstallationFinishedScore() {
        return installationFinishedScore;
    }

    public void setInstallationFinishedScore(Double installationFinishedScore) {
        this.installationFinishedScore = installationFinishedScore;
    }

    public Double getInstallationTotalScore() {
        return installationTotalScore;
    }

    public void setInstallationTotalScore(Double installationTotalScore) {
        this.installationTotalScore = installationTotalScore;
    }

    public Double getInstallationPlanHours() {
        return installationPlanHours;
    }

    public void setInstallationPlanHours(Double installationPlanHours) {
        this.installationPlanHours = installationPlanHours;
    }

    public Double getInstallationActualHours() {
        return installationActualHours;
    }

    public void setInstallationActualHours(Double installationActualHours) {
        this.installationActualHours = installationActualHours;
    }

    public Double getPtpFinishedScore() {
        return ptpFinishedScore;
    }

    public void setPtpFinishedScore(Double ptpFinishedScore) {
        this.ptpFinishedScore = ptpFinishedScore;
    }

    public Double getPtpTotalScore() {
        return ptpTotalScore;
    }

    public void setPtpTotalScore(Double ptpTotalScore) {
        this.ptpTotalScore = ptpTotalScore;
    }

    public Double getPtpPlanHours() {
        return ptpPlanHours;
    }

    public void setPtpPlanHours(Double ptpPlanHours) {
        this.ptpPlanHours = ptpPlanHours;
    }

    public Double getPtpActualHours() {
        return ptpActualHours;
    }

    public void setPtpActualHours(Double ptpActualHours) {
        this.ptpActualHours = ptpActualHours;
    }

    public Double getCleannessFinishedScore() {
        return cleannessFinishedScore;
    }

    public void setCleannessFinishedScore(Double cleannessFinishedScore) {
        this.cleannessFinishedScore = cleannessFinishedScore;
    }

    public Double getCleannessTotalScore() {
        return cleannessTotalScore;
    }

    public void setCleannessTotalScore(Double cleannessTotalScore) {
        this.cleannessTotalScore = cleannessTotalScore;
    }

    public Double getCleannessPlanHours() {
        return cleannessPlanHours;
    }

    public void setCleannessPlanHours(Double cleannessPlanHours) {
        this.cleannessPlanHours = cleannessPlanHours;
    }

    public Double getCleannessActualHours() {
        return cleannessActualHours;
    }

    public void setCleannessActualHours(Double cleannessActualHours) {
        this.cleannessActualHours = cleannessActualHours;
    }

    public Double getMcFinishedScore() {
        return mcFinishedScore;
    }

    public void setMcFinishedScore(Double mcFinishedScore) {
        this.mcFinishedScore = mcFinishedScore;
    }

    public Double getMcTotalScore() {
        return mcTotalScore;
    }

    public void setMcTotalScore(Double mcTotalScore) {
        this.mcTotalScore = mcTotalScore;
    }

    public Double getMcPlanHours() {
        return mcPlanHours;
    }

    public void setMcPlanHours(Double mcPlanHours) {
        this.mcPlanHours = mcPlanHours;
    }

    public Double getMcActualHours() {
        return mcActualHours;
    }

    public void setMcActualHours(Double mcActualHours) {
        this.mcActualHours = mcActualHours;
    }

    public Cell getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Cell errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addErrorMessage(String message) {

        if (errorMessage == null) {
            return;
        }

        String messages = WorkbookUtils.readAsString(errorMessage);

        if (!StringUtils.isEmpty(messages, true)) {
            messages = StringUtils.trim(messages) + "; " + message;
        } else {
            messages = message;
        }

        errorMessage.setCellValue(messages);
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }


    @Override
    public int compareTo(HierarchyNodePutDTO o) {
        if (this.getDepth() == o.getDepth()) {
            if(StringUtils.getStartStr(this.getNo()).equalsIgnoreCase(StringUtils.getStartStr(o.getNo()))) {
                return StringUtils.getStartFigure(this.getNo()) - StringUtils.getStartFigure(o.getNo());
            }
            return this.getNo().compareToIgnoreCase(o.getNo());

        } else if (this.getDepth() < o.getDepth()) {
            return -1;
        } else if (this.getDepth() > o.getDepth()) {
            return 1;
        } else{

            return this.getNo().compareToIgnoreCase(o.getNo());
        }

        //如果是 【123】【abc】则先比较
    }

    public static void main(String[] args) {
        String abc = "DECK10";
        String ab = "DECK3";
        abc.compareToIgnoreCase(ab);

        String d = abc.replace("(\\w+)(\\d+.*)","$1");

    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Boolean getFixedLevel() {
        return isFixedLevel;
    }

    public void setFixedLevel(Boolean fixedLevel) {
        isFixedLevel = fixedLevel;
    }
}
