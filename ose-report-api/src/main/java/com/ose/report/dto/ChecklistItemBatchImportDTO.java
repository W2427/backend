package com.ose.report.dto;

import com.ose.dto.BaseDTO;

import java.util.List;

public class ChecklistItemBatchImportDTO extends BaseDTO {

    private static final long serialVersionUID = 1837443152077364723L;

    private List<ChecklistItemImportDTO> checklistItems;

    public ChecklistItemBatchImportDTO() {
    }

    public List<ChecklistItemImportDTO> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItemImportDTO> checklistItems) {
        this.checklistItems = checklistItems;
    }

}
