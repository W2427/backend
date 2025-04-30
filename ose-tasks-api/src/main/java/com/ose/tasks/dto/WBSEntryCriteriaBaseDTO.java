package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.wbs.WBSEntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryCriteriaBaseDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "节点编号")
    private String no;

    @Schema(description = "父级")
    private String moduleParentNo;

    @Schema(description = "维度代码")
    private String hierarchyType;

    @Schema(description = "祖先父级ID")
    private List<Long> ancestorHierarchyIds;

    private List<Long> EntityIds;

    private String discipline;

    @Schema(description = "实体状态")
    private WBSEntityStatus entityStatus = WBSEntityStatus.NOT_DELETED;

    @Schema(description = "开始时间")
    private String createDateFrom;

    @Schema(description = "结束时间")
    private String createDateUntil;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateFromTime;

    @JsonIgnore
    @Schema(hidden = true)
    private Date createDateUntilTime;

    @JsonCreator
    public WBSEntryCriteriaBaseDTO() {
    }

    @Schema(description = "添加状态")
    private String addStatus;

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public List<Long> getAncestorHierarchyIds() {
        return ancestorHierarchyIds;
    }

    public void setAncestorHierarchyIds(List<Long> ancestorHierarchyIds) {
        this.ancestorHierarchyIds = ancestorHierarchyIds;
    }

    public WBSEntityStatus getEntityStatus() {
        return entityStatus;
    }

    public void setEntityStatus(WBSEntityStatus entityStatus) {
        this.entityStatus = entityStatus;
    }

    public List<Long> getEntityIds() {
        return EntityIds;
    }

    public void setEntityIds(List<Long> entityIds) {
        EntityIds = entityIds;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getModuleParentNo() {
        return moduleParentNo;
    }

    public void setModuleParentNo(String moduleParentNo) {
        this.moduleParentNo = moduleParentNo;
    }

    public String getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(String createDateFrom) {
        this.createDateFrom = createDateFrom;
        this.createDateFromTime = parseDate(createDateFrom);
    }

    public String getCreateDateUntil() {
        return createDateUntil;
    }

    public void setCreateDateUntil(String createDateUntil) {
        this.createDateUntil = createDateUntil;
        this.createDateUntilTime = parseDate(createDateUntil);
    }

    private Date parseDate(String dateStr) {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return f.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getCreateDateFromTime() {
        return createDateFromTime;
    }

    public void setCreateDateFromTime(Date createDateFromTime) {
        this.createDateFromTime = createDateFromTime;
    }

    public Date getCreateDateUntilTime() {
        return createDateUntilTime;
    }

    public void setCreateDateUntilTime(Date createDateUntilTime) {
        this.createDateUntilTime = createDateUntilTime;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }
}
