package com.ose.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 自定义属性定义Excel导入列。
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ColumnImportConfigBase extends BaseEntity {


    private static final long serialVersionUID = -1909261193821284506L;

    @Schema(description = "project Id")
    private Long projectId;

    @Schema(description = "orgId Id")
    private Long orgId;

    @Schema(description = "Status")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @Schema(description = "列名")
    private String columnName;

    @Schema(description = "列序号")
    private Integer columnNo;

    @Schema(description = "是否是表属性")
    private Boolean isTableField;

    @Schema(description = "列描述")
    private String columnDesc;

    @Schema(description = "列Code 编码，如 DISCIPLINE ENTITY TYPE等特殊CODE")
    private String columnCode;

    @Schema(description = "列数据类型，TEXT，DATE，FLOAT, DOUBLE, INTEGER, DATE")
    private String dataType;

    @Schema(description = "是否正常显示")
    private Boolean isDisplayed;

    @Schema(description = "对应的类名")
    private String clazzName;

    @Schema(description = "固定值")
    private String fixedValue;

    private String topFixedCellCoor; //顶部固定单元格的坐标，例如 压力单位等

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(Integer columnNo) {
        this.columnNo = columnNo;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getTableField() {
        return isTableField;
    }

    public void setTableField(Boolean tableField) {
        isTableField = tableField;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Boolean getDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(Boolean displayed) {
        isDisplayed = displayed;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getTopFixedCellCoor() {
        return topFixedCellCoor;
    }

    public void setTopFixedCellCoor(String topFixedCellCoor) {
        this.topFixedCellCoor = topFixedCellCoor;
    }
}
