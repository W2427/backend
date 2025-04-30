package com.ose.tasks.dto.taskpackage;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntryPlain;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 任务包 包括 汇总信息 数据传输对象。
 */
public class TaskPackageDTO extends BaseDTO {

    @Schema(description = "返回的四级计划分页信息")
    private Page<WBSEntryPlain> wbsEntries;

    @Schema(description = "四级计划总数量")
    private Long wbsTotalCount;

    @Schema(description = "完成的积极计划数量")
    private Long wbsFinishedCount;

    @Schema(description = "完成个数的百分比")
    private Double countPercent;

    @Schema(description = "四级计划总物量 寸径")
    private Double workLoadTotal;

    @Schema(description = "四级计划完成的物量 寸径")
    private Double workLoadFinished;

    @Schema(description = "完成物量的百分比")
    private Double workLoadPercent;


    @Schema(description = "专业")
    private String discipline;

    private List<String> sectorList;

    public List<String> getSectorList() {
        return sectorList;
    }

    public void setSectorList(List<String> sectorList) {
        this.sectorList = sectorList;
    }

    public Page<WBSEntryPlain> getWbsEntries() {
        return wbsEntries;
    }

    public void setWbsEntries(Page<WBSEntryPlain> wbsEntries) {
        this.wbsEntries = wbsEntries;
    }

    public Long getWbsTotalCount() {
        return wbsTotalCount;
    }

    public void setWbsTotalCount(Long wbsTotalCount) {
        this.wbsTotalCount = wbsTotalCount;
    }

    public Long getWbsFinishedCount() {
        return wbsFinishedCount;
    }

    public void setWbsFinishedCount(Long wbsFinishedCount) {
        this.wbsFinishedCount = wbsFinishedCount;
    }

    public Double getCountPercent() {
        return countPercent;
    }

    public void setCountPercent(Double countPercent) {
        this.countPercent = countPercent;
    }

    public Double getWorkLoadTotal() {
        return workLoadTotal;
    }

    public void setWorkLoadTotal(Double workLoadTotal) {
        this.workLoadTotal = workLoadTotal;
    }

    public Double getWorkLoadFinished() {
        return workLoadFinished;
    }

    public void setWorkLoadFinished(Double workLoadFinished) {
        this.workLoadFinished = workLoadFinished;
    }

    public Double getWorkLoadPercent() {
        return workLoadPercent;
    }

    public void setWorkLoadPercent(Double workLoadPercent) {
        this.workLoadPercent = workLoadPercent;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
