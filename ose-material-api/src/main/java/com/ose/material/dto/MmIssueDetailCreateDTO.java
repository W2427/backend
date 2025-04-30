package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


/**
 * 出库单明细表创建DTO
 */
public class MmIssueDetailCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -4842129503075793832L;

    @Schema(description = "出库详情")
    public List<MmIssueDetailCreateItemDTO> mmIssueDetailCreateItemDTOs;

    public List<MmIssueDetailCreateItemDTO> getMmIssueDetailCreateItemDTOs() {
        return mmIssueDetailCreateItemDTOs;
    }

    public void setMmIssueDetailCreateItemDTOs(List<MmIssueDetailCreateItemDTO> mmIssueDetailCreateItemDTOs) {
        this.mmIssueDetailCreateItemDTOs = mmIssueDetailCreateItemDTOs;
    }
}
