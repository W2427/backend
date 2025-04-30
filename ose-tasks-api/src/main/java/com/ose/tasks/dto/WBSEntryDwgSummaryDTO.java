package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.wbs.WBSEntryDwgDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * 扁平计划 图纸 数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntryDwgSummaryDTO extends BaseDTO {


    private static final long serialVersionUID = -3208584651341496278L;
    @Schema(description = "总发图率")
    private String sumIssuePercent;

    @Schema(description = "图纸情况清单")
    private Page<WBSEntryDwgDTO> subDwgList;

    public String getSumIssuePercent() {
        return sumIssuePercent;
    }

    public void setSumIssuePercent(String sumIssuePercent) {
        this.sumIssuePercent = sumIssuePercent;
    }

    public Page<WBSEntryDwgDTO> getSubDwgList() {
        return subDwgList;
    }

    public void setSubDwgList(Page<WBSEntryDwgDTO> subDwgList) {
        this.subDwgList = subDwgList;
    }
}
