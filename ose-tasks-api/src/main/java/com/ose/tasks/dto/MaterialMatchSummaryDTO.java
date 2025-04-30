package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.wbs.WBSEntryMaterialDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

/**
 * 扁平 计划 材料 数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialMatchSummaryDTO extends BaseDTO {


    private static final long serialVersionUID = 3679604008941113615L;

    @Schema(description = "总匹配率")
    private String sumMatchPercent;

    @Schema(description = "匹配的材料清单")
    private Page<WBSEntryMaterialDTO> matchList;

    public String getSumMatchPercent() {
        return sumMatchPercent;
    }

    public void setSumMatchPercent(String sumMatchPercent) {
        this.sumMatchPercent = sumMatchPercent;
    }

    public Page<WBSEntryMaterialDTO> getMatchList() {
        return matchList;
    }

    public void setMatchList(Page<WBSEntryMaterialDTO> matchList) {
        this.matchList = matchList;
    }

}
