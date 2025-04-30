package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;

import com.ose.vo.InspectParty;
import com.ose.dto.BaseDTO;

import com.ose.vo.InspectResult;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 实体类型 数据传输对象
 */
public class ExInspScheduleDTO extends BaseDTO {


    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "临时文件名")
    private List<String> temporaryFileNames;

    @Schema(description = "报检名称")
    private String name;

    @Schema(description = "备注")
    private String comment;

    @Schema(description = "具体位置")
    private String location;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "区分")
    private String coordinateCategory;

    @Schema(description = "外检时间")
    private Date externalInspectionTime;

    @Schema(description = "实体编号")
    private List<String> entityNos;

    @Schema(description = "外检结果: A;B;C")
    @Enumerated(EnumType.STRING)
    private InspectResult resultType;

    @Schema(description = "任务Ids")
    private List<Long> taskIds;

    @Schema(description = "流程Ids")
    private List<Long> actInstIds;

    @Schema(description = "检验方")
    private List<InspectParty> inspectParties;


    public List<String> getTemporaryFileNames() {
        return temporaryFileNames;
    }

    public void setTemporaryFileNames(List<String> temporaryFileNames) {
        this.temporaryFileNames = temporaryFileNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getCoordinateCategory() {
        return coordinateCategory;
    }

    public void setCoordinateCategory(String coordinateCategory) {
        this.coordinateCategory = coordinateCategory;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public List<String> getEntityNos() {
        return entityNos;
    }

    public void setEntityNos(List<String> entityNos) {
        this.entityNos = entityNos;
    }

    public InspectResult getResultType() {
        return resultType;
    }

    public void setResultType(InspectResult resultType) {
        this.resultType = resultType;
    }

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public List<InspectParty> getInspectParties() {
        return inspectParties;
    }

    public void setInspectParties(List<InspectParty> inspectParties) {
        this.inspectParties = inspectParties;
    }

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

}
