package com.ose.tasks.dto.taskpackage;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 任务包查询条件数据传输对象。
 */
public class TaskPackageCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = 5695344761702835099L;

    @Schema(description = "查询关键字")
    private String keyword;

    @Schema(description = "任务包名")
    private List<String> name;

    @Schema(description = "分类 ID")
    private Long categoryId;

    @Schema(description = "操作者 ID")
    private Long modifyNameId;

    @Schema(description = "工作组 ID")
    private Long teamId;

    @Schema(description = "工作场地 ID")
    private Long workSiteId;

    @Schema(description = "完成的个数百分比")
    private Double percentCount;

    @Schema(description = "查询类型")
    private String keyWordType;

    @Schema(description = "专业")
    private String discipline;

    @Schema(description = "功能分区")
    private String funcPart;

    @Schema(description = "排序方式")
    private String sortChange;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getWorkSiteId() {
        return workSiteId;
    }

    public void setWorkSiteId(Long workSiteId) {
        this.workSiteId = workSiteId;
    }

    public Double getPercentCount() {
        return percentCount;
    }

    public void setPercentCount(Double percentCount) {
        this.percentCount = percentCount;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public Long getModifyNameId() {
        return modifyNameId;
    }

    public void setModifyNameId(Long modifyNameId) {
        this.modifyNameId = modifyNameId;
    }

    public String getKeyWordType() {
        return keyWordType;
    }

    public void setKeyWordType(String keyWordType) {
        this.keyWordType = keyWordType;
    }

    public String getSortChange() {
        return sortChange;
    }

    public void setSortChange(String sortChange) {
        this.sortChange = sortChange;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
