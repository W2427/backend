package com.ose.tasks.dto.bpm;

import java.util.Date;
import java.util.List;

import com.ose.tasks.entity.bpm.BpmExInspApply;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据传输对象
 */
public class ExInspApplyDTO extends TodoBatchTaskCriteriaDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "外检处理列表")
    private List<BpmExInspApply> externalInspectionApplyList;

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

    public List<BpmExInspApply> getExternalInspectionApplyList() {
        return externalInspectionApplyList;
    }

    public void setExternalInspectionApplyList(List<BpmExInspApply> externalInspectionApplyList) {
        this.externalInspectionApplyList = externalInspectionApplyList;
    }
}
